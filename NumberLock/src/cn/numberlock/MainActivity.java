//package cn.numberlock;
//
//import cn.numberlock.util.Consts;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
///**
// * ������
// * @ClassName: MainActivity 
// * @author haoran.shu 
// * @date 2014��6��12�� ����11:51:35 
// * @version 1.0
// *
// */
//public class MainActivity extends Activity {
//	
//	/**
//	 * �����
//	 */
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);//���������ļ�
//	}
//	
//	/**
//	 * ��ť�ĵ���¼�
//	 * @param v
//	 */
//	public void doMainClick(View v){
//		//�жϵ���İ�ť
//		switch (v.getId()) {
//		case R.id.btn1://Test1Activity
//			createDialog(Test1Activity.class);//��ʾ�Ի���
//			break;
//
//		default:
//			break;
//		}
//	}
//	
//	/**
//	 * �����Ի���
//	 */
//	private void createDialog(@SuppressWarnings("rawtypes") final Class cls){
//		//����AlertDialog�Ի���
//		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//		dialog.setTitle(getString(R.string.dialog_title));//�Ի������
//		dialog.setMessage(getString(R.string.dialog_msg));//��ʾ������
//		dialog.setPositiveButton(getString(R.string.dialog_ok), new 
//				DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						//������ת
//						gotoActivity(cls, Consts.SETTING_PASSWORD);
//					}
//				});//ȷ����ť(��������)
//		dialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				//������ת
//				gotoActivity(cls, Consts.LOGIN_PASSWORD);
//			}
//		});//ȡ����ť(��¼)
//		dialog.show();//��ʾ�Ի���
//	}
//	
//	/**
//	 * ��ת����
//	 * @param type
//	 */
//	private void gotoActivity(@SuppressWarnings("rawtypes") Class cls, int type){
//		//������ͼIntent����
//		Intent intent = new Intent(this,cls);
//		intent.putExtra("type", type);//����ֵ
//		startActivity(intent);//������ת
//	}
//}
