//package com.diary.goal.setting.invalid;
//
//
//import com.diary.goal.setting.DiaryApplication;
//import com.diary.goal.setting.R;
//import com.diary.goal.setting.view.QuickView;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.ViewGroup.LayoutParams;
//import android.view.Window;
//import android.view.WindowManager;
//
//@Deprecated
//public class OverviewDialog extends Dialog {
//
//	Context context;
//	
//	public OverviewDialog(Context context, int theme) {
//		super(context, theme);
//		this.context=context;
//	}
//
//	public OverviewDialog(Context context) {
//        super(context,R.style.Dialog_Fullscreen);
//		this.context=context;
//	}
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//        Window dialogWindow = this.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER);
//        lp.width = 95*DiaryApplication.getInstance().getScreen_w()/100; 
//        lp.height = 72*DiaryApplication.getInstance().getScreen_h()/100;
//        dialogWindow.setAttributes(lp);
//        
//        setContentView(new QuickView(context),new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//
//		super.onCreate(savedInstanceState);
//	}
//	
//	
//}
