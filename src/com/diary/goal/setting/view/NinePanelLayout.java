package com.diary.goal.setting.view;

import com.diary.goal.setting.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

public class NinePanelLayout extends RelativeLayout {

	RelativeLayout filpView;
	Context context;
	LayoutInflater m_inflater;
	
	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;
	private int mTouchState = TOUCH_STATE_REST;
	private float mLastMotionX;
	private int mTouchSlop;
	
	public NinePanelLayout(Context context) {
		super(context);
		init(context);
	}
	public NinePanelLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public NinePanelLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}
	void init(Context con){
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		int action=event.getAction();
//		final float x = event.getX();
//		switch (action) {
//		case MotionEvent.ACTION_DOWN:
//			mTouchState = TOUCH_STATE_REST;
//			if(filpView==null){
//				filpView=(RelativeLayout)m_inflater.inflate(R.layout.flip_tape, null);
//				RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);//filpView.getLayoutParams();
//				lp.topMargin=this.getHeight()/2;
//				filpView.setLayoutParams(lp);
//				this.addView(filpView);
//			}
//			else{
//				filpView.setVisibility(View.VISIBLE);
//			}
//			Log.e("NinePanellayout onTouchEvent", "action_down");
//			break;
//		case MotionEvent.ACTION_MOVE:
//			final int deltaX = (int) (mLastMotionX - x);
//
//			Log.e("deltaX", deltaX+" "+mTouchSlop);
//			boolean xMoved = Math.abs(deltaX) > mTouchSlop;
//
//			if (xMoved) {
//				// Scroll if the user moved far enough along the X axis
//				mTouchState = TOUCH_STATE_SCROLLING;
//
//			}
//
//			if (mTouchState == TOUCH_STATE_SCROLLING) {
//				// Scroll to follow the motion event
//				mLastMotionX = x;
//				if(filpView!=null)
//					filpView.setVisibility(View.GONE);
//				return true;
//			}
//			Log.e("NinePanellayout onTouchEvent", "action_move");
//			break;
//		case MotionEvent.ACTION_UP:
//			if(filpView!=null)
//				filpView.setVisibility(View.GONE);
//			Log.e("NinePanellayout onTouchEvent", "action_up");
//			break;
//		}
//		return true;
		return super.onTouchEvent(event);
	}
	
	public void invisibleTape(){
		if(filpView!=null)
			filpView.setVisibility(View.GONE);
	}
}
