package com.diary.goal.setting.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.diary.goal.setting.R;
import com.diary.goal.setting.fragment.TemplateListFragment;

/**
 * 
 * 日记模板操作
 * 
 */
public class TemplateOperateActivity extends SherlockFragmentActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(arg0);
		
		setContentView(R.layout.template_operation_layout);
		switchFragment(new TemplateListFragment(), true);
		
		final ActionBar ab = getSupportActionBar();
		
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		
		this.setSupportProgressBarIndeterminateVisibility(false);
	}
	/**
	 * 
	 * @param fragment
	 * @param begin 是否第一次启动fragment
	 */
	public void switchFragment(android.support.v4.app.Fragment fragment,boolean begin){
		FragmentTransaction transaction=this.getSupportFragmentManager().beginTransaction();
		if(begin){
			transaction.add(R.id.template_switcher, fragment);
			//transaction.addToBackStack(null);
			transaction.commit();
		}else{
			transaction.replace(R.id.template_switcher, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
			return false;
		default:
			return false;
		}
	}

}
