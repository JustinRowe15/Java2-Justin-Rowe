package com.example.vimeofinder;

import com.example.tweetfinder.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainFragment extends Fragment {
	
	private MainListener listener;
	
	public interface MainListener {
		public void onVideoSearch();
		public void onExtrasSearch();
	}
	
	Button extrasButton;
	Button searchButton;
	EditText search;

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (MainListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement MainListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.form, container, false);
		
		//SEARCH BUTTON
		searchButton = (Button) view.findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onVideoSearch();
			}
		});
		
		//GO TO EXTRAS BUTTON
		extrasButton = (Button) view.findViewById(R.id.extrasButton);
		extrasButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onExtrasSearch();
			}
		});
		
		return view;
	};
	
}
