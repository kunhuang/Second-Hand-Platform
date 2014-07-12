package zju.secondhandplatform.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Login extends Activity {
	private Button loginButton;
	private Button registerButton;
	private TextView userNameText;
	private TextView passwdText;
	private String userName;
	private String passwd;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		loginButton = (Button) findViewById(R.id.login);
		registerButton = (Button) findViewById(R.id.loginRegist);
		userNameText = (TextView) findViewById(R.id.loginUsername);
		passwdText = (TextView) findViewById(R.id.loginPw);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				userName = (String) userNameText.getText();
				passwd = (String) passwdText.getText();

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("email", userName));
				params.add(new BasicNameValuePair("password", passwd));

				Json json = new Json("/json_api/get_account_id/", params);
				try {
					while (json.getJsonObj() == null) {
					}
					int success = json.getJsonObj().getInt("success");

					if (success == 1) {
						ClientApp clientApp = (ClientApp) getApplication();
						clientApp.setId(json.getJsonObj().getInt("id"));

						Intent intent = new Intent();
						intent.setClass(Login.this, MainActivity.class);
						startActivity(intent);
					} else {
						int error_type = json.getJsonObj().getInt("error_type");
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			Intent intent = new Intent();
			try {
				intent.setClass(Login.this, MainActivity.class);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return super.onKeyDown(keyCode, event);
	}
}
