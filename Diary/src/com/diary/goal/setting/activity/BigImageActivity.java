package com.diary.goal.setting.activity;

import com.diary.goal.setting.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class BigImageActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alertbigimage);
		Intent intent = getIntent();
		String URL=intent.getStringExtra("url");
		ImageView bigImage = (ImageView) findViewById(R.id.bigimage);
		Bitmap temp = BitmapFactory.decodeFile(URL);
		Uri uri = Uri.parse(URL);
		bigImage.setImageURI(uri);
		Toast.makeText(this, URL,0).show();
	}

	
}
