package cn.numberlock.util;
import cn.numberlock.R;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * ���ּ��̹����� -- ����ģʽ
 * @ClassName: NkManager 
 * @author haoran.shu 
 * @date 2014��6��16�� ����11:43:53 
 * @version 1.0
 *
 */
public class NkManager implements OnClickListener {
	private static NkManager nkManager;
	//���ּ����е�ÿһ������
	private Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six,
				    btn_seven, btn_eight, btn_neigh, btn_zero;
	private OnNumberClickListener onNumberClickListener;
	/**
	 * ˽�еĹ��췽��
	 */
	private NkManager(){
		
	}
	
	/**
	 * �����ṩ�ĳ�ʼ������
	 * @return
	 */
	public static NkManager getInstance(){
		if(nkManager == null){
			nkManager = new NkManager();
		}
		return nkManager;
	}
	
	/**
	 * ��ʼ�������ļ�
	 * @param ac
	 */
	public void initView(Activity ac, OnNumberClickListener onNumberClickListener){
		//��ʼ����ť
		btn_one = (Button) ac.findViewById(R.id.btn_one);//1
		btn_two = (Button) ac.findViewById(R.id.btn_two);//2
		btn_three = (Button) ac.findViewById(R.id.btn_three);//3
		btn_four = (Button) ac.findViewById(R.id.btn_four);//4
		btn_five = (Button) ac.findViewById(R.id.btn_five);//5
		btn_six = (Button) ac.findViewById(R.id.btn_six);//6
		btn_seven = (Button) ac.findViewById(R.id.btn_seven);//7
		btn_eight = (Button) ac.findViewById(R.id.btn_eight);//8
		btn_neigh = (Button) ac.findViewById(R.id.btn_neigh);//9
		btn_zero = (Button) ac.findViewById(R.id.btn_zero);//0
		//���õ���¼��ص�
		if(onNumberClickListener != null){
			this.onNumberClickListener = onNumberClickListener;
			btn_one.setOnClickListener(this);//1
			btn_two.setOnClickListener(this);//2
			btn_three.setOnClickListener(this);//3
			btn_four.setOnClickListener(this);//4
			btn_five.setOnClickListener(this);//5
			btn_six.setOnClickListener(this);//6
			btn_seven.setOnClickListener(this);//7
			btn_eight.setOnClickListener(this);//8
			btn_neigh.setOnClickListener(this);//9
			btn_zero.setOnClickListener(this);//0
		}
	}
	
	/**
	 * ���ְ�ť����ص��ӿ�
	 * @ClassName: onNumberClickListener 
	 * @author haoran.shu 
	 * @date 2014��6��16�� ����11:58:12 
	 * @version 1.0
	 *
	 */
	public interface OnNumberClickListener{
		/**
		 * ��������ְ�ť
		 * @param number	���ص��������
		 */
		void clickedNumber(int number);
	}
	
	/**
	 * ��ť�ĵ���¼��ص�
	 */
	@Override
	public void onClick(View v) {
		// �жϵ���İ�ť
		switch (v.getId()) {
		case R.id.btn_one://1
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(1);
			}
			break;
		case R.id.btn_two://2
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(2);
			}		
			break;
		case R.id.btn_three://3
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(3);
			}
			break;
		case R.id.btn_four://4
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(4);
			}
			break;
		case R.id.btn_five://5
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(5);
			}
			break;
		case R.id.btn_six://6
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(6);
			}
			break;
		case R.id.btn_seven://7
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(7);
			}
			break;
		case R.id.btn_eight://8
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(8);
			}
			break;
		case R.id.btn_neigh://9
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(9);
			}
			break;
		case R.id.btn_zero://0
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(0);
			}
			break;
		default:
			break;
		}
	}
	
}
