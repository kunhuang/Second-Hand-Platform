package zju.secondhandplatform.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageCenter extends ListFragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	private SimpleAdapter adapter;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MessageCenter newInstance(int sectionNumber) {
		MessageCenter fragment = new MessageCenter();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public MessageCenter() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ClientApp clientApp = (ClientApp) getActivity().getApplicationContext();
		if (clientApp.getId() == -1) {
			try {
				Intent intent = new Intent();
				intent.setClass(getActivity(), Login.class);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("account_id", ""
					+ clientApp.getId()));
			params.add(new BasicNameValuePair("password", clientApp
					.getPassword()));
			Json json = new Json("/json_api/get_message_array/", params);
			try {
				while (json.getJsonObj() == null) {
				}
				int success = json.getJsonObj().getInt("success");

				//{"data":{"total":1,"rows":[{"id":1,"state":null,"content":"test","time":10000000,"recv_account_id":8,"send_account_id":0,"subject":"hello"}]},"success":1}
				if (success == 1) {
					JSONObject jsonData = json.getJsonObj().getJSONObject(
							"data");
					int total = jsonData.getInt("total");
					JSONArray rows = jsonData.getJSONArray("rows");
					for (int i = 0; i < total; i++) {
						JSONObject row = rows.getJSONObject(i);
						String subject = row.getString("subject");
//						String state = row.getString("type");
						String content=row.getString("content");
						Long timeLong = row.getLong("time");
						String send_account_id = row.getString("send_account_id");
						
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						String time=sdf.format(new Date(timeLong*1000L));

						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("subject", subject);
						map.put("content", content);
						map.put("time", time);
						map.put("send_account_id", "系统");
						data.add(map);
					}
					adapter = new SimpleAdapter(this.getActivity(), data,
							R.layout.message_item, new String[] { "subject",
									"content","time","send_account_id"},
							new int[] { R.id.msgTheme2,R.id.textView1,
									R.id.msgTime2,R.id.msgSender2 });
					try {
						setListAdapter(adapter);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					int error_type = json.getJsonObj().getInt("error_type");
					if (error_type == -7) {
						Toast.makeText(
								this.getActivity().getApplicationContext(),
								"账户ID不存在", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(
								this.getActivity().getApplicationContext(),
								"未知错误:错误代码" + error_type, Toast.LENGTH_SHORT)
								.show();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_message_center,
				container, false);

		return rootView;
	}

//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//		super.onListItemClick(l, v, position, id);
//
//		HashMap<String, Object> view = (HashMap<String, Object>) l
//				.getItemAtPosition(position);
//		String goodsId = view.get("goodsId").toString();
//
//		Intent intent = new Intent();
//		intent.setClass(this.getActivity(), BuyerGoodsDetail.class);
//		intent.putExtra("GoodsId", goodsId);
//		startActivity(intent);
//	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
