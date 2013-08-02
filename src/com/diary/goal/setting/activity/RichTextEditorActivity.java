/***
  Copyright (c) 2012 CommonsWare, LLC
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.diary.goal.setting.activity;

import java.util.ArrayList;

import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.database.DiaryHelper;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.richedit.RichEditText;
import com.diary.goal.setting.tools.BitmapCustomize;
import com.diary.goal.setting.tools.Constant;

public class RichTextEditorActivity extends SherlockActivity implements OnNavigationListener{
	RichEditText editor = null;
	ArrayList<CharSequence> titleSwitch = new ArrayList<CharSequence>();
	int initialPosition=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_panal);
		dataInit();
		
		final ActionBar ab = getSupportActionBar();

		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(R.string.edit_back);
		
		// ab.setDisplayOptions(options, mask)
		ArrayAdapter<CharSequence> list = new ArrayAdapter(this, R.layout.sherlock_spinner_item, titleSwitch);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setSelectedNavigationItem(initialPosition);
        getSupportActionBar().setListNavigationCallbacks(list, this);
        
		editor = (RichEditText) findViewById(R.id.editor);
		editor.enableActionModes(true);

		DateModel model = DiaryApplication.getInstance().getDateModel();
		editor.setText(model.getText() == null ? "" : model.getText());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		case 1:
			DateModel model = DiaryApplication.getInstance().getDateModel();
			DiaryHelper helper = DiaryApplication.getInstance().getDbHelper();
			Cursor c = helper.getCategory(model);
			if (c != null && c.getCount() != 0)
				helper.updateDiaryContent(model, editor.getEditableText()
						.toString());
			else
				helper.insertDiaryContent(model, editor.getEditableText()
						.toString());
			if (c != null)
				c.close();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.edit_save)// add("Save")
		    .setIcon(R.drawable.save)//new BitmapDrawable(BitmapCustomize.customizePicture(this, R.drawable.save,0,0,false)))
		    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	void dataInit(){
		Cursor c=DiaryApplication.getInstance().getDbHelper().getStaticCategoryDetail(DiaryApplication.getInstance().getDateModel());
		if(c!=null){
			while(c.moveToNext()){
				titleSwitch.add(switchLanguage(c.getString(1)));
			}
		}
		if(c!=null){
			c.close();
		}
	}
	
	private String switchLanguage(String key){
		DateModel model=DiaryApplication.getInstance().getDateModel();
		if(model.getCategory_name().equals(key)){
			//initialPosition
		}
		Integer value=Constant.stringDict.get(key);
		return value==null?key:this.getResources().getString(value);
	}

}
