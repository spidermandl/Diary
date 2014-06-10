package com.kemallette.RichEditTextDemo.Activity;

import java.util.regex.Pattern;


import com.kemallette.RichEditText.Validations.PatternValidator;

public class MsgValidator extends PatternValidator {
    /** 
     * ^({[^{]+})\s*   ( (\[([^\[\]])+\] \s* [^{}\[\]]*  )*  \s* ({[^{]+})? \s* )*
     *    开头大标题                                                     小标题                                          正文部分                                                       开头大标题
     * @param _customErrorMessage
     * @param _pattern
     */
	
	/**
	 * 大标题匹配
	 */
	public final String BIG_TITLE_PAT_TERM="\\{[^\\{]+\\})\\s*";
	/**
	 * 小标题匹配
	 */
	public final String SMALL_TITLE_PAT_TERM="\\[([^\\[\\]])+\\]\\s*";
	/**
	 * 正文匹配
	 */
	public final String MAIN_MSG="[^\\{\\}\\[\\]]*";
	
	public MsgValidator(String _customErrorMessage, Pattern _pattern) {
		super(_customErrorMessage, _pattern);
	}
	
	static public Pattern getDefaultPattern(){
		return Pattern.compile("^(\\{[^\\{]+\\})\\s*((\\[([^\\[\\]])+\\]\\s* [^\\{\\}\\[\\]]*)*\\s*(\\{[^\\{]+\\})?\\s*)*");
	}
	

}
