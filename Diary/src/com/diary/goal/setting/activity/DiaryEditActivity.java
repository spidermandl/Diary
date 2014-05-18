package com.diary.goal.setting.activity;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.richtext.DiaryValidator;
import com.diary.goal.setting.richtext.RichTextEditView;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DiaryEditActivity extends SherlockActivity {

	public static final String SEQUENCE_NAME="title_order";
    private static final String TAMPLATE="{" +
    		"\""+SEQUENCE_NAME+"\":[\"健康\",\"修养\",\"心灵\",\"工作\",\"人脉\",\"财富\",\"创意\",\"MIT\"]," +
    		"\"健康\":[\"跑步\",\"身体\",\"饮食\",\"感悟\"]," +
    		"\"修养\":[\"学习\",\"读书\",\"外语\",\"感悟\"]," +
    		"\"心灵\":[\"感恩\",\"成功\",\"感悟\"]," +
    		"\"工作\":[\"效率\",\"进步\"]," +
    		"\"人脉\":[\"朋友\",\"家人\",\"恋人\"]," +
    		"\"财富\":[\"记账\",\"理财\",\"感悟\"]," +
    		"\"创意\":[\"想法\",\"行动\"]," +
    		"\"MIT\":[\"第一件事\",\"第二件事\",\"第三件事\"]}";
    
    private RichTextEditView[] editViews=new RichTextEditView[8];
    private TextView[] mainTitles=new TextView[8];
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		setContentView(R.layout.diary_edit);

		initViews();
		initFunctionality();
		super.onCreate(savedInstanceState);
	}
	
	private void initFunctionality() {
		int type=DiaryApplication.getInstance().getDateModel().getType().getType();
		editViews[type>5?type-2:type-1].requestFocus();
		for (int i=0;i<8;i++){
			editViews[i].addValidator(
					new DiaryValidator("sytax error", DiaryValidator.getSubTitlePattern()));
		}
		
	}

	protected void initViews(){
		final ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
//		ab.setDisplayHomeAsUpEnabled(true);
//		ab.setDisplayUseLogoEnabled(false);
//		ab.setDisplayShowHomeEnabled(false);
//		ab.setTitle(R.string.edit_back);
		
		
		editViews[0]=(RichTextEditView)findViewById(R.id.diary_content_1);
		editViews[1]=(RichTextEditView)findViewById(R.id.diary_content_2);
		editViews[2]=(RichTextEditView)findViewById(R.id.diary_content_3);
		editViews[3]=(RichTextEditView)findViewById(R.id.diary_content_4);
		editViews[4]=(RichTextEditView)findViewById(R.id.diary_content_5);
		editViews[5]=(RichTextEditView)findViewById(R.id.diary_content_6);
		editViews[6]=(RichTextEditView)findViewById(R.id.diary_content_7);
		editViews[7]=(RichTextEditView)findViewById(R.id.diary_content_8);
		
		mainTitles[0]=(TextView)findViewById(R.id.main_title_1);
		mainTitles[1]=(TextView)findViewById(R.id.main_title_2);
		mainTitles[2]=(TextView)findViewById(R.id.main_title_3);
		mainTitles[3]=(TextView)findViewById(R.id.main_title_4);
		mainTitles[4]=(TextView)findViewById(R.id.main_title_5);
		mainTitles[5]=(TextView)findViewById(R.id.main_title_6);
		mainTitles[6]=(TextView)findViewById(R.id.main_title_7);
		mainTitles[7]=(TextView)findViewById(R.id.main_title_8);
		
		try {
			JSONObject tamplate=new JSONObject(TAMPLATE);
			JSONArray titles=tamplate.getJSONArray(SEQUENCE_NAME);
			int main_index=0;
            for(int k=0;k<titles.length();k++){//遍历title字段
            	String mainTitle=titles.getString(k);
            	JSONArray array=tamplate.getJSONArray(mainTitle);
            	mainTitles[main_index].setText("{"+mainTitle+"}");
            	int length = array.length();  
            	StringBuffer subtitles=new StringBuffer();
                for(int i = 0; i < length; i++){//遍历JSONArray
                	subtitles.append('[');
                	subtitles.append(array.getString(i));
                	subtitles.append(']');
                	subtitles.append("\r\n");  
                }  
                editViews[main_index].setText(subtitles.toString());
                //editViews[main_index].enableActionModes(true);
                main_index++;
            } 
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 检测日记语法，并且保存正确日记
	 */
	private void saveDiary(){
		boolean hasError=false;
		for (int i=0;i<8;i++){
			if(!editViews[i].isValid()){
				Integer[] errors=DiaryApplication.getInstance().getSyntaxError();
				editViews[i].updateErrorSpans(errors);
				hasError=true;
				break;
			}
		}
		if(!hasError){
			
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, R.string.edit_save, 1, R.string.edit_save)// add("Save")
		    .setIcon(R.drawable.save)//new BitmapDrawable(BitmapCustomize.customizePicture(this, R.drawable.save,0,0,false)))
		    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			//this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
			break;
		case R.string.edit_save:
			this.saveDiary();
			break;
		case 1:
			this.finish();
			break;
			
		default:
			break;
		}
		return true;
	}
}
