package com.diary.goal.setting.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
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
	private OnClickListener clickListener;
	
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
		initFunctionality();
		account=(EditText)layout.findViewById(R.id.login_username);
		passwd=(EditText)layout.findViewById(R.id.login_passwd);
		resetPasswd=(TextView)layout.findViewById(R.id.login_find_passwd);
		resetPasswd.setOnClickListener(clickListener);
		signUp=(TextView)layout.findViewById(R.id.login_sign_up);
		signUp.setOnClickListener(clickListener);
	}
	
	private void initFunctionality(){
		clickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((UserAuthActivity)LoginFragment.this.getActivity()).isNetworkProcess())
					return;
				switch (v.getId()) {
				case R.id.login_find_passwd:
					
					break;
				case R.id.login_sign_up:
					((UserAuthActivity)LoginFragment.this.getActivity()).switchFragment(
							new RegisterFragment(), false);
					break;

				default:
					break;
				}
				
			}
		};
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case R.string.sign_in:
			((UserAuthActivity)this.getActivity()).setSupportProgress(Window.PROGRESS_END);
			((UserAuthActivity)this.getActivity()).setSupportProgressBarIndeterminateVisibility(true);
			break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(0, R.string.sign_in, 1, R.string.sign_in)
	    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

}
