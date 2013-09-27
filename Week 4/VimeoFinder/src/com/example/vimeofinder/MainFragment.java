/*
 *  project		VimeoFinder
 * 
 *  package		com.example.vimeofinder
 * 
 *  @author		Justin Rowe
 * 
 *  date		Sep 27, 2013
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class MainFragment.
 */
public class MainFragment extends Fragment {
	
	/** The listener. */
	private MainListener listener;
	
	/**
	 * The listener interface for receiving main events.
	 * The class that is interested in processing a main
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMainListener<code> method. When
	 * the main event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MainEvent
	 */
	public interface MainListener {
		
		/**
		 * On video search.
		 */
		public void onVideoSearch();
		
		/**
		 * On extras search.
		 */
		public void onExtrasSearch();
	}
	
	/** The extras button. */
	Button extrasButton;
	
	/** The search button. */
	Button searchButton;
	
	/** The search. */
	EditText search;

	/* (non-Javadoc)
	 * @see android.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (MainListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement MainListener");
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
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
