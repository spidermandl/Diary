package cn.numberlock.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * ϵͳ������
 * @ClassName: SystemUtils 
 * @author haoran.shu 
 * @date 2014��6��16�� ����10:52:28 
 * @version 1.0
 *
 */
public class SystemUtils {
	
	/**
	 * ��ȡ��Ļ�Ŀ�͸�
	 * @param context
	 * 					����Ϊ�����Ķ���Context
	 * @return
	 * 			����ֵΪ����Ϊ2int������,����
	 * 			int[0] -- ��ʾ��Ļ�Ŀ��
	 * 			int[1] -- ��ʾ��Ļ�ĸ߶�
	 */
	public static int[] getSystemDisplay(Context context){
		//����������Ļ��Ϣ��
		DisplayMetrics dm = new DisplayMetrics();
		//��ȡ���ڹ�����
		WindowManager wm =  (WindowManager) context.getSystemService(
				Context.WINDOW_SERVICE);
		//��ȡ��Ļ��Ϣ�����浽DisplayMetrics��
		wm.getDefaultDisplay().getMetrics(dm);
		//�������鱣����Ϣ
		int[] displays = new int[2];
		displays[0] = dm.widthPixels;//��Ļ���(��λ:px)
		displays[1] = dm.heightPixels;//��Ļ�߶�
		return displays;
	}
	
}
