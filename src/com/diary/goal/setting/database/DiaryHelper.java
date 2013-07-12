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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

public class DiaryHelper extends SQLiteOpenHelper{

	private SQLiteDatabase db;
	
	private static final String DB_NAME = "diary.db";//DB name
	private static final int DB_VERSION = 1;	//DB version
	
	public interface Tables {
		/**@table diary model config*/
		public static final String DIARY_CONFIG = "diary_config";
		/**@table diary track*/
		public static final String DIARY_TRACK = "diary_track";
	}
	
	public interface Views {
		public static final String TRACK_CONFIG_ALL = "view_diary_all";
	}

	public interface DiaryConfigColumn{
		public static final String _ID = Tables.DIARY_CONFIG+"_id";
		public static final String _SUDO_TYPE = "type";
		public static final String _CATEGORY_INDEX = "category_index";
		public static final String _CATEGORY_NAME = "category_name";
		public static final String _CATEGORY_TYPE = "category_type";
	}
	
	public interface DiaryTrackColumn{
		public static final String _ID = Tables.DIARY_TRACK+"_id";
		public static final String _CREATE_TIME = "create_time";
		public static final String _UPDATE_TIME = "update_time";
		public static final String _CONFIG_ID = DiaryConfigColumn._ID;
		public static final String _TEXT = "text";
	}
	
	public static final String CREATE_DIARY_CONFIG = 
			"CREATE TABLE IF NOT EXISTS " +  Tables.DIARY_TRACK + "("
			+ DiaryTrackColumn._ID + " integer primary key autoincrement,"//
			+ DiaryTrackColumn._CREATE_TIME + " datetime,"	//create time
			+ DiaryTrackColumn._UPDATE_TIME + " datetime,"	//edit time
			+ DiaryTrackColumn._CONFIG_ID + " integer,"	//id in table of diary_config
			+ DiaryTrackColumn._TEXT + " text"	//content
			+ ")";
	public static final String CREATE_DIARY_TRACK = 
			"CREATE TABLE IF NOT EXISTS " +  Tables.DIARY_TRACK + "("
			+ DiaryConfigColumn._ID + " integer primary key autoincrement,"//
			+ DiaryConfigColumn._SUDO_TYPE + " integer,"	//sudo type
			+ DiaryConfigColumn._CATEGORY_INDEX + " integer,"	//index of category
			+ DiaryConfigColumn._CATEGORY_NAME + " text,"	//name of category
			+ DiaryConfigColumn._CATEGORY_TYPE + " integer"	//type of category
			+ ")";
	public static final String CREATE_VIEW_TRACK_CONFIG ="CREATE VIEW "+Views.TRACK_CONFIG_ALL+" AS "
			 +" SELECT "+DiaryTrackColumn._ID+", "
			         +DiaryTrackColumn._CREATE_TIME+", "
			         +DiaryTrackColumn._UPDATE_TIME+", "
			         +DiaryTrackColumn._CONFIG_ID+", "
			         +DiaryTrackColumn._TEXT+", "
			         +DiaryConfigColumn._SUDO_TYPE+", "
			         +DiaryConfigColumn._CATEGORY_INDEX+", "
			         +DiaryConfigColumn._CATEGORY_NAME+", "
			         +DiaryConfigColumn._CATEGORY_TYPE
			  +" FROM "+Tables.DIARY_CONFIG+", "+Tables.DIARY_TRACK
              +" WHERE "+DiaryTrackColumn._CONFIG_ID+" = "+DiaryConfigColumn._ID;
	
	public static final String DROP_DIARY_TRACK = "DROP TABLE IF EXISTS " + Tables.DIARY_TRACK+" ";
	public static final String DROP_DIRAY_CONFIG = "DROP TABLE IF EXISTS " + Tables.DIARY_CONFIG+" ";
	public static final String DROP_VIEW_CONFIG_TRACK = "DROP VIEW IF EXISTS " + Views.TRACK_CONFIG_ALL;
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
		db.execSQL(CREATE_VIEW_TRACK_CONFIG);
		
		initDiaryConfig();
		
		db.setTransactionSuccessful();
		db.endTransaction();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.beginTransaction();
		
		db.execSQL(DROP_VIEW_CONFIG_TRACK);
		db.execSQL(DROP_DIARY_TRACK);
		db.execSQL(DROP_DIRAY_CONFIG);
		
		db.setTransactionSuccessful();
		db.endTransaction();
		onCreate(db);
	}

	/**
	 * init table of diary config
	 */
	private void initDiaryConfig(){
		JSONObject json=DiaryApplication.getInstance().getSudoConfig();
		for(Iterator iter = json.keys(); iter.hasNext();){
			try {
				JSONArray array=json.getJSONArray((String)iter.next());
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = (JSONObject) array.get(i);
					ContentValues values=new ContentValues();
					values.put(DiaryConfigColumn._SUDO_TYPE, i);
					values.put(DiaryConfigColumn._CATEGORY_INDEX, jo.getInt("index"));
					values.put(DiaryConfigColumn._CATEGORY_NAME, jo.getInt("name"));
					values.put(DiaryConfigColumn._CATEGORY_TYPE, jo.getInt("type"));
					db.insertOrThrow(Tables.DIARY_CONFIG, DiaryConfigColumn._ID, values);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public Cursor getDateDiaryAll(){
		Cursor c=db.query(Tables.DIARY_CONFIG, 
				new String[]{DiaryConfigColumn._ID,
				             DiaryConfigColumn._SUDO_TYPE,
				             DiaryConfigColumn._CATEGORY_INDEX,
				             DiaryConfigColumn._CATEGORY_NAME,
				             DiaryConfigColumn._CATEGORY_TYPE}, 
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
				DiaryTrackColumn._CREATE_TIME+" between '"+d+" 00:00:00' and '"+d+" 23:59:59'",
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
				DiaryTrackColumn._CREATE_TIME+" between '"+d+" 00:00:00' and '"+d+" 23:59:59' " +
						" and "
				+DiaryConfigColumn._SUDO_TYPE+ " = "+type+
				        " and "
				+DiaryConfigColumn._CATEGORY_INDEX+ " = "+category,
				null, null, null, null, null);
		
		return c;
	}
	
	public void insertDiaryContent(DateModel model,String text){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(DiaryTrackColumn._CREATE_TIME, format.format(model.getDate()));
		values.put(DiaryTrackColumn._UPDATE_TIME, format.format(model.getDate()));
		values.put(DIARY_TRACK_COLUMNS[3], model.getType().getType());
		values.put(DIARY_TRACK_COLUMNS[4], model.getCategory());
		values.put(DIARY_TRACK_COLUMNS[5], text);
		
		db.insertOrThrow(DIARY_TRACK_TABLENAME, DIARY_TRACK_COLUMNS[0], values);
	}
	
	public void updateDiaryContent(DateModel model,String text){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(DIARY_TRACK_COLUMNS[2], format.format(model.getDate()));
		values.put(DIARY_TRACK_COLUMNS[5], text);
		
		format = new SimpleDateFormat("yyyy-MM-dd");
		String date=format.format(model.getDate());
		int type=model.getType().getType();
		int category=model.getCategory();
		db.update(DIARY_TRACK_TABLENAME, values,
				DIARY_TRACK_COLUMNS[1]+" between '"+date+" 00:00:00' and '"+date+" 23:59:59' " +
				      " and "
		           +DIARY_TRACK_COLUMNS[3]+ " = "+type+
		              " and "
		           +DIARY_TRACK_COLUMNS[4]+ " = "+category,
		        null);
	}

}
