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
 * 			���ĵ�ַ��http://www.eoeandroid.com/thread-71005-1-1.html
 *         �ù�EditText�Ķ�֪����EditText�и��ص㣬�������泤����ʱ�򣬻����һ��ContextMenu���ṩ��ѡ�����֣����ƣ�
 *         ���еȹ��ܡ���ʱ�����ǻ��룬������������ContextMenu��ֱ�Ӿ���view
 *         ��ѡ�����֣��Ƕ����ð������źܶ��˱����������뷨���ܲ���
 *         ����Ҳ�ǡ������Ҿ��о���һ��EditText��TextView�Ĵ��룬Ȼ������������ˡ�
 *         ���Ϻܶ����϶�˵��Ҫѡ��һ�����֣�ֻ��Ҫ��Selection.getSelectionStart()��
 *         Selection.getSelectionEnd()ȷ��ѡ������ֵ�ͷ��β��Ȼ�����ɫ���С���ֱ�Ǻ��������Ҹ�˵�����Ĵ��������û�о�����֤
 *         ���ͷ��������ˣ�Ȼ��һ����˻���ת�أ�����������˺ܶ��ˣ����� ������ �ã�����������һ�½���취��
 *         TextView�Ǻܶ�View�Ļ��࣬��Button��EditText���Ǽ̳�����������EditText����Ĵ�����١����ǿ�һ��
 *         EditText��Դ�룬��һ��Override��getDefaultEditable�����������ֵ���˼���Ƿ�ɱ༭���������ֱ�ӷ���true
 *         ������һ��getDefaultMovementMethod����
 *         �������ص���ArrowKeyMovementMethod.getInstance
 *         ()��ͨ���鿴ArrowKeyMovementMethod��Դ�룬����ȷ������������ǵ���ContextMenu�͹켣������ġ�Ԫ�ס���
 *         ���棬�����Լ���һ��view�������Լ���EditText��
 * 
 *         Java���룺
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
 *         12.} ���ڲ���һ�£����ֳ���û��Ӧ�ˣ����ϲ�������getDefaultMovementMethod����������ContextMenu��
 *         ��һ��ArrowKeyMovementMethod�Ĵ���
 *         �������ṩ��KeyEvent���켣���¼�onTrackballEvent��touch�¼�
 *         onTouchEvent�Ĵ�����Щ�¼��ںδ����õ���
 *         �����ǿ���TextView��onTouchEvent��onTrackballEvent��
 *         onKeyEvent��������������ˣ�����Щ�¼��ص��е�����ArrowKeyMovementMethod�������Щ������
 *         ���и����⣬ContextMenu�����ﴥ���ģ�������⣬�ù�ContextMenu�Ķ�֪����view����Ҫʹ��ContextMenu��
 *         ��Ҫ����һ��onCreateContextMenu������Ȼ�������洴��ContextMenu�ĸ���ѡ���TextView������
 *         onCreateContextMenu����Ȼ�У����涨����ѡ�񡢸��ơ�ճ����ѡ�
 *         ��Ȼ�ҵ����������ô���ǾͿ��Խ�һ������ѡ������������ġ�
 *         onCreateContextMenuֻ�Ǵ����˵�����ô�˵����֮�󣬴�����ʲô�أ�onCreateContextMenu���涨����һ��
 *         MenuHandler����Ȼ����Ϊ�������ݸ�setOnMenuItemClickListener���ҵ�MenuHandler�����������
 *         onMenuItemClick���ص���onTextContextMenuItem����
 *         ���ҵ�onTextContextMenuItem��OMG
 *         �������ҵ����menu�����ĺ����ˡ���������ò��û�йؼ��Ķ�����ѡ��Ĳ��ֲ��������ô����Ӧ����������˵����Щ�¼������ˡ�
 *         �ص����ArrowKeyMovementMethod��onTouchEvent����
 *         ������һ����Ҫ�ķ���getLayout()��Ȼ���ȡһ��Layout����ͨ��x��y����֪����ǰ�ַ�����offsetλ�á�
 *         ��ô������Ϳ��������Ľ���ˡ�����Ե���κεط�Ȼ���϶����ͷ�֮���м�����־ͻᱻѡ�У�so beautiful��
 */
