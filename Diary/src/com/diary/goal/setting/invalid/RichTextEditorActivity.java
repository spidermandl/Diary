///***
//  Copyright (c) 2012 CommonsWare, LLC
//  
//  Licensed under the Apache License, Version 2.0 (the "License"); you may
//  not use this file except in compliance with the License. You may obtain
//  a copy of the License at
//    http://www.apache.org/licenses/LICENSE-2.0
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
// */
//
//package com.diary.goal.setting.invalid;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.WindowManager;
//import android.widget.ArrayAdapter;
//
//import com.actionbarsherlock.app.ActionBar;
//import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
//import com.actionbarsherlock.app.SherlockActivity;
//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuItem;
//import com.actionbarsherlock.view.Window;
//import com.diary.goal.setting.DiaryApplication;
//import com.diary.goal.setting.R;
//import com.diary.goal.setting.adapter.UnitOverviewAdapter;
//import com.diary.goal.setting.database.DiaryHelper;
//import com.diary.goal.setting.database.DiaryHelper.Tables;
//import com.diary.goal.setting.model.CategoryModel;
//import com.diary.goal.setting.model.DateModel;
//import com.diary.goal.setting.richedit.RichEditText;
//import com.diary.goal.setting.tools.BitmapCustomize;
//import com.diary.goal.setting.tools.Constant;
//import com.diary.goal.setting.tools.Constant.SudoType;
//
//public class RichTextEditorActivity extends SherlockActivity implements OnNavigationListener{
//	RichEditText editor = null;
//	/**
//	 * siwtching name of each category
//	 */
//	ArrayList<CharSequence> titleSwitch = new ArrayList<CharSequence>();
//	/**
//	 * premiary key of configTable
//	 */
//	ArrayList<Integer> indexs=new ArrayList<Integer>();
//	HashMap<Integer, CategoryModel> configTables;
//	/**
//	 * loadin item of navigation
//	 */
//	int initialPosition=0;
//	/**
//	 * initial text
//	 */
//	String initText;
//	boolean isChildText=true;
//	JSONObject childText;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//        
//		setContentView(R.layout.edit_panal);
//		
//		final ActionBar ab = getSupportActionBar();
//		
//		// set defaults for logo & home up
//		ab.setDisplayHomeAsUpEnabled(true);
//		ab.setDisplayUseLogoEnabled(false);
//		ab.setDisplayShowHomeEnabled(false);
//		ab.setTitle(R.string.edit_back);
//
//		DateModel model = DiaryApplication.getInstance().getDateModel();
//		if(model.getCategorySubIndex()==-1){
//			isChildText=false;
//			dataInit();
//			// ab.setDisplayOptions(options, mask)
//			ArrayAdapter<CharSequence> list = new ArrayAdapter(this, R.layout.sherlock_spinner_item, titleSwitch);
//			list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
//	        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//	        ab.setListNavigationCallbacks(list, this);
//	        ab.setSelectedNavigationItem(initialPosition);
//		}
//		editor = (RichEditText) findViewById(R.id.editor);
//		editor.enableActionModes(true);
//
//		initText=model.getText() == null ? "" : model.getText();
//		if(isChildText){
//			try {
//				childText=new JSONObject(initText);
//				initText=childText.getString(String.valueOf(model.getCategorySubIndex()));
//			} catch (JSONException e) {
//				childText=new JSONObject();
//			}
//		}
//		editor.setText(initText);
//	}
//
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// This uses the imported MenuItem from ActionBarSherlock
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			this.finish();
//			this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
//			break;
//		case 1:
//			saveEdit();
//			this.finish();
//			break;
//		default:
//			break;
//		}
//		return true;
//	}
//
//	/**
//	 * save text content
//	 */
//	private void saveEdit(){
//		if(!editor.getEditableText().toString().equals(initText)){
//			DateModel model = DiaryApplication.getInstance().getDateModel();
//			DiaryHelper helper = DiaryApplication.getInstance().getDbHelper();
//			Cursor c = helper.getCategory(model);
//
//			if (c != null && c.getCount() != 0){
//				if(!isChildText)
//					helper.updateDiaryContent(model, editor.getEditableText().toString());
//				else{
//					try {
//						childText.put(String.valueOf(model.getCategorySubIndex()),editor.getEditableText().toString());
//						helper.updateDiaryContent(model, childText.toString());
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			else{
//				if(!isChildText)
//					helper.insertDiaryContent(model, editor.getEditableText().toString());
//				else{
//					try {
//						childText.put(String.valueOf(model.getCategorySubIndex()),editor.getEditableText().toString());
//						helper.insertDiaryContent(model, childText.toString());
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			
//			if (c != null)
//				c.close();
//		}
//	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, 1, 1, R.string.edit_save)// add("Save")
//		    .setIcon(R.drawable.save)//new BitmapDrawable(BitmapCustomize.customizePicture(this, R.drawable.save,0,0,false)))
//		    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//		return true;
//	}
//
//	//@Override
//	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//		saveEdit();
//
//		DateModel model = DiaryApplication.getInstance().getDateModel();
//		CategoryModel category=configTables.get(indexs.get(itemPosition));
//		/*******************************switch date model*************************************/
//		model.setConfigId(indexs.get(itemPosition));
//		model.setCategory(category.getCategoryIndex());
//		model.setCategory_type(category.getCategoryType());
//		model.setCategory_name(category.getCategoryName());
//		/********************************************************************/
//		Cursor c=DiaryApplication.getInstance().getDbHelper().getCategory(model.getDate(),model.getType().getType(),model.getCategory());
//		if(c!=null&&c.getCount()!=0){
//			c.moveToFirst();
//			model.setText(c.getString(1));
//		}
//		else
//			model.setText("");
//		if(c!=null)
//			c.close();
//		initialPosition=itemPosition;
//		initText=model.getText() == null ? "" : model.getText();
//		editor.setText(initText);
//		Log.e("onNavigationItemSelected", model.getCategory()+" "+model.getCategory_name()+" "+model.getText());
//		return true;
//	}
//	
//	void dataInit(){
//		configTables=(HashMap<Integer,CategoryModel>)DiaryApplication.getInstance().getTableCacheElement(Tables.DIARY_CONFIG, 0);
//		DateModel model=DiaryApplication.getInstance().getDateModel();
//		int index=0;
//		for(Iterator<Integer> keys = configTables.keySet().iterator(); keys.hasNext();){
//			int _id=keys.next();
//			if(SudoType.getSudoType(configTables.get(_id).getSudoType())==model.getType()&&configTables.get(_id).getCategoryType()==UnitOverviewAdapter.TYPE_CONVENTIONAL_EDIT){
//				indexs.add(_id);
//				String name=configTables.get(_id).getCategoryName();
//				titleSwitch.add(switchLanguage(name));
//				if(model.getCategory_name().equals(name)){
//					initialPosition=index;
//				}
//				index++;
//			}
//		}
//		
//	}
//	
//	private String switchLanguage(String key){
//		Integer value=Constant.stringDict.get(key);
//		return value==null?key:this.getResources().getString(value);
//	}
//
//}
