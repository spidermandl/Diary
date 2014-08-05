package com.diary.goal.setting.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 描边字体的textview
 * @author desmond.duan
 *
 */
public class StrokeTextView extends TextView {

	public StrokeTextView(Context context) {
		super(context);
		init();
	}
	public StrokeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public StrokeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init(){
		this.setTextColor(0xFFFFFFFF);
		Paint textPaint=this.getPaint();
		textPaint.setColor(0xFF000000);
		textPaint.setStyle(Style.STROKE);
		textPaint.setStrokeWidth(1.5f);
		textPaint.setAntiAlias(true); 
		textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}
}
