package com.diary.goal.setting.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.diary.goal.setting.R;
import com.diary.goal.setting.fragment.LoginFragment;

public class UserAuthActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setTheme(R.style.Theme_Auth);
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
}
