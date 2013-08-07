package com.diary.goal.setting.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.view.View;

public class PentagramView extends View {

	Path mPath;
	ShapeDrawable mDrawable;
	double r;
	double per=Math.sin(18*2*Math.PI/360)/Math.sin(54*2*Math.PI/360);
	double cos18=Math.cos(18*2*Math.PI/360);
	double cos54=Math.cos(54*2*Math.PI/360);
	double sin18=Math.sin(18*2*Math.PI/360);
	double sin54=Math.sin(54*2*Math.PI/360);
	
	public PentagramView(Context context) {
		super(context);
		init();
	}
	
	public PentagramView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public PentagramView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	void init(){
		mPath=new Path();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		r=this.getHeight()/2;
		mPath.moveTo((float)(-r*cos18+r), (float)(r*sin18+r));
		mPath.lineTo((float)(-r*per*cos54+r), (float)(r*per*sin54+r));
		mPath.lineTo((float)(0+r), (float)(r+r));
		mPath.lineTo((float)(r*per*cos54+r), (float)(r*per*sin54+r));
		mPath.lineTo((float)(r*cos18+r), (float)(r*sin18+r));
		mPath.lineTo((float)(r*per*cos18+r), (float)(-r*per*sin18+r));
		mPath.lineTo((float)(r*cos54+r), (float)(-r*sin54+r));
		mPath.lineTo((float)(0+r), (float)(-r*per+r));
		mPath.lineTo((float)(-r*cos54+r), (float)(-r*sin54+r));
		mPath.lineTo((float)(-r*per*cos18+r), (float)(-r*per*sin18+r));
		mPath.close();
		
		mDrawable = new ShapeDrawable(new PathShape(mPath, (int)r, (int)r));
		mDrawable.draw(canvas);
		super.onDraw(canvas);
	}
}
