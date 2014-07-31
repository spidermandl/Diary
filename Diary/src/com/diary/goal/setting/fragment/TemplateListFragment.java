package com.diary.goal.setting.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.lee.pullrefresh.PullRefreshListViewActivity;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;

/**
 * 
 * 模板列表
 */
public class TemplateListFragment extends SherlockFragment{
	
	private PullToRefreshListView mPullListView;
	private ListView tempList;
	private OnItemClickListener itemListener;
	private TemplateListAdapter tempAdapter;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private boolean mIsStart = true;
    private int mCurIndex = 0;
    private static final int mLoadDataCount = 100;
    
	private Handler tempListHandler,
	              updateHandler;
	private Thread listThread,//获取模板列表线程
	               updateThread;//更新模板线程
	
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
		ViewGroup container=(ViewGroup)layout.findViewById(R.id.temp_list_container);
		tempList=(ListView)layout.findViewById(R.id.template_list);
		container.removeView(tempList);
		
		mPullListView = new PullToRefreshListView(this.getActivity()){
			@Override
			protected ListView createRefreshableView(Context context,
					AttributeSet attrs) {
		        setListView(tempList);
				return super.createRefreshableView(context, attrs);
			}
		};
		mPullListView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mPullListView.setPullLoadEnabled(false);
        mPullListView.setScrollLoadEnabled(true);

		container.addView(mPullListView);
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
        
        mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mIsStart = true;
        		/**
        		 * 获取模板列表
        		 */
        		fetchDiaryTemplates();
                //new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mIsStart = false;
                new GetDataTask().execute();
            }
        });
        setLastUpdateTime();
        
        mPullListView.doPullRefreshing(true, 500);
        
		
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
	
    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            boolean hasMoreData = true;
            if (mIsStart) {
//                mListItems.addFirst("Added after refresh...");
            } else {
                int start = mCurIndex;
                int end = mCurIndex + mLoadDataCount;
//                if (end >= mStrings.length) {
//                    end = mStrings.length;
//                    hasMoreData = false;
//                }
//                
//                for (int i = start; i < end; ++i) {
//                    mListItems.add(mStrings[i]);
//                }
                
                mCurIndex = end;
            }
            
            tempAdapter.notifyDataSetChanged();
            mPullListView.onPullDownRefreshComplete();
            mPullListView.onPullUpRefreshComplete();
            mPullListView.setHasMoreData(hasMoreData);
            setLastUpdateTime();

            super.onPostExecute(result);
        }
    }
    
    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        mPullListView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        
        return mDateFormat.format(new Date(time));
    }
    /**
     * 网络请求完毕
     */
    private void onUIFetchFinished(){
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
        mPullListView.setHasMoreData(true);
        setLastUpdateTime();
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
									DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID),
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
				onUIFetchFinished();
				/**
				 * 同步更新模板
				 */
				updateDiaryTemplate();
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
	/**
	 * 更新模板
	 */
	private void updateDiaryTemplate(){
		updateHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:
					
					break;
				case FAIL:
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		String user_id=DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID);
		DiaryTemplateModel[] adds=DiaryApplication.getInstance().getDbHelper().getDiaryTemplatesBySync(user_id, "0");
		DiaryTemplateModel[] updates=DiaryApplication.getInstance().getDbHelper().getDiaryTemplatesBySync(user_id, "2");
		DiaryTemplateModel[] dels=DiaryApplication.getInstance().getDbHelper().getDiaryTemplatesBySync(user_id, "-2");
		ArrayList<Long> addIds=((TemplateOperateActivity)this.getActivity()).getAddIDs();
		addIds.clear();
		/**
		 * 保存新增模板id
		 */
		for(DiaryTemplateModel m:adds){
			addIds.add(Long.parseLong(m._ID));
		}
		final String session_id= DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_SESSION_ID);
		final String addJson=transferModelToJsonArray(adds).toString();
		final String updateJson=transferModelToJsonArray(updates).toString();
		final String delJson=transferModelToJsonArray(dels).toString();
		if(updateThread==null){
			updateThread=new Thread() {
				public void run() {
					JSONObject result = API.pushUserTemplates(session_id,addJson,updateJson,delJson);
					if(result!=null&&result.has(Constant.SERVER_SUCCESS)){
						Message msg=new Message();
						msg.what=SUCCESS;
						msg.obj=result;
						updateHandler.sendMessage(msg);
					}else{
						updateHandler.sendEmptyMessage(FAIL);
					}
					updateThread=null;
				};
			};
			updateThread.start();
		}
	}
	/**
	 * 模板model转成JsonArray
	 * @param models
	 * @return
	 */
	private JSONArray transferModelToJsonArray(DiaryTemplateModel[] models){
		JSONArray sends=new JSONArray();
		String user_id=DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID);
		for(DiaryTemplateModel m:models){
			JSONObject obj=new JSONObject();
			try {
				obj.put(Constant.SERVER_TEMPLATE_LIST_ID, m._ID);
				obj.put(Constant.SERVER_USER_ID, user_id);
				obj.put(Constant.SERVER_TEMPLATE_LIST_NAME, m._NAME);
				obj.put(Constant.SERVER_TEMPLATE_LIST_FORMAT, m._TAMPLETE);
				obj.put(Constant.SERVER_TEMPLATE_LIST_SELECTED, m._SELECTED);
				obj.put(Constant.SERVER_USER_CREATED_AT, m._CREATE_TIME);
				obj.put(Constant.SERVER_USER_UPDATED_AT, m._UPDATE_TIME);
				sends.put(obj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sends;
	}
	
}
