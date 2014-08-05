package com.diary.goal.setting.richtext;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.R;
import com.diary.goal.setting.tools.Constant;
import com.kemallette.RichEditText.Text.BulletListSpan;
import com.kemallette.RichEditText.Text.FontSpan;
import com.kemallette.RichEditText.Text.ISpan;
import com.kemallette.RichEditText.Text.ImgSpan;
import com.kemallette.RichEditText.Text.RichTextStringBuilder;
import com.kemallette.RichEditText.Text.SizeSpan;
import com.kemallette.RichEditText.Text.SpanTypes;
import com.kemallette.RichEditText.Text.SpanUtil;
import com.kemallette.RichEditText.Text.StrikeSpan;
import com.kemallette.RichEditText.Text.TextBackgroundColorSpan;
import com.kemallette.RichEditText.Text.TextForgroundColorSpan;
import com.kemallette.RichEditText.Text.TextStyleSpan;
import com.kemallette.RichEditText.Text.TextSubscriptSpan;
import com.kemallette.RichEditText.Text.TextSuperscriptSpan;
import com.kemallette.RichEditText.Text.UnderliningSpan;
import com.kemallette.RichEditText.Validations.Validator;
import com.kemallette.RichEditText.Widget.EditTextValidator;
import com.kemallette.RichEditText.Widget.RichEditTextField;
import com.kemallette.RichEditText.Widget.RichTextWatcher;
import com.kemallette.RichEditText.Widget.SelectionChangeListener;
import com.kemallette.RichEditText.Widget.WidgetUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.widget.EditText;
/**
 * 正则判断编辑框
 * @author desmond.duan
 * 
 *
 */
