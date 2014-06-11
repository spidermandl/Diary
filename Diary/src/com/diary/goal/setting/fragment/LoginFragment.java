package com.diary.goal.setting.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.UserAuthActivity;

/**
 *   登陆界面
 *   @author Desmond Duan
 *
 */
public class LoginFragment extends SherlockFragment {

	private EditText account,passwd;
	private TextView resetPasswd,signUp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.login, container, false);
		initView(layout);
		return layout;
	}
	
	private void initView(View layout){
		account=(EditText)layout.findViewById(R.id.login_username);
		passwd=(EditText)layout.findViewById(R.id.login_passwd);
		resetPasswd=(TextView)layout.findViewById(R.id.login_find_passwd);
		signUp=(TextView)layout.findViewById(R.id.login_sign_up);
		signUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((UserAuthActivity)LoginFragment.this.getActivity()).switchFragment(
						new RegisterFragment(), false);
			}
		});
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();

		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(false);	
		ab.setDisplayUseLogoEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setTitle(R.string.app_name);
        /** 
         * Give some text to display if there is no data. In a real
         * application this would come from a resource.
         * We have a menu item to show in action bar.
         * 
         * onCreateOptionsMenu() will be called back
		 **/
        setHasOptionsMenu(true);
        
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(0, R.string.edit_save, 1, R.string.sign_in)
	    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

}
