package zju.secondhandplatform.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

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
	private String seller_id;

	private String id;
	private String passwd;
	private String goodsId;

	private SimpleAdapter adapter;
	
	private Bitmap bitmapQR;

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
		id = ""+clientApp.getId();
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
		// {"data":{"total":1,"rows":[{"id":3,"content":"editComment","time":1405388190,"account_id":8,"goods_id":32}]},"success":1}
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
				seller_id=row.getString("seller_id");

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
				while (commentJson.getJsonObj() == null) {
				}
				JSONObject commentData = commentJson.getJsonObj()
						.getJSONObject("data");
				int commentTotal = commentData.getInt("total");
				JSONArray commentRows = commentData.getJSONArray("rows");
				for (int i = 0; i < commentTotal; i++) {
					JSONObject commentRow = commentRows.getJSONObject(i);
					String content = commentRow.getString("content");
					Long timeLong = commentRow.getLong("time");
		//			String accountName = "匿名用户              ";
					String accountId = commentRow.getString("account_id");

					List<NameValuePair> params3 = new ArrayList<NameValuePair>();
					params3.add(new BasicNameValuePair("account_id", ""
							+ accountId));
					params3.add(new BasicNameValuePair("password", "123"));
					Json json3 = new Json("/json_api/get_account_info/",
							params3);
					while (json3.getJsonObj() == null) {
					}
					// {"data":{"total":1,"rows":[{"id":4,"buy_exp":0,"phone":"1234","bank_card":0,"type_seller":true,"email":"11kunhuang110@gmail.com","name":"hk","password":"12","sell_exp":0,"type_buyer":true}]},"success":1}
					JSONObject jsonData3 = json3.getJsonObj()
							.getJSONObject("data");
					// int total=data.getInt("total");
					JSONArray rows3 = jsonData3.getJSONArray("rows");
					JSONObject row3 = rows3.getJSONObject(0);
					String accountName = row3.getString("name");
					
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String time=sdf.format(new Date(timeLong*1000L));
					
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("content", content);
					map.put("time", time);
					map.put("accountName", accountName);
					comments.add(map);
				}
				adapter = new SimpleAdapter(this, comments,
						R.layout.comment_item, new String[] { "content",
								"time", "accountName" }, new int[] {
								R.id.detailCommentContent, R.id.commentTime2,
								R.id.commentName2 });
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
					params.add(new BasicNameValuePair("buyer_id", ""+ id));
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
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("account_id", "" + id));
					params.add(new BasicNameValuePair("password", passwd));
					Json json = new Json("/json_api/get_account_info/", params);
					String zfb=null;
					try {
						while (json.getJsonObj() == null) {
						}
						JSONObject data = json.getJsonObj().getJSONObject(
								"data");
						JSONArray rows = data.getJSONArray("rows");
						JSONObject row = rows.getJSONObject(0);
						zfb = row.getString("bank_card");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					
					/**@author LSL
					 * @content function generateQR
					 * @modifiedTime 2014-07-16 14:42
					 * */
					generateQR(goodsId, id, goodsPrice,seller_id);
					
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
	
	/**@author LSL
	 * @content function generateQR 生成二维码并在弹框中显示
	 * @modifiedTime 2014-07-16 14:42
	 * */
	void generateQR(String goodsIdQR, String idQR, String goodsPriceQR,String seller_id){
		//设置弹框内图片域
		ImageView imgQR = new ImageView(this);
		//imgQR.setImageResource(R.drawable.goods_img);
		
		//生成二维码
		try {
			// 调用createArtwork将传入信息转为二维码
			JSONObject jsonobj=new JSONObject();
			jsonobj.put("goodsID",goodsIdQR);
			jsonobj.put("ID",idQR);
			jsonobj.put("goodsPrice",goodsPriceQR);
			jsonobj.put("seller_id",seller_id);
			jsonobj.put("type","P");
			bitmapQR = createArtwork(jsonobj.toString());
			// 将生成的二维码显示在图片区域
			if (bitmapQR != null) {
				imgQR.setImageBitmap(bitmapQR);
				imgQR.invalidate();
				imgQR.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		//显示弹框
		new AlertDialog.Builder(this)
		.setTitle("二维码")
		.setView(imgQR)
		.setPositiveButton("确定", null)
		.show();
	}
	
	/**@author LSL
	 * @content function createArtwork 生成二维码图片
	 * @modifiedTime 2014-07-16 17:13
	 * */
	public Bitmap createArtwork(String str) throws WriterException {
		Bitmap res = Bitmap.createBitmap(300, 300, Config.ARGB_8888);

		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, 300, 300);
		int width = matrix.getWidth();
		int height = matrix.getHeight();

		final int WHITE = 0xFFFFFFFF;
		final int BLACK = 0xFF000000;
		final int RED = 0xFFFF0000;
		final int BLUE = 0xFF0000FF;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					if ((x > 60 && x < 120 && y > 73 && y < 77)
							|| (x > 58 && x < 62 && y >= 75 && y <= 225)
							|| (x > 60 && x < 120 && y > 223 && y < 227)) {
						pixels[y * width + x] = RED;
						// pixels[y * width + x-2] = BLACK ;
					} else if ((x > 180 && x < 240 && y > 73 && y < 77)
							|| (x > 178 && x < 182 && y >= 75 && y <= 225)
							|| (x > 180 && x < 240 && y > 148 && y < 152)) {
						pixels[y * width + x] = BLUE;
					} else {
						pixels[y * width + x] = BLACK;
					}
				} else {
					pixels[y * width + x] = WHITE;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
		bitmap.setPixel(0, 0, WHITE);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		// 绘图
		Canvas canvas = new Canvas(res);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		canvas.drawBitmap(bitmap, 0, 0, null);

		return res;
	}
}
