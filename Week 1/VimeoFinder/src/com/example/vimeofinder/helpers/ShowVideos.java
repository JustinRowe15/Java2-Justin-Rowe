package com.example.vimeofinder.helpers;

import com.example.tweetfinder.helpers.FindTweets;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.TextView;

public class ShowVideos extends GridLayout {
	TextView tweetAuthor;
	TextView tweetDate;
	TextView tweetMessage;
	FindTweets findTweets;
	Context context;
	
	public ShowVideos(Context context) {
		super (context);
		this.context = context;
		this.setColumnCount(2);
		
		TextView tweetAuthorHeader = new TextView(context);
		tweetAuthorHeader.setText("Author: ");
		tweetAuthor = new TextView(context);
		
		TextView tweetDateHeader = new TextView(context);
		tweetDateHeader.setText("Title: ");
		tweetDate = new TextView(context);
		
		TextView tweetMessageHeader = new TextView(context);
		tweetMessageHeader.setText("Upload Date: ");
		tweetMessage = new TextView(context);
		
		this.addView(tweetAuthorHeader);
		this.addView(tweetAuthor);
		this.addView(tweetDateHeader);
		this.addView(tweetDate);
		this.addView(tweetMessageHeader);
		this.addView(tweetMessage);
	}
	
	public void getTweets(String author, String date, String message) {
		tweetAuthor.setText(author);
		tweetDate.setText(date);
		tweetMessage.setText(message);
	}
}
