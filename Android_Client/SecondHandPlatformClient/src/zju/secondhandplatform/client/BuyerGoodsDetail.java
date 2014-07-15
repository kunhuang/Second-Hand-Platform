package zju.secondhandplatform.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BuyerGoodsDetail extends ListActivity {
	private Button addWishlistButton;
	private Button payButton;
	private Button submitCommentButton;
	private TextView goodsNameText;
	private TextView goodsPriceText;
	private TextView stateText;
	private TextView goodsContentText;
	private EditText editCommentText;
	private String goodsName;
	private String goodsPrice;
	private String state;
	private String goodsContent;
	private String editComment;

	private int seller_id;
	private String passwd;
	private String goodsId;

	private SimpleAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_buyer_goods_detail);

		addWishlistButton = (Button) findViewById(R.id.detailAddWishlist);
		payButton = (Button) findViewById(R.id.detailPay);
		submitCommentButton = (Button) findViewById(R.id.submitComment);
		goodsNameText = (TextView) findViewById(R.id.detailGoodsName);
		goodsPriceText = (TextView) findViewById(R.id.detailGoodsPrice2);
		stateText = (TextView) findViewById(R.id.detailStatus2);
		goodsContentText = (TextView) findViewById(R.id.detailGoodsContent);
		editCommentText = (EditText) findViewById(R.id.editComment);

		Intent intent = getIntent();
		goodsId = intent.getStringExtra("GoodsId");

		ClientApp clientApp = (ClientApp) this.getApplication();
		seller_id = clientApp.getId();
		passwd = clientApp.getPassword();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("seller_id", "" + seller_id));
		// params.add(new BasicNameValuePair("password", passwd));
		params.add(new BasicNameValuePair("goods_id", goodsId));
		Json json = new Json("/json_api/get_goods_info/", params);
		// "data":{"total":1,"rows":[{"id":1,"buyer_id":"","pure_price":10000,"state":"I","seller_id":"1","description":"全新品","name":"苹果电脑"}]}

		List<NameValuePair> params2 = new ArrayList<NameValuePair>();
		// params2.add(new BasicNameValuePair("account_id", "" + seller_id));
		// params2.add(new BasicNameValuePair("password", passwd));
		params2.add(new BasicNameValuePair("goods_id", goodsId));
		Json commentJson = new Json("/json_api/get_comment_array/", params2);
		//{"data":{"total":1,"rows":[{"id":3,"content":"editComment","time":1405388190,"account_id":8,"goods_id":32}]},"success":1}
		try {
			while (json.getJsonObj() == null) {
			}
			int success = json.getJsonObj().getInt("success");
			
			if (success == 1) {
				JSONObject data = json.getJsonObj().getJSONObject("data");
				// int total=data.getInt("total");
				JSONArray rows = data.getJSONArray("rows");
				JSONObject row = rows.getJSONObject(0);
				goodsName = row.getString("name");
				goodsPrice = row.getString("pure_price");
				state = row.getString("state");
				goodsContent = row.getString("description");

				if (state.equals("I")) {
					state = "未上架";
				} else if (state.equals("O")) {
					state = "已上架";
				} else if (state.equals("B")) {
					state = "已交易";
				} else if (state.equals("C")) {
					state = "下架";
				}
				goodsNameText.setText(goodsName);
				goodsPriceText.setText(goodsPrice);
				stateText.setText(state);
				goodsContentText.setText(goodsContent);

				List<HashMap<String, Object>> comments = new ArrayList<HashMap<String, Object>>();
				while(commentJson.getJsonObj()==null){}
				JSONObject commentData = commentJson.getJsonObj().getJSONObject("data");
				int commentTotal=commentData.getInt("total");
				JSONArray commentRows = commentData.getJSONArray("rows");
				for (int i = 0; i < commentTotal; i++) {
					JSONObject commentRow = commentRows.getJSONObject(i);
					String content = commentRow.getString("content");
					String time = commentRow.getString("time");
					String accountName = commentRow.getString("account_id");

					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("content", content);
					map.put("time", time);
					map.put("accountName", accountName);
					comments.add(map);
				}
				adapter = new SimpleAdapter(this, comments,
						R.layout.comment_item, new String[] { "content","time","accountName" },
						new int[] { R.id.detailCommentContent,R.id.commentTime2,R.id.commentName2 });
				try {
					setListAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				int error_type = json.getJsonObj().getInt("error_type");
				if (error_type == -3) {
					Toast.makeText(this.getApplicationContext(), "找不到该商品",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this.getApplicationContext(), "未知错误",
							Toast.LENGTH_SHORT).show();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		addWishlistButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				ClientApp clientApp = (ClientApp) getApplicationContext();
				if (clientApp.getId() == -1) {
					try {
						Intent intent = new Intent();
						intent.setClass(BuyerGoodsDetail.this, Login.class);
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("buyer_id", ""
							+ seller_id));
					params.add(new BasicNameValuePair("password", passwd));
					params.add(new BasicNameValuePair("goods_id", goodsId));
					Json json = new Json("/json_api/add_wishlist/", params);

					try {
						while (json.getJsonObj() == null) {
						}
						int success = json.getJsonObj().getInt("success");

						if (success == 1) {
							Toast.makeText(getApplicationContext(), "添加心愿单成功",
									Toast.LENGTH_SHORT).show();
						} else {
							int error_type = json.getJsonObj().getInt(
									"error_type");
							if (error_type == -5) {
								Toast.makeText(getApplicationContext(),
										"已经加入心愿单", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(),
										"未知错误:错误代码" + error_type,
										Toast.LENGTH_SHORT).show();
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		payButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				ClientApp clientApp = (ClientApp) getApplicationContext();
				if (clientApp.getId() == -1) {
					try {
						Intent intent = new Intent();
						intent.setClass(BuyerGoodsDetail.this, Login.class);
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"使用空付支付！bibibibi~~", Toast.LENGTH_SHORT).show();
				}
			}
		});

		submitCommentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				ClientApp clientApp = (ClientApp) getApplicationContext();
				if (clientApp.getId() == -1) {
					try {
						Intent intent = new Intent();
						intent.setClass(BuyerGoodsDetail.this, Login.class);
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					editComment = editCommentText.getText().toString();
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("account_id", ""
							+ clientApp.getId()));
					params.add(new BasicNameValuePair("password", passwd));
					params.add(new BasicNameValuePair("goods_id", goodsId));
					params.add(new BasicNameValuePair("content", editComment));
					Json json = new Json("/json_api/add_comment/", params);

					try {
						while (json.getJsonObj() == null) {
						}
						int success = json.getJsonObj().getInt("success");

						if (success == 1) {
							Toast.makeText(getApplicationContext(), "评论成功",
									Toast.LENGTH_SHORT).show();
						} else {
							int error_type = json.getJsonObj().getInt(
									"error_type");
							if (error_type == -5) {
								Toast.makeText(getApplicationContext(),
										"已经加入心愿单）", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(),
										"未知错误:错误代码" + error_type,
										Toast.LENGTH_SHORT).show();
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	protected void onCreateView(Bundle savedInstanceState) {
	}
}
