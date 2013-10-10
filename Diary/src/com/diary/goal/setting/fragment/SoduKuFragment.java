package com.diary.goal.setting.fragment;

import java.util.Calendar;
import java.util.HashMap;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.NinePanelAdapter;
import com.diary.goal.setting.listener.FlipPathListener;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.model.PanelDateModel;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.view.ViewFlow;

/**
 * diary main UI panel
 * nine square sudo panel style
 * @author Desmond
 *
 */
public class SoduKuFragment extends SherlockFragment {
	
	private ViewFlow viewFlow;
	private int position;//record current page of sudo view
	
	NinePanelAdapter mAdapter;
	Handler UIhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			viewFlow.invalidate();
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		View layout = inflater.inflate(R.layout.sudo_fragment_layout, container, false);
//		viewFlow=(ViewFlow)layout.findViewById(R.id.sudo_panel);
//		mAdapter=new NinePanelAdapter(this.getActivity());
//		viewFlow.setAdapter(mAdapter);
//		viewFlow.setFlipListener(new FlipPathListener() {
//			
//			//@Override
//			public void track(int steps) {
//				position=DiaryApplication.getInstance().getDateCursor();
//				position+=steps;
//				DiaryApplication.getInstance().setDateCursor(position);
//				
//				UIhandler.post(new Runnable() {
//					
//					//@Override
//					public void run() {
//						getPanelCache();	
//						UIhandler.sendEmptyMessage(0);
//					}
//				});
//				
//			}
//		});
//		return viewFlow;
		View layout = inflater.inflate(R.layout.nine_panel_frame, container, false);
		return layout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();

		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("≥øº‰»’º«");
        /** 
         * Give some text to display if there is no data. In a real
         * application this would come from a resource.
         * We have a menu item to show in action bar.
         * onCreateOptionsMenu() will be called back
		 **/
        setHasOptionsMenu(true);

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        View searchView = SearchViewCompat.newSearchView(getActivity());
        if (searchView != null) {
            SearchViewCompat.setOnQueryTextListener(searchView,
                    new OnQueryTextListenerCompat() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    // Called when the action bar search text has changed.  Since this
                    // is a simple array adapter, we can just have it do the filtering.
                    return true;
                }
            });
            item.setActionView(searchView);
        }
	}
	
	private void init(){
		DiaryApplication.getInstance().setOrientation(
					this.getResources().getConfiguration().orientation);
		getPanelCache();
	}
	
	/**
	 * init date link
	 * get current day status
	 */
	private synchronized void getPanelCache(){
		HashMap<Integer, PanelDateModel> panelStatus=DiaryApplication.getInstance().getPanelCache();
		position=DiaryApplication.getInstance().getDateCursor();
		if(panelStatus.containsKey(position)){
			DiaryApplication.getInstance().setPadStatus((PanelDateModel)panelStatus.get(position));
			DateModel model = new DateModel();
			model.setDate(((PanelDateModel)panelStatus.get(position)).getDate());
			DiaryApplication.getInstance().setDateModel(model);
		}
        else {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DAY_OF_MONTH, position);
			DateModel model = new DateModel();
			model.setDate(date.getTime());
			DiaryApplication.getInstance().setDateModel(model);
			Cursor c = DiaryApplication.getInstance().getDbHelper().getTodayPad(model.getDate());
			HashMap<Constant.SudoType, Boolean> status = new HashMap<Constant.SudoType, Boolean>();
			status.put(Constant.SudoType.SUDO_0, false);
			status.put(Constant.SudoType.SUDO_1, false);
			status.put(Constant.SudoType.SUDO_2, false);
			status.put(Constant.SudoType.SUDO_3, false);
			status.put(Constant.SudoType.SUDO_4, false);
			status.put(Constant.SudoType.SUDO_5, false);
			status.put(Constant.SudoType.SUDO_6, false);
			status.put(Constant.SudoType.SUDO_7, false);
			status.put(Constant.SudoType.SUDO_8, false);
			status.put(Constant.SudoType.SUDO_9, false);
			if (c != null) {
				while (c.moveToNext()) {
					status.put(Constant.SudoType.getSudoType(c.getInt(0)), true);
				}
				c.close();
			}
			PanelDateModel panelModel=new PanelDateModel();
			panelModel.setDate(date.getTime());
			panelModel.setPadStatus(status);
			DiaryApplication.getInstance().setPadStatus(panelModel);
			panelStatus.put(position, panelModel);
		}
	}
}
