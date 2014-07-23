package com.diary.goal.setting.database;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.invalid.DateModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

public class DiaryHelper extends SQLiteOpenHelper{

	private SQLiteDatabase db;
	
	private static final String DB_NAME = "diary.db";//DB name
	private static final int DB_VERSION = 1;	//DB version
	
	public interface Tables {
		/**@table diary model config*/
		public static final String DIARY_CONFIG = "diary_config";
		/**@table diary track*/
		public static final String DIARY_TRACK = "diary_track";
		/**@table 用户表*/
		public static final String USER = "user";
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
	 * 用户表(由于切换用户引发的一系列需求)
	 */
	public interface UserColumn{
		public static final String _USERNAME = "name";
		public static final String _PASSWD = "passwd";
		public static final String _EMAIL = "email";
		public static final String _LOGINTIME = "last_login_time";
		public static final String _PIC_PATH="pic_path";
		public static final String _NICKNAME="nick_name";
		public static final String _SYNC = "sync";//账户是否同步, 0:没有同步，1:已经同步
	}
	
	/**
	 * 
	 * 用户信息内存模型
	 */
	public static class UserModel{
		public String _ID;
		public String _USERNAME;
		public String _PASSWD;
		public String _EMAIL;
		public String _LOGINTIME;
		public String _PIC_PATH;
		public String _NICKNAME;
		public String _SYNC;
	}
	
