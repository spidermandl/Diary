package com.diary.goal.setting.richtext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.widget.EditText;

import com.diary.goal.setting.DiaryApplication;
import com.kemallette.RichEditText.Validations.PatternValidator;

/**
 * 日记语法格式验证器
 * {1}
 *   [1.1]
 *    ---------
 *   [1.2]
 *    --------
 * {2}
 *   [2.1]
 *    --------
 * @author Desmond Duan
 *
 */
public class DiaryValidator extends PatternValidator {
    /** 
     * *s 空白符
     * 
     * ^({[^{]+})\s*   ( (\[([^\[\]])+\] \s* [^{}\[\]]*  )*  \s* ({[^{]+})? \s* )*
     *    开头大标题                        小标题                              正文部分                           开头大标题
     * 
     * @param _customErrorMessage
     * @param _pattern
     */
	
	/**
	 * 大标题匹配
	 */
	public static final String BIG_TITLE_PAT_TERM="\\{[^\\{]+\\})\\s*";
	/**
	 * 小标题匹配
	 */
	public static final String SMALL_TITLE_PAT_TERM="\\[([^\\[\\]])+\\]\\s*";
	/**
	 * 正文匹配
	 */
	public final String MAIN_MSG="[^\\{\\}\\[\\]]*";
	
	public DiaryValidator(String _customErrorMessage, Pattern _pattern) {
		super(_customErrorMessage, _pattern);
	}
	
	@Override
	public boolean isValid(EditText et) {
		// TODO Auto-generated method stub
		boolean correct= super.isValid(et);
		if(this.pattern!=null&&!correct){//将匹配结果分割开
			Matcher match = this.pattern.matcher(et.getText());
			boolean at=match.lookingAt();
			String firstmatch=match.group();
			DiaryApplication.getInstance().setSyntaxError(this.pattern.split(et.getText(),-10));
		}
		return correct;
	}
	
	static public Pattern getDefaultPattern(){
		return Pattern.compile("^(\\{[^\\{]+\\})\\s*((\\[([^\\[\\]])+\\]\\s*[^\\{\\}\\[\\]]*)*\\s*(\\{[^\\{]+\\})?\\s*)*");
	}
	
	static public Pattern getSubTitlePattern(){
		/**
		 * 中间不能有空格
		 */
		return Pattern.compile(
				"(\\s*\\[([^\\[\\]])+\\]\\s*[^{}\\[\\]]*)*"
				);
	}
	

}
