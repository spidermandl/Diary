package com.kemallette.RichEditTextDemo.Activity;
//
//import java.util.regex.Pattern;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.actionbarsherlock.app.SherlockActivity;
//import com.kemallette.RichEditText.Validations.CreditCardValidator;
//import com.kemallette.RichEditText.Validations.EmailValidator;
//import com.kemallette.RichEditText.Validations.OrValidator;
//import com.kemallette.RichEditText.Widget.RichEditText;
//import com.kemallette.RichEditTextDemo.R;
//
//public class DiaryEditActivity extends SherlockActivity{
//
//	private FrameLayout	flContainer;
//	private TextView	tvExplanation;
//	private TextView	tvTitle;
//
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState){
//
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_examplegeneric);
//
//		flContainer = (FrameLayout) findViewById(R.id.fl);
//		tvExplanation = (TextView) findViewById(R.id.tv_explanation);
//		tvTitle = (TextView) findViewById(R.id.tv_title);
//
//		flContainer.addView(LayoutInflater.from(this)
//											.inflate(	R.layout.diary_edit,
//														flContainer,
//														false));
//		tvExplanation.setText(R.string.explanation_emailorcredit);
//		tvTitle.setText(R.string.emailorcredit_title);
//
//		// Interesting stuff starts here
//
//		DiaryEditText fdt = (DiaryEditText) findViewById(R.id.et);
//		Pattern pattern = MsgValidator.getDefaultPattern();
//		
//		fdt.addValidator(
//				new MsgValidator("syntax error", pattern)
//			);
//	}
//
//	public void onClickValidate(View v){
//
//		RichEditText fdt = (RichEditText) findViewById(R.id.et);
//		if (fdt.isValid())
//			Toast.makeText(	this,
//							":)",
//							Toast.LENGTH_LONG)
//					.show();
//	}
//}


import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.kemallette.RichEditTextDemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.diary.goal.setting.richtext.RichTextEditView;

public class DiaryEditActivity extends SherlockActivity {

    private static final String TAMPLATE="{\"健康\":[\"跑步\",\"身体\",\"饮食\",\"感悟\"]," +
    		"\"修养\":[\"学习\",\"读书\",\"外语\",\"感悟\"]," +
    		"\"心灵\":[\"感恩\",\"成功\",\"感悟\"]," +
    		"\"工作\":[\"效率\",\"进步\"]," +
    		"\"人脉\":[\"朋友\",\"家人\",\"恋人\"]," +
    		"\"财富\":[\"记账\",\"理财\",\"感悟\"]," +
    		"\"创意\":[\"想法\",\"行动\"]," +
    		"\"MIT\":[\"第一件事\",\"第二件事\",\"第三件事\"]}";
    
    private com.diary.goal.setting.richtext.RichTextEditView[] editViews=new com.diary.goal.setting.richtext.RichTextEditView[8];
    private TextView[] mainTitles=new TextView[8];
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		setContentView(R.layout.diary_edit);

		initViews();
		super.onCreate(savedInstanceState);
	}
	
	protected void initViews(){
		
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
			Iterator<String> it = tamplate.keys();  
			int main_index=0;
            while(it.hasNext()){//遍历JSONObject  
            	String mainTitle=it.next();
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
                main_index++;
            } 
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