	/**
	 * 日记模板表
	 */
	public interface DiaryTemplateColumn{
		public static final String _TAMPLETE = "templete"; //json:{[subtitle,type],[subtile,type]...}
		public static final String _SYNC = "sync";/*同步状态, 0:没有同步,
		                                                   1:已经同步,
		                                                   -1 不需要同步（如用户当天正编辑的模板）,
		                                                   -2 需要删除的模板（和服务器同步后该条目删除）
		                                          */
		public static final String _NAME = "name";//模板名
		public static final String _CREATER_ID = "creater_id";//创建者id
		public static final String _SELECTED = "_selected";//当前选中模板 0:没有选中,1:选中
	}
	/**
	 * 
	 * 日记模板内存模型
	 */
	public static class DiaryTemplateModel implements Parcelable{
		public String _ID;
		public String _TAMPLETE;
		public String _SYNC;
		public String _NAME;
		public String _SELECTED;
		public String _CREATE_TIME;
		public static final Parcelable.Creator<DiaryTemplateModel> CREATOR = new Parcelable.Creator<DiaryTemplateModel>() {
		    public DiaryTemplateModel createFromParcel(Parcel in) {
		    	DiaryTemplateModel model = new DiaryTemplateModel(); 
		    	model._ID=in.readString();
		    	model._TAMPLETE=in.readString();
		    	model._SYNC=in.readString();
		    	model._NAME=in.readString();
		    	model._SELECTED=in.readString();
		    	model._CREATE_TIME=in.readString();
		        return model;
		    }
		
		    public DiaryTemplateModel[] newArray(int size) {
		        return new DiaryTemplateModel[size];
		    }
		 };
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(_ID);
			dest.writeString(_TAMPLETE);
			dest.writeString(_SYNC);
			dest.writeString(_NAME);
			dest.writeString(_SELECTED);
			dest.writeString(_CREATE_TIME);
		}
	}
	/**
	 * 日记内容
	 */
	public interface DiaryContentColumn{
		public static final String _USER_ID="user_id";//本地用户id
		public static final String _CONTENT="_content";
		public static final String _SYNC = "sync";//是否同步, 0:没有同步，1:已经同步
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
			+ DiaryTemplateColumn._TAMPLETE + " text,"/** 模板存储json格式
										             * {
										             * title_order:[big1,big2,bi3]
										             * big1:[small1,small2]
										             * big2:[small1,small2]
										             * big3:[small1,small2]
										             * }
										             **/
			+DiaryTemplateColumn._SYNC+" text,"
			+DiaryTemplateColumn._NAME+" text,"
			+DiaryTemplateColumn._CREATER_ID+" integer,"
			+DiaryTemplateColumn._SELECTED+" text"
			+ ")";
	public static final String CREATE_DIARY_CONTENT = 
			"CREATE TABLE IF NOT EXISTS " +  Tables.DIARY_CONTENT + "("
			+ CommonColumn._ID + " integer primary key autoincrement,"//
			+ CommonColumn._CREATE_TIME + " datetime,"	
			+ CommonColumn._UPDATE_TIME + " datetime,"	 
			+ DiaryContentColumn._USER_ID + " integer,"
			+ DiaryContentColumn._CONTENT + " text," /** 日记字串存储json格式
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
			+DiaryTemplateColumn._SYNC+" integer"
			+ ")";

	public static final String CREATE_USER = 
			"CREATE TABLE IF NOT EXISTS " +  Tables.USER + "("
			+ CommonColumn._ID + " integer primary key autoincrement,"//
			+ CommonColumn._CREATE_TIME + " datetime,"	
			+ CommonColumn._UPDATE_TIME + " datetime,"	
			+ UserColumn._USERNAME + " text,"	
			+ UserColumn._PASSWD + " text,"	
			+ UserColumn._EMAIL + " text,"	
			+ UserColumn._LOGINTIME + " datetime,"	
			+ UserColumn._PIC_PATH + " text,"	
			+ UserColumn._NICKNAME + " text,"	
			+ UserColumn._SYNC + " integer" 
			+ ")";
	
	public static final String DROP_DIARY_TRACK = "DROP TABLE IF EXISTS " + Tables.DIARY_TRACK+" ";
	public static final String DROP_DIRAY_CONFIG = "DROP TABLE IF EXISTS " + Tables.DIARY_CONFIG+" ";
	public static final String DROP_VIEW_CONFIG_TRACK = "DROP VIEW IF EXISTS " + Views.TRACK_CONFIG_ALL;
	public static final String DROP_USER = "DROP TABLE IF EXISTS " + Tables.USER+" ";
	public static final String DROP_DIARY_TEMPLETE = "DROP TABLE IF EXISTS " + Tables.DIARY_TEMPLETE+" ";
	public static final String DROP_DIARY_CONTENT= "DROP TABLE IF EXISTS " + Tables.DIARY_CONTENT+" ";
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
		db.execSQL(CREATE_USER);
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
		db.execSQL(DROP_USER);
		
		
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
	/*******************************************************************************************
	 * 用户表操作方法
	 *******************************************************************************************/
	/**
	 * 获取本地用户id
	 * @param account
	 * @param passwd
	 * @return
	 */
	public long getUser(String account,String passwd){
		Cursor c=db.query(Tables.USER, new String[]{CommonColumn._ID}, 
				"( "+UserColumn._USERNAME+" = '"+account+"' or "+UserColumn._EMAIL+" = '"+account+"' ) and "+UserColumn._PASSWD+" = '"+passwd+"'",
				null,null,null,null);
		if(c==null||!c.moveToFirst()){
			createUser(account, passwd, null, new Date());
			return getUser(account,passwd);
		}
		long _id=c.getInt(0);
		c.close();
		return _id;
	}

