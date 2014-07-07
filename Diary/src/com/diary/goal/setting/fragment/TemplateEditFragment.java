package com.diary.goal.setting.fragment;

import java.util.HashMap;

import android.app.Instrumentation;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.TemplateEditExpandableAdapter;
import com.diary.goal.setting.database.DiaryHelper.DiaryTemplateModel;
import com.diary.goal.setting.tools.Constant;

/**
 * 
 * 模板编辑
 */
public class TemplateEditFragment extends SherlockFragment{

	private ExpandableListView editList;
	private TemplateEditExpandableAdapter expandableAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View layout = inflater.inflate(R.layout.template_edit_layout, container, false);
		initView(layout);
		initFunctionality();
		return layout;
	}
	
	private void initView(View layout) {
		editList=(ExpandableListView)layout.findViewById(R.id.template_edit_list);
	}

	private void initFunctionality() {
		Parcelable model = this.getArguments().getParcelable(Constant.TEMPLATE_EXCHANGE);
		expandableAdapter=new TemplateEditExpandableAdapter(this.getActivity(),model);
		editList.setAdapter(expandableAdapter);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();
		
		DiaryTemplateModel model = this.getArguments().getParcelable(Constant.TEMPLATE_EXCHANGE);
		ab.setTitle(model==null?this.getActivity().getResources().getString(R.string.default_template_name):model._NAME);
		
	    setHasOptionsMenu(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//this.getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			/**
			 * 模拟back键事件
			 */
			new Thread() {
				public void run() {
					try {
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
			break;
		case R.string.change_template_title:
			break;
		case R.string.save_template:
			break;
		case R.string.delete_template:
			break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(0, R.string.change_template_title, 1, R.string.change_template_title);
		menu.add(0, R.string.save_template, 1, R.string.save_template);
		menu.add(0, R.string.delete_template, 1, R.string.delete_template);
	}
}
