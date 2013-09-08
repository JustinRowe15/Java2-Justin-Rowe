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

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
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

public class MainActivity extends Activity implements OnClickListener {
	
	Context context;
	TextView tweetsView;
	Button getTweetsButton;
	FavoriteVideos favoriteTweets;
	Boolean connected = false;
	HashMap<String, String> history;
	Button searchButton;
	EditText search;
	TextView authorResult;
	TextView titleResult;
	TextView descriptionResult;
	TextView uploadResult;
	TextView playsResult;

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
		searchButton.setOnClickListener(this);
		
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				search = (EditText) findViewById(R.id.searchField); 
				getVideo(search.getText().toString());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		if(search.getText().toString().length() == 0)
		{
			Toast.makeText(this, "Please Enter A Username", Toast.LENGTH_LONG).show();
			return;
		}
		
		Handler vimeoHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				String response = null;
				
				if(msg.arg1 == RESULT_OK && msg.obj != null)
				{
					try {
						response = (String) msg.obj;
					} 
					catch (Exception e)
					{
						Log.e("handleMessage", e.getMessage().toString());
					}
					
					authorResult.setText(response);
				}
			}	
		};
		
		Messenger vimeoMessenger = new Messenger(vimeoHandler);
		
		Intent startVimeoFinderIntent = new Intent(this, VimeoService.class);
		startVimeoFinderIntent.putExtra(VimeoService.MESSENGER_KEY, vimeoMessenger);
		startVimeoFinderIntent.putExtra(VimeoService.URL_KEY, this.search.getText().toString());
		startService(startVimeoFinderIntent);

		
	}
	
	public void displayData(String result){
		
	}
	
	private class TweetRequest extends AsyncTask<URL, Void, String>{
		@Override
		protected String doInBackground(URL... urls){
			String response = "";
			for (URL url: urls){
				response = WebStuff.getURLStringResponse(url);
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result){
			Log.i("URL RESPONSE", result);
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
	
	//JSON 
	private void getVideo(String username){
		String baseURL = "http://vimeo.com/api/v2/" + username + "/videos.json";
		URL finalURL;
		try{
			finalURL = new URL(baseURL);
			TweetRequest tr = new TweetRequest();
			tr.execute(finalURL);
		} catch (MalformedURLException e){
			Log.e("BAD URL", "MALFORMED URL");
			finalURL = null;
		}
	}
	
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
}
