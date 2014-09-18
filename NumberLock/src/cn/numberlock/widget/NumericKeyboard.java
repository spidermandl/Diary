package cn.numberlock.widget;

import cn.numberlock.R;
import cn.trinea.android.common.util.SystemUtils;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	private float first_y = 0;// ����1��y����
//	private int title_height = 200;// �������߶�
	//�������鱣��ÿһ�е�Բ�ĺ�����
	private float[] xs = new float[3];
	//�������鱣��ÿһ�ŵ�Բ��������
	private float[] ys = new float[4];
	private float circle_x,circle_y;//�������Բ������
	private int number = -1;//���������
	private OnNumberClick onNumberClick;//���ֵ���¼�
	
	private final static int DIGITAL_SIZE=80;//���ִ�С
	private int CIRCLE_RADIUS;//����Բ�뾶
	private int VERTICAL_GAP;//Բ������
	/*
	 * �ж�ˢ������
	 * -1 ����������ˢ��
	 * 0  ����ˢ��
	 * 1  ����ˢ��
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
	 * @param onNumberClick
	 */
	public void setOnNumberClick(OnNumberClick onNumberClick){
		this.onNumberClick = onNumberClick;
	}

	// ��ʼ������
	private void initData(Context context) {
		// ��ȡ��Ļ�Ŀ��
		screen_width = SystemUtils.getSystemDisplay(context)[0];
		// Բ�뾶
		CIRCLE_RADIUS=screen_width/10;
		//Բ������
		VERTICAL_GAP=screen_width/20;//(1/2-1/5-1/5)/2;
		// ��ȡ����1��x����
		first_1_x = screen_width / 5;
		first_2_x =screen_width / 2;
		first_3_x = screen_width-screen_width / 5;
		// ��ȡ����1��y����
		first_y = (SystemUtils.getSystemDisplay(context)[1] - SystemUtils.getSystemDisplay(context)[1]/3) / 4;
		//���ÿһ�ŵĺ�����
		xs[0]=first_1_x; xs[1]=first_2_x; xs[2]=first_3_x;
		//���ÿһ�е�������
		ys[0]=first_y; ys[1]=first_y + 2*CIRCLE_RADIUS+VERTICAL_GAP; ys[2]=first_y + 4*CIRCLE_RADIUS+2*VERTICAL_GAP; ys[3]=first_y + 6*CIRCLE_RADIUS+3*VERTICAL_GAP;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// �������ʶ���
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);// ���û�����ɫ
		paint.setTextSize(DIGITAL_SIZE);// ���������С
		// �����ı�,ע���Ǵ����꿪ʼ���ϻ���
		// ���Ƶ�һ��1,2,3
		canvas.drawText("1", first_1_x-paint.measureText("1")/2, first_y, paint);
		canvas.drawText("2", first_2_x-paint.measureText("2")/2, first_y, paint);
		canvas.drawText("3", first_3_x-paint.measureText("3")/2, first_y, paint);
		// ���Ƶ�2��4,5,6
		canvas.drawText("4", first_1_x-paint.measureText("4")/2, first_y + 2*CIRCLE_RADIUS+VERTICAL_GAP, paint);
		canvas.drawText("5", first_2_x-paint.measureText("5")/2, first_y + 2*CIRCLE_RADIUS+VERTICAL_GAP, paint);
		canvas.drawText("6", first_3_x-paint.measureText("6")/2, first_y + 2*CIRCLE_RADIUS+VERTICAL_GAP, paint);
		// ���Ƶ�3��7,8,9
		canvas.drawText("7", first_1_x-paint.measureText("7")/2, first_y + 4*CIRCLE_RADIUS+2*VERTICAL_GAP, paint);
		canvas.drawText("8", first_2_x-paint.measureText("8")/2, first_y + 4*CIRCLE_RADIUS+2*VERTICAL_GAP, paint);
		canvas.drawText("9", first_3_x-paint.measureText("9")/2, first_y + 4*CIRCLE_RADIUS+2*VERTICAL_GAP, paint);
		// ���Ƶ�4��0
		canvas.drawText("0", first_2_x-paint.measureText("0")/2, first_y + 6*CIRCLE_RADIUS+3*VERTICAL_GAP, paint);
		//Ϊÿһ�����ֻ���һ��Բ
		paint.setColor(Color.WHITE);//���û�����ɫ
		paint.setStrokeWidth(3.0f);
		paint.setAntiAlias(true);//���ÿ����
		//���û��ƿ���Բ
		paint.setStyle(Paint.Style.STROKE);
		//���λ��Ƶ�һ�ŵ�Բ
		canvas.drawCircle(first_1_x, first_y, CIRCLE_RADIUS, paint);
		canvas.drawCircle(first_2_x, first_y, CIRCLE_RADIUS, paint);
		canvas.drawCircle(first_3_x, first_y, CIRCLE_RADIUS, paint);
		//���λ��Ƶ�2�ŵ�Բ
		canvas.drawCircle(first_1_x, first_y + 2*CIRCLE_RADIUS+VERTICAL_GAP, CIRCLE_RADIUS, paint);
		canvas.drawCircle(first_2_x, first_y + 2*CIRCLE_RADIUS+VERTICAL_GAP, CIRCLE_RADIUS, paint);
		canvas.drawCircle(first_3_x, first_y + 2*CIRCLE_RADIUS+VERTICAL_GAP, CIRCLE_RADIUS, paint);
		//���λ��Ƶ�3�ŵ�Բ
		canvas.drawCircle(first_1_x, first_y + 4*CIRCLE_RADIUS+2*VERTICAL_GAP, CIRCLE_RADIUS, paint);
		canvas.drawCircle(first_2_x, first_y + 4*CIRCLE_RADIUS+2*VERTICAL_GAP, CIRCLE_RADIUS, paint);
		canvas.drawCircle(first_3_x, first_y + 4*CIRCLE_RADIUS+2*VERTICAL_GAP, CIRCLE_RADIUS, paint);
		//�������һ��Բ
		canvas.drawCircle(first_2_x, first_y + 6*CIRCLE_RADIUS+3*VERTICAL_GAP, CIRCLE_RADIUS, paint);
	
		//�ж��Ƿ�������
		if(circle_x > 0 && circle_y > 0){//���
			if(type == 0){//����ˢ��
				paint.setColor(0x77777777);//���û�����ɫ
				paint.setStyle(Paint.Style.FILL);
				canvas.drawCircle(circle_x, circle_y, CIRCLE_RADIUS, paint);//����Բ
			}else if(type == 1){//����ˢ��
				paint.setColor(Color.WHITE);//���û�����ɫ
				paint.setStyle(Paint.Style.FILL);
				canvas.drawCircle(circle_x, circle_y, CIRCLE_RADIUS, paint);//����Բ
				//������ɺ�,����
				circle_x = 0; circle_y = 0;
			}
		}
	}

	/**
	 * ��ȡ��������¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//�¼��ж�
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN://����
				//�жϵ��������λ��
				float x = event.getX();//����ʱ��X����
				float y = event.getY();//����ʱ��Y����
				//�жϵ��������һ������Բ
				handleDown(x, y);
				return true;
			case MotionEvent.ACTION_UP://����
				type = 1;//����ˢ��
				invalidate();//ˢ�½���
				//���ص��������
				if(onNumberClick != null && number != -1){
					onNumberClick.onNumberReturn(number);
				}
				setDefault();//�ָ�Ĭ��
				//���͸����¼�
				sendAccessEvent(R.string.numeric_keyboard_up);
				return true;
			case MotionEvent.ACTION_CANCEL://ȡ��
				//�ָ�Ĭ��ֵ
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
	private void setDefault(){
		circle_x = 0; circle_y = 0;
		type = -1;
		number = -1;
		sendAccessEvent(R.string.numeric_keyboard_cancel);
	}
	
	/*
	 * ���ø�����������
	 */
	private void sendAccessEvent(int resId) {
		//��������
		setContentDescription(getContext().getString(resId));
		//���͸����¼�
		sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
		setContentDescription(null);
	}
	
	/*
	 * �жϵ��������һ������Բ
	 */
	private void handleDown(float x, float y){
		//�жϵ��������һ�е�����
		if(xs[0] - CIRCLE_RADIUS <= x && x <= xs[0] + CIRCLE_RADIUS){//��һ��
			//��ȡ�������Բ�ĺ�����
			circle_x = xs[0];
			//�жϵ��������һ��
			if(ys[0] - CIRCLE_RADIUS <= y && ys[0] + CIRCLE_RADIUS >= y){//��1��
				//��ȡ���������Բ��Բ��������
				circle_y = ys[0];
				number = 1;//���õ��������
			}else if(ys[1] - CIRCLE_RADIUS <= y && ys[1] + CIRCLE_RADIUS >= y){//��2��
				//��ȡ���������Բ��Բ��������
				circle_y = ys[1];
				number = 4;//���õ��������
			}else if(ys[2] - CIRCLE_RADIUS <= y && ys[2] + CIRCLE_RADIUS >= y){//��3��
				//��ȡ���������Բ��Բ��������
				circle_y = ys[2];
				number = 7;//���õ��������
			}
		}else if(xs[1] - CIRCLE_RADIUS <= x && x <= xs[1] + CIRCLE_RADIUS){//��2��
			//��ȡ�������Բ�ĺ�����
			circle_x = xs[1];
			//�жϵ��������һ��
			if(ys[0] - CIRCLE_RADIUS <= y && ys[0] + CIRCLE_RADIUS >= y){//��1��
				//��ȡ���������Բ��Բ��������
				circle_y = ys[0];
				number = 2;//���õ��������
			}else if(ys[1] - CIRCLE_RADIUS <= y && ys[1] + CIRCLE_RADIUS >= y){//��2��
				//��ȡ���������Բ��Բ��������
				circle_y = ys[1];
				number = 5;//���õ��������
			}else if(ys[2] - CIRCLE_RADIUS <= y && ys[2] + CIRCLE_RADIUS >= y){//��3��
				//��ȡ���������Բ��Բ��������
				circle_y = ys[2];
				number = 8;//���õ��������
			}else if(ys[3] - CIRCLE_RADIUS <= y && ys[3] + CIRCLE_RADIUS >= y){//��4��
				//��ȡ���������Բ��Բ��������
				circle_y = ys[3];
				number = 0;//���õ��������
			}
		}else if(xs[2] - CIRCLE_RADIUS <= x && x <= xs[2] + CIRCLE_RADIUS){//��3��
			//��ȡ�������Բ�ĺ�����
			circle_x = xs[2];
			//�жϵ��������һ��
			if(ys[0] - CIRCLE_RADIUS <= y && ys[0] + CIRCLE_RADIUS >= y){//��1��
				//��ȡ���������Բ��Բ��������
				circle_y = ys[0];
				number = 3;//���õ��������
			}else if(ys[1] - CIRCLE_RADIUS <= y && ys[1] + CIRCLE_RADIUS >= y){//��2��
				//��ȡ���������Բ��Բ��������
				circle_y = ys[1];
				number = 6;//���õ��������
			}else if(ys[2] - CIRCLE_RADIUS <= y && ys[2] + CIRCLE_RADIUS >= y){//��3��
				//��ȡ���������Բ��Բ��������
				circle_y = ys[2];
				number = 9;//���õ��������
			}
		}
		sendAccessEvent(R.string.numeric_keyboard_down);
		type = 0;//����ˢ��
		//���Ƶ��ʱ�ı���Բ
		invalidate();
	}
	
	/**
	 * ���ֵ���¼�
	 * @ClassName: OnNumberClick 
	 * @author haoran.shu 
	 * @date 2014��6��11�� ����11:41:09 
	 * @version 1.0
	 *
	 */
	public interface OnNumberClick{
		/**
		 * ���ص��������
		 * @param number
		 */
		public void onNumberReturn(int number);
	}
}
