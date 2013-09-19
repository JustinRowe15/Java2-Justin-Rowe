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
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.lib.FileStuff;
import com.example.lib.VimeoService;
import com.example.lib.WebStuff;
import com.example.tweetfinder.R;
import com.example.vimeofinder.helpers.FavoriteVideos;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

	Context context;
	TextView tweetsView;
	Button getTweetsButton;
	FavoriteVideos favoriteTweets;
	Boolean connected = false;
	HashMap<String, String> history;
	Button searchButton;
	String savedFile = "savedFile.txt";
	EditText search;
	TextView authorResult;
	TextView titleResult;
	TextView descriptionResult;
	TextView uploadResult;
	TextView playsResult;
	URL finalURL;
	static String enteredUsername;
	static ArrayList<String> authorList;
	static ArrayList<String> titleList;
	static ArrayList<String> uploadList;
	static ArrayList<String> numberOfPlaysList;

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
		searchButton = (Button) findViewById(R.id.searchButton);
		search = (EditText) findViewById(R.id.searchField);
		
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
	
	@SuppressLint("HandlerLeak")
	private void getVideo(String username) {
		String baseURL = "http://vimeo.com/api/v2/" + username + "/videos.json";
		try{
			finalURL = new URL(baseURL);
			Handler vimeoHandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					
					String response = null;
					
					if(msg.arg1 == RESULT_OK && msg.obj != null) {
						
						try {
							
							response = (String) msg.obj;
							JSONArray json = new JSONArray(response);
							
							String handle = "";
                            String title = "";
                            String date = "";
                            
                            //Log.i("MAIN", "json length->"+json.length()+", json.toString->"+json.toString());
							
                            FileStuff.storeStringFile(context, "savedFile.txt", json.toString());
                            
                            for(int i=0;i<json.length();i++) {
                            	handle = json.getJSONObject(i).getString("user_name");
                                title = json.getJSONObject(i).getString("title");
                                date = json.getJSONObject(i).getString("upload_date");
                                //Log.i("JSON DEBUG", "title="+title+", date="+date+", user_name="+handle);
                            }
                            
							//displayData();
						} 
						catch (Exception e)
						{
							Log.e("HANDLE MESSAGE", e.getMessage().toString());
                            e.printStackTrace();
						}
						
						displayData();
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
	public void displayData(){
		String username = search.getText().toString();
		enteredUsername = username;
		Uri uri = Uri.parse("content://com.example.vimeofinder.videoprovider/items");
		Cursor vimeoCursor = getContentResolver().query(uri, null, null, null, null);
		
		if (vimeoCursor.getCount()>0) {
			authorList = new ArrayList<String>();
			titleList = new ArrayList<String>();
			uploadList = new ArrayList<String>();
			numberOfPlaysList = new ArrayList<String>();
			
			if (vimeoCursor.moveToFirst()) {
				for (int i=0; i<vimeoCursor.getCount(); i++){
					String author = vimeoCursor.getString(1);
					String title = vimeoCursor.getString(2);
					String uploadDate = vimeoCursor.getString(3);
					String numberOfPlays = vimeoCursor.getString(4);
					
					authorList.add(author);
					titleList.add(title);
					uploadList.add(uploadDate);
					numberOfPlaysList.add(numberOfPlays);
					
					vimeoCursor.moveToNext();
				}
			}
			
			for (int i=0; i<5; i++){
				
				if(i >= titleList.size()){
					int resourceId = getResources().getIdentifier("vimeoAuthor" + i, "id", getPackageName());
					TextView authorTitle = (TextView) findViewById(resourceId);
					authorTitle.setText("");
					
					int resourceId1 = getResources().getIdentifier("vimeoTitle" + i, "id", getPackageName());
					TextView displayTitle = (TextView) findViewById(resourceId1);
					displayTitle.setText("");
					
					int resourceId2 = getResources().getIdentifier("vimeoUploadDate" + i, "id", getPackageName());
					TextView uploadTitle = (TextView) findViewById(resourceId2);
					uploadTitle.setText("");
					
					int resourceId3 = getResources().getIdentifier("vimeoNumberPlays" + i, "id", getPackageName());
					TextView playsTitle = (TextView) findViewById(resourceId3);
					playsTitle.setText("");
				} else {
					int resourceId = getResources().getIdentifier("vimeoAuthor" + i, "id", getPackageName());
					TextView authorTitle = (TextView) findViewById(resourceId);
					authorTitle.setText(authorList.get(i));
					
					int resourceId1 = getResources().getIdentifier("vimeoTitle" + i, "id", getPackageName());
					TextView displayTitle = (TextView) findViewById(resourceId1);
					displayTitle.setText(titleList.get(i));
					
					int resourceId2 = getResources().getIdentifier("vimeoUploadDate" + i, "id", getPackageName());
					TextView uploadTitle = (TextView) findViewById(resourceId2);
					uploadTitle.setText(uploadList.get(i));
					
					int resourceId3 = getResources().getIdentifier("vimeoNumberPlays" + i, "id", getPackageName());
					TextView playsTitle = (TextView) findViewById(resourceId3);
					playsTitle.setText(numberOfPlaysList.get(i));
				}
			}
		} else {
			Toast toast = Toast.makeText(context, "Please enter Vimeo username.", Toast.LENGTH_SHORT);
            toast.show();
		}
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }
	
	@Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getString("editText") != null)
        {
            search.setText(savedInstanceState.getString("editText"));
        }
        for (int i = 0; i < 5; i++) {
        	int resourceId = getResources().getIdentifier("vimeoAuthor" + i, "id", getPackageName());
			TextView authorTitle = (TextView) findViewById(resourceId);
			authorTitle.setText(savedInstanceState.getString("vimeoAuthor" + i));
			
			int resourceId1 = getResources().getIdentifier("vimeoTitle" + i, "id", getPackageName());
			TextView displayTitle = (TextView) findViewById(resourceId1);
			displayTitle.setText(savedInstanceState.getString("vimeoTitle" + i));
			
			int resourceId2 = getResources().getIdentifier("vimeoUploadDate" + i, "id", getPackageName());
			TextView uploadTitle = (TextView) findViewById(resourceId2);
			uploadTitle.setText(savedInstanceState.getString("vimeoUploadDate" + i));
			
			int resourceId3 = getResources().getIdentifier("vimeoNumberPlays" + i, "id", getPackageName());
			TextView playsTitle = (TextView) findViewById(resourceId3);
			playsTitle.setText(savedInstanceState.getString("vimeoNumberPlays" + i));
        }
    }
}
