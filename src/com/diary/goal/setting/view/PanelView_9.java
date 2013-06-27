package com.diary.goal.setting.view;

import com.diary.goal.setting.tools.Constant.SudoType;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class PanelView_9 extends PanelView {

	public PanelView_9(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public PanelView_9(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public PanelView_9(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	void init() {
		sudoType=SudoType.SQUARE_9;
		bgColor=0xFF0000FF;
		super.init();
	}
	
	@Override
	void selfDraw(Canvas canvas) {
		mPaint.setColor(0xFFFFFFFF);
		canvas.drawText(SudoType.getTypeString(sudoType), this.getMeasuredWidth()/2, this.getMeasuredHeight()/2, mPaint);
		
	}
}
