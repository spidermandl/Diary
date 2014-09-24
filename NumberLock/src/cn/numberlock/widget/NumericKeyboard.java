package cn.numberlock.widget;

import cn.numberlock.R;
import cn.numberlock.util.SystemUtils;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

/**
 * �Զ�������ּ���(������)
 * 
 * @ClassName: NumericKeyboard
 * @author haoran.shu
 * @date 2014��6��10�� ����5:16:41
 * @version 1.0
 * 
 */
public class NumericKeyboard extends View {
	private int screen_width = 0;// ��Ļ�Ŀ��
	private int first_1_x = 0;// ���Ƶ�һ��1��x����
	private int first_2_x = 0;// ���Ƶڶ���1��x����
	private int first_3_x = 0;// ���Ƶ�����1��x����
	private int first_y = 0;// ����1��y����
	// private int title_height = 200;// �������߶�
	// �������鱣��ÿһ�е�Բ�ĺ�����
	private float[] xs = new float[3];
	// �������鱣��ÿһ�ŵ�Բ��������
	private float[] ys = new float[4];
	//10��Բ������
	private float[][] center_s=new float[10][2];
	private float circle_x, circle_y;// �������Բ������
	private int number = -1;// ���������
	private OnNumberClick onNumberClick;// ���ֵ���¼�

	private int CIRCLE_RADIUS;// ����Բ�뾶
	private int VERTICAL_GAP;// Բ������
	/*
	 * �ж�ˢ������ -1 ����������ˢ�� 0 ����ˢ�� 1 ����ˢ��
	 */
	private int type = -1;

	/**
	 * ���췽��
	 * 
	 * @param context
	 * @param attrs
	 */
	public NumericKeyboard(Context context) {
		super(context);
		initData(context);// ��ʼ������
	}

