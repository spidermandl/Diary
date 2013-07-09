package com.diary.goal.setting.activity;

import com.diary.goal.setting.adapter.UnitOverviewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class UnitOverviewActivity extends Activity {

	private UnitOverviewAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ListView listview = new ListView(this);
		mAdapter=new UnitOverviewAdapter(this);
		listview.setAdapter(mAdapter);
		setContentView(listview);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mAdapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}
}