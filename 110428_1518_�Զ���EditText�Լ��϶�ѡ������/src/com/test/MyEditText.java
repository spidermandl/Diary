package com.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * @author chenjianli
 * 			博文地址：http://www.eoeandroid.com/thread-71005-1-1.html
 *         用过EditText的都知道，EditText有个特点，当在里面长按的时候，会出现一个ContextMenu，提供了选择文字，复制，
 *         剪切等功能。有时候，我们会想，如果不出现这个ContextMenu，直接就在view
 *         上选择文字，那多美好啊。相信很多人抱有这样的想法，很不幸
 *         ，我也是。于是我就研究了一下EditText和TextView的代码，然后将这个问题解决了。
 *         网上很多资料都说，要选择一段文字，只需要用Selection.getSelectionStart()和
 *         Selection.getSelectionEnd()确定选择的文字的头和尾，然后加颜色就行。简直是胡扯啊，我敢说这样的代码根本就没有经过验证
 *         ，就发到网上了，然后一大堆人互相转载，结果导致误导了很多人，杯具 啊！！ 好，我们来分析一下解决办法。
 *         TextView是很多View的基类，如Button、EditText都是继承自他，所以EditText里面的代码很少。我们看一下
 *         EditText的源码，有一个Override的getDefaultEditable方法，看名字的意思是是否可编辑，这个方法直接返回true
 *         。还有一个getDefaultMovementMethod方法
 *         ，它返回的是ArrowKeyMovementMethod.getInstance
 *         ()，通过查看ArrowKeyMovementMethod的源码，基本确定这个方法就是弹出ContextMenu和轨迹球监听的“元凶”。
 *         下面，我们自己做一个view来打造自己的EditText。
 * 
 *         Java代码：
 * 
 * 
 *         01.view plaincopy to clipboardprint?
 * 
 *         02.@Override
 *         05.public boolean getDefaultEditable() {
 * 
 *         06.return false ;
 * 
 *         07.}
 * 
 *         08.
 * 
 *         09.@Override
 * 
 *         10.protected MovementMethod getDefaultMovementMethod() {
 * 
 *         11.return null ;
 * 
 *         12.} 现在测试一下，发现长按没反应了，所料不错，就是getDefaultMovementMethod方法控制了ContextMenu。
 *         看一下ArrowKeyMovementMethod的代码
 *         ，里面提供了KeyEvent、轨迹球事件onTrackballEvent和touch事件
 *         onTouchEvent的处理。这些事件在何处调用的呢
 *         ？我们看看TextView的onTouchEvent、onTrackballEvent和
 *         onKeyEvent方法里面就明白了，在这些事件回调中调用了ArrowKeyMovementMethod里面的这些方法。
 *         还有个问题，ContextMenu在哪里触发的？这个问题，用过ContextMenu的都知道，view里面要使用ContextMenu，
 *         需要覆盖一个onCreateContextMenu方法，然后在里面创建ContextMenu的各个选项。在TextView里面找
 *         onCreateContextMenu，果然有，里面定义了选择、复制、粘贴等选项。
 *         既然找到了这个，那么我们就可以进一步分析选择是如何做到的。
 *         onCreateContextMenu只是创建菜单，那么菜单点击之后，触发了什么呢？onCreateContextMenu里面定义了一个
 *         MenuHandler对象，然后作为参数传递给setOnMenuItemClickListener，找到MenuHandler，发现里面的
 *         onMenuItemClick返回的是onTextContextMenuItem函数
 *         ，找到onTextContextMenuItem，OMG
 *         ，终于找到点击menu触发的函数了。但是里面貌似没有关键的东西，选择的部分不在这里。那么，就应该在上面所说的那些事件里面了。
 *         重点分析ArrowKeyMovementMethod的onTouchEvent方法
 *         。发现一个重要的方法getLayout()，然后获取一个Layout对象，通过x和y坐标知道当前字符串的offset位置。
 *         那么，问题就可以完美的解决了。你可以点击任何地方然后拖动，释放之后，中间的文字就会被选中，so beautiful！
 */