public class RichTextEditView extends RichEditTextField implements
		RichTextWatcher, TextWatcher, SelectionChangeListener, SpanTypes,
		EditorActionModeListener {

	// Replaces the default edit text factory that produces editables
	// which our custom editable and listener
	public class TextFactory extends Editable.Factory {

		@Override
		public Editable newEditable(final CharSequence source) {

			return new RichTextStringBuilder(source, mSpanWatcher);
		}

	}

	protected static final String TAG = "RichEditText";
	protected float density = 1.5f;
	protected EditTextValidator editTextValidator;
	protected final RichTextWatcher mSpanWatcher = this;
	protected Drawable mDrawable;
	protected int fgColor = android.R.color.transparent,
			bgColor = Color.LTGRAY;
	protected String mFontFamily;
	protected int bulletColor = android.R.color.transparent;
	protected int bulletMarginWidth = 5;
	protected boolean isTextBeSet = false;// 字符串为非手写输入
	protected Context context;
	
	protected String oldText;//记录上次被保存的字串

	private static final int SUB_TITLE = 1;// 标题模式
	private static final int PLAIN_TEXT = 0;// 正文模式
	private static final int ERROR_TEXT =2;//语法错误文本背景
	private static final int INVALID_POS = -1;// 无效模式
	private static final int COMBINING_SPAN_NUM = 2;// span组合数量

	public static final int SUBTITLE_SIZE = 90;// 小标题尺寸
	public static final int SUBTITLE_COLOR = 100;// 小标题颜色
	public static final int TEXT_SIZE = 110;// 正文尺寸
	public static final int TEXT_COLOR = 120;// 正文颜色
	public static final int ERROR_BACKGROUND_COLOR=130;//

	private static final char SUB_TITLE_LEFT = '[';
	private static final char SUB_TITLE_RIGHT = ']';
	/**
	 * 字符状态数组，标示每个输入字符的状态 1 标题位置 0 正文位置 -1 无效位置
	 */
	private ArrayList<Integer> editState = new ArrayList<Integer>();
	private ArrayList<ISpan> editSpans = new ArrayList<ISpan>();

	public RichTextEditView(Context context) {

		this(context, null);

		throw new RuntimeException(
				"This constructor isn't supported. Surely you have SOME attributes?");
	}

	public RichTextEditView(Context context, AttributeSet attrs) {

		this(context, attrs, -1);
		

	}

	public RichTextEditView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

		if (!isInEditMode()) {
			editTextValidator = new DiaryEditTextValidator(this, attrs,
					context);
			density = WidgetUtil.getScreenDensity(getContext());

			initViews(attrs);
			initAttributes(attrs);
		}
	}

	protected void initViews(AttributeSet attrs) {

		setEditTextValidator(new DiaryEditTextValidator(this, attrs,
				getContext()));

		this.requestFocus();
	}

	protected void initAttributes(AttributeSet attrs) {

		this.setEditableFactory(new TextFactory());
		this.addTextChangedListener(this);
		this.setOnSelectionChangedListener(this);

	}

	/**
	 * Add a validator to this RichEditText. The validator will be added in the
	 * queue of the current validators.
	 * 
	 * @param theValidator
	 * @throws IllegalArgumentException
	 *             if the validator is null
	 */
	public void addValidator(Validator theValidator)
			throws IllegalArgumentException {

		editTextValidator.addValidator(theValidator);
	}

	public EditTextValidator getEditTextValidator() {

		return editTextValidator;
	}

	public void setEditTextValidator(EditTextValidator editTextValidator) {

		this.editTextValidator = editTextValidator;
	}
	
	/**
	 * Calling *isValid()* will cause the EditText to go through
	 * customValidators and call {@link #Validator.isValid(EditText)}
	 * 
	 * @return true if the validity passes false otherwise.
	 */
	public boolean isValid() {

		return editTextValidator.isValid();
	}

	@Override
	public void onSpanAdded(Spannable text, Object what, int start, int end) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpanRemoved(Spannable text, Object what, int start, int end) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpanChanged(Spannable text, Object what, int ostart,
			int oend, int nstart, int nend) {
		// TODO Auto-generated method stub

	}

	/**
	 * calling sequence beforeReplace onTextActionCursorMove onBeforeCompose
	 * beforeTextChanged onTextChanged afterTextChanged afterReplace
	 * onTextActionCursorMove onAfterCompose beforeReplace
	 * onTextActionCursorMove onBeforeDelete beforeTextChanged onTextChanged
	 * afterTextChanged afterReplace onTextActionCursorMove onAfterDelete
	 * beforeReplace onTextActionCursorMove onBeforeAppend beforeTextChanged
	 * onTextChanged afterTextChanged afterReplace onTextActionCursorMove
	 * onAfterAppend
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		//Log.e("beforeTextChanged", "beforeTextChanged");
	}

	@Override
	public void afterTextChanged(Editable s) {
		//Log.e("afterTextChanged", "afterTextChanged");

	}

	@Override
	public void onBeforeCompose(int start, int end, CharSequence repText,
			int repStart, int repEnd) {
		//Log.e("onBeforeCompose", "onBeforeCompose:" + start + " end:" + end + " repStart:" + repStart + " repEnd:" + repEnd);
	}

	@Override
	public void onEditorAction(int actionCode) {
		// TODO Auto-generated method stub
		super.onEditorAction(actionCode);
	}
	
	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
	
	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
		// TODO Auto-generated method stub
		//super.onCreateContextMenu(menu);
	}
	@Override
	public void onSelectionChanged(int start, int end) {
		// TODO Auto-generated method stub
		// super.onSelectionChanged(start, end);
         
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (forceActionMode && mainMode != null){// && start != end) {
				postDelayed(new Runnable() {
					@TargetApi(Build.VERSION_CODES.HONEYCOMB)
					public void run() {
						if (!actionModeIsShowing) {
							startActionMode(mainMode);
						}
					}
				}, 500);
			}
		} else {
			if (sherlockEntryMode != null) {
				if (start == end) {
					if (sherlockActionMode != null) {
						sherlockActionMode.finish();
						sherlockActionMode = null;
					}
				} else {
					showSherlockEntryMode();
				}
			}
		}
	}

	@Override
	public void onTextChanged(CharSequence text, int start, int lengthBefore,
			int lengthAfter) {
		if (isTextBeSet && text.length() > 0) {
			Log.e("text is be set", "text is be set");
			paintAnalysis(0, 0, text, 0, text.length());
			isTextBeSet = false;
		}
	}

	@Override
	public void onAfterCompose(int start, int end, CharSequence repText,
			int repStart, int repEnd) {
		//Log.e("onAfterCompose", "onAfterCompose:" + start + " end:" + end + " repStart:" + repStart + " repEnd:" + repEnd + "-------------------" + repText);

		paintAnalysis(start, end, repText, repStart, repEnd);

	}

	@Override
	public void onBeforeInsert(int position, CharSequence repText,
			int repStart, int repEnd) {
		//Log.e("onBeforeInsert", "onBeforeInsert");
	}

	@Override
	public void onAfterInsert(int position, CharSequence repText, int repStart,
			int repEnd) {
		//Log.e("onAfterInsert", "onAfterInsert");
		paintAnalysis(position, position, repText, repStart, repEnd);
	}

	@Override
	public void onBeforeAppend(int position, CharSequence repText,
			int repStart, int repEnd) {
		//Log.e("onBeforeAppend", "onBeforeAppend");
	}

	@Override
	public void onAfterAppend(int position, CharSequence repText, int repStart,
			int repEnd) {
		// applySpan(BOLD, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//Log.e("onAfterAppend", "onAfterAppend repStart:" + repStart + " repEnd:" + repEnd);
		paintAnalysis(position, position, repText, repStart, repEnd);
	}

	@Override
	public void onBeforeDelete(int start, int end) {
		//Log.e("onBeforeDelete", "onBeforeDelete:" + start + " end:" + end);
	}

	@Override
	public void onAfterDelete(int start, int end) {
		//Log.e("onAfterDelete", "onAfterDelete start:" + start + " end:" + end);
		paintAnalysis(start, end, "", 0, 0);
	}

	@Override
	public void onTextActionCursorMove(int position) {
		//Log.e("onTextActionCursorMove", "onTextActionCursorMove");
	}

	@Override
	public void beforeReplace(final int start, final int end,
			final CharSequence repText, final int repStart, final int repEnd) {
		//Log.e("beforeReplace", "beforeReplace:" + start + " end:" + end + " repStart:" + repStart + " repEnd:" + repEnd);
		removeSpans();
	}

	@Override
	public void afterReplace(final int start, final int end,
			final CharSequence repText, final int repStart, final int repEnd) {
		//Log.e("afterReplace", "afterReplace start:" + start + " end:" + end + " repStart:" + repStart + " repEnd:" + repEnd);

	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		isTextBeSet = true;
		oldText=text.toString();
		super.setText(text, type);
	}

	/**
	 * 重新计算绘制字符串所需的所有span
	 * 
	 * @param start
	 *            替换字符起始位置
	 * @param end
	 *            替换字符结束位置
	 * @param repText
	 *            替换字符串
	 */
	private void paintAnalysis(int start, int end, CharSequence repText,
			int repStart, int repEnd) {
//        if(repText!=null&&repText.length()>0&&repText instanceof Editable){
//			Editable reptEditable=(Editable)repText;
//			//reptEditable.clearSpans();
////			ISpan[] spans=createFixedStyleSpans(PLAIN_TEXT, repStart, repEnd);
////			if (spans != null) {
////				for (ISpan span : spans) {
////					editSpans.add(span);
////					SpanUtil.reApplySpan(span, reptEditable);
////				}
////			}
//        }
		//SpanUtil.reApplySpan(makeSpan(TEXT_COLOR, start+repStart, start+repEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE),this.getEditableText());
		//SpanUtil.reApplySpan(makeSpan(NORMAL, start+repStart, start+repEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE), this.getEditableText());
		
		updateStatus(start, end, repText, repStart, repEnd);
		Log.e("array and text ", "--------------------------------" + this.getText().toString());
		String str = "";
		for (int i = 0; i < editState.size(); i++) {
			str += editState.get(i);
		}
		Log.e("array and text ", "-------------------------------:" + str);
		updateTextSpans();

	}

	/**
	 * 更新字符串对应的状态数组
	 * 
	 * @param start
	 *            原字符串中的开始位置
	 * @param end
	 *            原字符串中的结束位置
	 * @param repText
	 *            替换字符串
	 * @param repStart
	 *            替换字符串中的开始位置
	 * @param repEnd
	 *            替换字符串中的结束位置
	 */
	private void updateStatus(int start, int end, CharSequence repText,
			int repStart, int repEnd) {
		/**
		 * 设置状态数组的容量>=字符串容量
		 */
		Log.e("mET", this.getText().toString());
		if (this.getText().length() > editState.size()) {
			for (int i = editState.size(); i < this.getText().length(); i++) {
				editState.add(INVALID_POS);
			}
		}
		/**
		 * 分割原状态，空出repEnd-repStart个位置供repText填入 多余位置设为无效状态
		 */
		int lastPosition = editState.size() - 1;
		while (lastPosition >= 0 && editState.get(lastPosition) == INVALID_POS)
			lastPosition--;
		// lastPosition为原字符串最后一个位置
		int distance = repEnd - repStart - end + start;// end位置以后的状态需要移动distance距离
		for (int i = 0; i < lastPosition - end + 1 || i < Math.abs(distance); i++) {// end位置后有lastPosition-end个字符
																					// end位置后没有字符，需要移动distance的字符
			if (distance > 0) {
				// 从高位向右移动
				if (lastPosition - i >= 0)// 越界判断
					if (lastPosition + distance - i < editState.size()){//可能出现越界
						editState.set(lastPosition + distance - i,editState.get(lastPosition - i));
					}
			} else {
				// 从低位向左移动
				if (end + distance + i >= 0) {// 越界判断
					if (end + i < editState.size())
						editState.set(end + distance + i,
								editState.get(end + i));
					else
						editState.set(end + distance + i, INVALID_POS);
				}
				if (end + i > lastPosition + distance
						&& end + i <= lastPosition)// 最后distance个字符 赋无效值
					editState.set(end + i, INVALID_POS);

			}
		}
		/**
		 * 将repText的状态填入状态数组中
		 */
		setRepTextStatus(start, repText, repStart, repEnd);
		/**
		 * 处理插入字符串后，前方字符串状态变化
		 */
		setBefRepTextStatus(start - 1,
				repText.length() > repStart ? repText.charAt(repStart) : ' ');
		/**
		 * 处理插入字串后，后续字符状态的变化
		 */
		setAftRepTextStatus(
				start + repEnd - repStart,
				start + repEnd - repStart - 1 >= 0 ? this.getText().charAt(
						start + repEnd - repStart - 1) : ' ');

	}

	/**
	 * 更新字替换字符串对应的状态
	 * 
	 * @param position
	 *            替换位置在原字符串中的插入位置
	 * @param repText
	 *            替换字符串
	 * @param repStart
	 *            替换字符串中的开始位置
	 * @param repEnd
	 *            替换字符串中的结束位置
	 */
	private void setRepTextStatus(int position, CharSequence repText,
			int repStart, int repEnd) {
		int spanStart = 0;// 每轮循环repText读取的启始位置
		boolean stop = true;
		Log.e("setRepTextStatus", repText.toString());
		Log.e("setRepTextStatus editState size", editState.size()+" "+position);
		while (true) {
			if ((position + spanStart - 1 < 0)
					|| editState.get(position + spanStart - 1) == PLAIN_TEXT// 纯正文
					|| (editState.get(position + spanStart - 1) == SUB_TITLE&& this.getText().charAt(position + spanStart - 1) == SUB_TITLE_RIGHT// 最后一个标题字符
					|| (editState.get(position + spanStart - 1) == SUB_TITLE&& (position + spanStart - 2 < 0 || editState.get(position + spanStart - 2) == PLAIN_TEXT) && this.getText().charAt(position + spanStart - 1) != SUB_TITLE_LEFT)// 第一个正标题
																							                                                                                                                                                // ‘【’符号判断
					)) {
				/**
				 * 找下一个小标题起始点
				 */
				boolean inCycle = false;
				for (int i = spanStart; i < repEnd - repStart; i++) {
					if (repText.charAt(repStart + i) == SUB_TITLE_LEFT) {
						spanStart = i;
						inCycle = true;
						break;
					} else
						editState.set(position + i, PLAIN_TEXT);
				}
				// 没有找到标题起始点
				if (!inCycle) {
					return;
				}
			}
			/**
			 * 寻找小标题的终点
			 */
			for (int i = spanStart; i < repEnd - repStart; i++, spanStart++) {
				editState.set(position + i, SUB_TITLE);
				if (repText.charAt(repStart + i) == SUB_TITLE_RIGHT) {
					/**
					 * 找到小标题的终点
					 */
					if (i < repEnd - repStart - 1) {
						stop = false;
					}
					break;
				}
			}

			if (stop)
				/**
				 * 更新完毕
				 */
				return;
			else {
				/**
				 * 继续循环
				 */
				spanStart += 1;
				if(position + spanStart - 1>=this.getText().length()){//矫正spanStart
					return;
				}
				// spanStart+=tmpStart;
			}
		}
	}

	/**
	 * 矫正插入子串前字符的状态
	 * 
	 * @param position
	 *            插入字符串前的第一个字符位置
	 * @param rightchar
	 *            position位置的后一个字符
	 */
	private void setBefRepTextStatus(int position, char rightChar) {
		if (position >= 0 && editState.get(position) == SUB_TITLE
				&& rightChar == SUB_TITLE_LEFT) {
			int i = position;
			boolean reverse = false;
			while (i >= 0 && i <= position) {
				if (this.getText().charAt(i) == SUB_TITLE_RIGHT && !reverse) {
					break;
				} else if (this.getText().charAt(i) == SUB_TITLE_LEFT
						|| reverse) {
					reverse = true;
					editState.set(i, SUB_TITLE);
					i++;
				} else {
					editState.set(i, PLAIN_TEXT);
					i--;
				}
			}

		}
	}

	/**
	 * 矫正插入子串后字符的状态
	 * 
	 * @param position
	 *            插入字符串后的第一个字符位置
	 * @param leftchar
	 *            position位置的前一个字符
	 */
	private void setAftRepTextStatus(int position, char leftChar) {
		if (((position - 1) < 0 || editState.get(position - 1) == PLAIN_TEXT || leftChar == SUB_TITLE_RIGHT)
				&& position < editState.size()
				&& position >= 0
				&& editState.get(position) == SUB_TITLE) {
			/**
			 * position前一个字符为正文模式或']'，原字符截断处右半部分第一个字符为小标题模式
			 */
			int i = 0;
			while (position + i < editState.size()
					&& editState.get(position + i) != INVALID_POS
					&& this.getText().charAt(position + i) != SUB_TITLE_LEFT) {
				editState.set(position + i, PLAIN_TEXT);
				i++;
			}
		} else if ((position - 1) >= 0
				&& editState.get(position - 1) == SUB_TITLE
				&& leftChar != SUB_TITLE_RIGHT && position < editState.size()
				&& (position < 0 || editState.get(position) == PLAIN_TEXT)) {
			/**
			 * position前一个字符为为小标题模式，原字符截断处右半部分第一个字符为正文模式
			 */
			int i = 0;
			while (position + i < editState.size()
					&& editState.get(position + i) != INVALID_POS) {
				editState.set(position + i, SUB_TITLE);
				if (this.getText().charAt(position + i) == SUB_TITLE_RIGHT)// ']'符号也需要高亮
					break;
				i++;
			}
		} else if (this.getText().length() == 0) {
			/**
			 * 字符被全部清除
			 */
			for (int i = 0; i < editState.size(); i++) {
				if (editState.get(i) != INVALID_POS)
					editState.set(i, INVALID_POS);
				else
					break;
			}

		}
	}

	/**
	 * 根据editState状态数组，更新所有文本span区间
	 */
	private void updateTextSpans() {
		int start = 0, end = 0;
		int status = editState.get(0);
		int lastPosition = this.getText().length() - 1;
		/**
		 * 更新editSpans
		 */
		for (int i = 0; i <= lastPosition; i++) {
			if (status == editState.get(i) && i != lastPosition) {
				end++;
			} else {
				ISpan[] spans;
				if (status != editState.get(i) && i == lastPosition) {// 最后一个字符，且状态发生改变
					spans = createFixedStyleSpans(editState.get(i), i, i + 1);
					if (spans != null) {
						for (ISpan span : spans) {
							editSpans.add(span);
							SpanUtil.reApplySpan(span, this.getText());
						}
					}
				}
				spans = null;
				if (status == editState.get(i) && i == lastPosition) {// 最后一个字符，状态没有发生改变
					spans = createFixedStyleSpans(editState.get(end), start, end + 1);
				} else if (status != editState.get(i)) {// 字符状态发生改变
					spans = createFixedStyleSpans(editState.get(end - 1), start, end);
					start = end;
				}

				if (spans != null) {
					for (ISpan span : spans) {
						editSpans.add(span);
						SpanUtil.reApplySpan(span, this.getText());
					}
					end++;
					status = editState.get(i);
				}
			}
		}
	}
	/**
	 * 将正文转成json格式
	 * @return
	 */
	public JSONObject getTextWithJsonFormat(){
		JSONObject result=new JSONObject();
		JSONArray titles=new JSONArray();
		try {
			result.put(Constant.SUB_SEQUENCE_ORDER, titles);
			int i=0;
			String text=this.getText().toString();
			if(text.length()==0)
				return result;
			int titleL=0/*标题开头*/,titleR=0/*标题结尾*/,textL=0/*正文开头*/,textR=0/*正文结尾*/;
			while (i<getText().length()) {
				if(getText().charAt(i)==SUB_TITLE_LEFT){
					if(i!=0){
						textR=i;
						String s=text.substring(titleL, titleR);
						titles.put(s);
						result.put(s, text.subSequence(textL, textR));
					}
					titleL=i+1;
				}else if (getText().charAt(i)==SUB_TITLE_RIGHT){
					titleR=i;
					textL=i+1;
				}
				
				i++;
			}
			/**
			 * 读到最后一个字符，对上一批title text进行处理
			 */
			String s=null;
			if(getText().charAt(i-1)==SUB_TITLE_RIGHT){
				s=text.substring(titleL, i-1);
				titles.put(s);
				result.put(s, "");
			}
			else{
				s=text.substring(titleL,titleR);
				titles.put(s);
				result.put(s, text.subSequence(textL, i));
			}
			
			
			return result;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    /**
     * 为语法错误文字加背景标注
     * @param errorPos
     */
	public void updateErrorSpans(Integer[] errorPos){
		for (int i=0;i<errorPos.length;){
			ISpan[] spans = createFixedStyleSpans(ERROR_TEXT, errorPos[i],errorPos[i + 1]);
			i=i+2;
			if (spans != null) {
				for (ISpan span : spans) {
					if(span!=null){
						editSpans.add(span);
						SpanUtil.reApplySpan(span, this.getText());
					}
				}
			}
		}
	}
	/**
	 * 产生固定样式的字体,如标题样式，正文样式
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	private ISpan[] createFixedStyleSpans(int type, int start, int end) {
		ISpan[] spans = new ISpan[COMBINING_SPAN_NUM];
		switch (type) {
		case SUB_TITLE:
			spans[0] = makeSpan(SUBTITLE_COLOR, start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			// spans[1]=makeSpan(SUBTITLE_SIZE, start, end,
			// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			 spans[1]= makeSpan(BOLD, start, end,
			 Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			break;
		case ERROR_TEXT:
			spans[0] = makeSpan(TEXT_COLOR, start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			spans[1]=makeSpan(ERROR_BACKGROUND_COLOR, start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			break;
		default:
			spans[0] = makeSpan(TEXT_COLOR, start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			// spans[1]=makeSpan(TEXT_SIZE, start, end,
			// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			 spans[1]= makeSpan(NORMAL, start, end,
			 Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			break;
		}

		return spans;
	}

	/**
	 * 移除字符串所有格式
	 */
	private void removeSpans() {

		for (ISpan span : editSpans) {
			if(span!=null)
				span.removeSpan(this.getText());
		}
		editSpans.clear();
	}

	protected ISpan makeSpan(int type, int start, int end, int flag) {

		switch (type) {

		// Appearance Spans
		case NORMAL:
		case BOLD:
		case ITALIC:
		case BOLD_ITALIC:
			return new TextStyleSpan(type, start, end, flag);

		case UNDERLINE:
			return new UnderliningSpan(start, end, flag);

		case STRIKE:
			return new StrikeSpan(start, end, flag);

		case SUPERSCRIPT:
			return new TextSuperscriptSpan(start, end, flag);

		case SUBSCRIPT:
			return new TextSubscriptSpan(start, end, flag);
		case IMAGE:
			return new ImgSpan(start, end, flag, mDrawable);
		case BACKGROUND_COLOR:
			return new TextBackgroundColorSpan(start, end, flag, bgColor);
		case FOREGROUND_COLOR:
			return new TextForgroundColorSpan(start, end, flag, fgColor);
		case FONT:
			return new FontSpan(start, end, flag, mFontFamily);

			// Paragraph Spans
		case BULLET:
			return new BulletListSpan(start, end, density, bulletColor,
					bulletMarginWidth);
			// 小标题样式
		case SUBTITLE_SIZE:
			return new SizeSpan((1.5f), start, end, flag);
		case SUBTITLE_COLOR:
			return new TextForgroundColorSpan(start, end, flag, 0xFF29A0F8);
			// 正文样式
		case TEXT_SIZE:
			return new SizeSpan((1.0f), start, end, flag);
		case TEXT_COLOR:
			return new TextForgroundColorSpan(start, end, flag, 0xFF000000);
		case ERROR_BACKGROUND_COLOR:
			return new TextBackgroundColorSpan(start, end, flag, 0xFFFF0000);//0xFF555555);
		case OL:
			break;
		}

		return null;
	}

	public boolean isDirt() {
		String str=this.getText().toString();
		return !str.equals(oldText);
	}
    /**
     * 标记内容已被保存
     */
	public void reClean() {
		oldText=this.getText().toString();
	}
	
/*************************************************************************************************
 * tool bar part
 *************************************************************************************************/
	private boolean isSelectionChanging = false;
	private boolean actionModeIsShowing = false;
	private EditorActionModeCallback.Native mainMode = null;
	private EditorActionModeCallback.ABS sherlockEntryMode = null;
	private boolean forceActionMode = false;
	private com.actionbarsherlock.view.ActionMode sherlockActionMode = null;
	private boolean keyboardShortcuts = true;

	@Override
	public boolean doAction(int itemId) {
		if (itemId == R.id.cwac_richedittext_underline) {
			// toggleEffect(RichEditText.UNDERLINE);

			return (true);
		} else if (itemId == R.id.cwac_richedittext_strike) {
			// toggleEffect(RichEditText.STRIKETHROUGH);

			return (true);
		} else if (itemId == R.id.cwac_richedittext_superscript) {
			// toggleEffect(RichEditText.SUPERSCRIPT);

			return (true);
		} else if (itemId == R.id.cwac_richedittext_subscript) {
			// toggleEffect(RichEditText.SUBSCRIPT);

			return (true);
		} else if (itemId == R.id.cwac_richedittext_serif) {
			// applyEffect(RichEditText.TYPEFACE, "serif");

			return (true);
		} else if (itemId == R.id.cwac_richedittext_sans) {
			// applyEffect(RichEditText.TYPEFACE, "sans");

			return (true);
		} else if (itemId == R.id.cwac_richedittext_mono) {
			// applyEffect(RichEditText.TYPEFACE, "monospace");

			return (true);
		} else if (itemId == R.id.cwac_richedittext_normal) {
			// applyEffect(RichEditText.LINE_ALIGNMENT,Layout.Alignment.ALIGN_NORMAL);

			return (true);
		} else if (itemId == R.id.cwac_richedittext_center) {
			// applyEffect(RichEditText.LINE_ALIGNMENT,Layout.Alignment.ALIGN_CENTER);

			return (true);
		} else if (itemId == R.id.cwac_richedittext_opposite) {
			// applyEffect(RichEditText.LINE_ALIGNMENT,Layout.Alignment.ALIGN_OPPOSITE);

			return (true);
		} else if (itemId == R.id.cwac_richedittext_bold) {
			// toggleEffect(RichEditText.BOLD);

			return (true);
		} else if (itemId == R.id.cwac_richedittext_italic) {
			// toggleEffect(RichEditText.ITALIC);

			return (true);
		} else if (itemId == android.R.id.selectAll
				|| itemId == android.R.id.cut || itemId == android.R.id.copy
				|| itemId == android.R.id.paste) {
			onTextContextMenuItem(itemId);
		}

		return (true);
	}

	@Override
	public boolean onTextContextMenuItem(int id) {
		// TODO Auto-generated method stub
		return super.onTextContextMenuItem(id);
		//return true;
	}
	@Override
	public void setIsShowing(boolean isShowing) {
		actionModeIsShowing = isShowing;

	}
	private void showSherlockEntryMode() {
		if (!actionModeIsShowing) {
			// nasty reflection hack to get around the fact
			// that there is no inheritance hierarchy for
			// Sherlock*Activity

			Method method;
			try {
				method = getContext().getClass().getMethod("startActionMode",
						com.actionbarsherlock.view.ActionMode.Callback.class);
				sherlockActionMode = (com.actionbarsherlock.view.ActionMode) method
						.invoke(getContext(), sherlockEntryMode);
			} catch (Exception e) {
				Log.e(getClass().getSimpleName(),
						"Exception starting action mode", e);
			}
		}
	}
	public void enableActionModes(boolean forceActionMode) {
		this.forceActionMode = forceActionMode;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			enableNativeActionModes();
		} else {
			enableSherlockActionModes();
		}
	}

	@TargetApi(11)
	private void enableNativeActionModes() {
//		EditorActionModeCallback.Native effectsMode = new EditorActionModeCallback.Native(
//				(Activity) getContext(), R.menu.cwac_richedittext_effects,
//				this, this);
//
//		EditorActionModeCallback.Native fontsMode = new EditorActionModeCallback.Native(
//				(Activity) getContext(), R.menu.cwac_richedittext_fonts, this,
//				this);
//
//		mainMode = new EditorActionModeCallback.Native((Activity) getContext(),
//				R.menu.cwac_richedittext_main, this, this);
//
//		mainMode.addChain(R.id.cwac_richedittext_effects, effectsMode);
//		mainMode.addChain(R.id.cwac_richedittext_fonts, fontsMode);

		EditorActionModeCallback.Native entryMode = new EditorActionModeCallback.Native(
				(Activity) getContext(), R.menu.richedittext_multi_select, this,
				this);

		//entryMode.addChain(R.id.cwac_richedittext_format, mainMode);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setCustomSelectionActionModeCallback(entryMode);
		}
	}

	private void enableSherlockActionModes() {
//		EditorActionModeCallback effectsMode = new EditorActionModeCallback.ABS(
//				(Activity) getContext(), R.menu.cwac_richedittext_effects,
//				this, this);
//
//		EditorActionModeCallback fontsMode = new EditorActionModeCallback.ABS(
//				(Activity) getContext(), R.menu.cwac_richedittext_fonts, this,
//				this);
//
//		EditorActionModeCallback sherlockMainMode = new EditorActionModeCallback.ABS(
//				(Activity) getContext(), R.menu.cwac_richedittext_main, this,
//				this);
//
//		sherlockMainMode.addChain(R.id.cwac_richedittext_effects, effectsMode);
//		sherlockMainMode.addChain(R.id.cwac_richedittext_fonts, fontsMode);

		sherlockEntryMode = new EditorActionModeCallback.ABS(
				(Activity) getContext(), R.menu.cwac_richedittext_entry, this,
				this);

//		sherlockEntryMode.addChain(R.id.cwac_richedittext_format,
//				sherlockMainMode);
	}


}
