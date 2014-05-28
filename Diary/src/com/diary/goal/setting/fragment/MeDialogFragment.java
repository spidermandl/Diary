package com.diary.goal.setting.fragment;

import com.diary.goal.setting.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MeDialogFragment extends DialogFragment implements OnClickListener{
	public interface getDialogStringListener {  
		void   setTextString(String value);
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.me_dialog_fragment_layout, container, false);
		layout.findViewById(R.id.me_dialog_fragment_list).setOnClickListener(this);
		layout.findViewById(R.id.me_dialog_fragment_icon).setOnClickListener(this);
		layout.findViewById(R.id.me_dialog_fragment_cancel).setOnClickListener(this);
		return layout;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.me_dialog_fragment_list:
			getDialogStringListener activity=(getDialogStringListener)getActivity();
			activity.setTextString("列表模式");
			this.dismiss();
			break;
		case R.id.me_dialog_fragment_icon:
			getDialogStringListener activity1=(getDialogStringListener)getActivity();
			activity1.setTextString("图标模式");
			this.dismiss();
			break;
		case R.id.me_dialog_fragment_cancel:
			this.dismiss();
			break;
		default:
			break;
		}
	}
}
