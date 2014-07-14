package zju.secondhandplatform.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

	/**
     * A placeholder fragment containing a simple view.
     */
public class PersonalInfo extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        
    	private Button editButton;
    	private TextView userNameText;
    	private TextView emailText;
    	private TextView phoneText;
    	private TextView zfbText;
    	private String userName;
    	private String email;
    	private String phone;
    	private String zfb;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PersonalInfo newInstance(int sectionNumber) {
            PersonalInfo fragment = new PersonalInfo();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PersonalInfo() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_personal_info, container, false);

            ClientApp clientApp=(ClientApp)getActivity().getApplicationContext();
        	if(clientApp.getId()==-1){       		
        		try{
        			Intent intent = new Intent();
        			intent.setClass(getActivity(), Login.class);
        			startActivity(intent);
        		}catch(Exception e){
        			e.printStackTrace();
        		}        	
        	}
        	else{
        		editButton = (Button) rootView.findViewById(R.id.personalEdit);
        		userNameText = (TextView) rootView.findViewById(R.id.personalUsernameContent);
        		emailText = (TextView) rootView.findViewById(R.id.personalEmailContent);
        		phoneText = (TextView) rootView.findViewById(R.id.personalCellphoneContent);
        		zfbText = (TextView) rootView.findViewById(R.id.personalZfbContent);
        		
        		int id = clientApp.getId();
        		String passwd = clientApp.getPassword();
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
        		params.add(new BasicNameValuePair("account_id", "" + id));
        		params.add(new BasicNameValuePair("password", passwd));
        		Json json = new Json("/json_api/get_account_info/", params);
        		
        		
        	}
        	
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
