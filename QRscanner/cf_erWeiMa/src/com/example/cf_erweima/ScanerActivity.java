package com.example.cf_erweima;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.covics.zxingscanner.OnDecodeCompletionListener;
import com.covics.zxingscanner.ScannerView;

public class ScanerActivity extends Activity implements
		OnDecodeCompletionListener {
	// 向布局中添加扫描区
	private ScannerView cScannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scaner);
		init();
	}

	/**
	 * 对扫描区域初始化，将其绑定到decodeListener上
	 * 
	 * @version 
	 */
	private void init() {
		cScannerView = (ScannerView) findViewById(R.id.scanner_view);
		cScannerView.setOnDecodeListener(this);
	}

	/**
	 * 二维码解析结束后
	 * 
	 * @version 
	 * @param barcodeFormat
	 * @param barcode
	 * @param bitmap
	 */
	@Override
	public void onDecodeCompletion(String barcodeFormat, String barcode,
			Bitmap bitmap) {
		if (barcode != null) {
			Intent intent = new Intent();
			intent.putExtra("result", barcode);
			setResult(0x02, intent);
			this.finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		cScannerView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		cScannerView.onPause();
	}
}
