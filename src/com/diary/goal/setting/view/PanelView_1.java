package com.diary.goal.setting.view;

import java.util.Calendar;

import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Constant.SudoType;
import com.diary.goal.setting.tools.TextUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

public class PanelView_1 extends PanelView {

	public PanelView_1(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public PanelView_1(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public PanelView_1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	void init() {
		sudoType=SudoType.WORK;
		bgColor=0xFF00FF40;
		super.init();
	}
	
	@Override
	void selfDraw(Canvas canvas) {
		mPaint.setColor(0xFF000000);
		canvas.drawText(SudoType.getTypeString(sudoType), this.getMeasuredWidth()/2, this.getMeasuredHeight()/2, mPaint);
	}

}
