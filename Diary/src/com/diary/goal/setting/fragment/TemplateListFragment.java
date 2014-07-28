package com.diary.goal.setting.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.TemplateOperateActivity;
import com.diary.goal.setting.adapter.TemplateListAdapter;
import com.diary.goal.setting.database.DiaryHelper.DiaryTemplateModel;
import com.diary.goal.setting.tools.API;
import com.diary.goal.setting.tools.Constant;

/**
 * 
 * 模板列表
 */
public class TemplateListFragment extends SherlockFragment{
	
	private ListView tempList;
	private OnItemClickListener itemListener;
	private TemplateListAdapter tempAdapter;
	private Handler tempListHandler;
	private Thread listThread;
	
	private final static int SUCCESS=0;
	private final static int FAIL=1;
	
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
		
		fetchDiaryTemplates();
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();

		ab.setDisplayShowTitleEnabled(false);
		
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
	
	/**
	 * 向服务器请求最新模板
	 */
	private void fetchDiaryTemplates(){
		tempListHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:
					JSONObject result = (JSONObject)msg.obj;
					try {
						JSONArray array=result.getJSONArray(Constant.SERVER_TEMPLATE_LIST);
						SimpleDateFormat dateFormat=new SimpleDateFormat();
						for(int i=0;i<array.length();i++){
							JSONObject obj=array.getJSONObject(i);
							DiaryApplication.getInstance().getDbHelper().insertDiaryTemplate(
									dateFormat.parse(obj.getString(Constant.SERVER_USER_CREATED_AT)),
									dateFormat.parse(obj.getString(Constant.SERVER_USER_UPDATED_AT)),
									obj.getString(Constant.SERVER_TEMPLATE_LIST_NAME), 
									"1",
									obj.getString(Constant.SERVER_TEMPLATE_LIST_FORMAT), 
									obj.getString(Constant.SERVER_TEMPLATE_LIST_SELECTED));

						}
						
						tempAdapter.notifyDataSetChanged();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case FAIL:
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		final String cDate=DiaryApplication.getInstance().getDbHelper().getLatestTemplate(
				DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID));
		final String session_id=DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_SESSION_ID);
		if(listThread==null){
			listThread=new Thread() {
				public void run() {
					JSONObject result = API.getUserTemplates(session_id, cDate);
					if(result!=null&&result.has(Constant.SERVER_SUCCESS)){
						Message msg=new Message();
						msg.what=SUCCESS;
						msg.obj=result;
						tempListHandler.sendMessage(msg);
					}else{
						tempListHandler.sendEmptyMessage(FAIL);
					}
					listThread=null;
				};
			};
			listThread.start();
		}
	}
	
	private void updateDiaryTemplate(){
		
	}
	
}
