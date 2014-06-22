package com.diary.goal.setting.richtext;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.kemallette.RichEditText.Validations.AndValidator;
import com.kemallette.RichEditText.Widget.DefaultEditTextValidator;

public class DiaryEditTextValidator extends DefaultEditTextValidator {

	public DiaryEditTextValidator(EditText editText, AttributeSet attrs,
			Context context) {
		super(editText, attrs, context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void resetValidators(Context context) {

		// its possible the context may have changed so re-get the
		// defaultEmptyErrorString
		setEmptyErrorString(emptyErrorString);

		mValidator = new AndValidator();
	}

}
