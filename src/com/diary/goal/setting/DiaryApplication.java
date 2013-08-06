package com.diary.goal.setting;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.database.DiaryHelper;
import com.diary.goal.setting.database.DiaryHelper.DiaryConfigColumn;
import com.diary.goal.setting.database.DiaryHelper.Tables;
import com.diary.goal.setting.model.CategoryModel;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.model.PanelDateModel;
import com.diary.goal.setting.thread.UEHandler;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Function;
import com.flurry.android.FlurryAgent;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
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
	/**
	 * the data model tracking the date users are dealing with
	 */
	private DateModel dateModel;
	private HashMap<String,HashMap<Integer, Object>> tableCaches;
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
	
	private void setTableCaches(){
		Cursor c=dbHelper.getDateDiaryAll();
		if(c!=null){
			HashMap<Integer, Object> map=new HashMap<Integer, Object>();
			while(c.moveToNext()){
				CategoryModel model=new CategoryModel();
				model.setCategoryIndex(c.getInt(c.getColumnIndex(DiaryConfigColumn._CATEGORY_INDEX)));
				model.setCategoryName(c.getString(c.getColumnIndex(DiaryConfigColumn._CATEGORY_NAME)));
				model.setCategoryType(c.getInt(c.getColumnIndex(DiaryConfigColumn._CATEGORY_TYPE)));
				model.setSudoType(c.getInt(c.getColumnIndex(DiaryConfigColumn._SUDO_TYPE)));
				map.put(c.getInt(c.getColumnIndex(DiaryConfigColumn._ID)), model);
			}
			tableCaches.put(Tables.DIARY_CONFIG, map);
			c.close();
		}
	}
	
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
		
//		ueHandler = new UEHandler(this); 
//        Thread.setDefaultUncaughtExceptionHandler(ueHandler); 
		FlurryAgent.onStartSession(this, Constant.FLURRY_KEY);
		super.onCreate();
	}
	public void quit(){
		dbHelper.close();
		FlurryAgent.onEndSession(this);
    	ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE); 
    	if (Build.VERSION.SDK_INT < 8){
    		am.restartPackage(getPackageName());
    	}
    	else{
			Method method;
			try {
				method = am.getClass().getMethod("killBackgroundProcesses",new Class[] {String.class });
				method.invoke(am,"com.diary.goal.setting");
	   		} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}	
    		//am.killBackgroundProcesses("com.diary.goal.setting"); 
    	}
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
	public void clearbitmap(Integer resid){
		SoftReference<Bitmap> softBitmap=bitmapCache.get(resid);
		if(softBitmap!=null){
			softBitmap.clear();
		}
	}
	public void setOrientation(int o){
		if(initialOrientation!=o)
			reverseOrientation=true;
		else
			reverseOrientation=false;
	}
	public int getOrientation(){
		return initialOrientation;
	}
	public JSONObject getSudoConfig(){
		if(sudoConfig==null){
			try {
				sudoConfig=new JSONObject(inputStreamReader(this.getAssets().open(
						         this.getResources().getString(R.string.config_file))));
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
	
	private String inputStreamReader(InputStream is){
		byte[] buffer;
		try {
			buffer = new byte[is.available()];
	        is.read(buffer);  
	        String newStr = EncodingUtils.getString(buffer, this.getResources().getString(R.string.config_coding));
	        return newStr;
		} catch (IOException e) {
			return null;
		}  
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
	/**
	 * get info of static table.
	 * return the entire table or a specific row with _id provided
	 * @param table_name
	 * @param _id
	 * @return
	 */
	public Object getTableCacheElement(String table_name,Integer _id){
		if(tableCaches==null){
			tableCaches=new HashMap<String, HashMap<Integer,Object>>();
			setTableCaches();
		}
		HashMap<Integer,Object> map = tableCaches.get(table_name);
		if(map!=null){
			if(_id==0)
				return map;
			return map.get(_id);
		}
		return null;
	}
}
