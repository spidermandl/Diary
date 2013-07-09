package com.diary.goal.setting;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.database.DiaryHelper;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.model.PanelDateModel;
import com.diary.goal.setting.thread.UEHandler;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Function;
import com.flurry.android.FlurryAgent;

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
	private PanelDateModel padStatus;
	/**
	 * days link
	 */
	private HashMap<Integer, PanelDateModel> panelCache;
	private DateModel dateModel;
	private int dateCursor=0;
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
	HashMap<Integer,SoftReference<Bitmap>> bitmapCache=new HashMap<Integer, SoftReference<Bitmap>>();

	
	UEHandler ueHandler;
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
		
		//ueHandler = new UEHandler(this); 
        //Thread.setDefaultUncaughtExceptionHandler(ueHandler); 
		//FlurryAgent.onStartSession(this, Constant.FLURRY_KEY);
		super.onCreate();
	}
	public void quit(){
		dbHelper.close();
		//FlurryAgent.onEndSession(this);
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
		SoftReference<Bitmap> softBitmap=new SoftReference<Bitmap>(bitmap);
		bitmapCache.put(resid, softBitmap);
	}
	public Bitmap getBitmap(Integer resid){
		SoftReference<Bitmap> softBitmap=bitmapCache.get(resid);
		if(softBitmap!=null){
			Bitmap bitmap=softBitmap.get();
			return bitmap;
		}
		return null;
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

	public PanelDateModel getPadStatus(){
		return padStatus;
	}
	public void setPadStatus(PanelDateModel status){
		padStatus=status;
	}

	public DateModel getDateModel() {
		if(dateModel==null)
			dateModel=new DateModel();
		return dateModel;
	}

	public void setDateModel(DateModel dateModel) {
		this.dateModel = dateModel;
	}

	public HashMap<Integer, PanelDateModel> getPanelCache() {
		if(panelCache==null)
			panelCache=new HashMap<Integer, PanelDateModel>();
		return panelCache;
	}

	public void setPanelCache(HashMap<Integer, PanelDateModel> panelCache) {
		this.panelCache = panelCache;
	}

	public int getDateCursor() {
		return dateCursor;
	}

	public void setDateCursor(int dateCursor) {
		this.dateCursor = dateCursor;
	}
}
