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

public class EditUserInfo extends Activity {
	private Button EditUserInfoButton;
	private EditText userNameText;
	private EditText emailText;
	private EditText phoneText;
	private EditText zfbText;
	private String userName;
	private String email;
	private String phone;
	private String zfb;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_edit_user_info);

		EditUserInfoButton = (Button) findViewById(R.id.editUser);
		userNameText = (EditText) findViewById(R.id.editUserUsernameInput);
		emailText = (EditText) findViewById(R.id.editUserEmailInput);
		phoneText = (EditText) findViewById(R.id.editUserPwInput);
		zfbText = (EditText) findViewById(R.id.editUserRepwInput);

		EditUserInfoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				userName = userNameText.getText().toString();
				email = emailText.getText().toString();
				phone = phoneText.getText().toString();
				zfb = zfbText.getText().toString();

				ClientApp clientApp = (ClientApp) getApplicationContext();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("account_id", ""+clientApp.getId()));
				params.add(new BasicNameValuePair("password", clientApp.getPassword()));
				params.add(new BasicNameValuePair("name", userName));
				params.add(new BasicNameValuePair("email", email));
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("bank_card", zfb));

				Json json = new Json("/json_api/edit_account_info/", params);
				try {
					while (json.getJsonObj() == null) {
					}
					int success = json.getJsonObj().getInt("success");

					if (success == 1) {
						Toast.makeText(getApplicationContext(), "±‡º≠≥…π¶",
								Toast.LENGTH_SHORT).show();
					} else {
						int error_type = json.getJsonObj().getInt("error_type");
						if (error_type == -2) {
							Toast.makeText(getApplicationContext(),
									"’À∫≈“—¥Ê‘⁄£®” œ‰÷ÿ∏¥£©", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "Œ¥÷™¥ÌŒÛ",
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
