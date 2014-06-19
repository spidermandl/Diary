package com.diary.goal.setting.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.UserAuthActivity;
import com.diary.goal.setting.tools.API;
import com.diary.goal.setting.tools.Constant;
/**
 * 注册界面
 * @author Desmond Duan
 *
 */
public class RegisterFragment extends SherlockFragment {

	private EditText username,email,passwd,rePasswd;
	private OnClickListener clickListener;
	
	private Handler handler;
	private final static int SUCCESS=0;
	private final static int FAIL=1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//((SherlockFragmentActivity)this.getActivity()).requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.register, container, false);
		initView(layout);
		initFunctionality();
		return layout;
	}
	
	private void initView(View layout){
		username=(EditText)layout.findViewById(R.id.register_username);
		email=(EditText)layout.findViewById(R.id.register_email);
		passwd=(EditText)layout.findViewById(R.id.register_passwd);
		rePasswd=(EditText)layout.findViewById(R.id.confirm_passwd);
	}
	
	private void initFunctionality(){
		clickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((UserAuthActivity)RegisterFragment.this.getActivity()).isNetworkProcess())
					return;
				switch (v.getId()) {

				default:
					break;
				}
				
			}
		};
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:
					Toast.makeText(RegisterFragment.this.getActivity(), R.string.register_success, 500).show();
					SharedPreferences diary=RegisterFragment.this.getActivity().getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
					diary.edit().putString(Constant.P_USERNAME, username.getText().toString()).commit();  
					diary.edit().putString(Constant.P_PASSWORD, passwd.getText().toString()).commit(); 
					diary.edit().putString(Constant.P_EMAIL, email.getText().toString()).commit(); 
					break;
				case FAIL:
					if (msg.obj!=null) {
						Toast.makeText(getActivity(), msg.obj.toString(), 500).show();
					}
					break;
				default:
					break;
				}
				((UserAuthActivity)RegisterFragment.this.getActivity()).switchFragment(new LoginFragment(), false);
				super.handleMessage(msg);
			}
		};
		
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
			/**
			 * 错误判断
			 */
			if(passwd.getText().length()==0||rePasswd.getText().length()==0
					||username.getText().length()==0||email.getText().length()==0){
				Toast.makeText(RegisterFragment.this.getActivity(), R.string.register_error_void_item, 500).show();
			}
			else if(passwd.getText().toString()==rePasswd.getText().toString()){
				Toast.makeText(RegisterFragment.this.getActivity(), R.string.register_error_unidentical_passwd, 500).show();
			}
			/**
			 * 注册
			 */
			JSONObject result=API.register(username.getText().toString(), email.getText().toString(), passwd.getText().toString());
			try {
				if(result!=null&&result.has("success"))
					handler.sendEmptyMessage(SUCCESS);
				else{
					if(result!=null){
						Message msg=new Message();
						msg.obj=result.getString("fail");
						msg.what=FAIL;
						handler.sendMessage(msg);
					}else
						handler.sendEmptyMessage(FAIL);
				}
			} catch (JSONException e) {
				handler.sendEmptyMessage(FAIL);
				e.printStackTrace();
			}
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
