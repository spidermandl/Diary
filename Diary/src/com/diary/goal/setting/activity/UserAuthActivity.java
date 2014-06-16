package com.diary.goal.setting.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.diary.goal.setting.R;
import com.diary.goal.setting.fragment.LoginFragment;
/**
 * 用户注册登录界面
 * @author desmond.duan
 *
 */
public class UserAuthActivity extends SherlockFragmentActivity {

	private boolean isNetworkProcess=false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setTheme(R.style.Theme_Auth);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.user_auth_layout);
		switchFragment(new LoginFragment(), true);
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
}
