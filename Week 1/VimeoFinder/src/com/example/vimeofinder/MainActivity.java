package com.example.vimeofinder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.lib.FileStuff;
import com.example.lib.WebStuff;
import com.example.tweetfinder.R;
import com.example.tweetfinder.helpers.FindTweets;
import com.example.vimeofinder.helpers.FavoriteVideos;
import com.loopj.android.image.SmartImageView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	
	Context context;
	TextView tweetsView;
	Button getTweetsButton;
	FindTweets search;
	FavoriteVideos favoriteTweets;
	Boolean connected = false;
	HashMap<String, String> history;
	
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
				SmartImageView myImage = (SmartImageView) findViewById(R.id.my_image);
				String imageURL = object.getString("thumbnail_large");
				myImage.setImageUrl(imageURL);
			}
			/* String results = json.getJSONObject(0).getString("id");
				if(results.compareTo("N/A")==0){
					Toast toast = Toast.makeText(context, "Invalid User", Toast.LENGTH_SHORT);
					toast.show();
				} else {
					//updateData(results);
					Toast toast = Toast.makeText(context, "Valid User " + results, Toast.LENGTH_SHORT);
					toast.show();
					history.put(results, results.toString());
					FileStuff.storeObjectFile(context, "history", history, false);
					FileStuff.storeStringFile(context, "temp", results.toString(), true);
				} */
			} catch (JSONException e){
				Log.e("JSON", "JSON OBJECT EXCEPTION");
			}
			
		}
	}

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
		Button searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText search = (EditText) findViewById(R.id.searchField); 
				getTweet(search.getText().toString());
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
	
	private void getTweet(String username){
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
