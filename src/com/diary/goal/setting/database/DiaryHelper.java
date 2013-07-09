package com.diary.goal.setting.database;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.diary.goal.setting.model.DateModel;

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
		
		db.setTransactionSuccessful();
		db.endTransaction();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.beginTransaction();
		
		db.execSQL(DROP_DIARY_TRACK);
		
		db.setTransactionSuccessful();
		db.endTransaction();
		onCreate(db);
	}
	/**
	 * @table diary track
	 */
	public static final String DIARY_TRACK_TABLENAME = "diary_track";
	public static final String[] DIARY_TRACK_COLUMNS = {
		"_id","create_time", "update_time" , "type", "category","category_name","category_type","text"
	};
	public static final String CREATE_DIARY_TRACK = 
			"CREATE TABLE IF NOT EXISTS " +  DIARY_TRACK_TABLENAME + "("
			+ DIARY_TRACK_COLUMNS[0] + " integer primary key autoincrement,"//
			+ DIARY_TRACK_COLUMNS[1] + " datetime,"	//create time
			+ DIARY_TRACK_COLUMNS[2] + " datetime,"	//edit time
			+ DIARY_TRACK_COLUMNS[3] + " integer,"	//sudo type
			+ DIARY_TRACK_COLUMNS[4] + " integer,"	//category
			+ DIARY_TRACK_COLUMNS[5] + " text,"	//category name
			+ DIARY_TRACK_COLUMNS[6] + " integer,"	//category type
			+ DIARY_TRACK_COLUMNS[7] + " text"	//content
			+ ")";
	public static final String DROP_DIARY_TRACK = "DROP TABLE IF EXISTS " + DIARY_TRACK_TABLENAME+" ";
	
	/**
	 * get records existence of each day
	 * @return
	 */
	public Cursor getTodayPad(Date date){

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d=format.format(date);
		Cursor c=db.query(true, DIARY_TRACK_TABLENAME, new String[]{DIARY_TRACK_COLUMNS[3]},
				DIARY_TRACK_COLUMNS[1]+" between '"+d+" 00:00:00' and '"+d+" 23:59:59'",
				null, null, null, null, null);
		
		return c;
	}
	
	public Cursor getCategory(DateModel model){
		return getCategory(model.getDate(), model.getType().getType(), model.getCategory());
	}
	
	public Cursor getCategory(Date date,int type,int category){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d=format.format(date);
		Cursor c=db.query(true, DIARY_TRACK_TABLENAME, new String[]{DIARY_TRACK_COLUMNS[4],DIARY_TRACK_COLUMNS[5]},
				DIARY_TRACK_COLUMNS[1]+" between '"+d+" 00:00:00' and '"+d+" 23:59:59' " +
						" and "
				+DIARY_TRACK_COLUMNS[3]+ " = "+type+
				        " and "
				+DIARY_TRACK_COLUMNS[4]+ " = "+category,
				null, null, null, null, null);
		
		return c;
	}
	
	public void insertDiaryContent(DateModel model,String text){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(DIARY_TRACK_COLUMNS[1], format.format(model.getDate()));
		values.put(DIARY_TRACK_COLUMNS[2], format.format(model.getDate()));
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
