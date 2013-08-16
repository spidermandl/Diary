package com.diary.goal.setting.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.tools.BitmapCustomize;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Very first page,loading the promotion
 * @author DuanLei
 *
 */
public class FrontPageActivity extends Activity {

	
	@Override
	protected void onNewIntent(Intent intent) {
		this.finish();
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.front_page);
		
		RelativeLayout bg=(RelativeLayout)findViewById(R.id.front_page_bg);
		bg.setBackgroundDrawable(new BitmapDrawable(BitmapCustomize.customizePicture(this, R.drawable.front_page_bg, 
				DiaryApplication.getInstance().getScreen_w(), DiaryApplication.getInstance().getScreen_h(), false)));
		TextView start=(TextView)findViewById(R.id.start_journey);
		
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		Boolean isFirst = prefs.getBoolean("initialLoading", true);
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Intent intent=new Intent();
				intent.setClass(FrontPageActivity.this, SudoKuActivity.class);
				FrontPageActivity.this.startActivity(intent);
				super.handleMessage(msg);
			}
		};
		if(!isFirst){
			start.setVisibility(View.GONE);
			
			Timer timer=new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					handler.sendEmptyMessage(0);	
				}
			}, 5000);
			
		}else{
			SharedPreferences.Editor edit=prefs.edit();
			edit.putBoolean("initialLoading", false);
			edit.commit();
			
			start.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View arg0) {
					Intent intent=new Intent();
					intent.setClass(FrontPageActivity.this, SudoKuActivity.class);
					FrontPageActivity.this.startActivity(intent);
					
				}
			});
		}

		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		DiaryApplication.getInstance().quit();
		System.exit(0);
		super.onDestroy();
	}
}
