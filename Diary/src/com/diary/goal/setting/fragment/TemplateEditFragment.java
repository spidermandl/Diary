package com.diary.goal.setting.fragment;


import java.nio.channels.Pipe.SinkChannel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.TemplateEditExpandableAdapter;
import com.diary.goal.setting.adapter.TemplateEditExpandableAdapter.TemplateEditAction;
import com.diary.goal.setting.database.DiaryHelper.DiaryTemplateModel;
import com.diary.goal.setting.tools.Constant;
import com.flurry.org.apache.avro.data.Json;

/**
 * 
 * 模板编辑
 */
public class TemplateEditFragment extends SherlockFragment{

	private ExpandableListView editList;
	private TemplateEditExpandableAdapter expandableAdapter;
	private OnChildClickListener childClickListener;//列表child item事件
	private TemplateEditAction actionListener;//列表item內按钮事件
	
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
		childClickListener=new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		actionListener=new TemplateEditExpandableAdapter.TemplateEditAction() {
			
			@Override
			public void editItem(int group, int child) {
				new EditTempBuilder(TemplateEditFragment.this.getActivity(),group,child)
				.show();
				
			}
			
			@Override
			public void addItem(int group) {
				new EditTempBuilder(TemplateEditFragment.this.getActivity(),group,-1)
				.show();
				
			}
		};
		editList.setAdapter(expandableAdapter);
		expandableAdapter.setAction(actionListener);
		editList.setOnChildClickListener(childClickListener);

		
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
	
	/**
	 * 
	 * 模版编辑功能中的对话框
	 *
	 */
	class EditTempBuilder extends Builder{

		int group;
		int child;
		private OnClickListener popTextInputListener;//弹出文字输入框事件
		private AlertDialog.Builder textInputDialog;//文字输入对话框
		private EditText textInput;//输入框
		private DialogInterface.OnClickListener okListener=new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DiaryTemplateModel model=expandableAdapter.getDataModel();
				JSONObject tempContent=expandableAdapter.getTempJson();
				try {
					JSONArray array=tempContent.getJSONArray(expandableAdapter.getGroup(group).toString());
					String text=textInput.getEditableText().toString();
					boolean singlton=true;//小标题重复判断
					for(int i=0;i<array.length();i++){
						if(text.equalsIgnoreCase(array.getString(i))){
							singlton=false;
							break;
						}
					}
					if(singlton){
						if(child>=0){//编辑
							
						}else{//添加
							
						}		
					}else{
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				expandableAdapter.notifyDataSetChanged();
			}
		};
        private DialogInterface.OnClickListener cancelListener=new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		};
		
		public EditTempBuilder(Context arg0,int groupID,int childID) {
			super(arg0);
			group=groupID;
			child=childID;
			textInput=new EditText(arg0);
			this.setView(textInput);
			this.setPositiveButton(android.R.string.ok, okListener);
			this.setNegativeButton(android.R.string.cancel,cancelListener);
		}
		
	}
}
