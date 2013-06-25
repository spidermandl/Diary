package com.diary.goal.setting.view;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.RichTextEditorActivity;
import com.diary.goal.setting.activity.UnitOverviewActivity;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.richedit.RichEditText;
import com.diary.goal.setting.tools.BitmapCustomize;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

public abstract class PanelView extends View implements View.OnClickListener{

	protected Context context;
	protected Constant.SudoType sudoType=SudoType.NO_TYPE;
	protected Paint mPaint;
	
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
		setMeasuredDimension(DiaryApplication.getInstance().getScreen_w()/3, 
				DiaryApplication.getInstance().getScreen_h()/3);
	}
	
	void init(){
		this.setOnClickListener(this);
		mPaint=new Paint();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(!DiaryApplication.getInstance().getPadStatus().getPadStatus().get(sudoType)){
			Bitmap temp=BitmapCustomize.customizePicture(context, R.drawable.null_edit,
    				this.getWidth(),
    				this.getHeight(),false);
			canvas.drawBitmap(temp, zoomBitmap(temp, this.getWidth(), this.getHeight()), mPaint);
//		this.setBackgroundDrawable(new BitmapDrawable(
//	    		BitmapCustomize.customizePicture(context, R.drawable.null_edit,
//	    				this.getWidth(),
//	    				this.getHeight())));
		}
		selfDraw(canvas);
		super.onDraw(canvas);
	}
	
	abstract void selfDraw(Canvas canvas);
	
	@Override
	public void onClick(View v) {
		DiaryApplication.getInstance().getDateModel().setType(sudoType);
		Intent intent=new Intent();
		intent.setClass(context, UnitOverviewActivity.class);
		context.startActivity(intent);
		
	}
	
	private Matrix zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		return matrix;
	}
	
}
