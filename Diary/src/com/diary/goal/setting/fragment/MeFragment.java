package com.diary.goal.setting.fragment;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.TemplateOperateActivity;
import com.diary.goal.setting.activity.UserAuthActivity;
import com.diary.goal.setting.tools.Constant;
/**
 * 用户个人页面
 * @author desmond.duan
 *
 */
public class MeFragment extends SherlockFragment{
	
	TextView myTemplate,
	         myLogout,
	         mySync,
	         myVersionCheck;
	
	private OnClickListener listener;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.me_fragment_layout, container, false);
		initView(layout);
		initFunctionality();
		return layout;
	}

	private void initView(View layout) {
		myTemplate=(TextView)layout.findViewById(R.id.me_template);
		mySync=(TextView)layout.findViewById(R.id.me_sync);
		myVersionCheck=(TextView)layout.findViewById(R.id.me_version_check);
		myLogout=(TextView)layout.findViewById(R.id.me_logout);
	}
	
	private void initFunctionality() {
		listener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.me_template:
					Intent intent=new Intent();
					intent.setClass(MeFragment.this.getActivity(), TemplateOperateActivity.class);
					MeFragment.this.startActivityForResult(intent, 0);
					break;
				case R.id.me_sync:
					break;
				case R.id.me_version_check:
					break;
				case R.id.me_logout:
					MeFragment.this.getActivity().setResult(UserAuthActivity.RESULT_LOGOUT);
					MeFragment.this.getActivity().finish();
					break;
				default:
					break;
				}
				
			}
		};
		
		myTemplate.setOnClickListener(listener);
		myLogout.setOnClickListener(listener);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();
		
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		
		HashMap<String, Object> cache=DiaryApplication.getInstance().getMemCache();
		Object name=cache.get(Constant.P_ACCOUNT);
		if(name==null)
			ab.setTitle(R.string.me);
		else
			ab.setTitle(cache.get(Constant.P_ACCOUNT).toString());

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