public class MyEditText extends EditText {
	
	
	@Override
	public void setCursorVisible(boolean visible) {
		super.setCursorVisible(visible);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private int off; // �ַ�����ƫ��ֵ

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
		// �����κδ���Ϊ����ֹ������ʱ�򵯳������Ĳ˵�
	}

	@Override
	public boolean getDefaultEditable() {
		return false;//�Ƿ�ɱ༭
	}

	Layout layout;
	int line = 0;
	int curOff;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		layout = getLayout();
		switch (action) {
		case MotionEvent.ACTION_DOWN://����ʱ			
			Log.d("debug",					
					"MyEditText: getRawX()"+event.getRawX()+
					"getRawY()"+event.getRawY()+
					"getX()"+event.getX()+
					"getY()"+event.getX()				
			);		
			line = layout.getLineForVertical(getScrollY() + (int) event.getY());//�õ���ֱ�����ϵ�����ֵ�������Ǵ�������Y���ϵ�ƫ����
			off = layout.getOffsetForHorizontal(line, (int) event.getX());//�õ�ĳһ��ˮƽ�����ϵ�ƫ�����������ֱ��Ǹ�������ֵ�ʹ������ڸ���X����(��)
			//��ƫ��������ֵ����ݸ����ϵ����ֵĶ��ٶ��仯�������Ǻ����ϵ����ش�С��	
			//event.getX()��X���ϵĴ�����λ�ã� event.getY()��Y���ϵĴ�����λ��
			Log.d("debug", "Down : getX is "+event.getX()+" | getY is "+event.getY()+"\n | line is "+line+" | off is "+off);		
//			Selection.setSelection(getEditableText(), off);		
			Log.d("debug", "start:"+Selection.getSelectionStart(getEditableText())+"end:"+Selection.getSelectionEnd(getEditableText()));		
			
			startLeft = (int) (event.getX()-MainActivity.WIDTH/2);
			startRight = (int) (event.getX()+MainActivity.WIDTH/2);
			startTop = (int) (event.getY()-MainActivity.HEIGHT/2);
			startBottom = (int) (event.getY()+MainActivity.HEIGHT/2);	
			
			break;
		case MotionEvent.ACTION_MOVE://�ƶ�ʱ 
			line = layout.getLineForVertical(getScrollY() + (int) event.getY());//�õ���ֱ�����ϵ�����ֵ�������Ǵ�������Y���ϵ�ƫ����
			curOff = layout.getOffsetForHorizontal(line, (int) event.getX());//�õ�ĳһ��ˮƽ�����ϵ�ƫ�����������ֱ��Ǹ�������ֵ�ʹ������ڸ���X����(��)
			//��ƫ��������ֵ����ݸ����ϵ����ֵĶ��ٶ��仯�������Ǻ����ϵ����ش�С��		
			Log.d("debug", "Up : getX is "+event.getX()+" | getY is "+event.getY()+"\n | line is "+line+" | curOff is "+curOff);			
			Selection.setSelection(getEditableText(), off, curOff);
			
			endLeft = (int) (event.getX()-MainActivity.WIDTH/2);
			endRight = (int) (event.getX()+MainActivity.WIDTH/2);
			endTop = (int) (event.getY()-MainActivity.HEIGHT/2);
			endBottom = (int) (event.getY()+MainActivity.HEIGHT/2);
			
			//���˼·�����ƶ�ѡ������з����㲥�����ݹ�ȥ����λ�ò���
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
		case MotionEvent.ACTION_UP://�ɿ�ʱ
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