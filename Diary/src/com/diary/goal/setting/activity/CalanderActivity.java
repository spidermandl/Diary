package com.diary.goal.setting.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.adapter.CalendarAdapter;
import com.diary.goal.setting.view.ViewFlow;
/**
 * 日历界面
 * @author desmond.duan
 *
 */
public class CalanderActivity extends SherlockActivity {

	private ViewFlow viewFlow;
	private CalendarAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		
		viewFlow=new ViewFlow(this);
		mAdapter=new CalendarAdapter(this);
		viewFlow.setAdapter(mAdapter,Integer.MAX_VALUE/2);
		
		setContentView(viewFlow);
		
		// set defaults for logo & home up
		final ActionBar ab = getSupportActionBar();
		
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
//		ab.setDisplayUseLogoEnabled(true);
//		ab.setDisplayShowHomeEnabled(false);
		//ab.setTitle(R.string.edit_back);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			//this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
			break;
		case 1:
			this.finish();
			break;
		default:
			break;
		}
		return true;
	}
	
}
