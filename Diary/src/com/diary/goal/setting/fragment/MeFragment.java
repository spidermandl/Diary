package com.diary.goal.setting.fragment;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.MainFrameActivity;
import com.diary.goal.setting.activity.SettingActivity;
import com.diary.goal.setting.activity.TemplateOperateActivity;
import com.diary.goal.setting.activity.UserAuthActivity;
import com.diary.goal.setting.service.SyncDBService;
import com.diary.goal.setting.tools.API;
import com.diary.goal.setting.tools.Constant;
/**
 * 用户个人页面
 * @author desmond.duan
 *
 */
public class MeFragment extends SherlockFragment{
	
	private static final int SYNC_SUCCESS=1;
	private static final int VERSION_CHECK_SUCCESS=2;
	private static final int FAIL=3;
	
	TextView myTemplate,
	         myLogout,
	         mySync,
	         myVersionCheck,
	         mySetting;
	
	private OnClickListener listener;
	private Handler networkHandler;
	private boolean isProgress=false;//进度条显示
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.me_fragment_layout, container, false);
		initView(layout);
		initFunctionality();
		return layout;
	}

	private void initView(View layout) {
		myTemplate=(TextView)layout.findViewById(R.id.me_template);
		mySync=(TextView)layout.findViewById(R.id.me_sync);
		myVersionCheck=(TextView)layout.findViewById(R.id.me_version_check);
		myLogout=(TextView)layout.findViewById(R.id.me_logout);
		mySetting=(TextView)layout.findViewById(R.id.me_setting);
	}
	
	private void initFunctionality() {
		listener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainFrameActivity self=(MainFrameActivity)MeFragment.this.getActivity();
				if(!isProgress){
					switch (v.getId()) {
					case R.id.me_template:
						Intent intent=new Intent();
						intent.setClass(MeFragment.this.getActivity(), TemplateOperateActivity.class);
						MeFragment.this.startActivityForResult(intent, 0);
						break;
					case R.id.me_setting:
						intent=new Intent();
						intent.setClass(MeFragment.this.getActivity(), SettingActivity.class);
						MeFragment.this.startActivity(intent);
						break;
					case R.id.me_sync://同步
						self.setSupportProgressBarIndeterminateVisibility(true);
						isProgress=true;
						SyncDBService.startSelf(MeFragment.this.getActivity(),SyncDBService.DIARY_SYNC);
						SyncDBService.startSelf(MeFragment.this.getActivity(),SyncDBService.TEMPLATE_SYNC);
						new Thread(){
							public void run() {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								networkHandler.sendEmptyMessage(SYNC_SUCCESS);
							};
						}.start();
						break;
					case R.id.me_version_check://版本检查
						self.setSupportProgressBarIndeterminateVisibility(true);
						isProgress=true;
						new Thread(){
							@Override
							public void run() {
								JSONObject result=API.versionCheck(Constant.ANDROID_VERSION_CODE);
								Message msg=new Message();
								msg.what=VERSION_CHECK_SUCCESS;
								msg.obj=result;
								networkHandler.sendMessage(msg);
							}
						}.start();
						break;
					case R.id.me_logout://切换账户
						MeFragment.this.getActivity().setResult(UserAuthActivity.RESULT_LOGOUT);
						MeFragment.this.getActivity().finish();
						break;
					default:
						break;
					}
				}
				
			}
		};
		
		myTemplate.setOnClickListener(listener);
		mySync.setOnClickListener(listener);
		myVersionCheck.setOnClickListener(listener);
		myLogout.setOnClickListener(listener);
		mySetting.setOnClickListener(listener);
		
		networkHandler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case SYNC_SUCCESS:
					Toast.makeText(getActivity(), R.string.me_sync_finish, 500).show();
					break;
				case VERSION_CHECK_SUCCESS:
					AlertDialog.Builder hint=new AlertDialog.Builder(getActivity());
					if(msg.obj!=null){
						final JSONObject result=(JSONObject)msg.obj;
						if(result.has(Constant.SERVER_UPDATE_URL)){
							hint.setTitle(R.string.me_new_version);
							hint.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.GINGERBREAD){
											sysDownloadOver9(result.getString(Constant.SERVER_UPDATE_URL));
										}else{
											sysDownloadLess9(result.getString(Constant.SERVER_UPDATE_URL));
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
							hint.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {

									
								}
							});
							hint.show();
							break;
						}
					}
					hint.setTitle(R.string.me_no_more_update);
					hint.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					hint.show();
					break;
					
				case FAIL:
					break;
					
				default:
					break;
				}
				isProgress=false;
				((MainFrameActivity)MeFragment.this.getActivity()).setSupportProgressBarIndeterminateVisibility(false);
			};
		};
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();
		
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		
		HashMap<String, Object> cache=DiaryApplication.getInstance().getMemCache();
		Object name=cache.get(Constant.P_ACCOUNT);
		if(name==null)
			ab.setTitle(R.string.me);
		else
			ab.setTitle(cache.get(Constant.P_ACCOUNT).toString());

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 大于系统版本9 下载
	 * @param url
	 */
	@TargetApi(9)
	private void sysDownloadOver9(String url){
	    DownloadManager downloadManager = (DownloadManager) MeFragment.this.getActivity().getSystemService(Context.DOWNLOAD_SERVICE);									
		Uri uri = Uri.parse(url);
		Request request = new Request(uri);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);  
		request.setVisibleInDownloadsUi(false);
	    downloadManager.enqueue(request);
	}
	/**
	 * 小于系统版本9 下载
	 * @param url
	 */
	private void sysDownloadLess9(String url){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.parse(url);
		intent.setData(uri);  
        startActivity(intent);  
	}
}
