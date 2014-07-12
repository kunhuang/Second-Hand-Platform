package zju.secondhandplatform.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

	/**
     * A placeholder fragment containing a simple view.
     */
public class WishList extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static ClientApp clientApp;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static WishList newInstance(int sectionNumber,ClientApp app) {
        	clientApp=app;
            WishList fragment = new WishList();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public WishList() {    
        	if(clientApp.getId()==-1){
        		Intent intent = new Intent();
                intent.setClass(this.getActivity(), Login.class);
                startActivityForResult(intent, 0);
        	}
        	else{
        		
        	}
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	View rootView = inflater.inflate(R.layout.fragment_wish_list, container, false);
 //           View rootView = inflater.inflate(R.layout.fragment_wish_list, container, false);
 //           TextView textView = (TextView) rootView.findViewById(R.id.section_label);
 //           textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
//            textView.setText("This is WishList");
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
