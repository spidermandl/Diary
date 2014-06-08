package com.diary.goal.setting.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.diary.goal.setting.R;
import com.diary.goal.setting.fragment.LoginFragment;

public class UserAuthActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.user_auth_layout);
		//LoginFragment loginFrag=new LoginFragment();
		//this.getSupportFragmentManager().beginTransaction().add(R.layout.login, loginFrag).commit();
	}
	
	@Override
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		super.onAttachFragment(fragment);
	}
}
