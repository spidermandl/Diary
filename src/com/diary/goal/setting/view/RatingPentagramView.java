package com.diary.goal.setting.view;

import com.diary.goal.setting.R;
import com.diary.goal.setting.listener.OnRatingPentagramTouchUp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar.OnRatingBarChangeListener;
/**
 * redesign rating bar
 * @author duanlei
 *
 */
public class RatingPentagramView extends View {

	Path mPath;
	ShapeDrawable mDrawable;
	Paint nullPaint,activePaint;
	int starNum;
	float last_x=0;
	
	double r=0;
	double per=Math.sin(18*2*Math.PI/360)/Math.sin(54*2*Math.PI/360);
	double cos18=Math.cos(18*2*Math.PI/360);
	double cos54=Math.cos(54*2*Math.PI/360);
	double sin18=Math.sin(18*2*Math.PI/360);
	double sin54=Math.sin(54*2*Math.PI/360);

	OnRatingPentagramTouchUp listener;
	
	public RatingPentagramView(Context context, AttributeSet attrs) {
		this(context, attrs,1);
	}
	
	public RatingPentagramView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingPentagramView, defStyle, 0);
        starNum = a.getInt(R.styleable.RatingPentagramView_starNum, 1);
        a.recycle();
		init();
	}

	public void setOnTouchUpListener(OnRatingPentagramTouchUp l){
		this.listener=l;
	}
	
	void init(){
		mPath=new Path();
		nullPaint=new Paint();
		nullPaint.setColor(0xFFC0C0C0);
		nullPaint.setStyle(Style.FILL_AND_STROKE);
		nullPaint.setAntiAlias(true); 
		nullPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		
		activePaint=new Paint();
		activePaint.setColor(0xFFFF4000);
		activePaint.setStyle(Style.FILL_AND_STROKE);
		activePaint.setAntiAlias(true); 
		activePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		/**
		 *       |
		 *      /|\
		 *  \--/ | \--/
		 *   \   |   /
		 *    \  |  /
		 *    /  |  \
		 *   /  /-\  \
		 *  / /     \ \  
		 */
		/**
		 * calculate radius
		 */
		if(r==0){
			int width=this.getWidth();
			int height=this.getHeight();
			if(width>height){
				r=width/starNum>height?height/2:width/2/starNum;
			}else{
				r=height/starNum>width?width/2:height/2/starNum;
			}
		}
		int interval=(int)(last_x/r)+1;
		interval=interval>(2*starNum)?10:interval;
		interval=last_x==0?0:interval;
		int i;
		for(i=0;i<interval/2;i++){
			mPath.reset();
			mPath.moveTo((float)(i*2*r)+(float)(0+r), (float)(r*per+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*cos54+r), (float)(r*sin54+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*per*cos18+r), (float)(r*per*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*cos18+r), (float)(-r*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*per*cos54+r), (float)(-r*per*sin54+r));
			mPath.lineTo((float)(i*2*r)+(float)(0+r), (float)(-r+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*per*cos54+r), (float)(-r*per*sin54+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*cos18+r), (float)(-r*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*per*cos18+r), (float)(r*per*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*cos54+r), (float)(r*sin54+r));
			mPath.close();
			canvas.drawPath(mPath, activePaint);
		}
		if(interval%2!=0){
			/**
			 * draw pentagrams half by half
			 */
			mPath.reset();
			mPath.moveTo((float)(i*2*r)+(float)(0+r), (float)(r*per+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*cos54+r), (float)(r*sin54+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*per*cos18+r), (float)(r*per*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*cos18+r), (float)(-r*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*per*cos54+r), (float)(-r*per*sin54+r));
			mPath.lineTo((float)(i*2*r)+(float)(0+r), (float)(-r+r));
			mPath.close();
			canvas.drawPath(mPath,activePaint);
			
			mPath.reset();
			mPath.moveTo((float)(i*2*r)+(float)(0+r), (float)(-r+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*per*cos54+r), (float)(-r*per*sin54+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*cos18+r), (float)(-r*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*per*cos18+r), (float)(r*per*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*cos54+r), (float)(r*sin54+r));
			mPath.lineTo((float)(i*2*r)+(float)(0+r), (float)(r*per+r));
			mPath.close();
			canvas.drawPath(mPath, nullPaint);
			i++;
		}
		
		for(i=i;i<starNum;i++){
			//Log.e("coordinate", last_x+"  "+(i+1)*2*r+"");
			mPath.reset();
			mPath.moveTo((float)(i*2*r)+(float)(0+r), (float)(r*per+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*cos54+r), (float)(r*sin54+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*per*cos18+r), (float)(r*per*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*cos18+r), (float)(-r*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(-r*per*cos54+r), (float)(-r*per*sin54+r));
			mPath.lineTo((float)(i*2*r)+(float)(0+r), (float)(-r+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*per*cos54+r), (float)(-r*per*sin54+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*cos18+r), (float)(-r*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*per*cos18+r), (float)(r*per*sin18+r));
			mPath.lineTo((float)(i*2*r)+(float)(r*cos54+r), (float)(r*sin54+r));
			mPath.close();
			canvas.drawPath(mPath, nullPaint);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action=event.getAction();
        float x = event.getX();
        float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if(last_x<=r&&last_x>0&&x<=r){
				/**
				 * touching the very first half pentagram can clear all 
				 * if the previous first half was activited
				 */
				last_x=0;
			}else{
				last_x=x;
			}
			int interval=r!=0?(int)(last_x/r):0;
			if(interval<=(2*starNum)){
				invalidate();
				if(listener!=null){
					listener.touchUp((float)(interval+(last_x-interval)>0.5?0.5:0));
				}
			}
			break;
		}
		return true;
	}

	public void setRate(float rating) {
		// TODO Auto-generated method stub
		
	}
}
