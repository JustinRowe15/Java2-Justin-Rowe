/*
 *  project		VimeoFinder
 * 
 *  package		com.example.vimeofinder
 * 
 *  @author		Justin Rowe
 * 
 *  date		Sep 7, 2013
 */
package com.example.vimeofinder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.lib.FileStuff;
import com.example.lib.VimeoService;
import com.example.lib.WebStuff;
import com.example.tweetfinder.R;
import com.example.vimeofinder.helpers.FavoriteVideos;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity {
	
	/** The context. */
	Context context;
	
	/** The tweets view. */
	TextView tweetsView;
	
	/** The get tweets button. */
	Button getTweetsButton;
	
	/** The favorite tweets. */
	FavoriteVideos favoriteTweets;
	
	/** The connected. */
	Boolean connected = false;
	
	/** The history. */
	HashMap<String, String> history;
	
	/** The search button. */
	Button searchButton;
	
	/** The saved File. */
	String savedFile = "savedFile.txt";
	
	/** The search. */
	EditText search;
	
	/** The author result. */
	TextView authorResult;
	
	/** The title result. */
	TextView titleResult;
	
	/** The description result. */
	TextView descriptionResult;
	
	/** The upload result. */
	TextView uploadResult;
	
	/** The plays result. */
	TextView playsResult;
	
	/** The final url. */
	URL finalURL;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form);
		setTheme(R.style.AppTheme);
		setTheme(R.style.VimeoLabel);
		setTheme(R.style.VimeoInfo);
		
		context = this;
		history = getHistory();
		
		//Add Search Handler
		authorResult = (TextView) findViewById(R.id.vimeoAuthor);
		titleResult = (TextView) findViewById(R.id.vimeoTitle);
		descriptionResult = (TextView) findViewById(R.id.vimeoDescription);
		uploadResult = (TextView) findViewById(R.id.vimeoUploadDate);
		playsResult = (TextView) findViewById(R.id.vimeoNumberPlays);
		searchButton = (Button) findViewById(R.id.searchButton);
		search = (EditText) findViewById(R.id.searchField);
		search.setText(VideoProvider.VimeoData.CONTENT_URI.toString());
		
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				search = (EditText) findViewById(R.id.searchField); 
				if(search.getText().toString().length() == 0)
				{
					Toast.makeText(context, "Please Enter A Username", Toast.LENGTH_LONG).show();
					return;
				} else {
					getVideo(search.getText().toString());
				}
			}
		});
		
		//Detect Network Connection
		connected = WebStuff.getConnectionStatus(context);
		if(connected){
			Log.i("NETWORK CONNECTION", WebStuff.getConnectionType(context));
		}
		
		//Add Favorites Display
		favoriteTweets = new FavoriteVideos(context);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Gets the video.
	 *
	 * @param username the username
	 * @return the video
	 */
	@SuppressLint("HandlerLeak")
	private void getVideo(String username) {
		// TODO Auto-generated method stub
		String baseURL = "http://vimeo.com/api/v2/" + username + "/videos.json";
		try{
			finalURL = new URL(baseURL);
			
			Handler vimeoHandler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					
					String response = null;
					
					if(msg.arg1 == RESULT_OK && msg.obj != null) {
						try {
							response = (String) msg.obj;
							final JSONArray results = new JSONArray(response);
							FileStuff.storeStringFile(context, savedFile, results.toString());
							
							displayData(response);
						} 
						catch (Exception e)
						{
							Log.e("HANDLE MESSAGE", e.getMessage().toString());
						}
					}
				}	
			};
			
			Messenger vimeoMessenger = new Messenger(vimeoHandler);
			
			Intent startVimeoFinderIntent = new Intent(context, VimeoService.class);
			startVimeoFinderIntent.putExtra(VimeoService.MESSENGER_KEY, vimeoMessenger);
			startVimeoFinderIntent.putExtra(VimeoService.BASE_URL, baseURL);
			startService(startVimeoFinderIntent);
		} 
		catch (MalformedURLException e){
			Log.e("BAD URL", "MALFORMED URL");
			finalURL = null;
		}
	}
	
	/**
	 * Gets the history.
	 *
	 * @return the history
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, String> getHistory(){
		Object stored = FileStuff.readObjectFile(context, "history", false);
		
		HashMap<String, String> history;
		if(stored == null){
			Log.i("HISTORY", "NO HISTORY FILE FOUND");
			history = new HashMap <String, String>();
		} else {
			history = (HashMap<String, String>) stored;
		}
		return history;
	}
	
	/**
	 * Display data.
	 *
	 * @param result the result
	 */
	public void displayData(String result){
		try{
			JSONArray json = new JSONArray(result);
			int j = json.length();
			for (int i=0;i<j; i++){
				JSONObject object = json.getJSONObject(i);
				((TextView) findViewById(R.id.vimeoAuthor)).setText(object.getString("user_name"));
				((TextView) findViewById(R.id.vimeoTitle)).setText(object.getString("title"));
				((TextView) findViewById(R.id.vimeoDescription)).setText(object.getString("description"));
				((TextView) findViewById(R.id.vimeoUploadDate)).setText(object.getString("upload_date"));
				((TextView) findViewById(R.id.vimeoNumberPlays)).setText(object.getString("stats_number_of_plays"));
				}
			} catch (JSONException e){
				Log.e("JSON", "JSON OBJECT EXCEPTION");
			}
	}
}
