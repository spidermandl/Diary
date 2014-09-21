package cn.numberlock;

import cn.numberlock.util.Consts;
import cn.numberlock.util.MyPrefs;
import cn.numberlock.widget.InputCircleView;
import cn.numberlock.widget.NumericKeyboard;
import cn.numberlock.widget.NumericKeyboard.OnNumberClick;
import cn.numberlock.widget.PasswordTextView;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: MainActivity
 * @author haoran.shu
 * @date 2014��6��11�� ����12:02:48
 * @version 1.0
 * 
 */
public class Test1Activity extends Activity {
	private NumericKeyboard nk;// ���ּ��̲���
	// �����
	private InputCircleView et_pwd1, et_pwd2, et_pwd3, et_pwd4;
	private int type;
	private TextView tv_info;//��ʾ��Ϣ
	//�����ַ�������ÿһ�����������
	private String input;
	private StringBuffer fBuffer = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test1);// ���������ļ�
		initWidget();// ��ʼ���ؼ�
		initListener();// �¼�����
		//��ȡ���洫�ݵ�ֵ
		type = getIntent().getIntExtra("type", 1);
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initWidget() {
		nk = (NumericKeyboard) findViewById(R.id.nk);// ���ּ���
		// �����
		et_pwd1 = (InputCircleView) findViewById(R.id.et_pwd1);
		et_pwd2 = (InputCircleView) findViewById(R.id.et_pwd2);
		et_pwd3 = (InputCircleView) findViewById(R.id.et_pwd3);
		et_pwd4 = (InputCircleView) findViewById(R.id.et_pwd4);
		tv_info = (TextView) findViewById(R.id.tv_info);//��ʾ��Ϣ
	}

	/**
	 * �¼�����
	 */
	private void initListener() {
		// ���õ���İ�ť�ص��¼�
		nk.setOnNumberClick(new OnNumberClick() {
			@Override
			public void onNumberReturn(int number) {
				// ������ʾ����
				setNumber(number);
			}
		});
		//�������һ���������ı��ı��¼��ص�
		et_pwd4.setOnTextChangedListener(new InputCircleView.OnTextChangedListener() {
			@Override
			public void textChanged(String content) {
				input = et_pwd1.getTextContent() + et_pwd2.getTextContent()+
						et_pwd3.getTextContent() + et_pwd4.getTextContent();
				//�ж�����
				if(type == Consts.SETTING_PASSWORD){//��������
					//������������
					tv_info.setText(getString(R.string.please_input_pwd_again));
					type = Consts.SURE_SETTING_PASSWORD;
					fBuffer.append(input);//�����һ�����������
					clearInput();//�������
				}else if(type == Consts.LOGIN_PASSWORD){//��¼
					
				}else if(type == Consts.SURE_SETTING_PASSWORD){//ȷ������
					//�ж���������������Ƿ�һ��
					if(input.equals(fBuffer.toString())){//һ��
						showToastMsg(getString(R.string.setting_pwd_success));
						//�������뵽�ļ���
						MyPrefs.getInstance().initSharedPreferences(Test1Activity.this);
					}else{//��һ��
						showToastMsg(getString(R.string.not_equals));
						clearInput();//�������
					}
				}
			}
		});
	}

	/**
	 * ������ʾ������
	 * 
	 * @param text
	 */
	private void setNumber(int number) {
		// ��������������ʾ
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
	 * ������������--����
	 */
	private void clearInput() {
		et_pwd1.setInput(InputCircleView.NIL);
		et_pwd2.setInput(InputCircleView.NIL);
		et_pwd3.setInput(InputCircleView.NIL);
		et_pwd4.setInput(InputCircleView.NIL);
	}

	/**
	 * ɾ���ո����������
	 */
	private void deleteText() {
		// ������������ɾ��
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
	 * ��ť�ĵ���¼�����
	 * @param v
	 */
	public void doClick(View v){
		//�жϵ���İ�ť
		switch (v.getId()) {
		case R.id.btn_again://����
			clearInput();//����������������
			break;
		case R.id.btn_delete://ɾ��
			deleteText();//ɾ���ո����������
			break;

		default:
			break;
		}
	}
	
	/**
	 * ��ʾToast��ʾ��Ϣ
	 * @param text
	 */
	private void showToastMsg(String text){
		Toast.makeText(this, text, 1000).show();
	}
}
