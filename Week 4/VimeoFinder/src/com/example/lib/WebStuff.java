/*
 *  project		VimeoFinder
 * 
 *  package		com.example.lib
 * 
 *  @author		Justin Rowe
 * 
 *  date		Sep 27, 2013
 */
package com.example.lib;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class WebStuff.
 */
public class WebStuff {

	/** The conn. */
	static Boolean conn = false;
	
	/** The connection type. */
	static String connectionType = "Unavailable";
	
	/**
	 * Gets the connection type.
	 *
	 * @param context the context
	 * @return the connection type
	 */
	public static String getConnectionType(Context context){
		netInfo(context);
		return connectionType;
	}
	
	/**
	 * Gets the connection status.
	 *
	 * @param context the context
	 * @return the connection status
	 */
	public static Boolean getConnectionStatus(Context context){
		netInfo(context);
		return conn;
	}
	
	/**
	 * Net info.
	 *
	 * @param context the context
	 */
	private static void netInfo(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if(ni!=null){
			if(ni.isConnected()){
				connectionType = ni.getTypeName();
				conn = true;
			}
		}
	}
	
	/**
	 * Gets the uRL string response.
	 *
	 * @param url the url
	 * @return the uRL string response
	 */
	public static String getURLStringResponse(URL url) {
		String response = "";
		
		try{
			URLConnection conn = url.openConnection();
			BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
			
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer responseBuffer = new StringBuffer();
			
			while((bytesRead = bin.read(contentBytes)) != -1){
				response = new String(contentBytes,0,bytesRead);
				responseBuffer.append(response);
			}
			return responseBuffer.toString();
		} catch (Exception e) {
			Log.e("URL RESPONSE ERROR", "getURLStringResponse");
		}
		
		return response;
	}
	
}
