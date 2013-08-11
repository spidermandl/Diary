package com.diary.goal.setting.activity;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.tools.BitmapCustomize;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FrontPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.front_page);
		
		RelativeLayout bg=(RelativeLayout)findViewById(R.id.front_page_bg);
		bg.setBackgroundDrawable(new BitmapDrawable(BitmapCustomize.customizePicture(this, R.drawable.front_page_bg, 
				DiaryApplication.getInstance().getScreen_w(), DiaryApplication.getInstance().getScreen_h(), false)));
		
		TextView start=(TextView)findViewById(R.id.start_journey);
		start.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent=new Intent();
				intent.setClass(FrontPageActivity.this, SudoKuActivity.class);
				FrontPageActivity.this.startActivity(intent);
				
			}
		});
		super.onCreate(savedInstanceState);
	}
}
