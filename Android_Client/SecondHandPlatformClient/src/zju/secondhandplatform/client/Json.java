package zju.secondhandplatform.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.util.Log;

public class Json {
	private String httpurl = "http://192.16.137.1:8000";
	private List<NameValuePair> postParams;
	private String data = "";
	private JSONObject jsonObj = null;

	Json(String url, List<NameValuePair> postParams) {
		httpurl = httpurl + url;
		this.postParams = postParams;
		new HttpGetTask().execute();
	}

	public JSONObject getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}

	private class HttpGetTask extends AsyncTask<Void, Integer, String> {
		private static final String TAG = "HttpGetTask";

		public HttpGetTask() {
		}

		@Override
		protected String doInBackground(Void... p) {
			HttpURLConnection httpUrlConnection = null;
			HttpPost httpPost = new HttpPost(httpurl);
			HttpResponse httpResponse;

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(postParams,
						HTTP.UTF_8));
				Log.d(TAG, "About to connect to "+ httpurl);
				httpResponse = new DefaultHttpClient().execute(httpPost);
				Log.d(TAG, "Connectted");
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity());
					Log.d(TAG, "Get data");
					setJsonObj(new JSONObject(data));
				} else {
					Log.e(TAG, "ÍøÂçÁ¬½Ó´íÎó"
							+ httpResponse.getStatusLine().getStatusCode());
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}

			return data;
		}

		@Override
		protected void onPostExecute(String result) {}
	}
}
