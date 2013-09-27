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

public class ExtrasActivity extends Activity implements ExtrasFragment.ExtrasListener {
	
	String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.extras_fragment);
		setTheme(R.style.AppTheme);
		setTheme(R.style.VimeoLabel);
		setTheme(R.style.VimeoInfo);
		
		
	}

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
