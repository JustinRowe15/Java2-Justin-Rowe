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

public class VimeoService extends IntentService {
	
	public static final String MESSENGER_KEY = "";
	public static final String BASE_URL = null;
	static URL finalURL = null;

	public VimeoService() {
		super("VimeoService");
		// TODO Auto-generated constructor stub
	}

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
