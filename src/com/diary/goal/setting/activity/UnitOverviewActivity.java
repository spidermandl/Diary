package com.diary.goal.setting.activity;

import java.util.Date;

import com.diary.goal.setting.adapter.UnitOverviewAdapter;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class UnitOverviewActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent=getIntent();
		ListView listview=new ListView(this);
		listview.setAdapter(new UnitOverviewAdapter(this,new Date(),(SudoType)intent.getSerializableExtra("type")));
		setContentView(listview);
		super.onCreate(savedInstanceState);
	}
}
