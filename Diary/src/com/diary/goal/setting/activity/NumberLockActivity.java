package com.diary.goal.setting.activity;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.tools.MyPreference;

import cn.numberlock.R;
import cn.numberlock.util.Consts;
import cn.numberlock.widget.InputCircleView;
import cn.numberlock.widget.NumericKeyboard;
import cn.numberlock.widget.NumericKeyboard.OnNumberClick;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 数字密码解锁界面
 * 资源文件在NumberLock工程中
 * @author Desmond Duan
 *
 */
public class NumberLockActivity extends Activity {

	private NumericKeyboard nk;// 数字键盘布局
	// 密码框
	private InputCircleView et_pwd1, et_pwd2, et_pwd3, et_pwd4;
	private int type;
	private TextView tv_info;//提示信息
	//声明字符串保存每一次输入的密码
	private String input;
	private StringBuffer fBuffer = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.number_lock_layout);// 关联布局文件
		initWidget();// 初始化控件
		initListener();// 事件处理
		//获取界面传递的值
		type = getIntent().getIntExtra("type", 1);
	}

	/**
	 * 初始化控件
	 */
	private void initWidget() {
		nk = (NumericKeyboard) findViewById(R.id.nk);// 数字键盘
		// 密码框
		et_pwd1 = (InputCircleView) findViewById(R.id.et_pwd1);
		et_pwd2 = (InputCircleView) findViewById(R.id.et_pwd2);
		et_pwd3 = (InputCircleView) findViewById(R.id.et_pwd3);
		et_pwd4 = (InputCircleView) findViewById(R.id.et_pwd4);
		tv_info = (TextView) findViewById(R.id.tv_info);//提示信息
		((RelativeLayout.LayoutParams)tv_info.getLayoutParams()).topMargin=DiaryApplication.getInstance().getScreen_h()/7;
	}

	/**
	 * 事件处理
	 */
	private void initListener() {
		// 设置点击的按钮回调事件
		nk.setOnNumberClick(new OnNumberClick() {
			@Override
			public void onNumberReturn(int number) {
				// 设置显示密码
				setNumber(number);
			}
		});
		//监听最后一个密码框的文本改变事件回调
		et_pwd4.setOnTextChangedListener(new InputCircleView.OnTextChangedListener() {
			@Override
			public void textChanged(String content) {
				input = et_pwd1.getTextContent() + et_pwd2.getTextContent()+
						et_pwd3.getTextContent() + et_pwd4.getTextContent();
				//判断类型
				if(type == Consts.SETTING_PASSWORD){//设置密码
					//重新输入密码
					tv_info.setText(getString(R.string.please_input_pwd_again));
					type = Consts.SURE_SETTING_PASSWORD;
					fBuffer.append(input);//保存第一次输入的密码
					clearInput();//清除输入
				}else if(type == Consts.LOGIN_PASSWORD){//登录
					
				}else if(type == Consts.SURE_SETTING_PASSWORD){//确认密码
					//判断两次输入的密码是否一致
					if(input.equals(fBuffer.toString())){//一致
						showToastMsg(getString(R.string.setting_pwd_success));
						//保存密码到文件中
						MyPreference.getInstance().initSharedPreferences(NumberLockActivity.this);
					}else{//不一致
						showToastMsg(getString(R.string.not_equals));
						clearInput();//清除输入
					}
				}
			}
		});
	}

	/**
	 * 设置显示的密码
	 * 
	 * @param text
	 */
	private void setNumber(int number) {
		// 从左往右依次显示
		if (!et_pwd1.isInputed()) {
			et_pwd1.setInput(number);
			return;
		} else if (!et_pwd2.isInputed()) {
			et_pwd2.setInput(number);
			return;
		} else if (!et_pwd3.isInputed()) {
			et_pwd3.setInput(number);
		} else if (!et_pwd4.isInputed()) {
			et_pwd4.setInput(number);
		}
	}

	/**
	 * 清除输入的内容--重输
	 */
	private void clearInput() {
		et_pwd1.setInput(InputCircleView.NIL);
		et_pwd2.setInput(InputCircleView.NIL);
		et_pwd3.setInput(InputCircleView.NIL);
		et_pwd4.setInput(InputCircleView.NIL);
	}

	/**
	 * 删除刚刚输入的内容
	 */
	private void deleteText() {
		// 从右往左依次删除
		if (et_pwd4.isInputed()) {
			et_pwd4.setInput(InputCircleView.NIL);
			return;
		} else if (et_pwd3.isInputed()) {
			et_pwd3.setInput(InputCircleView.NIL);
			return;
		} else if (et_pwd2.isInputed()) {
			et_pwd2.setInput(InputCircleView.NIL);
		} else if (et_pwd1.isInputed()) {
			et_pwd1.setInput(InputCircleView.NIL);
		}
	}
	
	/**
	 * 按钮的点击事件处理
	 * @param v
	 */
	public void doClick(View v){
		//判断点击的按钮
		switch (v.getId()) {
		case R.id.btn_again://重输
			clearInput();//清除所有输入的内容
			break;
		case R.id.btn_delete://删除
			deleteText();//删除刚刚输入的内容
			break;

		default:
			break;
		}
	}
	
	/**
	 * 显示Toast提示信息
	 * @param text
	 */
	private void showToastMsg(String text){
		Toast.makeText(this, text, 1000).show();
	}
}
