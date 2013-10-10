package com.diary.goal.setting.activity;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.tools.BitmapCustomize;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ScrollView;

public class PaperOverviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.paper_overview);
		super.onCreate(savedInstanceState);
		
		ScrollView scrollView=(ScrollView)this.findViewById(R.id.paper_scroll);
		scrollView.setBackgroundDrawable(new BitmapDrawable(BitmapCustomize.customizePicture(this, R.drawable.paper_overview_diary,
				DiaryApplication.getInstance().getScreen_w(), DiaryApplication.getInstance().getScreen_h(), false)));
	}
}
