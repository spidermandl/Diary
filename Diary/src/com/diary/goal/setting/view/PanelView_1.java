package com.diary.goal.setting.view;

import java.util.Calendar;

import com.diary.goal.setting.R;
import com.diary.goal.setting.tools.BitmapCustomize;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

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
		sudoType=SudoType.SUDO_1;
		bgColor=0xFF00FF40;
		super.init();
	}
	
	@Override
	void selfDraw(Canvas canvas,boolean activated) {
//		mPaint.setColor(0xFF000000);
//		canvas.drawText(SudoType.getTypeString(sudoType), this.getMeasuredWidth()/2, this.getMeasuredHeight()/2, mPaint);
		Bitmap temp=BitmapCustomize.customizePicture(context, sudoType.getResDrawable(activated),
		this.getWidth()/2,
		this.getHeight()/3,false);
		Matrix matrix=zoomBitmap(temp, this.getWidth()/2, this.getHeight()/3);
		matrix.postTranslate(this.getWidth()/4, this.getHeight()/3);
		canvas.drawBitmap(temp, matrix, mPaint);
	}

}
