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

public class SellerGoodsDetail extends ListActivity {
	private Button onShelfButton;
	private Button offShelfButton;
	private Button editButton;
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

	private int id;
	private String passwd;
	private String goodsId;

	private ListView listView;
	private SimpleAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_seller_goods_detail);

		onShelfButton = (Button) findViewById(R.id.sellerOnshelf);
		offShelfButton = (Button) findViewById(R.id.sellerOffshelf);
		editButton = (Button) findViewById(R.id.sellerEdit);
		submitCommentButton = (Button) findViewById(R.id.submitComment);
		goodsNameText = (TextView) findViewById(R.id.sellerGoodsName);
		goodsPriceText = (TextView) findViewById(R.id.sellerGoodsPrice2);
		stateText = (TextView) findViewById(R.id.sellerStatus2);
		goodsContentText = (TextView) findViewById(R.id.detailGoodsContent);
		editCommentText = (EditText) findViewById(R.id.editComment);
//		listView = (ListView) findViewById(android.R.id.list);

		Intent intent = getIntent();
		goodsId = intent.getStringExtra("GoodsId");

		ClientApp clientApp = (ClientApp) this.getApplication();
		id = clientApp.getId();
		passwd = clientApp.getPassword();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("seller_id", "" + id));
		params.add(new BasicNameValuePair("password", passwd));
		params.add(new BasicNameValuePair("goods_id", goodsId));
		Json json = new Json("/json_api/get_goods_info/", params);

		List<NameValuePair> params2 = new ArrayList<NameValuePair>();
		params2.add(new BasicNameValuePair("account_id", "" + id));
		params2.add(new BasicNameValuePair("password", passwd));
		params2.add(new BasicNameValuePair("goods_id", goodsId));
		Json commentJson = new Json("/json_api/get_comment_array/", params2);
		try {
			while (json.getJsonObj() == null) {
			}
			int success = json.getJsonObj().getInt("success");
			// "data":{"total":1,"rows":[{"id":1,"buyer_id":"","pure_price":10000,"state":"I","seller_id":"1","description":"全新品","name":"苹果电脑"}]}
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
				} else if (state.equals("U")) {
					state = "更新";
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
					// listView.setAdapter(adapter);
					setListAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				int error_type = json.getJsonObj().getInt("error_type");
				if (error_type == -3) {
					Toast.makeText(this.getApplicationContext(),
							"找不到该商品", Toast.LENGTH_SHORT).show();
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

		onShelfButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("account_id", "" + id));
				params.add(new BasicNameValuePair("password", passwd));
				params.add(new BasicNameValuePair("account_type", "0"));
				params.add(new BasicNameValuePair("goods_id", goodsId));
				params.add(new BasicNameValuePair("type", "O"));
				Json json = new Json("/json_api/transact_goods/", params);
				
				try {
					while (json.getJsonObj() == null) {
					}
					int success = json.getJsonObj().getInt("success");

					if (success == 1) {
						Toast.makeText(getApplicationContext(), "上架成功",
								Toast.LENGTH_SHORT).show();
					} else {
						int error_type = json.getJsonObj().getInt("error_type");
						if (error_type == -5) {
							Toast.makeText(getApplicationContext(),
									"已经加入心愿单", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(getApplicationContext(), "未知错误:错误代码"+error_type,
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
		});


		offShelfButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("account_id", "" + id));
				params.add(new BasicNameValuePair("password", passwd));
				params.add(new BasicNameValuePair("account_type", "0"));
				params.add(new BasicNameValuePair("goods_id", goodsId));
				params.add(new BasicNameValuePair("type", "C"));
				Json json = new Json("/json_api/transact_goods/", params);
				
				try {
					while (json.getJsonObj() == null) {
					}
					int success = json.getJsonObj().getInt("success");

					if (success == 1) {
						Toast.makeText(getApplicationContext(), "下架成功",
								Toast.LENGTH_SHORT).show();
					} else {
						int error_type = json.getJsonObj().getInt("error_type");
						if (error_type == -5) {
							Toast.makeText(getApplicationContext(),
									"已经加入心愿单", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(getApplicationContext(), "未知错误:错误代码"+error_type,
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
		});
		

		submitCommentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				editComment=editCommentText.getText().toString();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("account_id", "" + id));
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
						int error_type = json.getJsonObj().getInt("error_type");
						if (error_type == -5) {
							Toast.makeText(getApplicationContext(),
									"已经加入心愿单）", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(getApplicationContext(), "未知错误:错误代码"+error_type,
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
		});
		
		
		editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(SellerGoodsDetail.this, EditGoods.class);
				intent.putExtra("GoodsId", goodsId);
				startActivity(intent);	
			}
		});
	}

	protected void onCreateView(Bundle savedInstanceState) {
	}
}
