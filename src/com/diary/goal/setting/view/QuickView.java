package com.diary.goal.setting.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
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
		super(context);
		this.context=context;
		init();
	}
	public QuickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init();
	}
	public QuickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		init();
	}
	
	void init(){
		
		content=new HashMap<Integer, ArrayList<String>>();
		lines=new ArrayList<String>();
        fontMetrics = this.getPaint().getFontMetrics(); 
        
		Cursor c=DiaryApplication.getInstance().getDbHelper().getDataInSingleDay(DiaryApplication.getInstance().getDateModel().getDate());
		if(c!=null){
			while (c.moveToNext()) {
				Integer key=Constant.stringDict.get(SudoType.getTypeString(SudoType.getSudoType(c.getInt(2))));
				if(content.containsKey(key))
					content.get(key).add(c.getString(1));
				else{
					ArrayList<String> ss=new ArrayList<String>();
					ss.add(c.getString(1));
					content.put(key, ss);
				}
				
			}
		}
		if(c!=null)
			c.close();
		
        for (Entry<Integer, ArrayList<String>> entry:content.entrySet()) {
        	lines.add(this.getResources().getString(entry.getKey()));
            for (String category : entry.getValue()) {
            	lines.add("  "+category);
            }
        }
//        setText(builder.toString());
//        this.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        
        mRect = new Rect();
        linePaint = new Paint();
//        linePaint.setStyle(Paint.Style.STROKE);
//        linePaint.setStrokeWidth(1);
        linePaint.setColor(Color.GRAY);
        //linePaint.setAntiAlias(true);
        this.setLineSpacing(add, mult); 
        
		wordPaint=new Paint();
		wordPaint.setColor(0xFF000000);
		wordPaint.setStyle(Style.STROKE);
		wordPaint.setAntiAlias(true); // Ïû³ý¾â³Ý   
		wordPaint.setFlags(Paint.ANTI_ALIAS_FLAG); // Ïû³ý¾â³Ý 
        wordPaint.setTextSize(-fontMetrics.ascent);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
        int count = lines.size(); 
        for (int i = 0; i < count; i++) {
            getLineBounds(i, mRect); 
            int baseline = (i + 1) * getLineHeight(); 
            canvas.drawLine(mRect.left, baseline, mRect.right, baseline, linePaint);
            canvas.drawText(lines.get(i), 0, baseline-fontMetrics.descent, wordPaint);
        } 
        while((count++)*getLineHeight()<this.getHeight()){
        	int baseline = count * getLineHeight(); 
            canvas.drawLine(mRect.left, baseline, mRect.right, baseline, linePaint);
        }
        super.onDraw(canvas); 
	}

}
