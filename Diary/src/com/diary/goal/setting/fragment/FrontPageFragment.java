package com.diary.goal.setting.fragment;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.MainFrameActivity;
import com.diary.goal.setting.activity.UserAuthActivity;
import com.diary.goal.setting.database.DiaryHelper.UserModel;
import com.diary.goal.setting.tools.API;
import com.diary.goal.setting.tools.BitmapCustomize;
import com.diary.goal.setting.tools.Constant;
/**
 * 首页界面
 * @author desmond.duan
 *
 */
public class FrontPageFragment extends SherlockFragment{
	
	private Handler handler;
	private final static int SUCCESS=0;
	private final static int FAIL=1;
	private final static int NO_SERVER=2;
	
	private String username,password;
	private long userID;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();
		ab.hide();
        
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = //inflater.inflate(R.layout.login, container, false);
		new View(this.getActivity());
		layout.setBackgroundDrawable(new BitmapDrawable(BitmapCustomize
				.customizePicture(this.getActivity(), R.drawable.global_element_bg, 
						DiaryApplication.getInstance().getScreen_w(), 
						DiaryApplication.getInstance().getScreen_h(), false)));
		
		initFunctionality();
		final boolean hasAccount=hasUserInfo();
		new Thread(){
			public void run() {
//				SharedPreferences diary=FrontPageFragment.this.getActivity().getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
//				String username=diary.getString(Constant.P_ACCOUNT, null);
//				String passwd=diary.getString(Constant.P_PASSWORD, null);
				if (hasAccount){
					JSONObject result=API.login(username, password);
					if(result!=null&&result.has(Constant.SERVER_SUCCESS)){
						Message msg=new Message();
						msg.what=SUCCESS;
						msg.obj=result;
						handler.sendMessage(msg);
						return;
					}
					/**
					 * 网络断开或服务器没有运行
					 */
					if(result==null){
						handler.sendEmptyMessage(NO_SERVER);
						return;
					}
				}
				handler.sendEmptyMessage(FAIL);
			};
		}.start();
		return layout;
	}
	
	private void initFunctionality(){
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:		
					Intent intent=new Intent();
					intent.setClass(FrontPageFragment.this.getActivity(), MainFrameActivity.class);
					FrontPageFragment.this.startActivityForResult(intent, 0);
					
					if(msg.obj!=null){
						JSONObject obj=(JSONObject)msg.obj;
						HashMap<String, String> cache=DiaryApplication.getInstance().getMemCache();
						try {
							cache.put(Constant.SERVER_SESSION_ID, obj.getString(Constant.SERVER_SESSION_ID));
							//cache.put(Constant.SERVER_USER_ID, obj.getString(Constant.SERVER_USER_ID));
							cache.put(Constant.SERVER_USER_ID, String.valueOf(userID));
							cache.put(Constant.SERVER_USER_NAME, username);
							DiaryApplication.getInstance().getDbHelper().loginTrigger(String.valueOf(userID));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
  
					break;
				case FAIL:
					((UserAuthActivity)FrontPageFragment.this.getActivity()).switchFragment(new LoginFragment(), false);
					break;
				case NO_SERVER:
					//String user_id=diary.getString(Constant.P_USER_ID, null);
					if(userID==0){//user_id 没有被存
						Toast.makeText(FrontPageFragment.this.getActivity(), R.string.server_error, 500).show();
						handler.sendEmptyMessage(FAIL);
						//FrontPageFragment.this.getActivity().finish();
					}else{
						intent=new Intent();
						intent.setClass(FrontPageFragment.this.getActivity(), MainFrameActivity.class);
						FrontPageFragment.this.startActivityForResult(intent, 0);
						
						HashMap<String, String> cache=DiaryApplication.getInstance().getMemCache();
						cache.put(Constant.SERVER_USER_ID, String.valueOf(userID));
					}
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
	}
	/**
	 * 判断是否有登陆记录
	 * @return
	 */
	private boolean hasUserInfo(){
		SharedPreferences diary=FrontPageFragment.this.getActivity().getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
		username=diary.getString(Constant.P_ACCOUNT, null);
		password=diary.getString(Constant.P_PASSWORD, null);
		if(username==null||username.length()==0||password==null||password.length()==0){
			userID=DiaryApplication.getInstance().getDbHelper().lastestLoginTime();
			if(userID==0)
				return false;
			else{
				UserModel model=DiaryApplication.getInstance().getDbHelper().getUserInfo(String.valueOf(userID));
				username=model._USERNAME;
				password=model._PASSWD;
			}
		}
		
		return true;
	}
}
