/*
 *  project		VimeoFinder
 * 
 *  package		com.example.vimeofinder.helpers
 * 
 *  @author		Justin Rowe
 * 
 *  date		Sep 7, 2013
 */
package com.example.vimeofinder.helpers;

import com.example.tweetfinder.helpers.FindTweets;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowVideos.
 */
public class ShowVideos extends GridLayout {
	
	/** The tweet author. */
	TextView tweetAuthor;
	
	/** The tweet date. */
	TextView tweetDate;
	
	/** The tweet message. */
	TextView tweetMessage;
	
	/** The find tweets. */
	FindTweets findTweets;
	
	/** The context. */
	Context context;
	
	/**
	 * Instantiates a new show videos.
	 *
	 * @param context the context
	 */
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
	
	/**
	 * Gets the tweets.
	 *
	 * @param author the author
	 * @param date the date
	 * @param message the message
	 * @return the tweets
	 */
	public void getTweets(String author, String date, String message) {
		tweetAuthor.setText(author);
		tweetDate.setText(date);
		tweetMessage.setText(message);
	}
}
