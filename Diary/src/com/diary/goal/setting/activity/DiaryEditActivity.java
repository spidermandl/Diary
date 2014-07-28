package com.diary.goal.setting.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
import com.diary.goal.setting.tools.API;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.view.RatingPentagramView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private RatingPentagramView[] ratingViews = new RatingPentagramView[8];
    private JSONObject templete=null;//存储正在编辑的日记模板和内容
    private boolean isFisrtLoad=true;//是否为第一次进入当天日记编辑
    //private String[] diaryModel;//日记查询的结果
    private final static int _CONTENT=0;
    private final static int _CREATE_TIME=1;
    
	private Handler handler;
	private final static int CREATE_SUCCESS=0;
	private final static int UPDATE_SUCCESS=1;
	private final static int FAIL=2;
	
	HashMap<String, String> memCache;//缓存
    
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
		String[] diaryModel= DiaryApplication.getInstance().getDbHelper().getDiaryContent(memCache.get(Constant.SERVER_USER_ID),new Date());
		String content= diaryModel[_CONTENT];
		if(content!=null){//当天日记已经编辑过
			try {
				templete=new JSONObject(content);
				isFisrtLoad=false;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if(isFisrtLoad){
			String latestTemplete=DiaryApplication.getInstance().getDbHelper().getCurrentAppliedDiaryTemplate(memCache.get(Constant.SERVER_USER_ID));
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
					templete=new JSONObject(this.getResources().getText(Constant.TAMPLATE).toString());
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
	            	mainTitles[main_index].setTextColor(0xFF777777);
	            	mainTitles[main_index].setTextSize(20);
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
	            		ratingViews[k].setRate(Float.valueOf(texts.getString(Constant.MAIN_STATUS)));
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
		
		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CREATE_SUCCESS:
					DiaryApplication.getInstance().getDbHelper().updateDiaryContent(memCache.get(Constant.SERVER_USER_ID),new Date(), null, 1);
					break;
				
				case UPDATE_SUCCESS:
					DiaryApplication.getInstance().getDbHelper().updateDiaryContent(memCache.get(Constant.SERVER_USER_ID),new Date(), null, 1);
					break;
				case FAIL:
					
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
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
		
		ratingViews[0]= (RatingPentagramView)findViewById(R.id.star_rating_1);
		ratingViews[1]= (RatingPentagramView)findViewById(R.id.star_rating_2);
		ratingViews[2]= (RatingPentagramView)findViewById(R.id.star_rating_3);
		ratingViews[3]= (RatingPentagramView)findViewById(R.id.star_rating_4);
		ratingViews[4]= (RatingPentagramView)findViewById(R.id.star_rating_5);
		ratingViews[5]= (RatingPentagramView)findViewById(R.id.star_rating_6);
		ratingViews[6]= (RatingPentagramView)findViewById(R.id.star_rating_7);
		ratingViews[7]= (RatingPentagramView)findViewById(R.id.star_rating_8);
		
		memCache=DiaryApplication.getInstance().getMemCache();
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
					subPart.put(Constant.MAIN_STATUS, ratingViews[i].getRate());
					restructDiary.put(titles.getString(i), subPart);
				}
				Log.e("save diary", restructDiary.toString());
				if(isFisrtLoad){
					Date date=new Date();
					DiaryApplication.getInstance().getDbHelper().insertDiaryContent(memCache.get(Constant.SERVER_USER_ID),date,date, restructDiary.toString(),0);
				}
				else
					DiaryApplication.getInstance().getDbHelper().updateDiaryContent(memCache.get(Constant.SERVER_USER_ID),new Date(), restructDiary.toString(),0);
				
				DiaryApplication.getInstance().updateStatusPanel();//更新九宫格状态
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			commitDiary(isFisrtLoad, new Date(), restructDiary.toString());//提交日记至服务器
			this.finish();
		}
		
	}

	/**
	 * 向服务器提交
	 * @param created
	 * @param date
	 * @param content
	 */
	private void commitDiary(final boolean created,final Date date,final String content){
		final String session_id=memCache.get(Constant.SERVER_SESSION_ID);
		final String user_id=memCache.get(Constant.SERVER_USER_ID);
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String[] diaryModel= DiaryApplication.getInstance().getDbHelper().getDiaryContent(user_id,new Date());
		new Thread(){
			public void run() {
				if(session_id!=null){
					JSONObject result=created?
							 API.createDiary(session_id, diaryModel[_CREATE_TIME], content)
							:API.updateDiary(session_id, diaryModel[_CREATE_TIME], format.format(date), content);
					if(result!=null&&result.has(Constant.SERVER_SESSION_ID)){
						Message msg=new Message();
						msg.what=isFisrtLoad?CREATE_SUCCESS:UPDATE_SUCCESS;
						msg.obj=date;
						handler.sendMessage(msg);
					}else{
						handler.sendEmptyMessage(FAIL);
					}
				}
			};
		}.start();
		
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
