package zju.secondhandplatform.client;

import java.util.ArrayList;
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
public class WishList extends ListFragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	private SimpleAdapter adapter;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static WishList newInstance(int sectionNumber) {
		WishList fragment = new WishList();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public WishList() {

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
			params.add(new BasicNameValuePair("buyer_id", ""
					+ clientApp.getId()));
			params.add(new BasicNameValuePair("password", clientApp
					.getPassword()));
			Json json = new Json("/json_api/get_wishlist/", params);
			try {
				while (json.getJsonObj() == null) {
				}
				int success = json.getJsonObj().getInt("success");

				// {"data":{"total":2,"rows":[{"id":2,"time":1405319645,"account_id":8,"payed":false,"goods_id":1},{"id":4,"time":1405391283,"account_id":8,"payed":false,"goods_id":32}]},"success":1}
				if (success == 1) {
					JSONObject jsonData = json.getJsonObj().getJSONObject(
							"data");
					int total = jsonData.getInt("total");
					JSONArray rows = jsonData.getJSONArray("rows");
					for (int i = 0; i < total; i++) {
						JSONObject row = rows.getJSONObject(i);
						String goodsId = row.getString("goods_id");
						Boolean payed = row.getBoolean("payed");
						String time = row.getString("time");
						// String goodsName = row.getString("name");
						// String price = row.getString("pure_price");

						String state;
						if (payed == false) {
							state = "未支付";
						} else {
							state = "已支付";
						}

						int id = clientApp.getId();
						String passwd = clientApp.getPassword();
						List<NameValuePair> params2 = new ArrayList<NameValuePair>();
						params2.add(new BasicNameValuePair("seller_id", "" + id));
						params2.add(new BasicNameValuePair("password", passwd));
						params2.add(new BasicNameValuePair("goods_id", goodsId));
						Json json2 = new Json("/json_api/get_goods_info/",
								params2);
						while(json2.getJsonObj()==null){}
						JSONObject josnData = json2.getJsonObj().getJSONObject(
								"data");
						// int total=data.getInt("total");
						JSONArray jsonRows = josnData.getJSONArray("rows");
						JSONObject jsonRow = jsonRows.getJSONObject(0);
						String goodsName = jsonRow.getString("name");
						
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("goodsId", goodsName);
						map.put("state", state);
						map.put("time", time);
						// map.put("goodsName", goodsName);
						// map.put("price", price);
						data.add(map);
					}
					adapter = new SimpleAdapter(this.getActivity(), data,
							R.layout.wish_list_item, new String[] { "goodsId",
									"state", "time" }, new int[] {
									R.id.wishlistItemGoodsName,
									R.id.wishlistItemStatusContent,
									R.id.wishlistItemTimeContent });
					// adapter = new SimpleAdapter(this.getActivity(), data,
					// R.layout.goods_item, new String[] { "goodsName",
					// "price" },
					// new int[] { R.id.sellerGoodsName,
					// R.id.sellerGoodsPrice });
					try {
						setListAdapter(adapter);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					int error_type = json.getJsonObj().getInt("error_type");
					if (error_type == -5) {
						Toast.makeText(
								this.getActivity().getApplicationContext(),
								"已经加入心愿单", Toast.LENGTH_SHORT).show();
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

		View rootView = inflater.inflate(R.layout.fragment_wish_list,
				container, false);

		return rootView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		HashMap<String, Object> view = (HashMap<String, Object>) l
				.getItemAtPosition(position);
		String goodsId = view.get("goodsId").toString();

		Intent intent = new Intent();
		intent.setClass(this.getActivity(), BuyerGoodsDetail.class);
		intent.putExtra("GoodsId", goodsId);
		startActivity(intent);
	}

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
