package com.diary.goal.setting.activity;

import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.base.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 全屏显示图片
 * @author desmond.duan
 *
 */
public class BigImageActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alertbigimage);
		Intent intent = getIntent();
		String URL = intent.getStringExtra("url");// 拍照url

		String URL2 = intent.getStringExtra("url2");// 相册url
		ImageView bigImage = (ImageView) findViewById(R.id.bigimage);

		Uri uri = Uri.parse(URL);

		bigImage.setImageURI(uri);
		Toast.makeText(getApplicationContext(), "   " + URL, 0).show();

	}
	// bigImage.setImageURI(uri2);

}
