package com.diary.goal.setting.view;

import com.diary.goal.setting.tools.Constant.SudoType;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

public class PanelView_4 extends PanelView {

	public PanelView_4(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public PanelView_4(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public PanelView_4(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	void selfDraw(Canvas canvas) {
		Paint paint=new Paint();
		paint.setColor(0xFFFFFF00);
		Log.e("444444444", "444444444");
		canvas.drawText(SudoType.getTypeString(sudoType), 0, 0, paint);
		
	}
	
	@Override
	void init() {
		sudoType=SudoType.FINANCE;
		super.init();
	}

}
