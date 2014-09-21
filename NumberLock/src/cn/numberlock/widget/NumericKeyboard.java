package cn.numberlock.widget;

import cn.numberlock.R;
import cn.trinea.android.common.util.SystemUtils;
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
 * 自定义的数字键盘(不完善)
 * 
 * @ClassName: NumericKeyboard
 * @author haoran.shu
 * @date 2014年6月10日 下午5:16:41
 * @version 1.0
 * 
 */
public class NumericKeyboard extends View {
	private int screen_width = 0;// 屏幕的宽度
	private int first_1_x = 0;// 绘制第一列1的x坐标
	private int first_2_x = 0;// 绘制第二列1的x坐标
	private int first_3_x = 0;// 绘制第三列1的x坐标
	private int first_y = 0;// 绘制1的y坐标
	// private int title_height = 200;// 标题栏高度
	// 声明数组保存每一列的圆心横坐标
	private float[] xs = new float[3];
	// 声明数组保存每一排的圆心纵坐标
	private float[] ys = new float[4];
	//10组圆心坐标
	private float[][] center_s=new float[10][2];
	private float circle_x, circle_y;// 点击处的圆心坐标
	private int number = -1;// 点击的数字
	private OnNumberClick onNumberClick;// 数字点击事件

	private int CIRCLE_RADIUS;// 数字圆半径
	private int VERTICAL_GAP;// 圆纵向间隔
	/*
	 * 判断刷新数据 -1 不进行数据刷新 0 按下刷新 1 弹起刷新
	 */
	private int type = -1;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param attrs
	 */
	public NumericKeyboard(Context context) {
		super(context);
		initData(context);// 初始化数据
	}

