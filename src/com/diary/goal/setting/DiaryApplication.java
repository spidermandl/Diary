package com.diary.goal.setting;

import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DiaryApplication extends Application {

	private int screen_width;
	private int screen_height;
	private int initialOrientation;
	/**
	 * compare orientation status with the initial one
	 * true if different;false if same
	 */
	private boolean reverseOrientation=false;
	private static DiaryApplication instance;

	/**
	 * store bitmap cache for accessing UI resource
	 */
	HashMap<Integer,Bitmap> bitmapCache=new HashMap<Integer, Bitmap>();

	public static DiaryApplication getInstance(){
		return instance;
	}

	@Override
	public void onCreate() {
		instance=this;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		WindowManager window=(WindowManager)(this.getSystemService(Context.WINDOW_SERVICE));
		window.getDefaultDisplay().getMetrics(displaymetrics);
		screen_width=displaymetrics.widthPixels;
		screen_height=displaymetrics.heightPixels;
		initialOrientation=this.getResources().getConfiguration().orientation;
		super.onCreate();
	}
	public int getScreen_w() {
		if(!reverseOrientation)
			return screen_width;
		else
			return screen_height;
	}

	public int getScreen_h() {
		if(!reverseOrientation)
			return screen_height;
		else
			return screen_width;
	}
	
	public void setBitmap(Integer resid,Bitmap bitmap){
		bitmapCache.put(resid, bitmap);
	}
	public Bitmap getBitmap(Integer resid){
		return (Bitmap)bitmapCache.get(resid);
	}
	
	public void setOrientation(int o){
		if(initialOrientation!=o)
			reverseOrientation=true;
		else
			reverseOrientation=false;
	}

}
