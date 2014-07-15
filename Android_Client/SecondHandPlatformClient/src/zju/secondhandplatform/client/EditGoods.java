package zju.secondhandplatform.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditGoods extends Activity {
	private Button EditGoodsButton;
	private EditText goodsNameText;
	private EditText goodsPriceText;
	private EditText goodsContentText;
	private String goodsName;
	private String goodsPrice;
	private String goodsContent;
	
	private String goodsId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_seller_add_goods);

		Intent intent = getIntent();
		goodsId = intent.getStringExtra("GoodsId");
		
		EditGoodsButton = (Button) findViewById(R.id.button1);
		goodsNameText = (EditText) findViewById(R.id.editText1);
		goodsPriceText = (EditText) findViewById(R.id.editText2);
		goodsContentText = (EditText) findViewById(R.id.editText4);

		EditGoodsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				goodsName = goodsNameText.getText().toString();
				goodsPrice = goodsPriceText.getText().toString();
				goodsContent = goodsContentText.getText().toString();

				ClientApp clientApp = (ClientApp) getApplication();
				int id=clientApp.getId();
				String passwd=clientApp.getPassword();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("seller_id", ""+id));
				params.add(new BasicNameValuePair("password", passwd));
				params.add(new BasicNameValuePair("goods_id", goodsId));
				params.add(new BasicNameValuePair("name", goodsName));
				params.add(new BasicNameValuePair("description", goodsContent));
				params.add(new BasicNameValuePair("pure_price", goodsPrice));

				Json json = new Json("/json_api/edit_goods_info/", params);
				try {
					while (json.getJsonObj() == null) {
					}
					int success = json.getJsonObj().getInt("success");

					if (success == 1) {
						Toast.makeText(getApplicationContext(), "修改成功",
								Toast.LENGTH_SHORT).show();
					} else {
						int error_type = json.getJsonObj().getInt("error_type");
						if (error_type == -4) {
							Toast.makeText(getApplicationContext(),
									"没有权限操作的goods（如别人的goods，只能在未上架的情况下修改价格）", Toast.LENGTH_SHORT).show();
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
