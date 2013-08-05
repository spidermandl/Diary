package com.diary.goal.setting.activity;

import java.util.Calendar;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.adapter.OverviewStrollAdapter;
import com.diary.goal.setting.listener.FlipPathListener;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.view.ViewFlow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * overview on each sudotype
 * @author duanlei
 *
 */
public class UnitOverviewActivity extends Activity {

	private OverviewStrollAdapter mAdapter;
	Handler UIhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
		    mAdapter.refresh();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		ViewFlow viewFlow=new ViewFlow(this);
		mAdapter=new OverviewStrollAdapter(this);
		viewFlow.setAdapter(mAdapter,mAdapter.getCount()-1+DiaryApplication.getInstance().getDateCursor());
		viewFlow.setFlipListener(new FlipPathListener() {
			
			@Override
			public void track(int steps) {

			    final int f_steps=steps;
			    UIhandler.post(new Runnable() {
					
					@Override
					public void run() {
						int position=DiaryApplication.getInstance().getDateCursor();
						position+=f_steps;
						DiaryApplication.getInstance().setDateCursor(position);
						Calendar date = Calendar.getInstance();
						DateModel model = DiaryApplication.getInstance().getDateModel();
						date.setTime(model.getDate());
						date.add(Calendar.DAY_OF_MONTH, f_steps);
//						Log.e("position",position+"");
//						Log.e("month", date.get(Calendar.MONTH)+1+"");
//						Log.e("day", date.get(Calendar.DAY_OF_MONTH)+"");
						model.setDate(date.getTime());
						UIhandler.sendEmptyMessage(0);
					}
				});
			}
		});
		setContentView(viewFlow);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mAdapter.refresh();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
