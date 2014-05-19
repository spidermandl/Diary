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
        myEditText.setCursorVisible(true);//�����Ƿ���ʾ���
        myEditText.setHighlightColor(0xE000F5FF);//���ñ�ѡ�����ֵı���ɫ
        imageViewOnTouchListener(imageViewOfStart,imageViewOfEnd);
        
    }
    
	int line = 0;
	int curOff;
	private int off; // �ַ�����ƫ��ֵ
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
				
					//event.getX()��X���ϵĴ�����λ�ã� event.getY()��Y���ϵĴ�����λ��
					Log.d("debug", "&&& : getX is "+event.getRawX()+" | getY is "+event.getRawY()+"\n | line is "+line+" | off is "+off);
					break;
				case MotionEvent.ACTION_MOVE:
					//�ƶ�ͼƬ��λ�ã�һ������ϵ�У��������Զ����������������Ϊ�ο�ֵ��
					int l = x - temp[0];//������߾�
					int t = y-temp[1];//�����ϱ߾�
					int r = x + v.getWidth()- temp[0];//�����ұ߾�
					int b = y-temp[1] + v.getHeight();//�����±߾�
					v.layout(l,t,r,b);
					v.postInvalidate();
					
					line = myEditText.getLayout().getLineForVertical(myEditText.getScrollY() + t+v.getHeight()/2);//�õ���ֱ�����ϵ�����ֵ�������Ǵ�������Y���ϵ�ƫ����
					off = myEditText.getLayout().getOffsetForHorizontal(line, l+v.getWidth()/2);//�õ�ĳһ��ˮƽ�����ϵ�ƫ�����������ֱ��Ǹ�������ֵ�ʹ������ڸ���X����(��)
					//��ƫ��������ֵ����ݸ����ϵ����ֵĶ��ٶ��仯�������Ǻ����ϵ����ش�С��
					
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
				
					//event.getX()��X���ϵĴ�����λ�ã� event.getY()��Y���ϵĴ�����λ��
					Log.d("debug", "&&& : getX is "+event.getRawX()+" | getY is "+event.getRawY()+"\n | line is "+line+" | off is "+off);
					break;
				case MotionEvent.ACTION_MOVE:
					//�ƶ�ͼƬ��λ��
					int l = x - temp[0];//������߾�
					int t = y-temp[1];//�����ϱ߾�
					int r = x + v.getWidth()- temp[0];//�����ұ߾�
					int b = y-temp[1] + v.getHeight();//�����±߾�
					v.layout(l,t,r,b);
					v.postInvalidate();

					Log.i("&&&", 
							">>l:"+(x - temp[0])+
							">>t:"+(y-temp[1])+
							">>r:"+(x + v.getWidth() - temp[0])+
							">>b:"+(temp[1] + v.getHeight())
					);
					
					line = myEditText.getLayout().getLineForVertical(myEditText.getScrollY() +t+v.getHeight()/2);//�õ���ֱ�����ϵ�����ֵ�������Ǵ�������Y���ϵ�ƫ����
					curOff = myEditText.getLayout().getOffsetForHorizontal(line, l+v.getWidth()/2);//�õ�ĳһ��ˮƽ�����ϵ�ƫ�����������ֱ��Ǹ�������ֵ�ʹ������ڸ���X����(��)
					//��ƫ��������ֵ����ݸ����ϵ����ֵĶ��ٶ��仯�������Ǻ����ϵ����ش�С��
					
					Selection.setSelection(myEditText.getEditableText(), off, curOff);
					
					Log.d("debug", "&&& : getX is "+event.getRawX()+" | getY is "+event.getRawY()+"\n | line is "+line+" | curOff is "+curOff);
					break;
				case MotionEvent.ACTION_UP:
					int startOffset = myEditText.getSelectionStart();//ȡ�ñ�ѡ�����ֵ�ͷλ��
					int endOffset = myEditText.getSelectionEnd();//βλ��
					CharSequence choosedString;
					if (startOffset > endOffset) {
						choosedString = myEditText.getText().subSequence(endOffset, startOffset);//��ȡ
					} else {
						choosedString = myEditText.getText().subSequence(startOffset, endOffset);//��ȡ
					}
					Toast.makeText(getApplicationContext(), choosedString, 1000).show();//��ʾ
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