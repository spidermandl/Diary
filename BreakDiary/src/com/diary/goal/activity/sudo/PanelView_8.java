package com.diary.goal.activity.sudo;


import com.diary.goal.util.BitmapCustomize;
import com.diary.goal.util.Constant.SudoType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class PanelView_8 extends PanelView {

	public PanelView_8(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public PanelView_8(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public PanelView_8(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	void init() {
		sudoType=SudoType.SUDO_8;
		bgColor=0xFFFF00FF;
		super.init();
	}
	
	@Override
	void selfDraw(Canvas canvas,boolean activated) {
//		mPaint.setColor(0xFF000000);
//		canvas.drawText(SudoType.getTypeString(sudoType), this.getMeasuredWidth()/2, this.getMeasuredHeight()/2, mPaint);
		Bitmap temp=BitmapCustomize.customizePicture(context, sudoType.getResDrawable(activated),
		this.getWidth(),
		this.getHeight(),false);
		canvas.drawBitmap(temp, zoomBitmap(temp, this.getWidth(), this.getHeight()), mPaint);
	}
}
