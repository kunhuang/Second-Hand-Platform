package zju.secondhandplatform.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Json {
	// URL url = new URL("http://10.180.47.68:8000/json_api/test/");
	// HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	String url = "http://10.180.36.203:8000/json_api/test/";
	HttpClient httpClient = new DefaultHttpClient();
	HttpPost httpRequest = new HttpPost(url);/* 建立HTTP Post连线 */
	HttpResponse httpResponse;

	Json(String url,List<NameValuePair> params) {
//		this.url="http://10.180.36.203:8000"+url;
		// Post运作传送变数必须用NameValuePair[]阵列储存
		// 传参数 服务端获取的方法为request.getParameter("name")

		try {
			// 发出HTTP request
	//		httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 取得HTTP responseHttpClient httpClient = new DefaultHttpClient(); 
			HttpGet httpRequest = new HttpGet("http://10.180.36.203:8000/json_api/test/");
			httpResponse = httpClient.execute(httpRequest);

			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取出回应字串
				String strResult = EntityUtils.toString(httpResponse
						.getEntity(),"Unicode");
//				JSONTokener jsonParser = new JSONTokener(strResult); 
				try {
					JSONArray jsonArray = new JSONArray(strResult);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

		//		strResult = "[" + strResult + "]";
		//		JSONArray ct = new JSONArray();
	    //        ct=JSONArray.fromObject(strResult);
			} else {
//				textView1.setText("Error Response"
//						+ httpResponse.getStatusLine().toString());
			}
		} catch (ClientProtocolException e) {
//			textView1.setText(e.getMessage().toString());
//			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
//			textView1.setText(e.getMessage().toString());
//			e.printStackTrace();
		} catch (IOException e) {
//			e.getMessage().toString();
			e.printStackTrace();
		}
	}
}
