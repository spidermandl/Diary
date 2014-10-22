package com.diary.goal.setting.activity;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.fragment.FrontPageFragment;
import com.diary.goal.setting.fragment.LoginFragment;
/**
 * 用户注册登录界面
 * @author desmond.duan
 *
 */
public class UserAuthActivity extends SherlockFragmentActivity {
	
	public static final String COMING_INTENT_TYPE="type";
	public static final int EXIT=1000;//程序退出
	public static final int LOGOUT=1001;//登出
	public static final int UNLOCK_NUMBER_LOCK=1002;//解锁数字密码
    /**
     * 判断是否在做网络请求
     */
	private boolean isNetworkProcess=false;
	//private ActionMode actionMode;
	
	@Override
	protected void onCreate(Bundle arg0) {
		setTheme(R.style.Theme_Auth);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(arg0);
		setContentView(R.layout.user_auth_layout);
//		actionMode=startActionMode(new UserAuthCallback());
		this.getSupportActionBar().hide();
		switchFragment(new FrontPageFragment(), true);
		this.setSupportProgressBarIndeterminateVisibility(false);
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		switch (intent.getIntExtra(COMING_INTENT_TYPE, -1)) {
		case EXIT:
			this.finish();
			break;
		case LOGOUT:
			this.getSupportFragmentManager().beginTransaction().replace(
					R.id.auth_switcher, new LoginFragment()).commitAllowingStateLoss();
			break;
		case UNLOCK_NUMBER_LOCK:
			this.getSupportFragmentManager().beginTransaction().replace(
					R.id.auth_switcher, new LoginFragment()).commitAllowingStateLoss();
			break;
		default:
			break;
		}
		super.onNewIntent(intent);
	}
	
	public void switchFragment(android.support.v4.app.Fragment fragment,boolean begin){
		if(begin){
			this.getSupportFragmentManager().beginTransaction().add(
					R.id.auth_switcher, fragment).commit();
		}else{
			this.getSupportFragmentManager().beginTransaction().replace(
					R.id.auth_switcher, fragment).commit();
		}
	}
	
	@Override
	public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
		setNetworkProcess(visible);
		super.setSupportProgressBarIndeterminateVisibility(visible);
	}

	public boolean isNetworkProcess() {
		return isNetworkProcess;
	}


	public void setNetworkProcess(boolean isNetworkProcess) {
		this.isNetworkProcess = isNetworkProcess;
	}
	
	
//	@Override
//	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//		switch (arg1) {
//		case RESULT_EXIT:
//			this.finish();
//			break;
//		case RESULT_LOGOUT:
//			this.getSupportFragmentManager().beginTransaction().replace(
//					R.id.auth_switcher, new LoginFragment()).commitAllowingStateLoss();
//			break;
//		default:
//			this.finish();
//			break;
//		}
//		super.onActivityResult(arg0, arg1, arg2);
//	}
//	public void showActionBar(){
//		actionMode=startActionMode(new UserAuthCallback());
//	}
//
//	public void hideActionBar(){
//		if(actionMode!=null){
//			actionMode.finish();
//		}
//	}
//
//	private final class UserAuthCallback implements ActionMode.Callback {
//
//		@Override
//		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public void onDestroyActionMode(ActionMode mode) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
}
