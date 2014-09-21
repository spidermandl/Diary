package cn.numberlock.widget;

import cn.numberlock.R;
import cn.trinea.android.common.util.SystemUtils;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
/**
 * 
 * @author Desmond Duan
 *
 */
public class InputCircleView extends View {

	public final static int NIL=-1;
	static int RADIUS;
	int inputNumber;
	OnTextChangedListener onTextChangedListener;
	
	public InputCircleView(Context context) {
		super(context);
		initData();
	}
	public InputCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
	}
	
	public InputCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData();
	}
	
	private void initData(){
		int screen_width = SystemUtils.getSystemDisplay(this.getContext())[0];
		RADIUS=screen_width/50;
		inputNumber=NIL;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		Paint paint=new Paint();
//		paint.setStrokeWidth(3.0f);
//		paint.setColor(Color.WHITE);//设置画笔颜色
//		paint.setAntiAlias(true);//设置抗锯齿
//		paint.setStyle(inputNumber!=NIL?Paint.Style.FILL:Paint.Style.STROKE);
//		canvas.drawCircle(RADIUS, RADIUS, RADIUS, paint);
		canvas.drawBitmap(
				BitmapFactory.decodeResource(this.getResources(), inputNumber!=NIL?R.drawable.icon_password:R.drawable.icon_password_null),
				null, new Rect(0, 0, RADIUS*2, RADIUS*2), null);
		super.onDraw(canvas);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(RADIUS*2, RADIUS*2);
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	/**
	 * 设置文本改变事件监听
	 * @param onTextChangedListener
	 */
	public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener){
		this.onTextChangedListener = onTextChangedListener;
	}
	
	public boolean isInputed(){
		return inputNumber==NIL?false:true;
	}
	
	public void setInput(int number){
		inputNumber=number;
		invalidate();
	}
	
	public String getTextContent(){
		return inputNumber==NIL?"":""+inputNumber;
	}

	/**
	 * 文本改变事件接口
	 * @ClassName: OnTextChangedListener 
	 * @author haoran.shu 
	 * @date 2014年6月12日 上午11:37:17 
	 * @version 1.0
	 *
	 */
	public interface OnTextChangedListener{
		/**
		 * 密码框文本改变时调用
		 * @param content
		 */
		public void textChanged(String content);
	}
}