public class MyEditText extends EditText {
	
	
	@Override
	public void setCursorVisible(boolean visible) {
		super.setCursorVisible(visible);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private int off; // 字符串的偏移值

	public MyEditText(Context context,Activity activity) {
		super(context);
		initialize();
	}

	@Override
	public void setHighlightColor(int color) {
		super.setHighlightColor(color);
	}
	
	private void initialize() {
//		setGravity(Gravity.TOP);
//		setBackgroundColor(Color.GRAY);
	}

	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
		// 不做任何处理，为了阻止长按的时候弹出上下文菜单
	}

	@Override
	public boolean getDefaultEditable() {
		return false;//是否可编辑
	}

	Layout layout;
	int line = 0;
	int curOff;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		layout = getLayout();
		switch (action) {
		case MotionEvent.ACTION_DOWN://按下时			
			Log.d("debug",					
					"MyEditText: getRawX()"+event.getRawX()+
					"getRawY()"+event.getRawY()+
					"getX()"+event.getX()+
					"getY()"+event.getX()				
			);		
			line = layout.getLineForVertical(getScrollY() + (int) event.getY());//得到垂直方向上的行数值，参数是触摸点在Y轴上的偏移量
			off = layout.getOffsetForHorizontal(line, (int) event.getX());//得到某一行水平方向上的偏移量，参数分别是该行行数值和触摸点在该行X轴上(续)
			//的偏移量，该值会根据该行上的文字的多少而变化，并不是横向上的像素大小；	
			//event.getX()：X轴上的触摸点位置， event.getY()：Y轴上的触摸点位置
			Log.d("debug", "Down : getX is "+event.getX()+" | getY is "+event.getY()+"\n | line is "+line+" | off is "+off);		
//			Selection.setSelection(getEditableText(), off);		
			Log.d("debug", "start:"+Selection.getSelectionStart(getEditableText())+"end:"+Selection.getSelectionEnd(getEditableText()));		
			
			startLeft = (int) (event.getX()-MainActivity.WIDTH/2);
			startRight = (int) (event.getX()+MainActivity.WIDTH/2);
			startTop = (int) (event.getY()-MainActivity.HEIGHT/2);
			startBottom = (int) (event.getY()+MainActivity.HEIGHT/2);	
			
			break;
		case MotionEvent.ACTION_MOVE://移动时 
			line = layout.getLineForVertical(getScrollY() + (int) event.getY());//得到垂直方向上的行数值，参数是触摸点在Y轴上的偏移量
			curOff = layout.getOffsetForHorizontal(line, (int) event.getX());//得到某一行水平方向上的偏移量，参数分别是该行行数值和触摸点在该行X轴上(续)
			//的偏移量，该值会根据该行上的文字的多少而变化，并不是横向上的像素大小；		
			Log.d("debug", "Up : getX is "+event.getX()+" | getY is "+event.getY()+"\n | line is "+line+" | curOff is "+curOff);			
			Selection.setSelection(getEditableText(), off, curOff);
			
			endLeft = (int) (event.getX()-MainActivity.WIDTH/2);
			endRight = (int) (event.getX()+MainActivity.WIDTH/2);
			endTop = (int) (event.getY()-MainActivity.HEIGHT/2);
			endBottom = (int) (event.getY()+MainActivity.HEIGHT/2);
			
			//设计思路：在移动选择过程中发出广播，传递过去光标的位置参数
			Intent intent = new Intent(CHOOSE_ACTION);

			intent.putExtra("startLeft", startLeft);
			intent.putExtra("startRight", startRight);
			intent.putExtra("startTop", startTop);
			intent.putExtra("startBottom", startBottom);
			
			intent.putExtra("endLeft", endLeft);
			intent.putExtra("endRight", endRight);
			intent.putExtra("endTop", endTop);
			intent.putExtra("endBottom", endBottom);
			
			intent.putExtra("off", off);
			intent.putExtra("curOff", curOff);
			
			getContext().getApplicationContext().sendBroadcast(intent);
			
			break;
		case MotionEvent.ACTION_UP://松开时
			Log.d("debug", "start:"+Selection.getSelectionStart(getEditableText())+"end:"+Selection.getSelectionEnd(getEditableText()));
			break;
		}
		return true;
	}

	public final static String CHOOSE_ACTION = "com.test.COPYTEXT";

    private int startLeft = 0;
    private int startRight = 0;
    private int startTop = 0;
    private int startBottom = 0;
    
    private int endLeft = 0;
    private int endRight = 0;
    private int endTop = 0;
    private int endBottom = 0;
    
}