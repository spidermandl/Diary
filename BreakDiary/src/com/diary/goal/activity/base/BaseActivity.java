package com.diary.goal.activity.base;

import java.util.ArrayList;
import java.util.List;

import com.diary.goal.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class BaseActivity extends SlidingFragmentActivity{
	private int mTitleRes;
	protected BaseContentFragment mFrag;
	protected List<BaseContentFragment> pageList;

	public BaseActivity() {
		mTitleRes = 0;
	}
	
	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new BaseContentFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (BaseContentFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}

		pageList=new ArrayList<BaseContentFragment>();
		pageList.add(mFrag);
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		ViewPager vp = new ViewPager(this);
		vp.setId("VP".hashCode());
		vp.setAdapter(new ContentPageAdapter(pageList,getSupportFragmentManager()));
		setContentView(vp);

		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) { }

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}

		});
		
		vp.setCurrentItem(0);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			toggle();
//			return true;
//		case R.id.github:
//			Util.goToGitHub(this);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getSupportMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
}
