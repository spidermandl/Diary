package com.diary.goal.setting.database;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DiaryHelper extends SQLiteOpenHelper{

	
	private SQLiteDatabase db;
	
	private static final String DB_NAME = "diary.db";//DB name
	private static final int DB_VERSION = 1;	//DB version
	
	public interface Tables {
		/**@table diary model config*/
		public static final String DIARY_CONFIG = "diary_config";
		/**@table diary track*/
		public static final String DIARY_TRACK = "diary_track";
		/**@table 日记模板*/
		public static final String DIARY_TEMPLETE = "diary_templete";
		/**@table 日记内容*/
		public static final String DIARY_CONTENT = "diary_content";
	}
	/**
	 * 公用字段
	 *
	 */
	public interface CommonColumn{
		public static final String _ID = "_id";
		public static final String _CREATE_TIME = "create_time";
		public static final String _UPDATE_TIME = "update_time";
	}
	
	public interface Views {
		public static final String TRACK_CONFIG_ALL = "view_diary_all";
	}

	public interface DiaryConfigColumn{
		public static final String _SUDO_TYPE = "type";
		public static final String _CATEGORY_INDEX = "category_index";
		public static final String _CATEGORY_NAME = "category_name";
		public static final String _CATEGORY_TYPE = "category_type";
		public static final String _CATEGORY_HINT = "category_hint";
	}
	
	public interface DiaryTrackColumn{
		public static final String _CONFIG_ID = "config"+CommonColumn._ID;
		public static final String _TEXT = "text";
	}
	
	/**
	 * 日记模板表
	 */
	public interface DiaryTampleteColumn{
		public static final String _TAMPLETE = "templete"; //json:{[subtitle,type],[subtile,type]...}
	}
	/**
	 * 日记内容
	 */
	public interface DiaryContentColumn{
		public static final String _CONTENT="_content";
	}
	
	public static final String CREATE_DIARY_TRACK = 
			"CREATE TABLE IF NOT EXISTS " +  Tables.DIARY_TRACK + "("
			+ CommonColumn._ID + " integer primary key autoincrement,"//
			+ CommonColumn._CREATE_TIME + " datetime,"	//create time
			+ CommonColumn._UPDATE_TIME + " datetime,"	//edit time
			+ DiaryTrackColumn._CONFIG_ID + " integer,"	//id in table of diary_config
			+ DiaryTrackColumn._TEXT + " text"	//content
			+ ")";
	public static final String CREATE_DIARY_CONFIG = 
			"CREATE TABLE IF NOT EXISTS " +  Tables.DIARY_CONFIG + "("
			+ CommonColumn._ID + " integer primary key autoincrement,"//
			+ DiaryConfigColumn._SUDO_TYPE + " integer,"	//sudo type
			+ DiaryConfigColumn._CATEGORY_INDEX + " integer,"	//index of category
			+ DiaryConfigColumn._CATEGORY_NAME + " text,"	//name of category
			+ DiaryConfigColumn._CATEGORY_TYPE + " integer,"	//type of category
			+ DiaryConfigColumn._CATEGORY_HINT + " text"   //text hint of category
			+ ")";
	public static final String CREATE_VIEW_TRACK_CONFIG ="CREATE VIEW "+Views.TRACK_CONFIG_ALL+" AS "
			 +" SELECT "+Tables.DIARY_TRACK+"."+CommonColumn._ID+" AS _id, "
			            +Tables.DIARY_TRACK+"."+CommonColumn._CREATE_TIME+", "
			            +Tables.DIARY_TRACK+"."+CommonColumn._UPDATE_TIME+", "
			            +Tables.DIARY_TRACK+"."+DiaryTrackColumn._CONFIG_ID+", "
			            +Tables.DIARY_TRACK+"."+DiaryTrackColumn._TEXT+", "
			            +Tables.DIARY_CONFIG+"."+DiaryConfigColumn._SUDO_TYPE+", "
			            +Tables.DIARY_CONFIG+"."+DiaryConfigColumn._CATEGORY_INDEX+", "
			            +Tables.DIARY_CONFIG+"."+DiaryConfigColumn._CATEGORY_NAME+", "
			            +Tables.DIARY_CONFIG+"."+DiaryConfigColumn._CATEGORY_TYPE+", "
			            +Tables.DIARY_CONFIG+"."+DiaryConfigColumn._CATEGORY_HINT
			  +" FROM "+Tables.DIARY_TRACK+ " JOIN " + Tables.DIARY_CONFIG + " ON "
              +Tables.DIARY_TRACK+"."+DiaryTrackColumn._CONFIG_ID + " = " + Tables.DIARY_CONFIG+"."+CommonColumn._ID;
	
	public static final String CREATE_DIARY_TEMPLETE = 
			"CREATE TABLE IF NOT EXISTS " +  Tables.DIARY_TEMPLETE + "("
			+ CommonColumn._ID + " integer primary key autoincrement,"//
			+ CommonColumn._CREATE_TIME + " datetime,"	
			+ CommonColumn._UPDATE_TIME + " datetime,"	
			+ DiaryTampleteColumn._TAMPLETE + " text"/** 模板存储json格式
										             * {
										             * title_order:[big1,big2,bi3]
										             * big1:[small1,small2]
										             * big2:[small1,small2]
										             * big3:[small1,small2]
										             * }
										             **/
			+ ")";
	public static final String CREATE_DIARY_CONTENT = 
			"CREATE TABLE IF NOT EXISTS " +  Tables.DIARY_CONTENT + "("
			+ CommonColumn._ID + " integer primary key autoincrement,"//
			+ CommonColumn._CREATE_TIME + " datetime,"	
			+ CommonColumn._UPDATE_TIME + " datetime,"	
			+ DiaryContentColumn._CONTENT + " text" /** 日记字串存储json格式
			                                         * {
			                                         * main_title_order:[big1,big2,bi3]
			                                         * big1:{  sub_title_order:[small1,small2]
			                                         *         small1:xxxxx
			                                         *         small2:xxxxx
			                                         *         index:1
			                                         *       }
			                                         * big2:{  sub_title_order:[small1,small2]
			                                         *         small1:xxxxx
			                                         *         small2:xxxxx
			                                         *         index:1
			                                         *       }
			                                         * big3:{  sub_title_order:[small1,small2]
			                                         *         small1:xxxxx
			                                         *         small2:xxxxx
			                                         *         index:1
			                                         *       }
			                                         * }
			                                         **/
			+ ")";
	
	public static final String DROP_DIARY_TRACK = "DROP TABLE IF EXISTS " + Tables.DIARY_TRACK+" ";
	public static final String DROP_DIRAY_CONFIG = "DROP TABLE IF EXISTS " + Tables.DIARY_CONFIG+" ";
	public static final String DROP_VIEW_CONFIG_TRACK = "DROP VIEW IF EXISTS " + Views.TRACK_CONFIG_ALL;
	public static final String DROP_DIARY_TEMPLETE = "DROP TABLE IF EXISTS" + Tables.DIARY_TEMPLETE+" ";
	public static final String DROP_DIARY_CONTENT= "DROP TABLE IF EXISTS" + Tables.DIARY_CONTENT+" ";
	/**
	 *******************************************************************************************************************************
	 */
	public DiaryHelper(Context context){
		super(context, getDataBasePath(), null, DB_VERSION);
		db = getWritableDatabase();
	}

	public static String getDataBasePath(){
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "diary");
			if(!f.exists()){
				if(f.mkdirs())
					return f.getAbsolutePath()+File.separator+DB_NAME;
			}
			else
				return f.getAbsolutePath()+File.separator+DB_NAME;
		}
		return DB_NAME;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		
		db.execSQL(CREATE_DIARY_TRACK);
		db.execSQL(CREATE_DIARY_CONFIG);
		db.execSQL(CREATE_DIARY_TEMPLETE);
		db.execSQL(CREATE_DIARY_CONTENT);
		
		db.execSQL(CREATE_VIEW_TRACK_CONFIG);
	    
		db.setTransactionSuccessful();
		db.endTransaction();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.beginTransaction();
		
		db.execSQL(DROP_VIEW_CONFIG_TRACK);
		db.execSQL(DROP_DIARY_TRACK);
		db.execSQL(DROP_DIRAY_CONFIG);
		db.execSQL(DROP_DIARY_TEMPLETE);
		db.execSQL(DROP_DIARY_CONTENT);
		
		db.setTransactionSuccessful();
		db.endTransaction();
		onCreate(db);
	}

	public Cursor getDateDiaryAll(){
		Cursor c=db.query(Tables.DIARY_CONFIG, 
				new String[]{CommonColumn._ID,
				             DiaryConfigColumn._SUDO_TYPE,
				             DiaryConfigColumn._CATEGORY_INDEX,
				             DiaryConfigColumn._CATEGORY_NAME,
				             DiaryConfigColumn._CATEGORY_TYPE,
				             DiaryConfigColumn._CATEGORY_HINT}, 
				null, null, null, null, null);
		return c;
	}
	
	/**
	 * edit category name of config table
	 */
	public void updateConfigCategoryName(DateModel model){
		ContentValues values=new ContentValues();
		values.put(DiaryConfigColumn._CATEGORY_NAME, model.getCategory_name());
		
		db.update(Tables.DIARY_CONFIG, values,
				CommonColumn._ID+ " = "+model.getConfigId(),
		        null);
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public Cursor getDataInSingleDay(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d=format.format(date);
		Cursor c=db.query(true, Views.TRACK_CONFIG_ALL, 
				new String[]{
				        DiaryTrackColumn._CONFIG_ID,
			            DiaryTrackColumn._TEXT,
			            DiaryConfigColumn._SUDO_TYPE,
			            DiaryConfigColumn._CATEGORY_INDEX,
			            DiaryConfigColumn._CATEGORY_NAME,
			            DiaryConfigColumn._CATEGORY_TYPE,
			            DiaryConfigColumn._CATEGORY_HINT
			            },
			            CommonColumn._CREATE_TIME+" between '"+d+" 00:00:00' and '"+d+" 23:59:59'",
				null, null, null, null, null);
		
		return c;
	}
	/**
	 * get records existence of each day
	 * @return
	 */
	public Cursor getTodayPad(Date date){

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d=format.format(date);
		Cursor c=db.query(true, Views.TRACK_CONFIG_ALL, new String[]{DiaryConfigColumn._SUDO_TYPE},
				CommonColumn._CREATE_TIME+" between '"+d+" 00:00:00' and '"+d+" 23:59:59'",
				null, null, null, null, null);
		
		return c;
	}
	
	public Cursor getCategory(DateModel model){
		return getCategory(model.getDate(), model.getType().getType(), model.getCategory());
	}
	
	public Cursor getCategory(Date date,int type,int category){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d=format.format(date);
		Cursor c=db.query(true, Views.TRACK_CONFIG_ALL, new String[]{DiaryConfigColumn._CATEGORY_INDEX,DiaryTrackColumn._TEXT},
				CommonColumn._CREATE_TIME+" between '"+d+" 00:00:00' and '"+d+" 23:59:59' " +
						" and "
				+DiaryConfigColumn._SUDO_TYPE+ " = "+type+
				        " and "
				+DiaryConfigColumn._CATEGORY_INDEX+ " = "+category,
				null, null, null, null, null);
		
		return c;
	}

	/**
	 * 新建模板
	 * @param date
	 * @param text
	 */
	public void insertDiaryTemplete(Date date,String text){
		ContentValues values = new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._CREATE_TIME, format.format(date));
		values.put(CommonColumn._UPDATE_TIME, format.format(date));
		values.put(DiaryTampleteColumn._TAMPLETE, text);
		
		db.insertOrThrow(Tables.DIARY_TEMPLETE, CommonColumn._ID, values);
	}
	/*******************************************************************************************
	 * 模板（templete）表操作方法
	 *******************************************************************************************/
	/**
	 * 更新模板
	 * @param date
	 * @param text
	 */
	public void updateDiaryTemplete(Date date,String text){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._UPDATE_TIME, format.format(date));
		values.put(DiaryTampleteColumn._TAMPLETE, text);
		
		format = new SimpleDateFormat("yyyy-MM-dd");
		String sDate=format.format(date);
		db.update(Tables.DIARY_TEMPLETE, values,
				CommonColumn._CREATE_TIME+" between '"+sDate+" 00:00:00' and '"+sDate+" 23:59:59' ",
		        null);
	}
	/**
	 * 获取模板
	 * @param date
	 * @return
	 */
	public String getDiaryTemplete(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sDate=date==null?null:format.format(date);
		Cursor c=db.query(Tables.DIARY_TEMPLETE, new String[]{DiaryTampleteColumn._TAMPLETE}, 
				sDate==null?null:CommonColumn._CREATE_TIME+" between '"+sDate+" 00:00:00' and '"+sDate+" 23:59:59' ",
				null,null,null,CommonColumn._CREATE_TIME+" DESC");
		if(c==null||c.getCount()==0)
			return null;
		if (c.moveToFirst()){
			String result=c.getString(0);
			c.close();
			return result;
		}
		return null;
	}
	/*******************************************************************************************
	 * 日记表（diary_content）表操作方法
	 *******************************************************************************************/
	/**
	 * 创建日记内容
	 * @param model
	 * @param text
	 */
	public void insertDiaryContent(DateModel model,String text){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._CREATE_TIME, format.format(model.getDate()));
		values.put(CommonColumn._UPDATE_TIME, format.format(model.getDate()));
		values.put(DiaryTrackColumn._CONFIG_ID, model.getConfigId());
		values.put(DiaryTrackColumn._TEXT, text);
		
		db.insertOrThrow(Tables.DIARY_TRACK, CommonColumn._ID, values);
	}
	public void insertDiaryContent(Date date,String text){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._CREATE_TIME, format.format(date));
		values.put(CommonColumn._UPDATE_TIME, format.format(date));
		values.put(DiaryContentColumn._CONTENT, text);
		
		db.insertOrThrow(Tables.DIARY_CONTENT, CommonColumn._ID, values);
	}
	/**
	 * 更新日记内容
	 * @param model
	 * @param text
	 */
	public void updateDiaryContent(DateModel model,String text){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._UPDATE_TIME, format.format(model.getDate()));
		values.put(DiaryTrackColumn._TEXT, text);
		
		format = new SimpleDateFormat("yyyy-MM-dd");
		String date=format.format(model.getDate());
		int config_id=model.getConfigId();
		db.update(Tables.DIARY_TRACK, values,
				CommonColumn._CREATE_TIME+" between '"+date+" 00:00:00' and '"+date+" 23:59:59' " +
				      " and "
		           +DiaryTrackColumn._CONFIG_ID+ " = "+config_id,
		        null);
	}
	
	public void updateDiaryContent(Date date,String text){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._UPDATE_TIME, format.format(date));
		values.put(DiaryContentColumn._CONTENT, text);
		
		format = new SimpleDateFormat("yyyy-MM-dd");
		String sDate=format.format(date);
		db.update(Tables.DIARY_CONTENT, values,
				CommonColumn._CREATE_TIME+" between '"+sDate+" 00:00:00' and '"+sDate+" 23:59:59' ",
		        null);
	}
	/**
	 * 判断当天是否写了日记
	 * @param date
	 * @return
	 */
	public String getDiaryContent(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sDate=format.format(date);
		Cursor c=db.query(Tables.DIARY_CONTENT, new String[]{DiaryContentColumn._CONTENT}, 
				CommonColumn._CREATE_TIME+" between '"+sDate+" 00:00:00' and '"+sDate+" 23:59:59' ",
				null,null,null,null);
		if(c==null||c.getCount()==0)
			return null;
		if(c.moveToFirst()){
			String result=c.getString(0);
			c.close();
			return  result;
		}
	    return null;
	}

}
