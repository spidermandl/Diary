package cn.numberlock;

import cn.numberlock.util.Consts;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/**
 * 主界面
 * @ClassName: MainActivity 
 * @author haoran.shu 
 * @date 2014年6月12日 上午11:51:35 
 * @version 1.0
 *
 */
public class MainActivity extends Activity {
	
	/**
	 * 主入口
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);//关联布局文件
	}
	
	/**
	 * 按钮的点击事件
	 * @param v
	 */
	public void doMainClick(View v){
		//判断点击的按钮
		switch (v.getId()) {
		case R.id.btn1://Test1Activity
			createDialog(Test1Activity.class);//显示对话框
			break;
		case R.id.btn2://Test2Activity
			createDialog(Test2Activity.class);//显示对话框		
			break;

		default:
			break;
		}
	}
	
	/**
	 * 创建对话框
	 */
	private void createDialog(@SuppressWarnings("rawtypes") final Class cls){
		//创建AlertDialog对话框
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.dialog_title));//对话框标题
		dialog.setMessage(getString(R.string.dialog_msg));//显示的内容
		dialog.setPositiveButton(getString(R.string.dialog_ok), new 
				DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//界面跳转
						gotoActivity(cls, Consts.SETTING_PASSWORD);
					}
				});//确定按钮(设置密码)
		dialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//界面跳转
				gotoActivity(cls, Consts.LOGIN_PASSWORD);
			}
		});//取消按钮(登录)
		dialog.show();//显示对话框
	}
	
	/**
	 * 跳转界面
	 * @param type
	 */
	private void gotoActivity(@SuppressWarnings("rawtypes") Class cls, int type){
		//创建意图Intent对象
		Intent intent = new Intent(this,cls);
		intent.putExtra("type", type);//传递值
		startActivity(intent);//界面跳转
	}
}
