package com.diary.goal.setting.activity;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.fragment.CommunityFragment;
import com.diary.goal.setting.fragment.MeFragment;
import com.diary.goal.setting.fragment.ActionFragment;
import com.diary.goal.setting.fragment.SoduKuFragment;
import com.diary.goal.setting.tools.API;
import com.diary.goal.setting.tools.Constant;

public class MainFrameActivity extends SherlockFragmentActivity {
	TabHost mTabHost;
    TabManager mTabManager;
    
    private Handler handler;
	private final static int SUCCESS=0;
	private final static int FAIL=1;

	/**
	 * request code of an activty
	 */
	public final static int REQUEST_UNITOVERVIEW=1;
	public final static int REQUEST_PAPEROVERVIEW=2;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_frame);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

        mTabManager.addTab(mTabHost.newTabSpec(this.getResources().getString(R.string.tab_host_main)).setIndicator(this.getResources().getString(R.string.tab_host_main)),
        		SoduKuFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec(this.getResources().getString(R.string.tab_host_fast_write)).setIndicator(this.getResources().getString(R.string.tab_host_fast_write)),
        		ActionFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec(this.getResources().getString(R.string.tab_host_view)).setIndicator(this.getResources().getString(R.string.tab_host_view)),
        		CommunityFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec(this.getResources().getString(R.string.tab_host_dream)).setIndicator(this.getResources().getString(R.string.tab_host_dream)),
        		MeFragment.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        
        syncDiary();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }
    

    /**
     * 同步机制，同步服务器上的日记
     */
    private void syncDiary(){

    	handler=new Handler(){
    		@Override
    		public void handleMessage(Message msg) {
    			switch (msg.what) {
				case SUCCESS:
					JSONObject obj = (JSONObject)msg.obj;
					try {
						JSONArray array=obj.getJSONArray(Constant.SERVER_DIARY_LIST);
						String uid=DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID);
						for(int i=0;i<array.length();i++){
							JSONObject diary=array.getJSONObject(i);
							
							String created_at=diary.getString(Constant.SERVER_CREATED_AT);
							String updated_at=diary.getString(Constant.SERVER_UPDATED_AT);
							String content=diary.getString(Constant.SERVER_CONTENT);
							DiaryApplication.getInstance().getDbHelper().insertDiaryContent(uid, created_at, updated_at,content, 1);//插入单条日记
						}
						DiaryApplication.getInstance().getDbHelper().updateSynRecord(new Date(), uid, 1);//账户同步完成
					} catch (JSONException e) {
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
    	HashMap<String, String> cache=DiaryApplication.getInstance().getMemCache();
		final String session_id=cache.get(Constant.SERVER_SESSION_ID);
		//String user_id=cache.get(Constant.SERVER_USER_ID);
		String user_id=DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID);
		if(session_id!=null){
			if(!DiaryApplication.getInstance().getDbHelper().getSynRecord(user_id)){
				//没有同步过
				new Thread(){
		    		public void run() {
	    				JSONObject result=API.fetchDiarys(session_id);			
						if(result!=null&&result.has(Constant.SERVER_SUCCESS)){
							Message msg=new Message();
							msg.obj=result;
							msg.what=SUCCESS;
							handler.sendMessage(msg);
						}
						else{
							handler.sendEmptyMessage(FAIL);
						}
		    		};
		    	}.start();
			}
		}
		
    }
    /**
     * This is a helper class that implements a generic mechanism for
     * associating fragments with the tabs in a tab host.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between fragments.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabManager supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct fragment shown in a separate content area
     * whenever the selected tab changes.
     */
    public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }

            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        public void onTabChanged(String tabId) {
            TabInfo newTab = mTabs.get(tabId);
            if (mLastTab != newTab) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                }
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(mActivity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(mContainerId, newTab.fragment, newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }
                }

                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
        }
    }
}
