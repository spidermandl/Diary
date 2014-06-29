package com.diary.goal.setting.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.TemplateListAdapter;

/**
 * 
 * 模板列表
 */
public class TemplateListFragment extends SherlockFragment{

	
	private ListView tempList;
	
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
		TemplateListAdapter adapter =new TemplateListAdapter(this.getActivity());
		tempList.setAdapter(adapter);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		menu.add(0, R.string.sign_in, 1, R.string.sign_in)
//		.setIcon(iconRes)
//	    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}
	
}
