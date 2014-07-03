package com.diary.goal.setting.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.TemplateOperateActivity;
import com.diary.goal.setting.activity.UserAuthActivity;
import com.diary.goal.setting.adapter.TemplateListAdapter;

/**
 * 
 * 模板列表
 */
public class TemplateListFragment extends SherlockFragment{

	
	private ListView tempList;
	private OnItemClickListener itemListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View layout = inflater.inflate(R.layout.template_list_layout, container, false);
		initView(layout);
		initFunctionality();
		return layout;
	}
	
	private void initView(View layout) {
		tempList=(ListView)layout.findViewById(R.id.template_list);
		
	}
	
	private void initFunctionality() {
		itemListener=new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				((TemplateOperateActivity)TemplateListFragment.this.getActivity()).switchFragment(
						new TemplateEditFragment(), false);
				
			}
		};
		TemplateListAdapter adapter =new TemplateListAdapter(this.getActivity());
		tempList.setAdapter(adapter);
		tempList.setOnItemClickListener(itemListener);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.string.add_template:
			((TemplateOperateActivity)TemplateListFragment.this.getActivity()).switchFragment(
					new TemplateEditFragment(), false);
			break;

		default:
			break;
		}
		return true;
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(0, R.string.add_template, 1, R.string.add_template)
		.setIcon(R.drawable.ico_btn_plus_gray)
	    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}
	
}
