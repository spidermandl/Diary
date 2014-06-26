package com.diary.goal.setting.activity;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.diary.goal.setting.R;
import com.diary.goal.setting.fragment.FrontPageFragment;
/**
 * 用户注册登录界面
 * @author desmond.duan
 *
 */
public class UserAuthActivity extends SherlockFragmentActivity {
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
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		this.finish();
		super.onActivityResult(arg0, arg1, arg2);
	}
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
