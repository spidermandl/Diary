package com.diary.goal.setting.view;

import com.diary.goal.setting.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class NinePanelLayout extends ViewGroup {

	RelativeLayout filpView;
	Context context;
	LayoutInflater m_inflater;
	int width,height;
	
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
		width=MeasureSpec.getSize(widthMeasureSpec);
		height=MeasureSpec.getSize(heightMeasureSpec);
		for(int i=0;i<this.getChildCount();i++){
			View view=this.getChildAt(i);
			view.measure(MeasureSpec.makeMeasureSpec(width/3, MeasureSpec.EXACTLY), 
					MeasureSpec.makeMeasureSpec(height/3, MeasureSpec.EXACTLY));
		}
		setMeasuredDimension(width, height);
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count=this.getChildCount();
		for(int i=0;i<count;i++){
			View view=this.getChildAt(i);
			int leftMargin=i%3*(width/3);
			int topMargin=i/3*(height/3);
			view.layout(l+leftMargin, t+topMargin, 
					l+leftMargin+width/3, t+topMargin+height/3);
		}
	}
	void init(Context con){
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
	}
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		int action=event.getAction();
//		switch (action) {
//		case MotionEvent.ACTION_DOWN:
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
//			Log.e("NinePanelLayout", "ACTION_down");
//			break;
//		case MotionEvent.ACTION_MOVE:
//			if(filpView!=null)
//				filpView.setVisibility(View.GONE);
//			Log.e("NinePanelLayout", "ACTION_move");
//			break;
//		case MotionEvent.ACTION_UP:
//			Log.e("NinePanelLayout", "ACTION_UP");
//			if(filpView!=null)
//				filpView.setVisibility(View.GONE);
//			break;
//		}
//		return super.onTouchEvent(event);
//	}
	
	public void invisibleTape(){
		if(filpView!=null)
			filpView.setVisibility(View.GONE);
	}
}
