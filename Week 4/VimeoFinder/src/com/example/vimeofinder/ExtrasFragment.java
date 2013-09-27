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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class ExtrasFragment.
 */
public class ExtrasFragment extends Fragment {

	/** The listener. */
	private ExtrasListener listener;
	
	/**
	 * The listener interface for receiving extras events.
	 * The class that is interested in processing a extras
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addExtrasListener<code> method. When
	 * the extras event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ExtrasEvent
	 */
	public interface ExtrasListener {
		
		/**
		 * On vimeo search.
		 */
		public void onVimeoSearch();
	}
	
	/** The extras button. */
	Button extrasButton;
	
	/** The search. */
	EditText search;
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (ExtrasListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement MainListener");
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
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
