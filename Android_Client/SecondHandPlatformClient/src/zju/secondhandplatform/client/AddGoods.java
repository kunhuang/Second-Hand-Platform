package zju.secondhandplatform.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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

public class AddGoods extends Activity {
	private Button AddGoodsButton;
	private Button imgButton;
	private EditText goodsNameText;
	private EditText goodsPriceText;
	private EditText goodsContentText;
	private String goodsName;
	private String goodsPrice;
	private String goodsContent;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_seller_add_goods);

		AddGoodsButton = (Button) findViewById(R.id.button1);
		imgButton = (Button) findViewById(R.id.button2);
		goodsNameText = (EditText) findViewById(R.id.editText1);
		goodsPriceText = (EditText) findViewById(R.id.editText2);
		goodsContentText = (EditText) findViewById(R.id.editText4);

		AddGoodsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				goodsName = goodsNameText.getText().toString();
				goodsPrice = goodsPriceText.getText().toString();
				goodsContent = goodsContentText.getText().toString();

				ClientApp clientApp = (ClientApp) getApplication();
				int id = clientApp.getId();
				String passwd = clientApp.getPassword();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("seller_id", "" + id));
				params.add(new BasicNameValuePair("password", passwd));
				params.add(new BasicNameValuePair("name", goodsName));
				params.add(new BasicNameValuePair("description", goodsContent));
				params.add(new BasicNameValuePair("pure_price", goodsPrice));

				Json json = new Json("/json_api/add_goods/", params);
				try {
					while (json.getJsonObj() == null) {
					}
					int success = json.getJsonObj().getInt("success");

					if (success == 1) {
						Toast.makeText(getApplicationContext(), "添加成功",
								Toast.LENGTH_SHORT).show();
					} else {
						int error_type = json.getJsonObj().getInt("error_type");
						if (error_type == -2) {
							Toast.makeText(getApplicationContext(),
									"账号已存在（邮箱重复）", Toast.LENGTH_SHORT).show();
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

		imgButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				File file = new File("goods_img.jpg");
				String httpUrl = "http://10.180.26.157:8000/json_api/add_photo/";
				HttpPost request = new HttpPost(httpUrl);
				HttpClient httpClient = new DefaultHttpClient();
				FileEntity entity = new FileEntity(file, "binary/octet-stream");
				HttpResponse response;
				try {
					request.setEntity(entity);
					entity.setContentEncoding("binary/octet-stream");
					response = httpClient.execute(request);

					// 如果返回状态为200，获得返回的结果
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						// 图片上传成功
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	protected void onCreateView(Bundle savedInstanceState) {
	}
}
