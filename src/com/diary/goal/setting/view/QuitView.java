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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class QuitView extends TextView {

	Context context;
	HashMap<Integer,ArrayList<String>> content;
    private Paint mPaint;
    private Rect mRect; 
    private float mult = 1.5f; 
    private float add = 2.0f; 
	
	public QuitView(Context context) {
		super(context);
		this.context=context;
		init();
	}
	public QuitView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init();
	}
	public QuitView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		init();
	}
	
	void init(){
		setBackgroundColor(0x00000000);
		content=new HashMap<Integer, ArrayList<String>>();
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
		
        StringBuilder builder = new StringBuilder();
        for (Entry<Integer, ArrayList<String>> entry:content.entrySet()) {
        	builder.append(this.getResources().getString(entry.getKey())).append(":\n\n");
            for (String category : entry.getValue()) {
                  builder.append("  ").append(category).append("\n\n");
            }
        }
        setText(builder.toString());
        
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        this.setLineSpacing(add, mult); 
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
        int count = getLineCount(); 
        for (int i = 0; i < count; i++) { 
            getLineBounds(i, mRect); 
            int baseline = (i + 1) * getLineHeight(); 
            canvas.drawLine(mRect.left, baseline, mRect.right, baseline, mPaint); 
        } 
        super.onDraw(canvas); 
	}

}
