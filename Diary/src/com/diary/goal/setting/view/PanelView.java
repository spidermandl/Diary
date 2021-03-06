package com.diary.goal.setting.view;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.DiaryEditActivity;
import com.diary.goal.setting.activity.MainFrameActivity;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
/**
 * 九宫格父类
 * @author desmond.duan
 *
 */
public abstract class PanelView extends ViewGroup implements View.OnClickListener{

	protected Context context;
	protected Constant.SudoType sudoType=SudoType.SUDO_0;
	protected Paint mPaint,cPaint;
	protected int bgColor;
	protected StrokeTextView title;
	
	public PanelView(Context context) {
		super(context);
		this.context=context;
		init();
	}
	public PanelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init();
	}
	public PanelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		init();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		setMeasuredDimension(DiaryApplication.getInstance().getScreen_w()/3, 
//				DiaryApplication.getInstance().getScreen_h()/3);
		int width=MeasureSpec.getSize(widthMeasureSpec);
		int height=MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
	void init(){
		this.setWillNotDraw(false);
		this.setOnClickListener(this);
		mPaint=new Paint();

		cPaint=new Paint();
		cPaint.setColor(0xFFFFFFFF);
		cPaint.setStyle(Style.STROKE);
		cPaint.setStrokeWidth(1);
		cPaint.setAntiAlias(true); 
		cPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		
		if(sudoType!=SudoType.SUDO_5){
			title=new StrokeTextView(context);
			title.setGravity(Gravity.CENTER_HORIZONTAL);
			title.setText(context.getResources().getString(sudoType.getResString()));
			addView(title);
		}
		
		this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sudo_bg));

	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(title!=null){
		    title.layout(0, 3*(b-t)/4,r-l, b-t);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		if(!DiaryApplication.getInstance().getPadStatus().getPadStatus().get(sudoType)){
//			Bitmap temp=BitmapCustomize.customizePicture(context, R.drawable.null_edit,
//    				this.getWidth(),
//    				this.getHeight(),false);
//			canvas.drawBitmap(temp, zoomBitmap(temp, this.getWidth(), this.getHeight()), mPaint);
//		}
//		else{
//			canvas.drawColor(bgColor);
//		}
		
		selfDraw(canvas,DiaryApplication.getInstance().getSudoKuStatus().get(sudoType));
//		String text=context.getResources().getString(sudoType.getResString());
//		canvas.drawText(text, (this.getWidth()-cPaint.measureText(text))/2, this.getHeight()*3/4, cPaint);
		super.onDraw(canvas);
	}
	
	abstract void selfDraw(Canvas canvas,boolean activated);
	
	//@Override
	public void onClick(View v) {
		Intent intent=new Intent();
		//intent.setClass(context, RichTextEditorActivity.class);
		intent.setClass(context, DiaryEditActivity.class);
		intent.putExtra("sudoType", sudoType.getType());
		((Activity)context).startActivityForResult(intent, MainFrameActivity.REQUEST_UNITOVERVIEW);
	}
	
	protected Matrix zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		return matrix;
	}
	
}
