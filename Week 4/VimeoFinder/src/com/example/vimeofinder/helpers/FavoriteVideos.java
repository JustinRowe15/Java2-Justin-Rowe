/*
 *  project		VimeoFinder
 * 
 *  package		com.example.vimeofinder.helpers
 * 
 *  @author		Justin Rowe
 * 
 *  date		Sep 27, 2013
 */
package com.example.vimeofinder.helpers;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

// TODO: Auto-generated Javadoc
/**
 * The Class FavoriteVideos.
 */
public class FavoriteVideos extends LinearLayout {
	
	/** The add. */
	Button add;
	
	/** The remove. */
	Button remove;
	
	/** The list. */
	Spinner list;
	
	/** The context. */
	Context context;
	
	/** The tweets. */
	ArrayList<String> tweets = new ArrayList<String>();
	
	/**
	 * Instantiates a new favorite videos.
	 *
	 * @param context the context
	 */
	public FavoriteVideos(Context context){
		super(context);
		this.context = context;
		LayoutParams layoutParams;
		
		tweets.add("Select Favorite");
		list = new Spinner(context);
		layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
		list.setLayoutParams(layoutParams);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, tweets);
		listAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		list.setAdapter(listAdapter);
		list.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){
				Log.i("FAVORITE SELECTED", parent.getItemAtPosition(pos).toString());
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.i("FAVORITE SELECTED", "NONE");
			}
			
		});
		
		updateFavorites();
		
		add = new Button(context);
		add.setText("+");
		remove = new Button(context);
		remove.setText("-");
		
		this.addView(list);
		this.addView(add);
		this.addView(remove);
		
		layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(layoutParams);
	}
	
	/**
	 * Update favorites.
	 */
	private void updateFavorites() {
		tweets.add("TWEET 1");
		tweets.add("TWEET 2");
	}
}
