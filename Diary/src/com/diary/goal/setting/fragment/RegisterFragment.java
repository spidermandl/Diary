package com.diary.goal.setting.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.UserAuthActivity;
/**
 * 注册界面
 * @author Desmond Duan
 *
 */
public class RegisterFragment extends SherlockFragment {

	private EditText username,email,passwd,rePasswd;
	private OnClickListener clickListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.register, container, false);
		initView(layout);
		return layout;
	}
	
	private void initView(View layout){
		username=(EditText)layout.findViewById(R.id.register_username);
		email=(EditText)layout.findViewById(R.id.register_email);
		passwd=(EditText)layout.findViewById(R.id.register_passwd);
		rePasswd=(EditText)layout.findViewById(R.id.confirm_passwd);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();

		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(true);	
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
		if(((UserAuthActivity)RegisterFragment.this.getActivity()).isNetworkProcess())
			return true;
		switch (item.getItemId()) {
		case android.R.id.home:
			((UserAuthActivity)RegisterFragment.this.getActivity()).switchFragment(
					new LoginFragment(), false);
			break;
		case R.string.sign_up:
			((UserAuthActivity)RegisterFragment.this.getActivity()).setSupportProgressBarIndeterminateVisibility(true);
			break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(0, R.string.sign_up, 1, R.string.sign_up)
	    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}
}
