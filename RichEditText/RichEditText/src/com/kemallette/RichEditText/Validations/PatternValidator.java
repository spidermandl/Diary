package com.kemallette.RichEditText.Validations;


import java.util.regex.Pattern;

import android.widget.EditText;

/**
 * Base class for regexp based validators.
 * 
 * @see DomainValidator
 * @see EmailValidator
 * @see IpAddressValidator
 * @see PhoneValidator
 * @see WebUrlValidator
 * @see RegexpValidator
 * 
 */
public class PatternValidator extends Validator{

	protected Pattern	pattern;


	public PatternValidator(String _customErrorMessage, Pattern _pattern){

		super(_customErrorMessage);
		if (_pattern == null)
			throw new IllegalArgumentException("_pattern must not be null");
		pattern = _pattern;
	}


	public boolean isValid(EditText et){

		return pattern.matcher(et.getText())
		              .matches();
	}

}
