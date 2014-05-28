package com.diary.goal.setting.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.diary.goal.setting.R;
import com.diary.goal.setting.fragment.MeDialogFragment.getDialogStringListener;

public class MeFragment extends SherlockFragment implements OnClickListener,getDialogStringListener{
	
	TextView lineOneValue;
	
	
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
		layout.findViewById(R.id.me_fragment_line_one).setOnClickListener(this);
		layout.findViewById(R.id.me_fragment_line_two).setOnClickListener(this);
		lineOneValue=(TextView)layout.findViewById(R.id.me_fragment_line_one_value);
		
		return layout;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.me_fragment_line_one:
			showDialog(new MeDialogFragment());
			break;
		case R.id.me_fragment_line_two:
			
			break;
		default:
			break;
		}
	}
	private void showDialog(DialogFragment dialog)
	{
		FragmentManager fragmentManager=getFragmentManager();
		dialog.show(fragmentManager, "haha");
	}
	public void setTextString(String value){
		lineOneValue.setText(value);
	}
}
