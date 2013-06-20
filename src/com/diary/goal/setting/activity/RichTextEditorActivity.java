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

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.database.DiaryHelper;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.richedit.RichEditText;

public class RichTextEditorActivity extends SherlockActivity {
  RichEditText editor=null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.edit_panal);
    
    editor=(RichEditText)findViewById(R.id.editor);
    editor.enableActionModes(true);
    DateModel model=DiaryApplication.getInstance().getDateModel();
    editor.setText(model.getText()==null?"":model.getText());
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      //This uses the imported MenuItem from ActionBarSherlock
	  switch(item.getItemId()){
	  case 1:
		  DateModel model=DiaryApplication.getInstance().getDateModel();
		  DiaryHelper helper=DiaryApplication.getInstance().getDbHelper();
		  Cursor c=DiaryApplication.getInstance().getDbHelper().getCategory(model);
		  if(c!=null&&c.getCount()!=0)
			  helper.updateDiaryContent(model, editor.getEditableText().toString());
		  else
			  helper.insertDiaryContent(model, editor.getEditableText().toString());
		  if(c!=null)
			  c.close();
		  break;
	  default:
		  break;
	  }
      return true;
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, 1, 1, "Save")//add("Save")
          .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

      return true;
  }
  

}
