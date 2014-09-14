package cn.numberlock;

import cn.numberlock.util.NkManager;
import android.app.Activity;
import android.os.Bundle;
/**
 * 
 * @ClassName: Test2Activity 
 * @author haoran.shu 
 * @date 2014年6月12日 下午12:01:20 
 * @version 1.0
 *
 */
public class Test2Activity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test2);//关联布局文件
		/*  获取控件宽高
			ViewTreeObserver vtb = fl_keyboard.getViewTreeObserver();
			vtb.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout() {
					fl_keyboard.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					int width = fl_keyboard.getWidth();
					System.out.println("控件宽度："+width);
				}
			});
		*/
		NkManager.getInstance().initView(this, new NkManager.OnNumberClickListener() {
			
			@Override
			public void clickedNumber(int number) {
				System.out.println("您点击了："+number);
			}
		});
	}
}
