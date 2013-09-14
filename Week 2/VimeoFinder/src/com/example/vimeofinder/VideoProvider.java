/**
 * 
 */
package com.example.vimeofinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.lib.FileStuff;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * @author justinrowe
 *
 */
public class VideoProvider extends ContentProvider {

	public static final String AUTHORITY = "com.example.vimeofinder.videoprovider";
	
	public static class VimeoData implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/videos");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vid.example.vimeofinder.video";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.video/vnd.example.vimeofinder.video";
		
		//Define Columns
		public static final String VIDEOAUTHOR_COLUMN = "author";
		public static final String VIDEOTITLE_COLUMN = "name";
		public static final String VIDEODATE_COLUMN = "date";
		public static final String VIDEOVIEWS_COLUMN = "views";
		
		public static final String[] PROJECTION = { "_Id", VIDEOAUTHOR_COLUMN, VIDEOTITLE_COLUMN, VIDEODATE_COLUMN, VIDEOVIEWS_COLUMN };
		
		private VimeoData() {};
		
	}
	
	public static final int VIDEOS = 1;
	public static final int VIDEO_ID = 2;
	
	private static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "videos", VIDEOS);
		uriMatcher.addURI(AUTHORITY, "videos/#", VIDEO_ID);
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)){
		//get all videos
		case VIDEOS:
			return VimeoData.CONTENT_TYPE;
		//get a particular video
		case VIDEO_ID:
			return VimeoData.CONTENT_ITEM_TYPE;
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		MatrixCursor result = new MatrixCursor(VimeoData.PROJECTION); 
		String JSONString = FileStuff.readStringFile(getContext(), "savedFile.txt", false);
		
		JSONArray json = null;
		JSONObject results = null;
		
		try{
			json = new JSONArray(JSONString);
			
		} catch (JSONException e) {	
			Log.e("VIDEO PROVIDER JSON", e.toString(), e);
		}
		
		switch (uriMatcher.match(uri)){
		//get all videos
		case VIDEOS:
			for (int i=0; i<5; i++) {
				try {
				results = json.getJSONObject(i);
				result.addRow(new Object[] {i+1, results.getString("username"), results.getString("title"), results.getString("upload_date"), results.getString("stats_number_of_plays")});
				} catch (Exception e) {
					Log.e("JSON FOR LOOP", e.toString(), e);
				}
			}
			break;
			
		//get a particular video
		case VIDEO_ID:
			String videoRequested = uri.getLastPathSegment();
			
			for (int i=0; i<5; i++) {
				try {
					results = json.getJSONObject(i);
					if (results.getString("username").contentEquals(videoRequested)){
						try {
							result.addRow(new Object[] {i+1, results.getString("username"), results.getString("title"), results.getString("upload_date"), results.getString("stats_number_of_plays")});
						} catch (Exception e) {
							Log.e("SPECIFIC VIDEO REQUEST", e.toString(), e);
						}
					}
				} catch (Exception e) {
					Log.e("JSON FOR LOOP", e.toString(), e);
				}
			}
			break;
		
		}
		// TODO Auto-generated method stub
		return result;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
