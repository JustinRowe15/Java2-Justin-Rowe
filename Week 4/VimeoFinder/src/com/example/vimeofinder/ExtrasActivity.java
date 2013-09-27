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
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// TODO: Auto-generated Javadoc
/**
 * The Class ExtrasActivity.
 */
public class ExtrasActivity extends Activity implements ExtrasFragment.ExtrasListener {
	
	/** The username. */
	String username;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.extras_fragment);
		setTheme(R.style.AppTheme);
		setTheme(R.style.VimeoLabel);
		setTheme(R.style.VimeoInfo);
		
		
	}

	/* (non-Javadoc)
	 * @see com.example.vimeofinder.ExtrasFragment.ExtrasListener#onVimeoSearch()
	 */
	@Override
	public void onVimeoSearch() {
		
		Bundle data = getIntent().getExtras();
		if(data != null){
		  username = data.getString("myKey");
		}
		
		String vimeoURL = "https://vimeo.com/" + username + "/videos";
		
		Intent internetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(vimeoURL));
		internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
		internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(internetIntent);
		
	}
}
