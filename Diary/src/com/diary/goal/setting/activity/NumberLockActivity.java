package com.diary.goal.setting.activity;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.MyPreference;

import cn.numberlock.R;
import cn.numberlock.widget.InputCircleView;
import cn.numberlock.widget.NumericKeyboard;
import cn.numberlock.widget.NumericKeyboard.OnNumberClick;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 数字密码解锁界面
 * 资源文件在NumberLock工程中
 * @author Desmond Duan
 *
 */
public class NumberLockActivity extends Activity{
	public static final String TYPE="type";

	/** 初始设置密码 */
	public static final int SETTING_PASSWORD = 0;
	/** 确认密码 */
	public static final int SURE_SETTING_PASSWORD = 1;
	/** 验证登录密码 */
	public static final int LOGIN_PASSWORD = 2;
	/**清除密码*/
	public static final int CLEAR_PASSWORD =3;
	
	
	private NumericKeyboard nk;// 数字键盘布局
	// 密码框
	private InputCircleView et_pwd1, et_pwd2, et_pwd3, et_pwd4;
	private int type;
	private TextView tv_info,//提示信息
	        passwd_forget,
	        passwd_backwards;
	//声明字符串保存每一次输入的密码
	private String input;
	private StringBuffer fBuffer = new StringBuffer();
	//忘记密码
	private boolean forgetPasswd=false;
	//回退输入
	private boolean backwards=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.number_lock_layout);// 关联布局文件
		initWidget();// 初始化控件
		initListener();// 事件处理
	}

	/**
	 * 初始化控件
	 */
	private void initWidget() {
		//获取界面传递的值
		type = getIntent().getIntExtra(TYPE, 1);
		
		nk = (NumericKeyboard) findViewById(R.id.nk);// 数字键盘
		// 密码框
		et_pwd1 = (InputCircleView) findViewById(R.id.et_pwd1);
		et_pwd2 = (InputCircleView) findViewById(R.id.et_pwd2);
		et_pwd3 = (InputCircleView) findViewById(R.id.et_pwd3);
		et_pwd4 = (InputCircleView) findViewById(R.id.et_pwd4);
		tv_info = (TextView) findViewById(R.id.tv_info);//提示信息
		passwd_forget=(TextView)findViewById(R.id.btn_number_forget);
		passwd_backwards=(TextView)findViewById(R.id.btn_passwd_backwards);
		((RelativeLayout.LayoutParams)tv_info.getLayoutParams()).topMargin=DiaryApplication.getInstance().getScreen_h()/7;
	
		if(type==LOGIN_PASSWORD){
			forgetPasswd=true;
		}
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
				if(type==LOGIN_PASSWORD){
					forgetPasswd=true;
				}
				backwards=true;
			}
			
			@Override
			public void onInstructionShow(float[] point1,float[] point2){
				if(backwards){
					RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)passwd_backwards.getLayoutParams();
					params.leftMargin=(int)point1[0]-passwd_backwards.getWidth()/2;
					params.topMargin=(int)point1[1]-passwd_backwards.getHeight()/2;
					passwd_backwards.requestLayout();
					passwd_backwards.setVisibility(View.VISIBLE);
				}else{
					passwd_backwards.setVisibility(View.INVISIBLE);
				}				
				if(forgetPasswd){
					RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)passwd_forget.getLayoutParams();
					params.leftMargin=(int)point2[0]-passwd_forget.getWidth();
					params.topMargin=(int)point2[1]-passwd_forget.getHeight()/2;
					passwd_forget.requestLayout();
					passwd_forget.setVisibility(View.VISIBLE);
				}else{
					passwd_forget.setVisibility(View.INVISIBLE);
				}
			}
		});
		//监听最后一个密码框的文本改变事件回调
		et_pwd4.setOnTextChangedListener(new InputCircleView.OnTextChangedListener() {
			@Override
			public void textChanged(String content) {
				input = et_pwd1.getTextContent() + et_pwd2.getTextContent()+
						et_pwd3.getTextContent() + et_pwd4.getTextContent();
				//判断类型
				if(type == SETTING_PASSWORD){//设置密码
					//重新输入密码
					tv_info.setText(getString(R.string.please_input_pwd_again));
					type = SURE_SETTING_PASSWORD;
					fBuffer.append(input);//保存第一次输入的密码
					clearInput();//清除输入
					Intent intent= new Intent();
					intent.setClass(NumberLockActivity.this, NumberLockActivity.class);
					intent.putExtra(TYPE, SURE_SETTING_PASSWORD);
					NumberLockActivity.this.startActivity(intent);
				}else if(type == LOGIN_PASSWORD){//登录
					String numberCode=MyPreference.getInstance().readString(Constant.P_NUMBER_LOCK);
			        if(input.equals(numberCode)){
			        	DiaryApplication.getInstance().getMemCache().put(Constant.P_NUMBER_LOCK_ACTIVATED, Boolean.FALSE);
			        	NumberLockActivity.this.finish();
			        }else{
			        	showToastMsg(getString(R.string.login_fail));
						clearInput();//清除输入
			        }
				}else if(type == SURE_SETTING_PASSWORD){//确认密码
					//判断两次输入的密码是否一致
					if(input.equals(fBuffer.toString())){//一致
						showToastMsg(getString(R.string.setting_pwd_success));
						//保存密码到文件中
						MyPreference.getInstance().writeString(Constant.P_NUMBER_LOCK, fBuffer.toString());
						DiaryApplication.getInstance().getMemCache().put(Constant.P_NUMBER_LOCK_ACTIVATED, Boolean.FALSE);
						NumberLockActivity.this.finish();
					}else{//不一致
						showToastMsg(getString(R.string.not_equals));
						clearInput();//清除输入
						Intent intent= new Intent();
						intent.setClass(NumberLockActivity.this, NumberLockActivity.class);
						intent.putExtra(TYPE, SURE_SETTING_PASSWORD);
						NumberLockActivity.this.startActivity(intent);
					}
				}else if(type==CLEAR_PASSWORD){//清除密码
					String numberCode=MyPreference.getInstance().readString(Constant.P_NUMBER_LOCK);
			        if(input.equals(numberCode)){
			        	MyPreference.getInstance().writeString(Constant.P_NUMBER_LOCK, "");
			        	DiaryApplication.getInstance().getMemCache().put(Constant.P_NUMBER_LOCK_ACTIVATED, Boolean.FALSE);
			        	NumberLockActivity.this.finish();
			        }else{
			        	showToastMsg(getString(R.string.login_fail));
						clearInput();//清除输入
			        }
				}
			}
		});
		/**
		 * 忘记密码 按钮
		 */
		passwd_forget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v.getVisibility()==View.VISIBLE){
					
				}
				
			}
		});
		
		/**
		 * 删除密码 按钮
		 */
		passwd_backwards.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v.getVisibility()==View.VISIBLE){
					deleteText();//删除刚刚输入的内容
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
			return;
		} else if (!et_pwd4.isInputed()) {
			et_pwd4.setInput(number);
			return;
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
	 * 显示Toast提示信息
	 * @param text
	 */
	private void showToastMsg(String text){
		Toast.makeText(this, text, 1000).show();
	}
}
