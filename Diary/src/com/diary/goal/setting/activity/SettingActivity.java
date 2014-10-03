package com.diary.goal.setting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.base.BaseSherlockActivity;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.MyPreference;

/**
 * 用户设置界面
 * @author Desmond Duan
 *
 */
public class SettingActivity extends BaseSherlockActivity implements OnClickListener{

	private TextView passwd_protect_status;
	private CheckBox passwd_protect_check;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.setting);
		initView();
		
		final ActionBar ab = getSupportActionBar();
		
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(true);
		ab.setTitle(R.string.setting_title);
		
		super.onCreate(savedInstanceState);
	}
	
	private void initView(){
		passwd_protect_status=(TextView)findViewById(R.id.passwd_status);
		passwd_protect_check=(CheckBox)findViewById(R.id.passwd_check);
		
		passwd_protect_check.setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		String numberCode = MyPreference.getInstance().readString(Constant.P_NUMBER_LOCK);
		if(numberCode==null||numberCode.equals("")){//密码关闭
			passwd_protect_check.setChecked(false);
			passwd_protect_status.setText(R.string.passwd_protection_close);
		}else{
			passwd_protect_check.setChecked(true);
			passwd_protect_status.setText(R.string.passwd_protection_open);
		}
		super.onResume();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case android.R.id.home:
		    this.finish();
			//this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
			break;
		default:
			break;
		}
		return true;
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.passwd_check:
			Intent intent=new Intent();
			intent.setClass(this, NumberLockActivity.class);
			if(passwd_protect_check.isChecked()){//check时间先于click事件
				//设置密码
				passwd_protect_check.setChecked(false);
				intent.putExtra(NumberLockActivity.TYPE,NumberLockActivity.SETTING_PASSWORD);
			}else{
				//取消密码
				passwd_protect_check.setChecked(true);
				intent.putExtra(NumberLockActivity.TYPE,NumberLockActivity.CLEAR_PASSWORD);
			}
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
}
