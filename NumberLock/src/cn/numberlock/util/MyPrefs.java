package cn.numberlock.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences������--����ģʽ
 * @ClassName: MyPrefs 
 * @author haoran.shu 
 * @date 2014��6��12�� ����9:33:41 
 * @version 1.0
 *
 */
public class MyPrefs {
	private static MyPrefs myPrefs;//˽�л�
	private SharedPreferences sp;
	//�ṩ˽�еĹ��췽��
	private MyPrefs(){}
	/**
	 * �����ṩ�ĳ�ʼ������
	 * @return
	 */
	public static MyPrefs getInstance(){
		//��ʼ���������
		if(myPrefs == null){
			myPrefs = new MyPrefs();
		}
		return myPrefs;
	}
	
	/**
	 * ��ʼ��SharedPreferences����
	 * @param context
	 */
	public MyPrefs initSharedPreferences(Context context){
		//��ȡSharedPreferences����
		if(sp == null){
			sp = context.getSharedPreferences(Consts.PREF_NAME, 
					Context.MODE_PRIVATE);
		}
		return myPrefs;
	}
	
	/**
	 * ��SharedPreferences��д��String���͵�����
	 * @param text
	 */
	public void writeString(String key, String value){
		//��ȡ�༭������
		Editor editor = sp.edit();
		//д������
		editor.putString(key, value);
		editor.commit();//�ύд�������
	}
	
	/**
	 * ����key��ȡSharedPreferences�е�String���͵�����
	 * @param key
	 * @return
	 */
	public String readString(String key){
		return sp.getString(key, "");
	}
}
