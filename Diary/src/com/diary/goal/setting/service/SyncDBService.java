package com.diary.goal.setting.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.database.DiaryHelper.DiaryContentModel;
import com.diary.goal.setting.database.DiaryHelper.DiaryTemplateModel;
import com.diary.goal.setting.tools.API;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.MyLog;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * 数据库同步服务
 * 
 * @author desmond.duan
 * 
 */
public class SyncDBService extends Service {

	public static final String REQUEST_SIGNAL="signal";
	
	public static final int EXIT_SYNC = -1;// 停止信号
	public static final int NONE_SYNC = 0;// 闲置信号
	public static final int DIARY_SYNC = 1;// 同步日记信号
	public static final int TEMPLATE_SYNC = 2;// 同步日记模板信号

	public static final int SUCCESS = 1;
	public static final int FAIL = 2;

	ArrayList<Integer> signals = new ArrayList<Integer>();
	SyncThread sThread = new SyncThread();
	Handler signalHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DIARY_SYNC:
				fetchDiary();
				break;
			case TEMPLATE_SYNC:
				postTemplate();
				break;
			default:
				break;
			}
		};
	};

	public static void startSelf(Activity host,int signal){
		Intent intent=new Intent();
		intent.putExtra(REQUEST_SIGNAL, signal);
		intent.setClass(host, SyncDBService.class);
		host.startService(intent);
	}
	
	public static void stopService(Activity host){
		Intent intent = new Intent();
		intent.setClass(host, SyncDBService.class);
		host.stopService(intent);
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.optSignal(true, intent==null?NONE_SYNC:intent.getIntExtra(REQUEST_SIGNAL, NONE_SYNC));
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
	    this.optSignal(true, EXIT_SYNC);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 处理信号队列
	 * 
	 * @param opt
	 *            ture 塞入 ;false 取出. FIFO
	 * @param sig
	 * @return
	 */
	private synchronized int optSignal(boolean opt, int sig) {
		if (opt) {
			signals.add(sig);
			return 0;
		} else {
			if (signals.size() == 0)
				return NONE_SYNC;
			return signals.remove(0);
		}
	}

	/**
	 * 下载用户日记
	 */
	private void fetchDiary() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:
					JSONObject obj = (JSONObject) msg.obj;
					try {
						JSONArray array = obj.getJSONArray(Constant.SERVER_DIARY_LIST);
						String uid = DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID).toString();
						for (int i = 0; i < array.length(); i++) {
							JSONObject diary = array.getJSONObject(i);

							String created_at = diary.getString(Constant.SERVER_CREATED_AT);
							String updated_at = diary.getString(Constant.SERVER_UPDATED_AT);
							String content = diary.getString(Constant.SERVER_CONTENT);
							DiaryApplication.getInstance().getDbHelper().insertDiaryContent(uid, created_at,updated_at, content, 1);// 插入单条日记
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case FAIL:

					break;

				default:
					break;
				}
				postDiary();
				super.handleMessage(msg);
			}
		};
		HashMap<String, Object> cache = DiaryApplication.getInstance().getMemCache();
		cache.put(Constant.P_DEFAULT_TEMPLATE,this.getResources().getString(Constant.TAMPLATE));
		final Object session_id = cache.get(Constant.P_SESSION);
		if (session_id != null) {
			String user_id = cache.get(Constant.SERVER_USER_ID)==null?"-1":cache.get(Constant.SERVER_USER_ID).toString();
			if (!DiaryApplication.getInstance().getDbHelper().getSynDiary(user_id)) {
				// 没有同步过
				new Thread() {
					public void run() {
						JSONObject result = API.fetchDiarys(session_id.toString());
						if (result != null&& result.has(Constant.SERVER_SUCCESS)) {
							Message msg = new Message();
							msg.obj = result;
							msg.what = SUCCESS;
							handler.sendMessage(msg);
						} else {
							handler.sendEmptyMessage(FAIL);
						}
					};
				}.start();
			}else{
				postDiary();
			}
		}else{
			postDiary();
		}
	}
	/**
	 * 上传日记
	 */
	private void postDiary(){
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:
					HashMap<String, Object> memCache = DiaryApplication.getInstance().getMemCache();
					DiaryApplication.getInstance().getDbHelper().updateDiaryContent(
							memCache.get(Constant.SERVER_USER_ID).toString(),(Date)msg.obj, null, 1);
					break;
				case FAIL:

					break;

				default:
					break;
				}
				sThread.setProcess(false);
				super.handleMessage(msg);
			}
		};
		HashMap<String, Object> memCache = DiaryApplication.getInstance().getMemCache();
		final Object session_id=memCache.get(Constant.P_SESSION);
		final String user_id=memCache.get(Constant.SERVER_USER_ID).toString();
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final DiaryContentModel diaryModel= DiaryApplication.getInstance().getDbHelper().getDiaryContent(user_id,new Date());
		final Date date=new Date();
		if (session_id != null) {
			new Thread(){
				public void run() {
					if(diaryModel._SYNC==0){//没有同步
						JSONObject result=API.updateDiary(session_id.toString(), diaryModel._CREATE_TIME, format.format(date), diaryModel._CONTENT);
						if(result!=null&&result.has(Constant.SERVER_SUCCESS)){
							Message msg=new Message();
							msg.what=SUCCESS;
							msg.obj=date;
							handler.sendMessage(msg);
						}else{
							handler.sendEmptyMessage(FAIL);
						}
					}else{
						sThread.setProcess(false);
					}
				};
			}.start();
		}else{
			sThread.setProcess(false);
		}
		
	}

	/**
	 * 上传日记模板
	 */
	private void postTemplate(){
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:
					String user_id=DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID).toString();
					DiaryTemplateModel[] adds=DiaryApplication.getInstance().getDbHelper().getDiaryTemplatesBySync(user_id, "0");
					DiaryTemplateModel[] updates=DiaryApplication.getInstance().getDbHelper().getDiaryTemplatesBySync(user_id, "2");
					DiaryTemplateModel[] dels=DiaryApplication.getInstance().getDbHelper().getDiaryTemplatesBySync(user_id, "-2");
					for (DiaryTemplateModel add:adds){
						add._SYNC="1";
						DiaryApplication.getInstance().getDbHelper().updateDiaryTemplate(add);
					}
					for(DiaryTemplateModel update:updates){
						update._SYNC="1";
						DiaryApplication.getInstance().getDbHelper().updateDiaryTemplate(update);
						
					}
					for(DiaryTemplateModel del:dels){
						DiaryApplication.getInstance().getDbHelper().deleteDiaryTemplate(del);
					}
					break;
				case FAIL:
					break;
				default:
					break;
				}
				sThread.setProcess(false);
				super.handleMessage(msg);
			}
		};
		String user_id=DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID).toString();
		DiaryTemplateModel[] adds=DiaryApplication.getInstance().getDbHelper().getDiaryTemplatesBySync(user_id, "0");
		DiaryTemplateModel[] updates=DiaryApplication.getInstance().getDbHelper().getDiaryTemplatesBySync(user_id, "2");
		DiaryTemplateModel[] dels=DiaryApplication.getInstance().getDbHelper().getDiaryTemplatesBySync(user_id, "-2");
		ArrayList<Long> addIds=(ArrayList<Long>)DiaryApplication.getInstance().getMemCache().get(Constant.P_TEMPLATE_ADDLIST);
		addIds.clear();
		/**
		 * 保存新增模板id
		 */
		for(DiaryTemplateModel m:adds){
			addIds.add(Long.parseLong(m._ID));
		}
		final Object session_id= DiaryApplication.getInstance().getMemCache().get(Constant.P_SESSION);
		final String addJson=transferModelToJsonArray(adds).toString();
		final String updateJson=transferModelToJsonArray(updates).toString();
		final String delJson=transferModelToJsonArray(dels).toString();
		if(session_id!=null&&(adds.length>0||updates.length>0||dels.length>0)){
			new Thread() {
				public void run() {
					JSONObject result = API.pushUserTemplates(session_id.toString(),addJson,updateJson,delJson);
					if(result!=null&&result.has(Constant.SERVER_SUCCESS)){
						Message msg=new Message();
						msg.what=SUCCESS;
						msg.obj=result;
						handler.sendMessage(msg);
					}else{
						handler.sendEmptyMessage(FAIL);
					}
				};
			}.start();
		}else{
			sThread.setProcess(false);
		}
		
	}
	
	/**
	 * 模板model转成JsonArray
	 * @param models
	 * @return
	 */
	private JSONArray transferModelToJsonArray(DiaryTemplateModel[] models){
		JSONArray sends=new JSONArray();
		String user_id=DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID).toString();
		for(DiaryTemplateModel m:models){
			JSONObject obj=new JSONObject();
			try {
				obj.put(Constant.SERVER_TEMPLATE_LIST_ID, m._ID);
				obj.put(Constant.SERVER_USER_ID, user_id);
				obj.put(Constant.SERVER_TEMPLATE_LIST_NAME, m._NAME);
				obj.put(Constant.SERVER_TEMPLATE_LIST_FORMAT, m._TAMPLETE);
				obj.put(Constant.SERVER_TEMPLATE_LIST_SELECTED, m._SELECTED);
				obj.put(Constant.SERVER_USER_CREATED_AT, m._CREATE_TIME);
				obj.put(Constant.SERVER_USER_UPDATED_AT, m._UPDATE_TIME);
				sends.put(obj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sends;
	}
	/**
	 * 同步管理线程
	 * 
	 * @author desmond.duan
	 * 
	 */
	class SyncThread extends Thread {
		private int idleCount;
		private boolean isProcessing = false;// 判断是否在执行网络请求

		
		public SyncThread(){
			idleCount=0;
			this.start();
		}
		
		public synchronized void setProcess(boolean is) {
			isProcessing = is;
		}

		@Override
		public void run() {
			while (true) {
				if (!isProcessing) {
					switch (optSignal(false, 0)) {
					case EXIT_SYNC:
						MyLog.e("----------------------------------", "EXIT_SYNC");
						return;
					case NONE_SYNC:
						//MyLog.e("----------------------------------", "NONE_SYNC");
						try {
							if(idleCount>=120){
								if(DiaryApplication.getInstance().isCacheFlag()){
									optSignal(true, DIARY_SYNC);
									optSignal(true, TEMPLATE_SYNC);
								}else{
									//缓存被清除
									idleCount=0;
								}
							}
							idleCount++;
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case DIARY_SYNC:
						MyLog.e("----------------------------------", "DIARY_SYNC");
						signalHandler.sendEmptyMessage(DIARY_SYNC);
						isProcessing = true;
						idleCount=0;
						break;
					case TEMPLATE_SYNC:
						MyLog.e("----------------------------------", "TEMPLATE_SYNC");
						signalHandler.sendEmptyMessage(TEMPLATE_SYNC);
						isProcessing =true;
						idleCount=0;
						break;
					default:

						break;
					}
				} else {
					try {
						//MyLog.e("----------------------------------", "isProcessing");
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

}
