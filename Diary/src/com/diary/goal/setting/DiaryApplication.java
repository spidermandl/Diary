package com.diary.goal.setting;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.database.DiaryHelper;
import com.diary.goal.setting.thread.UEHandler;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Constant.SudoType;
import com.flurry.android.FlurryAgent;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;
/**
 * 程序进程管理
 * @author desmond.duan
 *
 */
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
	 * 保存九宫格状态
	 */
	private HashMap<SudoType, Boolean> SudoKuStatus;
	/**
	 * 数据缓存,包括用户id等服务器信息
	 */
	private HashMap<String, String> memCache;

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
	 * 日记语法检测的错误信息
	 * 记录错误字符的位置位置
	 */
	private Integer[] errorArray;
	public Integer[] getSyntaxError(){
		return errorArray;
	}
	public void setSyntaxError(Integer[] errors){
		errorArray=errors;
	}

	/**
	 * 图片缓存，所有图片资源统一内存管理
	 */
	HashMap<Integer,SoftReference<Bitmap>> bitmapCache=new HashMap<Integer, SoftReference<Bitmap>>();
    /**
     * 程序崩溃异常处理handler
     */
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
		dbHelper = new DiaryHelper(this);
		memCache = new HashMap<String, String>();
		
//		ueHandler = new UEHandler(this); 
//        Thread.setDefaultUncaughtExceptionHandler(ueHandler); 
		FlurryAgent.onStartSession(this, Constant.FLURRY_KEY);
		super.onCreate();
	}
	/**
	 * 进程退出
	 */
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
	/**
	 * 获取屏幕宽度
	 * @return
	 */
	public int getScreen_w() {
		if(!reverseOrientation)
			return screen_width;
		else
			return screen_height;
	}
	/**
	 * 获取屏幕高度
	 * @return
	 */
	public int getScreen_h() {
		if(!reverseOrientation)
			return screen_height;
		else
			return screen_width;
	}
	
	public HashMap<SudoType, Boolean> getSudoKuStatus() {
		return SudoKuStatus;
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

	public DiaryHelper getDbHelper() {
		return dbHelper;
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

	/**
	 * 更新九宫格状态
	 * 每次进入九宫格界面需要显示每个格子状态
	 * 状态保存于SudoKuStatus中
	 */
	public void updateStatusPanel(){
		if(SudoKuStatus==null){
			/**
			 * 初始化sudoKuStatus
			 */
	        SudoKuStatus=new HashMap<Constant.SudoType, Boolean>();
	        SudoKuStatus.put(SudoType.SUDO_1, false);
	        SudoKuStatus.put(SudoType.SUDO_2, false);
	        SudoKuStatus.put(SudoType.SUDO_3, false);
	        SudoKuStatus.put(SudoType.SUDO_4, false);
	        SudoKuStatus.put(SudoType.SUDO_6, false);
	        SudoKuStatus.put(SudoType.SUDO_7, false);
	        SudoKuStatus.put(SudoType.SUDO_8, false);
	        SudoKuStatus.put(SudoType.SUDO_9, false);
		}
		
		String diaryText=DiaryApplication.getInstance().getDbHelper().getDiaryContent(new Date());
		if(diaryText!=null){
			try {
				JSONObject diaryObject=new JSONObject(diaryText);
				JSONArray array=diaryObject.getJSONArray(Constant.MAIN_SEQUENCE_ORDER);
				for(int i=0;i<array.length();i++){//大标题循环
					JSONObject subObj=diaryObject.getJSONObject(array.getString(i));
					JSONArray subArr=subObj.getJSONArray(Constant.SUB_SEQUENCE_ORDER);
					boolean hasEdited=false;
					for(int j=0;j<subArr.length();j++){//小标题循环
						String subTit=subArr.getString(j);
						String text = subObj.getString(subTit);
						float rating= Float.valueOf(subObj.getString(Constant.MAIN_STATUS));
						if(rating>0.0){//评过星级
							hasEdited=true;
							break;
						}
						if(text!=null&&text.length()!=0){
							int k=0;
							while(k<text.length()){
								if(text.charAt(k)!=' '&&text.charAt(k)!='\n'){//有效文字
									hasEdited=true;
								}
								k++;
							}
						}
					}
					SudoKuStatus.put(SudoType.getSudoType(i<4?i+1:i+2), hasEdited);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public HashMap<String, String> getMemCache() {
		return memCache;
	}
	public void setMemCache(HashMap<String, String> memCache) {
		this.memCache = memCache;
	}

}





