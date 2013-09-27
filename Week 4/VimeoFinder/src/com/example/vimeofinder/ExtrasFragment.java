package com.example.vimeofinder;

import com.example.tweetfinder.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ExtrasFragment extends Fragment {

	private ExtrasListener listener;
	
	public interface ExtrasListener {
		public void onVimeoSearch();
	}
	
	Button extrasButton;
	EditText search;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (ExtrasListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement MainListener");
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.activity_extras, container, false);
		
		extrasButton = (Button) view.findViewById(R.id.extras_button);
		extrasButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	
				listener.onVimeoSearch();
				
			}
		});
		
		return view;
	}
}
