package com.diary.goal.setting.view;

import com.diary.goal.setting.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class NinePanelLayout extends RelativeLayout {

	RelativeLayout filpView;
	Context context;
	LayoutInflater m_inflater;
	
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
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action=event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if(filpView==null){
				filpView=(RelativeLayout)m_inflater.inflate(R.layout.flip_tape, null);
				RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);//filpView.getLayoutParams();
				lp.topMargin=this.getHeight()/2;
				filpView.setLayoutParams(lp);
				this.addView(filpView);
			}
			else{
				filpView.setVisibility(View.VISIBLE);
			}
			Log.e("NinePanelLayout", "ACTION_down");
			break;
		case MotionEvent.ACTION_MOVE:
			if(filpView!=null)
				filpView.setVisibility(View.GONE);
			Log.e("NinePanelLayout", "ACTION_move");
			break;
		case MotionEvent.ACTION_UP:
			Log.e("NinePanelLayout", "ACTION_UP");
			if(filpView!=null)
				filpView.setVisibility(View.GONE);
			break;
		}

		return super.onTouchEvent(event);
	}
}
