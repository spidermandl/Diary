package cn.numberlock;

import cn.numberlock.util.NkManager;
import android.app.Activity;
import android.os.Bundle;
/**
 * 
 * @ClassName: Test2Activity 
 * @author haoran.shu 
 * @date 2014��6��12�� ����12:01:20 
 * @version 1.0
 *
 */
public class Test2Activity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test2);//���������ļ�
		/*  ��ȡ�ؼ����
			ViewTreeObserver vtb = fl_keyboard.getViewTreeObserver();
			vtb.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout() {
					fl_keyboard.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					int width = fl_keyboard.getWidth();
					System.out.println("�ؼ���ȣ�"+width);
				}
			});
		*/
		NkManager.getInstance().initView(this, new NkManager.OnNumberClickListener() {
			
			@Override
			public void clickedNumber(int number) {
				System.out.println("������ˣ�"+number);
			}
		});
	}
}
