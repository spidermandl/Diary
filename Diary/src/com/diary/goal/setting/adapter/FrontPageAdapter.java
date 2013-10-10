
package com.diary.goal.setting.adapter;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.MainFrameActivity;
import com.diary.goal.setting.activity.SudoKuActivity;
import com.diary.goal.setting.tools.BitmapCustomize;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FrontPageAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private static final int[] ids = {R.drawable.front_page_bg1, 
		                              R.drawable.front_page_bg2, 
		                              R.drawable.front_page_bg3,
		                              R.drawable.front_page_bg4};
	private static final int[] layouts={R.layout.front_page_1, 
                                        R.layout.front_page_2, 
                                        R.layout.front_page_3,
                                        R.layout.front_page_4};

	public FrontPageAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	
	public int getCount() {
		return ids.length;   //返回很大的值使得getView中的position不断增大来实现循环
	}

	
	public Object getItem(int position) {
		return position;
	}

	
	public long getItemId(int position) {
		return position;
	}

	
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView=getParseLayout(position);
		}

		
		return convertView;
	}

	private View getParseLayout(int index){
		View view = mInflater.inflate(layouts[index], null);
		view.setBackgroundDrawable(new BitmapDrawable(BitmapCustomize.customizePicture(mContext, ids[index], 
				DiaryApplication.getInstance().getScreen_w(), 
				DiaryApplication.getInstance().getScreen_h(), false)));
		View start=view.findViewById(R.id.start_journey);
		if(start!=null){
			start.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Intent intent=new Intent();
					intent.setClass(mContext, MainFrameActivity.class);
					mContext.startActivity(intent);
				}
			});
		}
		return view;
	}
}
