package com.diary.goal.setting.view;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class DashLineView extends View {

	int mOrientation;
	Paint fgPaintSel;
	
	public DashLineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public DashLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DashLineView, defStyle, 0);
        mOrientation = a.getInt(R.styleable.DashLineView_orientation, 1);
        a.recycle();
        
        fgPaintSel = new Paint();
        fgPaintSel.setStrokeWidth((float)4.0);
        fgPaintSel.setColor(Color.BLACK);
		fgPaintSel.setStyle(Style.STROKE);
		fgPaintSel.setPathEffect(new DashPathEffect(new float[] {2,3}, 0));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(mOrientation==0){
			canvas.drawLine(0, 0, this.getMeasuredWidth(), 0, fgPaintSel);
		}
		else{
			canvas.drawLine(0, 0, 0, this.getMeasuredHeight(), fgPaintSel);
			//RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)this.getLayoutParams();
			//param.leftMargin=DiaryApplication.getInstance().getScreen_w()/3;
		}
			
		super.onDraw(canvas);
	}
}
