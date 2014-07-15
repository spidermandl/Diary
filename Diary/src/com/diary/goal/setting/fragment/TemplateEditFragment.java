package com.diary.goal.setting.fragment;


import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.TemplateEditExpandableAdapter;
import com.diary.goal.setting.adapter.TemplateEditExpandableAdapter.TemplateEditAction;
import com.diary.goal.setting.database.DiaryHelper.DiaryTemplateModel;
import com.diary.goal.setting.tools.Constant;
import com.flurry.org.apache.avro.data.Json;

/**
 * 模板编辑功能实现
 */
public class TemplateEditFragment extends SherlockFragment{

	private ExpandableListView editList;
	private TemplateEditExpandableAdapter expandableAdapter;
	private OnChildClickListener childClickListener;//列表child item事件
	private TemplateEditAction actionListener;//列表item內按钮事件
	
	final static int ADD_ITEM=0;//添加item
	final static int EDIT_ITEM=1;//编辑item
	final static int DELETE_TEMPLATE=2;//删除模板
	final static int RENAME_TITLE=3;//重命名模板title
	
	boolean isChanged=false;//模板是否被编辑过
	
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
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		/**
		 * 初始系统控件
		 */
		super.onActivityCreated(savedInstanceState);
		
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();

		ab.setDisplayShowTitleEnabled(false);
		
	    setHasOptionsMenu(true);
	}
	
	/**
	 * 初始ui控件
	 * @param layout
	 */
	private void initView(View layout) {
		editList=(ExpandableListView)layout.findViewById(R.id.template_edit_list);
	}
	/**
	 * 初始ui功能，监听器事件
	 */
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
				new EditTempBuilder(TemplateEditFragment.this.getActivity(),group,child,EDIT_ITEM)
				.show();
				
			}
			
			@Override
			public void addItem(int group) {
				new EditTempBuilder(TemplateEditFragment.this.getActivity(),group,-1,ADD_ITEM)
				.show();
				
			}

			@Override
			public void deleteItem(int group, int child) {
//				new EditTempBuilder(TemplateEditFragment.this.getActivity(),group,child,DELETE_ITEM)
//				.show();
				JSONObject tempContent=expandableAdapter.getTempJson();
				JSONArray array;
				try {
					array = tempContent.getJSONArray(expandableAdapter.getGroup(group).toString());
					isChanged=true;
					array.remove(child);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		editList.setAdapter(expandableAdapter);
		expandableAdapter.setAction(actionListener);
		editList.setOnChildClickListener(childClickListener);

		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//this.getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			if(isChanged){
				new AlertDialog.Builder(this.getActivity())
				.setMessage(R.string.template_save_indication)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						backToPreviousFragment();
					}
				}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
			}else{
				backToPreviousFragment();
			}
			
			break;
		case R.string.change_template_title:
			new EditTempBuilder(TemplateEditFragment.this.getActivity(),-1,-1,RENAME_TITLE)
			.show();
			break;
		case R.string.save_template:
			DiaryTemplateModel model=expandableAdapter.getDataModel();
			if(model._TAMPLETE==null)
				addTemplate();
			else
				saveTemplate();
			break;
		case R.string.delete_template:
			new EditTempBuilder(TemplateEditFragment.this.getActivity(),-1,-1,DELETE_TEMPLATE)
			.show();
			break;
		case R.string.new_template_name:
			new EditTempBuilder(TemplateEditFragment.this.getActivity(),-1,-1,RENAME_TITLE)
			.show();
			break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		DiaryTemplateModel model = this.getArguments().getParcelable(Constant.TEMPLATE_EXCHANGE);
        menu.add(1,R.string.new_template_name,1,model==null?this.getResources().getString(R.string.new_template_name):model._NAME)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(0, R.string.change_template_title, 1, R.string.change_template_title);
		menu.add(0, R.string.save_template, 1, R.string.save_template);
		menu.add(0, R.string.delete_template, 1, R.string.delete_template);
	}
	
    /**
     * 返回
     */
	private void backToPreviousFragment(){
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
	}
	/**
	 * 删除模板操作
	 */
	private void deleteTemplate(){
		isChanged=false;
		backToPreviousFragment();
	}
	/**
	 * 保存模板操作
	 */
	private void saveTemplate(){
		isChanged=false;
	}
	
	/**
	 * 新增模板操作
	 */
	private void addTemplate(){
		DiaryTemplateModel model=expandableAdapter.getDataModel();
		model._TAMPLETE=expandableAdapter.getTempJson().toString();
		long _id=DiaryApplication.getInstance().getDbHelper().insertDiaryTemplate(new Date(), model._TAMPLETE, "0", model._NAME, "0");
		isChanged=false;
	}
	/**
	 * 
	 * 模版编辑功能中的对话框
	 *
	 */
	class EditTempBuilder extends Builder{

		int status;
		Context context;
		int group;
		int child;
		private EditText textInput;//输入框
		private DialogInterface.OnClickListener okListener=new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DiaryTemplateModel model=expandableAdapter.getDataModel();
				JSONObject tempContent=expandableAdapter.getTempJson();
				try {	
					switch (status) {
					case ADD_ITEM:
						JSONArray array=tempContent.getJSONArray(expandableAdapter.getGroup(group).toString());
						String text=textInput.getEditableText().toString();
						if(text.length()==0){
							return;
						}
						//小标题重复判断
						for(int i=0;i<array.length();i++){
							if(text.equalsIgnoreCase(array.getString(i))){
								Toast.makeText(context, R.string.item_conflict, 500).show();
								return;
							}
						}
						isChanged=true;
						array.put(text);
						break;
					case EDIT_ITEM:
						array=tempContent.getJSONArray(expandableAdapter.getGroup(group).toString());
						text=textInput.getEditableText().toString();
						if(text.length()==0){
							return;
						}
						//小标题重复判断
						for(int i=0;i<array.length();i++){
							if(text.equalsIgnoreCase(array.getString(i))){
								Toast.makeText(context, R.string.item_conflict, 500).show();
								return;
							}
						}
						isChanged=true;
						array.put(child, text);
						break;
					case DELETE_TEMPLATE:
						deleteTemplate();
						break;
					case RENAME_TITLE:
						text=textInput.getEditableText().toString();
						if(text.length()==0){
							return;
						}
						isChanged=true;
						final ActionBar ab = TemplateEditFragment.this.getSherlockActivity().getSupportActionBar();
						ab.setTitle(text);
						break;
					default:
						break;
					}
					expandableAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		};
        private DialogInterface.OnClickListener cancelListener=new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		};
		
		public EditTempBuilder(Context arg0,int groupID,int childID,int action) {
			super(arg0);
			context=arg0;
			group=groupID;
			child=childID;
			textInput=new EditText(arg0);
			status=action;
			switch (status) {
				case ADD_ITEM:
					this.setTitle(R.string.template_add_item_title);
					this.setView(textInput);
					break;
				case EDIT_ITEM:
					this.setTitle(R.string.template_edit_item_title);
					this.setView(textInput);
					break;
				case DELETE_TEMPLATE:
					this.setMessage(R.string.template_del);
					//this.setTitle(R.string.template_del);
					break;
				case RENAME_TITLE:
					this.setTitle(R.string.template_rename_title);
					this.setView(textInput);
					break;
				default:
					break;
			}
			
			this.setPositiveButton(android.R.string.ok, okListener);
			this.setNegativeButton(android.R.string.cancel,cancelListener);
		}
		
	}
}
