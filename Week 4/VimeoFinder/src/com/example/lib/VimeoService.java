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

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class VimeoService.
 */
public class VimeoService extends IntentService {
	
	/** The Constant MESSENGER_KEY. */
	public static final String MESSENGER_KEY = "";
	
	/** The Constant BASE_URL. */
	public static final String BASE_URL = null;
	
	/** The final url. */
	static URL finalURL = null;

	/**
	 * Instantiates a new vimeo service.
	 */
	public VimeoService() {
		super("VimeoService");
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("onHandleIntent", "started");
		
		Bundle extras = intent.getExtras();
		Messenger messenger = (Messenger) extras.get(MESSENGER_KEY);
		String vimeoURL = extras.getString(BASE_URL);
		
		try {
			finalURL = new URL(vimeoURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		Message message = Message.obtain();
		message.arg1 = Activity.RESULT_OK;
		message.obj = WebStuff.getURLStringResponse(finalURL);
		
		try {
			messenger.send(message);
		} 
		catch (RemoteException e) {
		Log.e("onHandleIntent", e.getMessage().toString());
		e.printStackTrace();
		}
	}

}