	/**
	 * 获取本地用户id
	 * @param account
	 * @param passwd
	 * @return
	 */
	public UserModel getUserInfo(String account,String passwd){
		Cursor c=db.query(Tables.USER, 
				new String[]{CommonColumn._ID,UserColumn._USERNAME,
				             UserColumn._PASSWD,UserColumn._EMAIL,
				             UserColumn._LOGINTIME,UserColumn._NICKNAME,
				             UserColumn._PIC_PATH,UserColumn._SYNC}, 
				"( "+UserColumn._USERNAME+" = '"+account+"' or "+UserColumn._EMAIL+" = '"+account+"' ) and "+UserColumn._PASSWD+" = '"+passwd+"'",
				null,null,null,null);
		if(c==null||!c.moveToFirst()){
			createUser(account, passwd, null, new Date());
			return getUserInfo(account,passwd);
		}
		UserModel model=new UserModel();
		model._ID=String.valueOf(c.getInt(0));
		model._USERNAME=c.getString(1);
		model._PASSWD=c.getString(2);
		model._EMAIL=c.getString(3);
		model._LOGINTIME=c.getString(4);
		model._NICKNAME=c.getString(5);
		model._PIC_PATH=c.getString(6);
		model._SYNC=String.valueOf(c.getInt(7));
		c.close();
		return model;
	}
	/**
	 * 获取本地用户id
	 * @param _id
	 * @return
	 */
	public UserModel getUserInfo(String _id){
		Cursor c=db.query(Tables.USER, 
				new String[]{CommonColumn._ID,UserColumn._USERNAME,
				             UserColumn._PASSWD,UserColumn._EMAIL,
				             UserColumn._LOGINTIME,UserColumn._NICKNAME,
				             UserColumn._PIC_PATH,UserColumn._SYNC}, 
				CommonColumn._ID+" = "+_id,
				null,null,null,null);
		if(c==null||!c.moveToFirst()){
			return null;
		}
		UserModel model=new UserModel();
		model._ID=String.valueOf(c.getInt(0));
		model._USERNAME=c.getString(1);
		model._PASSWD=c.getString(2);
		model._EMAIL=c.getString(3);
		model._LOGINTIME=c.getString(4);
		model._NICKNAME=c.getString(5);
		model._PIC_PATH=c.getString(6);
		model._SYNC=String.valueOf(c.getInt(7));
		c.close();
		return model;
	}
	
	/**
	 * 创建用户
	 * @param name
	 * @param passwd
	 * @param email
	 * @param date
	 */
	public void createUser(String name,String passwd,String email,Date date){
		Cursor c=db.query(Tables.USER, null, 
				"( "+UserColumn._USERNAME+" = '"+name+"' or "+UserColumn._EMAIL+" = '"+name+"' ) and "+UserColumn._PASSWD+" = '"+passwd+"'",
				null,null,null,null);
		if(c!=null&&c.getCount()!=0){
			c.close();
			return;
		}
		ContentValues values = new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._CREATE_TIME, format.format(date));
		values.put(CommonColumn._UPDATE_TIME, format.format(date));
		values.put(UserColumn._USERNAME, name);
		values.put(UserColumn._PASSWD, passwd);
		values.put(UserColumn._EMAIL, email);
		values.put(UserColumn._SYNC, 0);
		
		db.insertOrThrow(Tables.USER, CommonColumn._ID, values);
	}
	
	/**
	 * 判断账户是否已经同步
	 * @param account
	 * @param passwd
	 * @return
	 */
	public boolean getSynRecord(String account,String passwd){
		Cursor c=db.query(Tables.USER, new String[]{UserColumn._SYNC}, 
				"( "+UserColumn._USERNAME+" = '"+account+"' or "+UserColumn._EMAIL+" = '"+account+"' ) and "+UserColumn._PASSWD+" = '"+passwd+"'",
				null,null,null,null);
		if(c==null||!c.moveToFirst()){
			return false;
		}
		if(c.getInt(0)==0){
			c.close();
			return false;
		}
		c.close();
		return true;
	}
	/**
	 * 判断账户是否已经同步
	 * @param userid
	 * @return
	 */
	public boolean getSynRecord(String userid){
		Cursor c=db.query(Tables.USER, new String[]{UserColumn._SYNC}, 
				CommonColumn._ID+" = "+userid,
				null,null,null,null);
		if(c==null||!c.moveToFirst()){
			if(c!=null)
				c.close();
			return false;
		}
		if(c.getInt(0)==0){
			c.close();
			return false;
		}
		c.close();
		return true;
	}
	/**
	 * 用户所有日记同步完成
	 * @param date
	 * @param userid
	 */
	public void updateSynRecord(Date date,String userid,int sync){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._UPDATE_TIME, format.format(date));
		values.put(UserColumn._SYNC, sync);
		
