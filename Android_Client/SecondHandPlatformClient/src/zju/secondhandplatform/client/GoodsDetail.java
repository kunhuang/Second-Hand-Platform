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

public class GoodsDetail extends ListActivity {
	private Button addWishlistButton;
	private TextView goodsNameText;
	private TextView goodsPriceText;
	private TextView sellerNameText;
	private TextView goodsContentText;
	private String goodsName;
	private String goodsPrice;
	private String sellerName;
	private String goodsContent;

	private int seller_id;
	private String passwd;
	private String goodsId;

	private ListView listView;
	private SimpleAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_goods_detail);

		addWishlistButton = (Button) findViewById(R.id.detailAddWishlist);
		goodsNameText = (TextView) findViewById(R.id.detailGoodsName);
		goodsPriceText = (TextView) findViewById(R.id.detailGoodsPrice);
		sellerNameText = (TextView) findViewById(R.id.detailSellerName);
		goodsContentText = (TextView) findViewById(R.id.detailGoodsContent);
		listView = (ListView) findViewById(android.R.id.list);

		Intent intent = getIntent();
		goodsId = intent.getStringExtra("GoodsId");

		ClientApp clientApp = (ClientApp) this.getApplication();
		seller_id = clientApp.getId();
		passwd = clientApp.getPassword();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("seller_id", "" + seller_id));
		params.add(new BasicNameValuePair("password", passwd));
		params.add(new BasicNameValuePair("goods_id", goodsId));
		Json json = new Json("/json_api/get_goods_info/", params);

		List<NameValuePair> params2 = new ArrayList<NameValuePair>();
		params2.add(new BasicNameValuePair("account_id", "" + seller_id));
		params2.add(new BasicNameValuePair("password", passwd));
		params2.add(new BasicNameValuePair("goods_id", goodsId));
		Json json2 = new Json("/json_api/get_comment_array/", params2);
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
				sellerName = row.getString("seller_id");
				goodsContent = row.getString("description");

				goodsNameText.setText(goodsName);
				goodsPriceText.setText(goodsPrice);
				sellerNameText.setText(sellerName);
				goodsContentText.setText(goodsContent);

				List<HashMap<String, Object>> comments = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < 10; i++) {
					String content = "性价比超高！";

					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("content", content);
					comments.add(map);
				}
				adapter = new SimpleAdapter(this, comments,
						R.layout.comment_item, new String[] { "content" },
						new int[] { R.id.detailCommentContent });
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

		addWishlistButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("buyer_id", "" + seller_id));
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
						int error_type = json.getJsonObj().getInt("error_type");
						if (error_type == -5) {
							Toast.makeText(getApplicationContext(),
									"已经加入心愿单）", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(getApplicationContext(), "未知错误",
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
	}

	protected void onCreateView(Bundle savedInstanceState) {
	}
}
