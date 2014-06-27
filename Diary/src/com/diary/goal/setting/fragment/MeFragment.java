package com.diary.goal.setting.fragment;

import android.R.menu;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.diary.goal.setting.R;
/**
 * 用户个人页面
 * @author desmond.duan
 *
 */
public class MeFragment extends SherlockFragment implements OnClickListener{
	
	TextView lineOneValue;
	TextView syncText;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.me_fragment_layout, container, false);
		
		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();
		
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(R.string.me);

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.me_fragment_line_one:
//			showDialog(new MeDialogFragment(MeFragment.this));
//			break;
//		case R.id.me_fragment_line_two:
//			Toast.makeText(getActivity(), "此处跳转fragment", Toast.LENGTH_LONG).show();
//			break;
		default:
			break;
		}
	}
	private void showDialog(DialogFragment dialog)
	{
		FragmentManager fragmentManager=getFragmentManager();
		dialog.show(fragmentManager,"abc");
	}
	public void setTextString(String value){
		lineOneValue.setText(value);
	}
}
