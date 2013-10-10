package com.diary.goal.setting.view;


import com.diary.goal.setting.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class DiaryViewFlipper extends ViewFlipper implements android.view.GestureDetector.OnGestureListener {

	Context context;
	
	public DiaryViewFlipper(Context context) {
		super(context);
		init(context);
	}
	
	public DiaryViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	void init(Context con){
		this.context=con;
	}
	
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e2.getX() - e1.getX() > 80) { // 从左向右滑动（左进右出）
			Animation rInAnim = AnimationUtils.loadAnimation(context,R.anim.push_right_in); // 向右滑动左侧进入的渐变效果（alpha 0.1 -> 1.0）
			Animation rOutAnim = AnimationUtils.loadAnimation(context,R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0 -> 0.1）

			this.setInAnimation(rInAnim);// 在容器里插入内部动画
			this.setOutAnimation(rOutAnim);// 在容器里插入外部动画
			this.showPrevious();// 播放容器中的先前的动画
			return true;
		} else if (e2.getX() - e1.getX() < -80) { // 从右向左滑动（右进左出）
			Animation lInAnim = AnimationUtils.loadAnimation(context,R.anim.push_left_in); // 向左滑动左侧进入的渐变效果（alpha 0.1 -> 1.0）
			Animation lOutAnim = AnimationUtils.loadAnimation(context,R.anim.push_left_out); // 向左滑动右侧滑出的渐变效果（alpha 1.0 -> 0.1）

			this.setInAnimation(lInAnim);// 在容器里插入内部动画
			this.setOutAnimation(lOutAnim);// 在容器里插入外部动画
			this.showNext();// 播放下一个动画
			return true;
		}
		return true;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
