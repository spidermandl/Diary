package com.diary.goal.setting.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.diary.goal.setting.tools.Constant;
import com.flurry.org.apache.avro.data.Json;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
/**
 * 编写日记activity
 * @author desmond.duan
 *
 */
public class DiaryEditActivity extends SherlockActivity {
     
    private RichTextEditView[] editViews=new RichTextEditView[8];
    private TextView[] mainTitles=new TextView[8];
    private JSONObject templete=null;//存储正在编辑的日记模板和内容
    private boolean isFisrtLoad=true;//是否为第一次进入当天日记编辑
	@Override
	protected void onCreate(Bundle savedInstanceState){
		setContentView(R.layout.diary_edit);

		initViews();
		initFunctionality();
		super.onCreate(savedInstanceState);
	}
	
	private void initFunctionality() {
		/**
		 * 载入模板 和 日记
		 */
		String content=DiaryApplication.getInstance().getDbHelper().getDiaryContent(new Date());
		if(content!=null){//当天日记已经编辑过
			try {
				templete=new JSONObject(content);
				isFisrtLoad=false;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if(isFisrtLoad){
			String latestTemplete=DiaryApplication.getInstance().getDbHelper().getDiaryTemplete(null);
			if(latestTemplete!=null){//数据库中存在模板
				try {
					templete=new JSONObject(latestTemplete);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(templete==null){//使用默认模板
				try {
					templete=new JSONObject(Constant.TAMPLATE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(templete!=null){
			try {
				JSONArray titles = templete.getJSONArray(Constant.MAIN_SEQUENCE_ORDER);
				int main_index=0;
	            for(int k=0;k<titles.length();k++){//遍历title字段
	            	String mainTitle=titles.getString(k);
	            	StringBuffer subtitles=new StringBuffer();
	            	mainTitles[main_index].setText("{"+mainTitle+"}");
	            	if(isFisrtLoad){//没有正文的text
		            	JSONArray array=templete.getJSONArray(mainTitle);
		            	int length = array.length();  
		                for(int i = 0; i < length; i++){//遍历JSONArray
		                	subtitles.append('[');
		                	subtitles.append(array.getString(i));
		                	subtitles.append(']');
		                	subtitles.append("\n\n");  
		                }  
	            	}else{
	            		JSONObject texts=templete.getJSONObject(mainTitle);
	            		JSONArray array=texts.getJSONArray(Constant.SUB_SEQUENCE_ORDER);
		            	int length = array.length();  
		                for(int i = 0; i < length; i++){//遍历JSONArray
		                	subtitles.append('[');
		                	subtitles.append(array.getString(i));
		                	subtitles.append(']'); 
		                	subtitles.append(texts.getString(array.getString(i)));
		                }  
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
		Log.e("init diary", templete.toString());
		/**
		 * 获得焦点焦点控件
		 */
		int type=getIntent().getIntExtra("sudoType", 2);
		editViews[type>5?type-2:type-1].requestFocus();
		for (int i=0;i<8;i++){
			editViews[i].addValidator(
					new DiaryValidator(null, DiaryValidator.getSubTitlePattern()));
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
		
	}
	/**
	 * 检测日记语法，并且保存正确日记
	 */
	private void saveDiary(){
		boolean hasError=false;
		for (int i=0;i<8;i++){//错误检测
			if(!editViews[i].isValid()){
				Integer[] errors=DiaryApplication.getInstance().getSyntaxError();
				editViews[i].updateErrorSpans(errors);
				hasError=true;
				break;
			}
		}
		if(!hasError){//日记保存
			JSONObject restructDiary=new JSONObject();
			try {
				JSONArray titles = templete.getJSONArray(Constant.MAIN_SEQUENCE_ORDER);//大标题
				restructDiary.put(Constant.MAIN_SEQUENCE_ORDER, titles);//放入大标题数据
				for (int i=0;i<8;i++){//赋入小标题内容
					JSONObject subPart=editViews[i].getTextWithJsonFormat();
					subPart.put(Constant.MAIN_STATUS, 0);
					restructDiary.put(titles.getString(i), subPart);
				}
				Log.e("save diary", restructDiary.toString());
				if(isFisrtLoad)
					DiaryApplication.getInstance().getDbHelper().insertDiaryContent(new Date(), restructDiary.toString());
				else
					DiaryApplication.getInstance().getDbHelper().updateDiaryContent(new Date(), restructDiary.toString());
				
				DiaryApplication.getInstance().updateStatusPanel();//更新九宫格状态
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.finish();
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
