package com.diary.goal.setting;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.database.DiaryHelper;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Function;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DiaryApplication extends Application {

	/**
	 * conf file for diary index
	 */
	private JSONObject sudoConfig;
	/**
	 * access to local database
	 */
	private DiaryHelper dbHelper;
	/**
	 * get current day pad status
	 */
	private HashMap<Constant.SudoType, Boolean> padStatus;
	
	private DateModel dateModel;
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
		dbHelper=new DiaryHelper(this);
		
		padStatus = new HashMap<Constant.SudoType, Boolean>();
		padStatus.put(Constant.SudoType.WORK, false);
		padStatus.put(Constant.SudoType.SOCIAL, false);
		padStatus.put(Constant.SudoType.FAMILY, false);
		padStatus.put(Constant.SudoType.FINANCE, false);
		padStatus.put(Constant.SudoType.DATE, false);
		padStatus.put(Constant.SudoType.SQUARE_6, false);
		padStatus.put(Constant.SudoType.HEALTHY, false);
		padStatus.put(Constant.SudoType.SQUARE_8, false);
		padStatus.put(Constant.SudoType.SQUARE_9, false);
		
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
	public JSONObject getSudoConfig(){
		if(sudoConfig==null){
			try {
				sudoConfig=new JSONObject(Function.ReadInputStream(this.getAssets().open("sudo_conf")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sudoConfig; 
	}
	
	public DiaryHelper getDbHelper() {
		return dbHelper;
	}

	public HashMap<Constant.SudoType, Boolean> getPadStatus(){
		return padStatus;
	}

	public DateModel getDateModel() {
		if(dateModel==null)
			dateModel=new DateModel();
		return dateModel;
	}

	public void setDateModel(DateModel dateModel) {
		this.dateModel = dateModel;
	}
}
