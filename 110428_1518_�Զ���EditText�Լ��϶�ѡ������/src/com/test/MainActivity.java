package com.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private ImageView imageViewOfStart;
	private ImageView imageViewOfEnd;

    public static int WIDTH;
    public static int HEIGHT;
    
	private MyEditText myEditText;
	int temp[] = {0,0};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        MyEditText myEditText = new MyEditText(getApplicationContext(), this);
//        myEditText.setText(R.string.hello);
//        myEditText.setTextColor(Color.WHITE);
//        setContentView(myEditText);
        imageViewOfStart = (ImageView)findViewById(R.id.imageViewStart);
        imageViewOfEnd = (ImageView)findViewById(R.id.imageViewEnd);

        imageViewOfStart.setVisibility(View.VISIBLE);
        imageViewOfEnd.setVisibility(View.VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imageview_fenxiang_start);
        WIDTH = bitmap.getWidth();
        HEIGHT = bitmap.getHeight();
        Log.d("testValue", "ON: W H  : W"+WIDTH/2+" |H"+HEIGHT/2);		

        myEditText = (MyEditText)findViewById(R.id.myEditText);
        myEditText.setCursorVisible(true);//设置是否显示光标
        myEditText.setHighlightColor(0xE000F5FF);//设置被选中文字的背景色
        imageViewOnTouchListener(imageViewOfStart,imageViewOfEnd);
        
    }
    
	int line = 0;
	int curOff;
	private int off; // 字符串的偏移值
    private void imageViewOnTouchListener(ImageView imageViewStart,ImageView imageViewEnd){
    	
    	imageViewStart.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int x = (int)event.getRawX();
				int y = (int)event.getRawY();
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					temp[0] = (int) event.getX();
					temp[1] = y-v.getTop();
					
					Log.d("debug", 
							
							"MainActivity: getRawX()"+event.getRawX()+
							"getRawY()"+event.getRawY()+
							"getX()"+event.getX()+
							"getY()"+event.getX()
							
					);
//					Log.d("debug", "layout is "+myEditText.getLayout());
				
					//event.getX()：X轴上的触摸点位置， event.getY()：Y轴上的触摸点位置
					Log.d("debug", "&&& : getX is "+event.getRawX()+" | getY is "+event.getRawY()+"\n | line is "+line+" | off is "+off);
					break;
				case MotionEvent.ACTION_MOVE:
					//移动图片的位置（一下坐标系中，上下是以顶部，左右是以左侧为参考值）
					int l = x - temp[0];//设置左边距
					int t = y-temp[1];//设置上边距
					int r = x + v.getWidth()- temp[0];//设置右边距
					int b = y-temp[1] + v.getHeight();//设置下边距
					v.layout(l,t,r,b);
					v.postInvalidate();
					
					line = myEditText.getLayout().getLineForVertical(myEditText.getScrollY() + t+v.getHeight()/2);//得到垂直方向上的行数值，参数是触摸点在Y轴上的偏移量
					off = myEditText.getLayout().getOffsetForHorizontal(line, l+v.getWidth()/2);//得到某一行水平方向上的偏移量，参数分别是该行行数值和触摸点在该行X轴上(续)
					//的偏移量，该值会根据该行上的文字的多少而变化，并不是横向上的像素大小；
					
					Selection.setSelection(myEditText.getEditableText(), off, curOff);
					
					Log.d("debug", "&&& : getX is "+event.getRawX()+" | getY is "+event.getRawY()+"\n | line is "+line+" | curOff is "+curOff);
					break;
				}
				return true;
			}
		});
    	
    	imageViewEnd.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int x = (int)event.getRawX();
				int y = (int)event.getRawY();
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					temp[0] = (int) event.getX();
					temp[1] = y-v.getTop();
					
					Log.d("debug", "v :"+"L>>"+v.getLeft()+"R>>"+v.getRight()+"T>>"+v.getTop()+"B>>"+v.getBottom());
				
					//event.getX()：X轴上的触摸点位置， event.getY()：Y轴上的触摸点位置
					Log.d("debug", "&&& : getX is "+event.getRawX()+" | getY is "+event.getRawY()+"\n | line is "+line+" | off is "+off);
					break;
				case MotionEvent.ACTION_MOVE:
					//移动图片的位置
					int l = x - temp[0];//设置左边距
					int t = y-temp[1];//设置上边距
					int r = x + v.getWidth()- temp[0];//设置右边距
					int b = y-temp[1] + v.getHeight();//设置下边距
					v.layout(l,t,r,b);
					v.postInvalidate();

					Log.i("&&&", 
							">>l:"+(x - temp[0])+
							">>t:"+(y-temp[1])+
							">>r:"+(x + v.getWidth() - temp[0])+
							">>b:"+(temp[1] + v.getHeight())
					);
					
					line = myEditText.getLayout().getLineForVertical(myEditText.getScrollY() +t+v.getHeight()/2);//得到垂直方向上的行数值，参数是触摸点在Y轴上的偏移量
					curOff = myEditText.getLayout().getOffsetForHorizontal(line, l+v.getWidth()/2);//得到某一行水平方向上的偏移量，参数分别是该行行数值和触摸点在该行X轴上(续)
					//的偏移量，该值会根据该行上的文字的多少而变化，并不是横向上的像素大小；
					
					Selection.setSelection(myEditText.getEditableText(), off, curOff);
					
					Log.d("debug", "&&& : getX is "+event.getRawX()+" | getY is "+event.getRawY()+"\n | line is "+line+" | curOff is "+curOff);
					break;
				case MotionEvent.ACTION_UP:
					int startOffset = myEditText.getSelectionStart();//取得被选中文字的头位置
					int endOffset = myEditText.getSelectionEnd();//尾位置
					CharSequence choosedString;
					if (startOffset > endOffset) {
						choosedString = myEditText.getText().subSequence(endOffset, startOffset);//截取
					} else {
						choosedString = myEditText.getText().subSequence(startOffset, endOffset);//截取
					}
					Toast.makeText(getApplicationContext(), choosedString, 1000).show();//显示
					break;
				}
				return true;
			}
		});
    }
    
    protected void onResume() {
    	super.onResume();
    	intentFilter = new IntentFilter();
    	intentFilter.addAction(MyEditText.CHOOSE_ACTION);
    	if (textChooseBroadCast == null) {
        	textChooseBroadCast = new TextChooseBroadCast();
		}
    	registerReceiver(textChooseBroadCast, intentFilter);
    };
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(textChooseBroadCast);
    }
    
    private class TextChooseBroadCast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(MyEditText.CHOOSE_ACTION)) {
				int startLeft = intent.getIntExtra("startLeft",50);
				int startRight = intent.getIntExtra("startRight",50+imageViewOfStart.getWidth());
				int startTop = intent.getIntExtra("startTop",50);
				int startBottom = intent.getIntExtra("startBottom",50+imageViewOfStart.getHeight());
				Log.d("testValue", "Start>>"+startLeft+"|"+startRight+"|"+startTop+"|"+startBottom);
				imageViewOfStart.layout(startLeft, startTop, startRight, startBottom);
				imageViewOfStart.postInvalidate();
				
				int endLeft = intent.getIntExtra("endLeft",50);
				int endRight = intent.getIntExtra("endRight",100+imageViewOfStart.getWidth());
				int endTop = intent.getIntExtra("endTop",50);
				int endBottom = intent.getIntExtra("endBottom",50+imageViewOfStart.getHeight());
				Log.d("testValue", "End>>"+startLeft+"|"+startRight+"|"+startTop+"|"+startBottom);
				imageViewOfEnd.layout(endLeft, endTop, endRight, endBottom);
				imageViewOfEnd.postInvalidate();
				
				off = intent.getIntExtra("off", 10);
				curOff = intent.getIntExtra("curOff", 50);
			}
		}
    	
    }
    
    private TextChooseBroadCast textChooseBroadCast;
    private IntentFilter intentFilter;
    
}