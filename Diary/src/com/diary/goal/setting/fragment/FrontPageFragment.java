package com.diary.goal.setting.fragment;

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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.MainFrameActivity;
import com.diary.goal.setting.activity.UserAuthActivity;
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
		
		new Thread(){
			public void run() {
				SharedPreferences diary=FrontPageFragment.this.getActivity().getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
				String username=diary.getString(Constant.P_USERNAME, null);
				String passwd=diary.getString(Constant.P_USERNAME, null);
				if (username!=null&&username.length()>0&&passwd!=null&&passwd.length()>0){
					JSONObject result=API.login(username, passwd);
					if(result!=null&&result.has(Constant.SERVER_SUCCESS)){
						String session_id=null;
						try {
							session_id=result.getString(Constant.SERVER_SESSION_ID);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg=new Message();
						msg.what=SUCCESS;
						msg.obj=session_id;
						handler.sendMessage(msg);
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
					FrontPageFragment.this.startActivity(intent);
					
					if(msg.obj!=null){
						SharedPreferences diary=FrontPageFragment.this.getActivity().getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
						diary.edit().putString(Constant.P_SESSION, msg.obj.toString()).commit();  
					}
  
					break;
				case FAIL:
					((UserAuthActivity)FrontPageFragment.this.getActivity()).switchFragment(new LoginFragment(), false);

					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
	}
}
