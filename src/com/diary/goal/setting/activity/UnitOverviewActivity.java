package com.diary.goal.setting.activity;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.UnitOverviewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class UnitOverviewActivity extends Activity {

	private UnitOverviewAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ExpandableListView listview = new ExpandableListView(this);
		listview.setGroupIndicator(null);
		//listview.setGroupIndicator(this.getResources().getDrawable(R.drawable.group_icon_selector));
		//listview.setIndicatorBounds(DiaryApplication.getInstance().getScreen_w()-20, DiaryApplication.getInstance().getScreen_w()-10);
		listview.setDividerHeight(0);
		mAdapter=new UnitOverviewAdapter(this);
		listview.setAdapter(mAdapter);
		//listview.addHeaderView(v);
		setContentView(listview);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mAdapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