	public NumericKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);// 初始化数据
	}

	/**
	 * 设置数字点击事件
	 * 
	 * @param onNumberClick
	 */
	public void setOnNumberClick(OnNumberClick onNumberClick) {
		this.onNumberClick = onNumberClick;
	}

	// 初始化数据
	private void initData(Context context) {
		// 获取屏幕的宽度
		screen_width = SystemUtils.getSystemDisplay(context)[0];
		// 圆半径
		CIRCLE_RADIUS = screen_width / 10;
		// 圆纵向间隔
		VERTICAL_GAP = screen_width / 20;// (1/2-1/5-1/5)/2;
		// 获取绘制1的x坐标
		first_1_x = screen_width / 5;
		first_2_x = screen_width / 2;
		first_3_x = screen_width - screen_width / 5;
		// 获取绘制1的y坐标
		first_y = (SystemUtils.getSystemDisplay(context)[1] - SystemUtils.getSystemDisplay(context)[1] / 3) / 4;
		// 添加每一排的横坐标
		xs[0] = first_1_x;
		xs[1] = first_2_x;
		xs[2] = first_3_x;
		// 添加每一列的纵坐标
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
		// 判断是否点击数字
		if (circle_x > 0 && circle_y > 0) {// 点击
			int id = getNumberID(number, true);
			if (type == 0) {// 按下刷新
				rect.left = (int) circle_x - CIRCLE_RADIUS;
				rect.right = (int) circle_x + CIRCLE_RADIUS;
				rect.top = (int) circle_y - CIRCLE_RADIUS;
				rect.bottom = (int) circle_y + CIRCLE_RADIUS;
				nullnumber=number;
				canvas.drawBitmap(BitmapFactory.decodeResource(this.getResources(), id),null, rect, null);
			} else if (type == 1) {// 弹起刷新
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
	 * 获取数字图片
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
	 * 获取触摸点击事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 事件判断
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:// 按下
			// 判断点击的坐标位置
			float x = event.getX();// 按下时的X坐标
			float y = event.getY();// 按下时的Y坐标
			// 判断点击的是哪一个数字圆
			handleDown(x, y);
			return true;
		case MotionEvent.ACTION_UP:// 弹起
			type = 1;// 弹起刷新
			invalidate();// 刷新界面
			// 返回点击的数字
			if (onNumberClick != null && number != -1) {
				onNumberClick.onNumberReturn(number);
			}
			setDefault();// 恢复默认
			// 发送辅助事件
			sendAccessEvent(R.string.numeric_keyboard_up);
			return true;
		case MotionEvent.ACTION_CANCEL:// 取消
			// 恢复默认值
			setDefault();
			return true;
		default:
			break;
		}
		return false;
	}

	/*
	 * 恢复默认值
	 */
	private void setDefault() {
		circle_x = 0;
		circle_y = 0;
		type = -1;
		number = -1;
		sendAccessEvent(R.string.numeric_keyboard_cancel);
	}

	/*
	 * 设置辅助功能描述
	 */
	private void sendAccessEvent(int resId) {
		// 设置描述
		setContentDescription(getContext().getString(resId));
		// 发送辅助事件
		sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
		setContentDescription(null);
	}

	/*
	 * 判断点击的是哪一个数字圆
	 */
	private void handleDown(float x, float y) {
		// 判断点击的是那一列的数据
		if (xs[0] - CIRCLE_RADIUS <= x && x <= xs[0] + CIRCLE_RADIUS) {// 第一列
			// 获取点击处的圆心横坐标
			circle_x = xs[0];
			// 判断点击的是哪一排
			if (ys[0] - CIRCLE_RADIUS <= y && ys[0] + CIRCLE_RADIUS >= y) {// 第1排
				// 获取点击的数字圆的圆心纵坐标
				circle_y = ys[0];
				number = 1;// 设置点击的数字
			} else if (ys[1] - CIRCLE_RADIUS <= y && ys[1] + CIRCLE_RADIUS >= y) {// 第2排
				// 获取点击的数字圆的圆心纵坐标
				circle_y = ys[1];
				number = 4;// 设置点击的数字
			} else if (ys[2] - CIRCLE_RADIUS <= y && ys[2] + CIRCLE_RADIUS >= y) {// 第3排
				// 获取点击的数字圆的圆心纵坐标
				circle_y = ys[2];
				number = 7;// 设置点击的数字
			}
		} else if (xs[1] - CIRCLE_RADIUS <= x && x <= xs[1] + CIRCLE_RADIUS) {// 第2列
			// 获取点击处的圆心横坐标
			circle_x = xs[1];
			// 判断点击的是哪一排
			if (ys[0] - CIRCLE_RADIUS <= y && ys[0] + CIRCLE_RADIUS >= y) {// 第1排
				// 获取点击的数字圆的圆心纵坐标
				circle_y = ys[0];
				number = 2;// 设置点击的数字
			} else if (ys[1] - CIRCLE_RADIUS <= y && ys[1] + CIRCLE_RADIUS >= y) {// 第2排
				// 获取点击的数字圆的圆心纵坐标
				circle_y = ys[1];
				number = 5;// 设置点击的数字
			} else if (ys[2] - CIRCLE_RADIUS <= y && ys[2] + CIRCLE_RADIUS >= y) {// 第3排
				// 获取点击的数字圆的圆心纵坐标
				circle_y = ys[2];
				number = 8;// 设置点击的数字
			} else if (ys[3] - CIRCLE_RADIUS <= y && ys[3] + CIRCLE_RADIUS >= y) {// 第4排
				// 获取点击的数字圆的圆心纵坐标
				circle_y = ys[3];
				number = 0;// 设置点击的数字
			}
		} else if (xs[2] - CIRCLE_RADIUS <= x && x <= xs[2] + CIRCLE_RADIUS) {// 第3列
			// 获取点击处的圆心横坐标
			circle_x = xs[2];
			// 判断点击的是哪一排
			if (ys[0] - CIRCLE_RADIUS <= y && ys[0] + CIRCLE_RADIUS >= y) {// 第1排
				// 获取点击的数字圆的圆心纵坐标
				circle_y = ys[0];
				number = 3;// 设置点击的数字
			} else if (ys[1] - CIRCLE_RADIUS <= y && ys[1] + CIRCLE_RADIUS >= y) {// 第2排
				// 获取点击的数字圆的圆心纵坐标
				circle_y = ys[1];
				number = 6;// 设置点击的数字
			} else if (ys[2] - CIRCLE_RADIUS <= y && ys[2] + CIRCLE_RADIUS >= y) {// 第3排
				// 获取点击的数字圆的圆心纵坐标
				circle_y = ys[2];
				number = 9;// 设置点击的数字
			}
		}
		sendAccessEvent(R.string.numeric_keyboard_down);
		type = 0;// 按下刷新
		// 绘制点击时的背景圆
		invalidate();
	}

	/**
	 * 数字点击事件
	 * 
	 * @ClassName: OnNumberClick
	 * @author haoran.shu
	 * @date 2014年6月11日 上午11:41:09
	 * @version 1.0
	 * 
	 */
	public interface OnNumberClick {
		/**
		 * 返回点击的数字
		 * 
		 * @param number
		 */
		public void onNumberReturn(int number);
	}
}