		db.update(Tables.USER, values, CommonColumn._ID+" = "+userid, null);
	}
	/**
	 * 最近一次登录
	 * @param account
	 * @param passwd
	 */
	public void loginTrigger(String account,String passwd){
		Date date=new Date();
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(UserColumn._LOGINTIME, format.format(date));
		
		db.update(Tables.USER, values, "( "+UserColumn._USERNAME+" = '"+account+"' or "+UserColumn._EMAIL+" = '"+account+"' ) and "+UserColumn._PASSWD+" = '"+passwd+"'", null);
	}
	/**
	 * 最近一次登录
	 * @param _id
	 */
	public void loginTrigger(String _id){
		Date date=new Date();
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(UserColumn._LOGINTIME, format.format(date));
		
		db.update(Tables.USER, values, CommonColumn._ID+" = "+_id, null);
	}
	/**
	 * 最后登录用户
	 * @return
	 */
	public long lastestLoginTime(){
		Cursor c=db.query(Tables.USER, new String[]{CommonColumn._ID}, 
				null, null, null, null, UserColumn._LOGINTIME+" DESC ", "1");
		if(c==null||!c.moveToFirst()){
			if(c!=null)
				c.close();
			return 0;
		}
		long _id= c.getInt(0);
		c.close();
		return _id;
	}
	/**********************************************************************************
	 *日记模板表操作方法
	 **********************************************************************************/
	/**
	 * 新建模板
	 * @param date
	 * @param text
	 */
	public long insertDiaryTemplate(Date date,String text,String sync,String name,String selected){
		ContentValues values = new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._CREATE_TIME, format.format(date));
		values.put(CommonColumn._UPDATE_TIME, format.format(date));
		values.put(DiaryTemplateColumn._TAMPLETE, text);
		values.put(DiaryTemplateColumn._SYNC, sync);
		values.put(DiaryTemplateColumn._NAME, name);
		values.put(DiaryTemplateColumn._SELECTED, selected);
		
		return db.insertOrThrow(Tables.DIARY_TEMPLETE, CommonColumn._ID, values);
	}
	/*******************************************************************************************
	 * 模板（template）表操作方法
	 *******************************************************************************************/
	/**
	 * 更新模板
	 * @param model
	 */
	public void updateDiaryTemplate(DiaryTemplateModel model){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._UPDATE_TIME, format.format(new Date()));
		if(model._NAME!=null)
			values.put(DiaryTemplateColumn._NAME, model._NAME);
		if(model._TAMPLETE!=null)
			values.put(DiaryTemplateColumn._TAMPLETE, model._TAMPLETE);
		if(model._SYNC!=null)
			values.put(DiaryTemplateColumn._SYNC, model._SYNC);
		if(model._SELECTED!=null)
			values.put(DiaryTemplateColumn._SELECTED, model._SELECTED);
		db.update(Tables.DIARY_TEMPLETE, values,
				CommonColumn._ID+" = "+model._ID,
		        null);
	}
	/**
	 * 更新模板
	 * @param date
	 * @param text
	 * @param sync
	 * @param name
	 * @param selected
	 */
	public void updateDiaryTemplate(Date date,String text,String sync,String name,String selected){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._UPDATE_TIME, format.format(date));
		if(text!=null)
			values.put(DiaryTemplateColumn._TAMPLETE, text);
		if(sync!=null)
			values.put(DiaryTemplateColumn._SYNC, sync);
		if(name!=null)
			values.put(DiaryTemplateColumn._NAME, name);
		if(selected!=null)
			values.put(DiaryTemplateColumn._SELECTED, selected);
		
		format = new SimpleDateFormat("yyyy-MM-dd");
		String sDate=format.format(date);
		db.update(Tables.DIARY_TEMPLETE, values,
				CommonColumn._CREATE_TIME+" between '"+sDate+" 00:00:00' and '"+sDate+" 23:59:59' ",
		        null);
	}
	/**
	 * 删除
	 * @param model
	 */
	public void deleteDiaryTemplate(DiaryTemplateModel model){
		db.delete(Tables.DIARY_TEMPLETE, CommonColumn._ID+" = "+model._ID, null);
	}
	/**
	 * 获得所有创建的日记模板
	 * @return
	 */
	public DiaryTemplateModel[] getFixedDiaryTemplates(){
		Cursor c=db.query(Tables.DIARY_TEMPLETE, 
                          new String[]{CommonColumn._ID,
									   DiaryTemplateColumn._NAME,
				                       DiaryTemplateColumn._SELECTED,
				                       DiaryTemplateColumn._SYNC,
				                       DiaryTemplateColumn._TAMPLETE,
				                       CommonColumn._CREATE_TIME}, 
				          DiaryTemplateColumn._SYNC+" <> '-2'", 
				          null, null, null, 
                          CommonColumn._CREATE_TIME+" DESC");
		DiaryTemplateModel[] results= new DiaryTemplateModel[c==null?0:c.getCount()];
		if(results.length==0){//模板为空
			createDefaultTemplate();
			if(c!=null)
				c.close();
			return getFixedDiaryTemplates();
		}
		int index=0;
		while(c.moveToNext()){
			DiaryTemplateModel model=new DiaryTemplateModel();
			model._ID = c.getString(0);
			model._NAME = c.getString(1);
			model._SELECTED = c.getString(2);
			model._SYNC = c.getString(3);
			model._TAMPLETE = c.getString(4);
			model._CREATE_TIME = c.getString(5);
			results[index]=model;
			index++;
		}
		if(c!=null)
			c.close();
		return results;
	}
	/**
	 * 获取正要编辑的模板
	 * @param date
	 * @return
	 */
	public String getCurrentAppliedDiaryTemplate(){
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		String sDate=format.format(date);
//		Cursor c=db.query(Tables.DIARY_TEMPLETE, new String[]{DiaryTemplateColumn._TAMPLETE}, 
//				CommonColumn._CREATE_TIME+" between '"+sDate+" 00:00:00' and '"+sDate+" 23:59:59' and "+DiaryTemplateColumn._SYNC+" = '-1'",
//				null,null,null,CommonColumn._CREATE_TIME+" DESC");
//		if(c==null||c.getCount()==0){
			
			Cursor c=db.query(Tables.DIARY_TEMPLETE, new String[]{DiaryTemplateColumn._TAMPLETE}, 
				DiaryTemplateColumn._SELECTED+" = '1'",
				null,null,null,null);
			if(c==null||c.getCount()==0){
				//没有选中的日记模板，创建默认模板，并设置为选中
				String result=createDefaultTemplate();
				if(c!=null)
					c.close();
				return result;
			}
			if (c.moveToFirst()){
				String result=c.getString(0);
				c.close();
				return result;
			}
			return null;
//		}
//		if (c.moveToFirst()){
//			String result=c.getString(0);
//			c.close();
//			return result;
//		}
//		return null;
	}
	
	/**
	 * 创建第一个日记模板
	 */
	private String createDefaultTemplate(){
		String result=DiaryApplication.getInstance().getResources().getText(R.string.default_template).toString();
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate=format.format(new Date());
		values.put(CommonColumn._CREATE_TIME, sDate);
		values.put(CommonColumn._UPDATE_TIME, sDate);
		values.put(DiaryTemplateColumn._TAMPLETE,result);
		values.put(DiaryTemplateColumn._SYNC, "-1");
		values.put(DiaryTemplateColumn._NAME, DiaryApplication.getInstance().getResources().getText(R.string.default_template_name).toString());
		values.put(DiaryTemplateColumn._SELECTED, "1");
		
		db.insertOrThrow(Tables.DIARY_TEMPLETE, CommonColumn._ID, values);
		return result;
	}
	/*******************************************************************************************
	 * 日记表（diary_content）表操作方法
	 *******************************************************************************************/
	/**
	 * 创建日记内容
	 * @param model
	 * @param text
	 */
	/**
	 * @deprecated
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
	/**
	 * 新建日记
	 * @param userid
	 * @param createDate
	 * @param updateDate
	 * @param text
	 * @param sync
	 */
	public void insertDiaryContent(String userid,Date createDate,Date updateDate,String text,int sync){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._CREATE_TIME, format.format(createDate));
		values.put(CommonColumn._UPDATE_TIME, format.format(updateDate));
		values.put(DiaryContentColumn._USER_ID, userid);
		values.put(DiaryContentColumn._CONTENT, text);
		values.put(DiaryContentColumn._SYNC, sync);
		
		db.insertOrThrow(Tables.DIARY_CONTENT, CommonColumn._ID, values);
	}
	/**
	 * 新建日记
	 * @param userid
	 * @param createDate
	 * @param updateDate
	 * @param text
	 * @param sync
	 */
	public void insertDiaryContent(String userid,String createDate,String updateDate,String text,int sync){
		ContentValues values=new ContentValues();
		values.put(CommonColumn._CREATE_TIME, createDate);
		values.put(CommonColumn._UPDATE_TIME, updateDate);
		values.put(DiaryContentColumn._USER_ID, userid);
		values.put(DiaryContentColumn._CONTENT, text);
		values.put(DiaryContentColumn._SYNC, sync);
		
		db.insertOrThrow(Tables.DIARY_CONTENT, CommonColumn._ID, values);
	}
	/**
	 * 更新日记内容
	 * @deprecated
	 * @param model
	 * @param text
	 */
	public void updateDiaryContent(DateModel model,String text,int sync){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._UPDATE_TIME, format.format(model.getDate()));
		values.put(DiaryTrackColumn._TEXT, text);
		values.put(DiaryContentColumn._SYNC, sync);
		
		format = new SimpleDateFormat("yyyy-MM-dd");
		String date=format.format(model.getDate());
		int config_id=model.getConfigId();
		db.update(Tables.DIARY_TRACK, values,
				CommonColumn._CREATE_TIME+" between '"+date+" 00:00:00' and '"+date+" 23:59:59' " +
				      " and "
		           +DiaryTrackColumn._CONFIG_ID+ " = "+config_id,
		        null);
	}
	/**
	 * 更新日记内容
	 * @param userid
	 * @param date
	 * @param text
	 * @param sync
	 */
	public void updateDiaryContent(String userid,Date date,String text,int sync){
		ContentValues values=new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put(CommonColumn._UPDATE_TIME, format.format(date));
		if (text!=null)
			values.put(DiaryContentColumn._CONTENT, text);
		values.put(DiaryContentColumn._SYNC, sync);
		
		format = new SimpleDateFormat("yyyy-MM-dd");
		String sDate=format.format(date);
		db.update(Tables.DIARY_CONTENT, values,
				DiaryContentColumn._USER_ID+" = "+userid+" and "+CommonColumn._CREATE_TIME+" between '"+sDate+" 00:00:00' and '"+sDate+" 23:59:59' ",
		        null);
	}
	/**
	 * 获取当天日记
	 * @param date
	 * @return
	 */
	public String[] getDiaryContent(String userid,Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sDate=format.format(date);
		Cursor c=db.query(Tables.DIARY_CONTENT, new String[]{DiaryContentColumn._CONTENT,CommonColumn._CREATE_TIME}, 
				DiaryContentColumn._USER_ID+" = "+userid+" and "+CommonColumn._CREATE_TIME+" between '"+sDate+" 00:00:00' and '"+sDate+" 23:59:59' ",
				null,null,null,null);
		if(c==null||c.getCount()==0){
			if(c!=null)
				c.close();
			return new String[]{null,null};
		}
		if(c.moveToFirst()){
			String[] result=new String[2];
			result[0]=c.getString(0);
			result[1]=c.getString(1);
			c.close();
			return  result;
		}
	    return new String[]{null,null};
	}

	


}
