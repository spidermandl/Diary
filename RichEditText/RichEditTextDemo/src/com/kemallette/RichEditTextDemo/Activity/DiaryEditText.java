package com.kemallette.RichEditTextDemo.Activity;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.Log;

import com.kemallette.RichEditText.Text.ISpan;
import com.kemallette.RichEditText.Text.SpanUtil;
import com.kemallette.RichEditText.Widget.RichEditText;

public class DiaryEditText extends RichEditText {

	private static final int SUB_TITLE=1;//标题模式
	private static final int PLAIN_TEXT=0;//正文模式
	private static final int INVALID_POS=-1;//无效模式
	/**
	 * 字符状态数组，标示每个输入字符的状态
	 * 1 标题位置
	 * 0 正文位置
	 * -1 无效位置
	 */
	private ArrayList<Integer> editState=new ArrayList<Integer>();
	private ArrayList<ISpan> editSpans=new ArrayList<ISpan>();
	
	public DiaryEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public DiaryEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public DiaryEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/** calling sequence
	 * 
	 	beforeReplace
        onTextActionCursorMove
        onBeforeCompose
        beforeTextChanged
        onTextChanged
        afterTextChanged
        afterReplace
        onTextActionCursorMove
        onAfterCompose
        
        beforeReplace
        onTextActionCursorMove
        onBeforeDelete
        beforeTextChanged
        onTextChanged
        afterTextChanged
        afterReplace
        onTextActionCursorMove
        onAfterDelete
        
        beforeReplace
		onTextActionCursorMove
		onBeforeAppend
		beforeTextChanged
		onTextChanged
		afterTextChanged
		afterReplace
		onTextActionCursorMove
		onAfterAppend
	 */

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		Log.e("beforeTextChanged", "beforeTextChanged");
		super.beforeTextChanged(s, start, count, after);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.e("onTextChanged", "onTextChanged");
		super.onTextChanged(s, start, before, count);
	}

	@Override
	public void afterTextChanged(Editable s) {
		Log.e("afterTextChanged", "afterTextChanged");
		//s.clearSpans();
		super.afterTextChanged(s);
	}

	@Override
	public void onBeforeCompose(int start, int end, CharSequence repText,
			int repStart, int repEnd){
		Log.e("onBeforeCompose", "onBeforeCompose:"+start+" end:"+end+" repStart:"+repStart+" repEnd:"+repEnd);
	}

	@Override
	public void onAfterCompose(int start, int end, CharSequence repText,
			int repStart, int repEnd){
		Log.e("onAfterCompose", "onAfterCompose:"+start+" end:"+end+" repStart:"+repStart+" repEnd:"+repEnd+"-------------------"+repText);
		
		paintAnalysis(start, end, repText,repStart,repEnd);
		
	}

	@Override
	public void onBeforeInsert(int position, CharSequence repText,
			int repStart, int repEnd){
		Log.e("onBeforeInsert", "onBeforeInsert");
	}

	@Override
	public void onAfterInsert(int position, CharSequence repText, int repStart,
			int repEnd){
		Log.e("onAfterInsert", "onAfterInsert");
		paintAnalysis(position, position, repText,repStart,repEnd);
	}

	@Override
	public void onBeforeAppend(int position, CharSequence repText,
			int repStart, int repEnd){
		Log.e("onBeforeAppend", "onBeforeAppend");
	}

	@Override
	public void onAfterAppend(int position, CharSequence repText, int repStart,
			int repEnd){
		//applySpan(BOLD, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		Log.e("onAfterAppend", "onAfterAppend repStart:"+repStart+" repEnd:"+repEnd);
		paintAnalysis(position, position, repText,repStart,repEnd);
	}

	@Override
	public void onBeforeDelete(int start, int end){
		Log.e("onBeforeDelete", "onBeforeDelete:"+start+" end:"+end);
	}

	@Override
	public void onAfterDelete(int start, int end){
		Log.e("onAfterDelete", "onAfterDelete start:"+start+" end:"+end);
		paintAnalysis(start, end, "",0,0);
	}

	@Override
	public void onTextActionCursorMove(int position){
		Log.e("onTextActionCursorMove", "onTextActionCursorMove");
	}

	@Override
	public void beforeReplace(final int start, final int end,
			final CharSequence repText, final int repStart, final int repEnd){
		Log.e("beforeReplace", "beforeReplace:"+start+" end:"+end+" repStart:"+repStart+" repEnd:"+repEnd);
		removeSpans();
	}

	@Override
	public void afterReplace(final int start, final int end,
			final CharSequence repText, final int repStart, final int repEnd){
		Log.e("afterReplace", "afterReplace start:"+start+" end:"+end+" repStart:"+repStart+" repEnd:"+repEnd);

	}
	
	
	/**
	 * 重新计算绘制字符串所需的所有span
	 * @param start 替换字符起始位置
	 * @param end 替换字符结束位置
	 * @param repText 替换字符串
	 */
	private void paintAnalysis(int start, int end, CharSequence repText,int repStart,int repEnd){
//		for (int i=repStart+1;i<=repEnd;i++){
//		    ISpan[] repSpans=mEt.getText().getSpans(repStart, i, ISpan.class);
//			for (ISpan span:repSpans){
//				span.removeSpan(mEt.getText());
//			}
//		}
		updateStatus(start, end, repText, repStart, repEnd);
		Log.e("array and text ", "--------------------------------"+mEt.getText().toString());
		String str="";
		for(int i=0;i<editState.size();i++){
			str+=editState.get(i);
		}
		Log.e("array and text ", "-------------------------------:"+str);
		updateTextSpans();

	}
	

	/**
	 * 更新字符串对应的状态数组
	 * @param start 原字符串中的开始位置
	 * @param end 原字符串中的结束位置
	 * @param repText 替换字符串
	 * @param repStart 替换字符串中的开始位置
	 * @param repEnd 替换字符串中的结束位置
	 */
	private void updateStatus(int start, int end, CharSequence repText,int repStart,int repEnd){
		/**
		 * 设置状态数组的容量>=字符串容量
		 */
		Log.e("mET", mEt.getText().toString());
		if(mEt.getText().length()>editState.size()){
			for(int i=editState.size();i<mEt.getText().length();i++){
				editState.add(INVALID_POS);
			}
		}
		/**
		 * 分割原状态，空出repEnd-repStart个位置供repText填入
		 * 多余位置设为无效状态
		 */
		int lastPosition=editState.size()-1;
		while(lastPosition>=0&&editState.get(lastPosition)==INVALID_POS)
			lastPosition--;
		//lastPosition为原字符串最后一个位置
		int distance=repEnd-repStart-end+start;//end位置以后的状态需要移动distance距离
		for(int i=0;i<lastPosition-end+1||i<Math.abs(distance);i++){//end位置后有lastPosition-end个字符
			                                                        //end位置后没有字符，需要移动distance的字符
			if(distance>0){
				//从高位向右移动
				if(lastPosition-i>=0)//越界判断
					editState.set(lastPosition+distance-i,editState.get(lastPosition-i));
			}else{
				//从低位向左移动	
				if(end+distance+i>=0){//越界判断
					if(end+i<editState.size())
					    editState.set(end+distance+i, editState.get(end+i));
					else
						editState.set(end+distance+i, INVALID_POS);
				}
				if(end+i>lastPosition+distance&&end+i<=lastPosition)//最后distance个字符 赋无效值
					editState.set(end+i, INVALID_POS);
			}
		}
		/**
		 * 将repText的状态填入状态数组中
		 */
		setRepTextStatus(start, repText, repStart, repEnd);
		/**
		 * 处理插入字符串后，前方字符串状态变化
		 */
		setBefRepTextStatus(start-1, repText.length()>repStart?repText.charAt(repStart):' ');
		/**
		 * 处理插入字串后，后续字符状态的变化
		 */
		setAftRepTextStatus(start+repEnd-repStart, start+repEnd-repStart-1>=0?mEt.getText().charAt(start+repEnd-repStart-1):' ');
		
	}
	/**
	 * 更新字替换字符串对应的状态
	 * @param position 替换位置在原字符串中的插入位置
	 * @param repText 替换字符串
	 * @param repStart 替换字符串中的开始位置
	 * @param repEnd 替换字符串中的结束位置
	 */
	private void setRepTextStatus(int position, CharSequence repText,int repStart,int repEnd){
		int spanStart=0;//每轮循环repText读取的启始位置
		boolean stop=true;
		while(true){
			if(position+spanStart-1<0||editState.get(position+spanStart-1)==PLAIN_TEXT//纯正文
					||(editState.get(position+spanStart-1)==SUB_TITLE&&mEt.getText().charAt(position+spanStart-1)==']'//最后一个标题字符
					||(editState.get(position+spanStart-1)==SUB_TITLE&&(position+spanStart-2<0||editState.get(position+spanStart-2)==PLAIN_TEXT)&&mEt.getText().charAt(position+spanStart-1)!='[')//第一个正标题 ‘【’符号判断
					)){
				/**
				 * 找下一个小标题起始点
				 */
				boolean inCycle=false;
				for(int i=spanStart;i<repEnd-repStart;i++){
					if(repText.charAt(repStart+i)=='['){
						spanStart=i;
						inCycle=true;
						break;
					}else
						editState.set(position+i, PLAIN_TEXT);
				}
				//没有找到标题起始点
				if(!inCycle){
					return;
				}
			}
			/**
			 * 寻找小标题的终点
			 */
			int tmpStart=spanStart;//计数游标定位
			for (int i=spanStart;i<repEnd-repStart;i++,spanStart++){
				editState.set(position+i, SUB_TITLE);
				if(repText.charAt(repStart+i-tmpStart)==']'){
					/**
					 * 找到小标题的终点
					 */
					if (i<repEnd-repStart-1){
						stop=false;
					}
					break;
				}
			}
			
			if(stop)
				/**
				 * 更新完毕
				 */
				return;
			else{
				/**
				 * 继续循环
				 */
				spanStart+=1;
				//spanStart+=tmpStart;
			}
		}
	}
	/**
	 * 矫正插入子串前字符的状态
	 * @param position 插入字符串前的第一个字符位置
	 * @param rightchar position位置的后一个字符
	 */
	private void setBefRepTextStatus(int position,char rightChar){
		if(position>=0&&editState.get(position)==SUB_TITLE&&rightChar=='['){
			int i=position;
			boolean reverse=false;
			while(i>=0&&i<=position){
				if(mEt.getText().charAt(i)==']'&&!reverse){
					break;
				}else if(mEt.getText().charAt(i)=='['||reverse){
					reverse=true;
					editState.set(i, SUB_TITLE);
					i++;
				}else{
				    editState.set(i, PLAIN_TEXT);
				    i--;
				}
			}
			
		}
	}
	/**
	 * 矫正插入子串后字符的状态
	 * @param position 插入字符串后的第一个字符位置
	 * @param leftchar position位置的前一个字符
	 */
	private void setAftRepTextStatus(int position,char leftChar){
		if(((position-1)<0||editState.get(position-1)==PLAIN_TEXT||leftChar==']') && 
				position<editState.size()&&position>=0&&editState.get(position)==SUB_TITLE){
			/**
			 * position前一个字符为正文模式或']'，原字符截断处右半部分第一个字符为小标题模式
			 */
			int i=0;
			while(position+i<editState.size()&&
				  editState.get(position+i)!=INVALID_POS&&
				  mEt.getText().charAt(position+i)!='['){
				editState.set(position+i, PLAIN_TEXT);
				i++;
			}
		}else if((position-1)>=0&&editState.get(position-1)==SUB_TITLE&&leftChar!=']' && 
				position<editState.size()&&(position<0||editState.get(position)==PLAIN_TEXT)){
			/**
			 * position前一个字符为为小标题模式，原字符截断处右半部分第一个字符为正文模式
			 */
			int i=0;
			while(position+i<editState.size()&&
				  editState.get(position+i)!=INVALID_POS){
				editState.set(position+i, SUB_TITLE);
				if(mEt.getText().charAt(position+i)==']')//']'符号也需要高亮
					break;
				i++;
			}
		}else if (mEt.getText().length()==0){
			/**
			 * 字符被全部清除
			 */
			for (int i=0;i<editState.size();i++){
				if(editState.get(i)!=INVALID_POS)
					editState.set(i, INVALID_POS);
				else
					break;
			}
			
		}
	}
	/**
	 * 根据editState状态数组，更新所有文本span区间
	 */
	private void updateTextSpans(){
		int start=0,end=0;
		int status=editState.get(0);
		int lastPosition=mEt.getText().length()-1;
		/**
		 * 更新editSpans
		 */
		for(int i=0;i<=lastPosition;i++){
			if(status==editState.get(i)&&i!=lastPosition){
				end++;
			}else{
				ISpan span;
				if(status!=editState.get(i)&&i==lastPosition){//最后一个字符，且状态发生改变
					//span=makeSpan(editState.get(i)==SUB_TITLE?BOLD:NORMAL, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					span=editState.get(i)==SUB_TITLE?makeSpan(BOLD, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE):
						makeSpan(NORMAL, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					if(span!=null){
			    		editSpans.add(span);
			    		SpanUtil.reApplySpan(span, mEt.getText());
			    	}
				}
				span=null;
				if (status==editState.get(i)&&i==lastPosition){//最后一个字符，状态没有发生改变
					//span=makeSpan(editState.get(end)==SUB_TITLE?BOLD:NORMAL, start, end+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					span=editState.get(end)==SUB_TITLE?makeSpan(BOLD, start, end+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE):
						makeSpan(NORMAL, start, end+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}else if(status!=editState.get(i)){//字符状态发生改变
					//span=makeSpan(editState.get(end-1)==SUB_TITLE?BOLD:NORMAL, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					span=editState.get(end-1)==SUB_TITLE?makeSpan(BOLD, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE):
						makeSpan(NORMAL, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					start=end;
				}
				
		    	if(span!=null){
		    		editSpans.add(span);
		    		SpanUtil.reApplySpan(span, mEt.getText());
			    	end++;
			    	status=editState.get(i);
		    	}
			}
		}
	}
	/**
	 * 移除字符串所有格式
	 */
	private void removeSpans(){
		for(ISpan span:editSpans){
			span.removeSpan(mEt.getText());
		}
		editSpans.clear();
	}
	class EditSpan {
		int start;
		int end;
		ArrayList<ISpan> appliedSpans=new ArrayList<ISpan>();
		int type=PLAIN_TEXT;//
	}

}
