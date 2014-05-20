package com.diary.goal.setting.richtext;

import java.util.ArrayList;
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
			Matcher matcher = this.pattern.matcher(et.getText());
			ArrayList<Integer> errors=new ArrayList<Integer>();//存储所有不符合语法的字串位置
			int count=0;
			while(matcher.find()) {//遍历所有正确语法的字串
				int start=matcher.start();
				int end=matcher.end();
				if(start==end){
					continue;
				}
				if(count!=0&&start!=0){//非第一次不匹配
					errors.add(start);
					count++;
				}
				if(count==0&&start!=0){//第一次不匹配，且起始位置不匹配
					errors.add(0);
					errors.add(start);
					count=count+2;
				}

				errors.add(end);//记录结束位置，作为下一个不匹配的开始位置
				count++;
				
			}
			if (errors.get(errors.size()-1)==et.getText().length()){//最后一个字符匹配正则表达式
			    errors.remove(errors.size()-1);
			}
			else{
				errors.add(et.getText().length());
			}
			DiaryApplication.getInstance().setSyntaxError(errors.toArray(new Integer[0]));
		}
		return correct;
	}
	/**
	 * 全文匹配正则表达式
	 * @return
	 */
	static public Pattern getDefaultPattern(){
		return Pattern.compile("^(\\{[^\\{]+\\})\\s*((\\[([^\\[\\]])+\\]\\s*[^\\{\\}\\[\\]]*)*\\s*(\\{[^\\{]+\\})?\\s*)*");
	}
	/**
	 * 小标题匹配正则表达式
	 * @return
	 */
	static public Pattern getSubTitlePattern(){
		/**
		 * 中间不能有空格
		 */
		return Pattern.compile(
				"(\\s*\\[([^\\[\\]])+\\]\\s*[^{}\\[\\]]*)*"
				);
	}
	/**
	 * 获取小标题
	 * @return
	 */
	static public Pattern getSubTitle(){
		return Pattern.compile(
				"(?<=\\[)[^\\[\\]]+(?=\\])"
				);
	}
	/**
	 * 获取正文
	 * @return
	 */
	static public Pattern getText(){
		return Pattern.compile(
				"(?<=\\])[^\\[\\]]+(?=\\[)"
				);
	}
	

}
