package cn.sf.style;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import cn.sf.R;

public class StyleDemo extends Activity {
    /** Called when the activity is first created. */
    Button btn;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.style);
        btn = (Button)findViewById(R.id.btn);
        btn.setOnTouchListener(new OnTouchListener(){

			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					btn.setBackgroundResource(R.drawable.btn);
				}
				if(event.getAction()==MotionEvent.ACTION_UP){
					btn.setBackgroundResource(R.drawable.button9);
				}
				return false;
			}
        	
        });
    }
}