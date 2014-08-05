package com.diary.goal.setting.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.R;
import com.diary.goal.setting.database.DiaryHelper.DiaryTemplateModel;
import com.diary.goal.setting.tools.Constant;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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
	private boolean canEdit=true;//模板是否可编辑
	
	private TemplateEditAction action;
	/**
	 * 
	 * item内按钮事件
	 */
	public interface TemplateEditAction{
		void addItem(int group);//添加模板item
		void editItem(int group,int child);//编辑模板item
		void deleteItem(int group,int child);//删除模板item
	}
	
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
				if(dataModel._SYNC.equals("-1"))
					canEdit=false;
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
		
	}
	
	/**
	 * 判断是否可以编辑
	 * @return
	 */
	public boolean editable(){
		return canEdit;
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
		try {
			return tempMainTitle.getString(groupPosition);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		try {
			return tempContent.getJSONArray(tempMainTitle.getString(groupPosition)).getString(childPosition);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			holder.subNum=(TextView)convertView.findViewById(R.id.template_sub_num);
			convertView.setTag(holder);
		}
		MainViewHolder holder=(MainViewHolder)convertView.getTag();
		try {
			final int groupP=groupPosition;
			if(isExpanded||getChildrenCount(groupPosition)==0){
				holder.add.setVisibility(View.VISIBLE);
				holder.subNum.setVisibility(View.INVISIBLE);
			}else{
				holder.add.setVisibility(View.INVISIBLE);
				holder.subNum.setVisibility(View.VISIBLE);
				holder.subNum.setText("("+getChildrenCount(groupPosition)+")");
			}
			//不可编辑状态
			if(!canEdit){
				holder.add.setVisibility(View.INVISIBLE);
				holder.subNum.setVisibility(View.VISIBLE);
				holder.subNum.setText("("+getChildrenCount(groupPosition)+")");
			}
			holder.add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (action!=null){
						action.addItem(groupP);
					}
					
				}
			});
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
			holder.delButton=(ImageView)convertView.findViewById(R.id.del_sub_title);
			holder.eraseLine=(View)convertView.findViewById(R.id.erase_line);
			convertView.setTag(holder);
		}
		SubViewHolder holder=(SubViewHolder)convertView.getTag();
		try {
			final int groupP=groupPosition,childP=childPosition ;
			holder.delButton.setVisibility(View.INVISIBLE);
			holder.eraseLine.setVisibility(View.INVISIBLE);
			if(!canEdit)
				holder.edit.setVisibility(View.INVISIBLE);
			holder.edit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (action!=null){
						action.editItem(groupP, childP);
					}
					
				}
			});
			holder.title.setText(tempContent.getJSONArray(tempMainTitle.getString(groupPosition)).getString(childPosition));
			
			holder.delButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (action!=null){
						action.deleteItem(groupP, childP);
					}
					
				}
			});
			if(canEdit){
				convertView.setOnLongClickListener(new View.OnLongClickListener() {
	
					@Override
					public boolean onLongClick(View v) {
						SubViewHolder holder=(SubViewHolder)v.getTag();
						holder.delButton.setVisibility(View.VISIBLE);
						holder.eraseLine.setVisibility(View.VISIBLE);
						holder.edit.setVisibility(View.INVISIBLE);
						return false;
					}
				});
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public void setAction(TemplateEditAction action) {
		this.action = action;
	}

	public DiaryTemplateModel getDataModel(){
		return dataModel;
	}
	public JSONObject getTempJson(){
		return tempContent;
	}
	/**
	 * 主标题                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
	 */
	class MainViewHolder{
		TextView title;
		ImageView add;
		TextView subNum;
	}
	/**
	 * 副标题
	 */
	class SubViewHolder{
		TextView title;
		ImageView edit;
		ImageView delButton;
		View eraseLine;
	}
}
