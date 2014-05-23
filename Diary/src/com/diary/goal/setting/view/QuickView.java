package com.diary.goal.setting.view;

import java.util.ArrayList;
import java.util.HashMap;



import com.actionbarsherlock.app.SherlockActivity;
import com.diary.goal.setting.DiaryApplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class QuickView extends TextView {

	
	Context context;
	HashMap<Integer,ArrayList<String>> content;
	ArrayList<String> lines;
	FontMetrics fontMetrics;
	
    private Paint linePaint,wordPaint;
    private Rect mRect; 
    private float mult = 1.5f; 
    private float add = 2.0f; 
	
	public QuickView(Context context) {
		this(context,null);
	}
	public QuickView(Context context, AttributeSet attrs) {
		this(context, attrs,android.R.attr.textViewStyle);
	}
	public QuickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		init();
	}
	
	
	void init(){
		
//		content=new HashMap<Integer, ArrayList<String>>();
//		lines=new ArrayList<String>();
//        fontMetrics = this.getPaint().getFontMetrics(); 
//        
//		Cursor c=DiaryApplication.getInstance().getDbHelper().getDataInSingleDay(DiaryApplication.getInstance().getDateModel().getDate());
//		if(c!=null){
//			while (c.moveToNext()) {
//				Integer key=Constant.stringDict.get(SudoType.getTypeString(SudoType.getSudoType(c.getInt(2))));
//				if(content.containsKey(key))
//					content.get(key).add(c.getString(1));
//				else{
//					ArrayList<String> ss=new ArrayList<String>();
//					ss.add(c.getString(1));
//					content.put(key, ss);
//				}
//			}
//		}
//		if(c!=null)
//			c.close();
//		lines.add(getDiaryFirstLine());
//        for (Entry<Integer, ArrayList<String>> entry:content.entrySet()) {
//        	lines.add(this.getResources().getString(entry.getKey()));
//            for (String category : entry.getValue()) {
//            	lines.add("  "+category);
//            }
//        }
////        setText(builder.toString());
////        this.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        
//        mRect = new Rect();
//        linePaint = new Paint();
////        linePaint.setStyle(Paint.Style.STROKE);
////        linePaint.setStrokeWidth(1);
//        linePaint.setColor(Color.GRAY);
//        //linePaint.setAntiAlias(true);
//        this.setLineSpacing(add, mult); 
//        
//		wordPaint=new Paint();
//		wordPaint.setColor(0xFF000000);
//		wordPaint.setStyle(Style.STROKE);
//		wordPaint.setAntiAlias(true); //
//		wordPaint.setFlags(Paint.ANTI_ALIAS_FLAG); //
//        wordPaint.setTextSize(-fontMetrics.ascent);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//MeasureSpec.getMode(heightMeasureSpec);
//		int height=MeasureSpec.getSize(heightMeasureSpec);
//		int barHeight=((SherlockActivity)this.getContext()).getSupportActionBar().getHeight();
//		//((SherlockActivity)this.getContext())
//		if(height<DiaryApplication.getInstance().getScreen_h()-barHeight){
//			int measuredHeight=MeasureSpec.makeMeasureSpec(DiaryApplication.getInstance().getScreen_h()-barHeight, MeasureSpec.EXACTLY);
//			super.onMeasure(widthMeasureSpec, measuredHeight);
//			return;
//		}
		
//		if(lines.size()*this.getLineHeight()<DiaryApplication.getInstance().getScreen_h()){
//			int measuredHeight=MeasureSpec.makeMeasureSpec(DiaryApplication.getInstance().getScreen_h(), MeasureSpec.EXACTLY);
//			super.onMeasure(widthMeasureSpec, measuredHeight);
//			return;
//		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
//	@Override
//	protected void onDraw(Canvas canvas) {
//		int width=DiaryApplication.getInstance().getScreen_w();
//        int count = lines.size();
//        
//        for (int i = 0; i < count; i++) {
//            getLineBounds(i, mRect); 
//            int baseline = (i + 1) * getLineHeight(); 
//            canvas.drawLine(mRect.left, baseline, mRect.right, baseline, linePaint);
//            String text=lines.get(i);
//            canvas.drawText(text, (width-wordPaint.measureText(text))/2, baseline-fontMetrics.descent, wordPaint);
//        } 
//        while((count++)*getLineHeight()<this.getHeight()){
//        	int baseline = count * getLineHeight(); 
//            canvas.drawLine(mRect.left, baseline, mRect.right, baseline, linePaint);
//        }
//        super.onDraw(canvas); 
//	}
//	
//	private String getDiaryFirstLine(){
//		DateModel model=DiaryApplication.getInstance().getDateModel();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(model.getDate());
//		int month=calendar.get(Calendar.MONTH)+1;
//		int day=calendar.get(Calendar.DAY_OF_MONTH);
//		int day_of_week=calendar.get(Calendar.DAY_OF_WEEK);
//		return Function.getAbbrMonth(context,month)+day+context.getResources().getString(R.string.day)+"     "
//				+context.getResources().getString(Constant.getWeekDay(day_of_week));
//	}

}
