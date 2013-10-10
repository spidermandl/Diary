package com.diary.goal.setting.dialog;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.listener.OnCategoryChange;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
/**
 * Dialog for change category name
 * @author DuanLei
 *
 */
public class CategoryEditDialog extends Builder {

	EditText edit;
	Context context;
	OnCategoryChange listener;
	
	public CategoryEditDialog(Context arg0) {
		super(arg0);
		this.context=arg0;
		init();
	}
	
	void init(){
		edit=new EditText(this.context);
		edit.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        edit.setText(DiaryApplication.getInstance().getDateModel().getCategory_name());
        setView(edit).
		setPositiveButton(R.string.edit_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	if(listener!=null){
            		listener.changeCategoryName(edit.getEditableText().toString());
            	}
            }
        }).
        setNegativeButton(R.string.edit_dismiss, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	
            }
        });
	}
	
	public void setOnCategoryChangeListener(OnCategoryChange l){
		this.listener=l;
	}

}
