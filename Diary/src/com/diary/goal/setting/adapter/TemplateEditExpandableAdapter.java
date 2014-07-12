package com.diary.goal.setting.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.R;
import com.diary.goal.setting.database.DiaryHelper.DiaryTemplateModel;
import com.diary.goal.setting.tools.Constant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 模版编辑adapter
 * 
 */
public class TemplateEditExpandableAdapter extends BaseExpandableListAdapter{

	Context context;
	private LayoutInflater m_inflater;
	private DiaryTemplateModel dataModel;
	private JSONObject tempContent;//模板全文
	private JSONArray tempMainTitle;//模板大标题
	
	private OnClickListener popTextInputListener;//弹出文字输入框事件
	private AlertDialog.Builder textInputDialog;//文字输入对话框
	private EditText textInput;//输入框
	
	/**
	 * @param con
	 * @param model 数据源
	 */
	public TemplateEditExpandableAdapter(Context con,Parcelable model){
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(model!=null){
			dataModel=(DiaryTemplateModel)model;
			try {
				tempContent=new JSONObject(dataModel._TAMPLETE);
				tempMainTitle=tempContent.getJSONArray(Constant.MAIN_SEQUENCE_ORDER);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		/**
		 * （新建模板）取默认模板
		 */
		if(tempContent==null){
			dataModel=new DiaryTemplateModel();
			try {
				tempContent=new JSONObject(this.context.getString(R.string.default_template));
				tempMainTitle=tempContent.getJSONArray(Constant.MAIN_SEQUENCE_ORDER);
				for(int i=0;i<tempMainTitle.length();i++){
					String key=tempMainTitle.getString(i);
					tempContent.remove(key);
					tempContent.put(key, new JSONArray());
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		/**
		 * 监听事件
		 */
		popTextInputListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.template_add://添加小标题
					textInput=new EditText(context); 
					new AlertDialog.Builder(context)
					.setView(textInput)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							textInput.getEditableText().toString();
						}
					})
					.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					}).show();
					break;
				case R.id.edit_sub_title://编辑小标题
					textInput=new EditText(context); 
					new AlertDialog.Builder(context)
					.setView(textInput)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					})
					.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					}).show();
					break;
					
				default:
					break;
				}
				
			}
		};
		
	}
	
	private void updateTemplate(){
		
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return tempMainTitle.length();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		try {
			return tempContent.getJSONArray(tempMainTitle.getString(groupPosition)).length();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = m_inflater.inflate(R.layout.template_edit_prior_item, null);
			MainViewHolder holder=new MainViewHolder();
			holder.title=(TextView)convertView.findViewById(R.id.template_prior_title);
			holder.add=(ImageView)convertView.findViewById(R.id.template_add);
			convertView.setTag(holder);
		}
		MainViewHolder holder=(MainViewHolder)convertView.getTag();
		try {
			holder.title.setText(tempMainTitle.getString(groupPosition));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView==null){                                                             
			//Log.e("create View", "null "+position);
			convertView = m_inflater.inflate(R.layout.template_edit_sub_item, null);
			SubViewHolder holder=new SubViewHolder();
			holder.title=(TextView)convertView.findViewById(R.id.template_sub_title);
			holder.edit=(ImageView)convertView.findViewById(R.id.edit_sub_title);
			convertView.setTag(holder);
		}
		SubViewHolder holder=(SubViewHolder)convertView.getTag();
		try {
			holder.title.setText(tempContent.getJSONArray(tempMainTitle.getString(groupPosition)).getString(childPosition));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 主标题
	 */
	class MainViewHolder{
		TextView title;
		ImageView add;
	}
	/**
	 * 副标题
	 */
	class SubViewHolder{
		TextView title;
		ImageView edit;
	}
}
