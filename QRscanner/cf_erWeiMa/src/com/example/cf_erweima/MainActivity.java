package com.example.cf_erweima;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button cButtonScan;
	private TextView cTextView;

	// private Button cButtonCreate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		cButtonScan = (Button) findViewById(R.id.btn_scan);// ‘扫描二维码’按钮
		// cButtonCreate = (Button) findViewById(R.id.btn_create);//‘生成二维码’按钮
		cTextView = (TextView) findViewById(R.id.tv_info);
		cButtonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ScanerActivity.class);
				startActivityForResult(intent, 0x01);
			}
		});
		/*
		 * cButtonCreate.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(MainActivity.this, CreateActivity.class);
		 * startActivity(intent); } });
		 */
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0x01 && resultCode == 0x02 && data != null) {
			if (data.getExtras().containsKey("result")) {
				String result = data.getExtras().getString("result");
				cTextView.setText(result);

				try {
					JSONObject resultJson = new JSONObject(result);
					String type = resultJson.getString("type");
					if (type.equals("P")) {
						String goodsPrice = resultJson.getString("goodsPrice");
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("account_id", ""
								+ resultJson.getString("seller_id")));
						params.add(new BasicNameValuePair("password", "123"));
						Json json1 = new Json("/json_api/get_account_info/",
								params);
						while (json1.getJsonObj() == null) {
						}
						// {"data":{"total":1,"rows":[{"id":4,"buy_exp":0,"phone":"1234","bank_card":0,"type_seller":true,"email":"11kunhuang110@gmail.com","name":"hk","password":"12","sell_exp":0,"type_buyer":true}]},"success":1}
						JSONObject jsonData1 = json1.getJsonObj()
								.getJSONObject("data");
						// int total=data.getInt("total");
						JSONArray rows1 = jsonData1.getJSONArray("rows");
						JSONObject row1 = rows1.getJSONObject(0);
						String zfb = row1.getString("bank_card");

						// Toast.makeText(getApplicationContext(),
						// "使用空付支付！bibibibi~~", Toast.LENGTH_LONG).show();
						Toast toast = Toast.makeText(getApplicationContext(),
								"使用空付支付！bibibibi~~", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();

						kongfu(zfb, goodsPrice);// 使用空付支付

						// 更改商品状态位支付
						List<NameValuePair> params2 = new ArrayList<NameValuePair>();
						params2.add(new BasicNameValuePair("account_id",
								resultJson.getString("ID")));
						params2.add(new BasicNameValuePair("password", "123"));
						params2.add(new BasicNameValuePair("account_type", "1"));
						params2.add(new BasicNameValuePair("goods_id",
								resultJson.getString("goodsID")));
						params2.add(new BasicNameValuePair("type", "B"));
						Json json2 = new Json("/json_api/transact_goods/",
								params2);

						try {
							while (json2.getJsonObj() == null) {
							}
							int success = json2.getJsonObj().getInt("success");

							if (success == 1) {
								Toast.makeText(getApplicationContext(), "使用空付支付！bibibibi~~，支付成功",
										Toast.LENGTH_SHORT).show();
							} else {
								int error_type = json2.getJsonObj().getInt(
										"error_type");
								if (error_type == -4) {
									Toast.makeText(
											getApplicationContext(),
											"没有权限操作的goods（如别人的goods，只能在未上架的情况下修改价格）",
											Toast.LENGTH_SHORT).show();
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
					} else if (type.equals("O")) {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("account_id", ""
								+ resultJson.getString("seller_id")));
						params.add(new BasicNameValuePair("password", "123"));
						params.add(new BasicNameValuePair("account_type", "0"));
						params.add(new BasicNameValuePair("goods_id",
								resultJson.getString("goodsID")));
						params.add(new BasicNameValuePair("type", "O"));
						Json json = new Json("/json_api/transact_goods/",
								params);

						try {
							while (json.getJsonObj() == null) {
							}
							int success = json.getJsonObj().getInt("success");

							if (success == 1) {
								Toast.makeText(getApplicationContext(), "上架成功",
										Toast.LENGTH_SHORT).show();
							} else {
								int error_type = json.getJsonObj().getInt(
										"error_type");
								if (error_type == -4) {
									Toast.makeText(
											getApplicationContext(),
											"没有权限操作的goods（如别人的goods，只能在未上架的情况下修改价格）",
											Toast.LENGTH_SHORT).show();
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
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private int kongfu(String zfb, String goodsPrice) {
//
//		TimerTask task=new TimerTask(){
//			public void run(){
//				
//			}
//		};
//		Timer timer=new Timer();
//		timer.schedule(task, 100000);
		
//				new Thread(new Runnable(){
//			public void run(){
//				try {
//					Thread.sleep(200000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
		
		return 0;
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * menu.add(Menu.NONE, Menu.FIRST + 1, 1, "鍏充簬").setIcon(
	 * 
	 * android.R.drawable.ic_menu_info_details);
	 * 
	 * menu.add(Menu.NONE, Menu.FIRST + 2, 2, "甯姪").setIcon(
	 * 
	 * android.R.drawable.ic_menu_help);
	 * 
	 * return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case Menu.FIRST + 1: new AlertDialog.Builder(this)
	 * .setMessage(
	 * "浣滆�:钄℃湁椋瀄n\n鐗堟潈褰掍笂娴锋寔鍒涗俊鎭妧鏈湁闄愬叕鍙告墍鏈塡n\n浠讳綍浜轰笉寰椾慨鏀规湰绋嬪簭鍚庡浼犳湰浣滃搧 ")
	 * .setPositiveButton("纭畾", new DialogInterface.OnClickListener() { public
	 * void onClick( DialogInterface dialoginterface, int i) { // 鎸夐挳浜嬩欢 }
	 * }).setIcon(android.R.drawable.ic_menu_info_details)
	 * .setTitle("浣滆�").show(); break;
	 * 
	 * case Menu.FIRST + 2:
	 * 
	 * new AlertDialog.Builder(this)
	 * .setMessage("浣跨敤杩囩▼涓鏈夐棶棰樻垨寤鸿\n璇峰彂閭欢鑷砪aiyoufei@looip.cn")
	 * .setPositiveButton("纭畾", new DialogInterface.OnClickListener() { public
	 * void onClick( DialogInterface dialoginterface, int i) { // 鎸夐挳浜嬩欢 }
	 * }).setTitle("甯姪") .setIcon(android.R.drawable.ic_menu_help).show();
	 * break; } return false; }
	 */

}
