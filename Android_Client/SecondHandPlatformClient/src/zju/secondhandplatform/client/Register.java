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

public class Register extends Activity {
	private Button registerButton;
	private EditText userNameText;
	private EditText emailText;
	private EditText passwdText;
	private EditText rePasswdText;
	private String userName;
	private String email;
	private String passwd;
	private String rePasswd;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);

		registerButton = (Button) findViewById(R.id.regist);
		userNameText = (EditText) findViewById(R.id.registUsernameInput);
		emailText = (EditText) findViewById(R.id.registEmailInput);
		passwdText = (EditText) findViewById(R.id.registPwInput);
		rePasswdText = (EditText) findViewById(R.id.registRepwInput);

		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				userName = userNameText.getText().toString();
				email = emailText.getText().toString();
				passwd = passwdText.getText().toString();
				rePasswd = rePasswdText.getText().toString();

				if (!passwd.equals(rePasswd)) {
					Toast.makeText(getApplicationContext(), "两次输入密码不一致！",
							Toast.LENGTH_SHORT).show();
				} else {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("name", userName));
					params.add(new BasicNameValuePair("email", email));
					params.add(new BasicNameValuePair("password", passwd));

					Json json = new Json("/json_api/add_account/", params);
					try {
						while (json.getJsonObj() == null) {
						}
						int success = json.getJsonObj().getInt("success");

						if (success == 1) {
							Toast.makeText(getApplicationContext(), "注册成功",
									Toast.LENGTH_SHORT).show();
							ClientApp clientApp = (ClientApp) getApplication();
							Intent intent = new Intent();
							intent.setClass(Register.this, Login.class);
							startActivity(intent);
						} else {
							int error_type = json.getJsonObj().getInt(
									"error_type");
							if (error_type == -2) {
								Toast.makeText(getApplicationContext(),
										"账号已存在（邮箱重复）", Toast.LENGTH_SHORT)
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
				intent.setClass(Register.this, Login.class);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return super.onKeyDown(keyCode, event);
	}
}
