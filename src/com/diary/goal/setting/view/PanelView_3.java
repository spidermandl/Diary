package com.diary.goal.setting.view;

import com.diary.goal.setting.tools.Constant.SudoType;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class PanelView_3 extends PanelView {

	public PanelView_3(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public PanelView_3(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public PanelView_3(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	void selfDraw(Canvas canvas) {
		Paint paint=new Paint();
		paint.setColor(0xFFFF00FF);
		canvas.drawText(SudoType.getTypeString(sudoType), 0, 0, paint);
		
	}
	
	@Override
	void init() {
		sudoType=SudoType.FAMILY;
		super.init();
	}
}
