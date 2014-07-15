package com.diary.goal.setting.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.TemplateOperateActivity;
import com.diary.goal.setting.adapter.TemplateListAdapter;
import com.diary.goal.setting.database.DiaryHelper.DiaryTemplateModel;
import com.diary.goal.setting.tools.Constant;

/**
 * 
 * 模板列表
 */
public class TemplateListFragment extends SherlockFragment{

	
	private ListView tempList;
	private OnItemClickListener itemListener;
	private TemplateListAdapter tempAdapter;
	
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
		tempAdapter =new TemplateListAdapter(this.getActivity());
		itemListener=new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Fragment fragment=new TemplateEditFragment();
				Bundle args=new Bundle();
				args.putParcelable(Constant.TEMPLATE_EXCHANGE, (DiaryTemplateModel)tempAdapter.getItem(position));
				fragment.setArguments(args);
				((TemplateOperateActivity)TemplateListFragment.this.getActivity()).switchFragment(
						fragment, false);
				
			}
		};
		
		tempList.setAdapter(tempAdapter);
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
		case android.R.id.home:
			this.getActivity().finish();
			break;
		case R.string.add_template:
			Fragment fragment=new TemplateEditFragment();
			Bundle args=new Bundle();
			fragment.setArguments(args);
			((TemplateOperateActivity)TemplateListFragment.this.getActivity()).switchFragment(
					fragment, false);
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
