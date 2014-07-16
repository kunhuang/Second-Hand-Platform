package com.example.cf_erweima;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	private Button cButtonScan;
	private TextView cTextView;
//	private Button cButtonCreate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		cButtonScan = (Button) findViewById(R.id.btn_scan);//‘扫描二维码’按钮
		//cButtonCreate = (Button) findViewById(R.id.btn_create);//‘生成二维码’按钮
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
		cButtonCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						CreateActivity.class);
				startActivity(intent);
			}
		});
		*/
	}


	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0x01 && resultCode == 0x02 && data != null) {
			if (data.getExtras().containsKey("result")) {
				cTextView.setText(data.getExtras().getString("result"));
			}
		}
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "鍏充簬").setIcon(

		android.R.drawable.ic_menu_info_details);

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "甯姪").setIcon(

		android.R.drawable.ic_menu_help);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			new AlertDialog.Builder(this)
					.setMessage(
							"浣滆�:钄℃湁椋瀄n\n鐗堟潈褰掍笂娴锋寔鍒涗俊鎭妧鏈湁闄愬叕鍙告墍鏈塡n\n浠讳綍浜轰笉寰椾慨鏀规湰绋嬪簭鍚庡浼犳湰浣滃搧 ")
					.setPositiveButton("纭畾",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// 鎸夐挳浜嬩欢
								}
							}).setIcon(android.R.drawable.ic_menu_info_details)
					.setTitle("浣滆�").show();
			break;

		case Menu.FIRST + 2:

			new AlertDialog.Builder(this)
					.setMessage("浣跨敤杩囩▼涓鏈夐棶棰樻垨寤鸿\n璇峰彂閭欢鑷砪aiyoufei@looip.cn")
					.setPositiveButton("纭畾",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// 鎸夐挳浜嬩欢
								}
							}).setTitle("甯姪")
					.setIcon(android.R.drawable.ic_menu_help).show();
			break;
		}
		return false;
	}
	*/

}
