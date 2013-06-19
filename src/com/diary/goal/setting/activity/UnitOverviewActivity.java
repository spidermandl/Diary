package com.diary.goal.setting.activity;

import java.util.Date;

import com.diary.goal.setting.adapter.UnitOverviewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class UnitOverviewActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ListView listview=new ListView(this);
		listview.setAdapter(new UnitOverviewAdapter(this));
		setContentView(listview);
		super.onCreate(savedInstanceState);
	}
}