	public NumericKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);// ��ʼ������
	}

	/**
	 * �������ֵ���¼�
	 * 
	 * @param onNumberClick
	 */
	public void setOnNumberClick(OnNumberClick onNumberClick) {
		this.onNumberClick = onNumberClick;
	}

	// ��ʼ������
	private void initData(Context context) {
		// ��ȡ��Ļ�Ŀ��
		screen_width = SystemUtils.getSystemDisplay(context)[0];
		// Բ�뾶
		CIRCLE_RADIUS = screen_width / 10;
		// Բ������
		VERTICAL_GAP = screen_width / 20;// (1/2-1/5-1/5)/2;
		// ��ȡ����1��x����
		first_1_x = screen_width / 5;
		first_2_x = screen_width / 2;
		first_3_x = screen_width - screen_width / 5;
		// ��ȡ����1��y����
		first_y = 2*CIRCLE_RADIUS;
		// ���ÿһ�ŵĺ�����
		xs[0] = first_1_x;
		xs[1] = first_2_x;
		xs[2] = first_3_x;
		// ���ÿһ�е�������
		ys[0] = first_y;
		ys[1] = first_y + 2 * CIRCLE_RADIUS + VERTICAL_GAP;
		ys[2] = first_y + 4 * CIRCLE_RADIUS + 2 * VERTICAL_GAP;
		ys[3] = first_y + 6 * CIRCLE_RADIUS + 3 * VERTICAL_GAP;
		
		center_s[0][0]=first_2_x;center_s[0][1]=first_y + 6 * CIRCLE_RADIUS + 3 * VERTICAL_GAP;//0
		center_s[1][0]=first_1_x;center_s[1][1]=first_y;//1
		center_s[2][0]=first_2_x;center_s[2][1]=first_y;//2
		center_s[3][0]=first_3_x;center_s[3][1]=first_y;//3
		center_s[4][0]=first_1_x;center_s[4][1]=first_y + 2 * CIRCLE_RADIUS + VERTICAL_GAP;//4
		center_s[5][0]=first_2_x;center_s[5][1]=first_y + 2 * CIRCLE_RADIUS + VERTICAL_GAP;//5
		center_s[6][0]=first_3_x;center_s[6][1]=first_y + 2 * CIRCLE_RADIUS + VERTICAL_GAP;//6
		center_s[7][0]=first_1_x;center_s[7][1]=first_y + 4 * CIRCLE_RADIUS + 2 * VERTICAL_GAP;//7
		center_s[8][0]=first_2_x;center_s[8][1]=first_y + 4 * CIRCLE_RADIUS + 2 * VERTICAL_GAP;//8
		center_s[9][0]=first_3_x;center_s[9][1]=first_y + 4 * CIRCLE_RADIUS + 2 * VERTICAL_GAP;//9
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect rect=new Rect();
		int nullnumber=-1;
		// �ж��Ƿ�������
		if (circle_x > 0 && circle_y > 0) {// ���
			int id = getNumberID(number, true);
			if (type == 0) {// ����ˢ��
				rect.left = (int) circle_x - CIRCLE_RADIUS;
				rect.right = (int) circle_x + CIRCLE_RADIUS;
				rect.top = (int) circle_y - CIRCLE_RADIUS;
				rect.bottom = (int) circle_y + CIRCLE_RADIUS;
				nullnumber=number;
				canvas.drawBitmap(BitmapFactory.decodeResource(this.getResources(), id),null, rect, null);
			} else if (type == 1) {// ����ˢ��
				rect.left = (int) circle_x - CIRCLE_RADIUS;
				rect.right = (int) circle_x + CIRCLE_RADIUS;
				rect.top = (int) circle_y - CIRCLE_RADIUS;
				rect.bottom = (int) circle_y + CIRCLE_RADIUS;
				nullnumber=number;
				canvas.drawBitmap(BitmapFactory.decodeResource(this.getResources(), id),null, rect, null);
				circle_x = 0;
				circle_y = 0;
			}
		}
		for(int i=0;i<10;i++){
			if(i!=nullnumber){
				rect.left=(int)center_s[i][0]-CIRCLE_RADIUS;
				rect.right=(int)center_s[i][0]+CIRCLE_RADIUS;
				rect.top = (int)center_s[i][1] - CIRCLE_RADIUS;
				rect.bottom = (int)center_s[i][1] + CIRCLE_RADIUS;
				canvas.drawBitmap(BitmapFactory.decodeResource(this.getResources(), getNumberID(i, false)),null, rect, null);
			}
		}

	}

	/**
	 * ��ȡ����ͼƬ
	 * @param number
	 * @param pressed
	 * @return
	 */
	private int getNumberID(int number,boolean pressed){
		int id=0;
		switch (number) {
		case 0:
			id = pressed?R.drawable.icon_keyboard_num0_pressed:R.drawable.icon_keyboard_num0;
			break;
		case 1:
			id = pressed?R.drawable.icon_keyboard_num1_pressed:R.drawable.icon_keyboard_num1;
			break;
		case 2:
			id = pressed?R.drawable.icon_keyboard_num2_pressed:R.drawable.icon_keyboard_num2;
			break;
		case 3:
			id = pressed?R.drawable.icon_keyboard_num3_pressed:R.drawable.icon_keyboard_num3;
			break;
		case 4:
			id = pressed?R.drawable.icon_keyboard_num4_pressed:R.drawable.icon_keyboard_num4;
			break;
		case 5:
			id = pressed?R.drawable.icon_keyboard_num5_pressed:R.drawable.icon_keyboard_num5;
			break;
		case 6:
			id = pressed?R.drawable.icon_keyboard_num6_pressed:R.drawable.icon_keyboard_num6;
			break;
		case 7:
			id = pressed?R.drawable.icon_keyboard_num7_pressed:R.drawable.icon_keyboard_num7;
			break;
		case 8:
			id = pressed?R.drawable.icon_keyboard_num8_pressed:R.drawable.icon_keyboard_num8;
			break;
		case 9:
			id = pressed?R.drawable.icon_keyboard_num9_pressed:R.drawable.icon_keyboard_num9;
			break;
		default:
			break;
		}
		
		return id;
	}
	/**
	 * ��ȡ��������¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// �¼��ж�
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:// ����
			// �жϵ��������λ��
			float x = event.getX();// ����ʱ��X����
			float y = event.getY();// ����ʱ��Y����
			// �жϵ��������һ������Բ
			handleDown(x, y);
			return true;
		case MotionEvent.ACTION_UP:// ����
			type = 1;// ����ˢ��
			invalidate();// ˢ�½���
			// ���ص��������
			if (onNumberClick != null && number != -1) {
				onNumberClick.onNumberReturn(number);
			}
			setDefault();// �ָ�Ĭ��
			// ���͸����¼�
			sendAccessEvent(R.string.numeric_keyboard_up);
			return true;
		case MotionEvent.ACTION_CANCEL:// ȡ��
			// �ָ�Ĭ��ֵ
			setDefault();
			return true;
		default:
			break;
		}
		return false;
	}

	/*
	 * �ָ�Ĭ��ֵ
	 */
	private void setDefault() {
		circle_x = 0;
		circle_y = 0;
		type = -1;
		number = -1;
		sendAccessEvent(R.string.numeric_keyboard_cancel);
	}

	/*
	 * ���ø�����������
	 */
	private void sendAccessEvent(int resId) {
		// ��������
		setContentDescription(getContext().getString(resId));
		// ���͸����¼�
		sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
		setContentDescription(null);
	}

	/*
	 * �жϵ��������һ������Բ
	 */
	private void handleDown(float x, float y) {
		// �жϵ��������һ�е�����
		if (xs[0] - CIRCLE_RADIUS <= x && x <= xs[0] + CIRCLE_RADIUS) {// ��һ��
			// ��ȡ�������Բ�ĺ�����
			circle_x = xs[0];
			// �жϵ��������һ��
			if (ys[0] - CIRCLE_RADIUS <= y && ys[0] + CIRCLE_RADIUS >= y) {// ��1��
				// ��ȡ���������Բ��Բ��������
				circle_y = ys[0];
				number = 1;// ���õ��������
			} else if (ys[1] - CIRCLE_RADIUS <= y && ys[1] + CIRCLE_RADIUS >= y) {// ��2��
				// ��ȡ���������Բ��Բ��������
				circle_y = ys[1];
				number = 4;// ���õ��������
			} else if (ys[2] - CIRCLE_RADIUS <= y && ys[2] + CIRCLE_RADIUS >= y) {// ��3��
				// ��ȡ���������Բ��Բ��������
				circle_y = ys[2];
				number = 7;// ���õ��������
			}
		} else if (xs[1] - CIRCLE_RADIUS <= x && x <= xs[1] + CIRCLE_RADIUS) {// ��2��
			// ��ȡ�������Բ�ĺ�����
			circle_x = xs[1];
			// �жϵ��������һ��
			if (ys[0] - CIRCLE_RADIUS <= y && ys[0] + CIRCLE_RADIUS >= y) {// ��1��
				// ��ȡ���������Բ��Բ��������
				circle_y = ys[0];
				number = 2;// ���õ��������
			} else if (ys[1] - CIRCLE_RADIUS <= y && ys[1] + CIRCLE_RADIUS >= y) {// ��2��
				// ��ȡ���������Բ��Բ��������
				circle_y = ys[1];
				number = 5;// ���õ��������
			} else if (ys[2] - CIRCLE_RADIUS <= y && ys[2] + CIRCLE_RADIUS >= y) {// ��3��
				// ��ȡ���������Բ��Բ��������
				circle_y = ys[2];
				number = 8;// ���õ��������
			} else if (ys[3] - CIRCLE_RADIUS <= y && ys[3] + CIRCLE_RADIUS >= y) {// ��4��
				// ��ȡ���������Բ��Բ��������
				circle_y = ys[3];
				number = 0;// ���õ��������
			}
		} else if (xs[2] - CIRCLE_RADIUS <= x && x <= xs[2] + CIRCLE_RADIUS) {// ��3��
			// ��ȡ�������Բ�ĺ�����
			circle_x = xs[2];
			// �жϵ��������һ��
			if (ys[0] - CIRCLE_RADIUS <= y && ys[0] + CIRCLE_RADIUS >= y) {// ��1��
				// ��ȡ���������Բ��Բ��������
				circle_y = ys[0];
				number = 3;// ���õ��������
			} else if (ys[1] - CIRCLE_RADIUS <= y && ys[1] + CIRCLE_RADIUS >= y) {// ��2��
				// ��ȡ���������Բ��Բ��������
				circle_y = ys[1];
				number = 6;// ���õ��������
			} else if (ys[2] - CIRCLE_RADIUS <= y && ys[2] + CIRCLE_RADIUS >= y) {// ��3��
				// ��ȡ���������Բ��Բ��������
				circle_y = ys[2];
				number = 9;// ���õ��������
			}
		}
		sendAccessEvent(R.string.numeric_keyboard_down);
		type = 0;// ����ˢ��
		// ���Ƶ��ʱ�ı���Բ
		invalidate();
	}

	/**
	 * ���ֵ���¼�
	 * 
	 * @ClassName: OnNumberClick
	 * @author haoran.shu
	 * @date 2014��6��11�� ����11:41:09
	 * @version 1.0
	 * 
	 */
	public interface OnNumberClick {
		/**
		 * ���ص��������
		 * 
		 * @param number
		 */
		public void onNumberReturn(int number);
	}
}
