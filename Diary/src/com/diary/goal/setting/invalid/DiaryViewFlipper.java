package com.diary.goal.setting.invalid;


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
		if (e2.getX() - e1.getX() > 80) { // �������һ���������ҳ���
			Animation rInAnim = AnimationUtils.loadAnimation(context,R.anim.push_right_in); // ���һ���������Ľ���Ч��alpha 0.1 -> 1.0��
			Animation rOutAnim = AnimationUtils.loadAnimation(context,R.anim.push_right_out); // ���һ����Ҳ໬���Ľ���Ч��alpha 1.0 -> 0.1��

			this.setInAnimation(rInAnim);// ������������ڲ�����
			this.setOutAnimation(rOutAnim);// ������������ⲿ����
			this.showPrevious();// ���������е���ǰ�Ķ���
			return true;
		} else if (e2.getX() - e1.getX() < -80) { // �������󻬶����ҽ������
			Animation lInAnim = AnimationUtils.loadAnimation(context,R.anim.push_left_in); // ���󻬶�������Ľ���Ч��alpha 0.1 -> 1.0��
			Animation lOutAnim = AnimationUtils.loadAnimation(context,R.anim.push_left_out); // ���󻬶��Ҳ໬���Ľ���Ч��alpha 1.0 -> 0.1��

			this.setInAnimation(lInAnim);// ������������ڲ�����
			this.setOutAnimation(lOutAnim);// ������������ⲿ����
			this.showNext();// ������һ������
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
