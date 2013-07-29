package com.diary.goal.setting.activity;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.NinePanelAdapter;
import com.diary.goal.setting.adapter.OverviewStrollAdapter;
import com.diary.goal.setting.adapter.UnitOverviewAdapter;
import com.diary.goal.setting.listener.FlipPathListener;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.view.ViewFlow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class UnitOverviewActivity extends Activity {

	private UnitOverviewAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//listview.addHeaderView(v);
//		setContentView(R.layout.essay_overview);
//		ExpandableListView listview=(ExpandableListView)this.findViewById(R.id.overview_list);
//		//listview.setGroupIndicator(null);
//		//listview.setGroupIndicator(this.getResources().getDrawable(R.drawable.group_icon_selector));
//		//listview.setIndicatorBounds(DiaryApplication.getInstance().getScreen_w()-20, DiaryApplication.getInstance().getScreen_w()-10);
//		listview.setDividerHeight(0);
//		mAdapter=new UnitOverviewAdapter(this);
//		listview.setAdapter(mAdapter);
//		TextView title=(TextView)this.findViewById(R.id.overview_title);
//		title.setText(Constant.stringDict.get(Constant.SudoType.getTypeString(DiaryApplication.getInstance().getDateModel().getType())));
		
		ViewFlow viewFlow=new ViewFlow(this);
		viewFlow.setAdapter(new OverviewStrollAdapter(this),Integer.MAX_VALUE);
		viewFlow.setFlipListener(new FlipPathListener() {
			
			@Override
			public void track(int steps) {
				
			}
		});
		setContentView(viewFlow);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mAdapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
