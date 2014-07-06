package com.diary.goal.setting.adapter;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.database.DiaryHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
/**
 * 
 * 日记模板列表适配器
 *
 */
public class TemplateListAdapter extends BaseAdapter {

	Context context;
	private LayoutInflater m_inflater;
	private DiaryHelper.DiaryTemplateModel[] dataModel;
	
	public TemplateListAdapter(Context con){
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dataModel=DiaryApplication.getInstance().getDbHelper().getFixedDiaryTemplates();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataModel.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataModel[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Integer.parseInt(dataModel[position]._ID);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){                                                             
			//Log.e("create View", "null "+position);
			convertView = m_inflater.inflate(R.layout.template_list_item, null);
			ViewHolder holder=new ViewHolder();
			holder.title=(TextView)convertView.findViewById(R.id.template_title);
			holder.created_at=(TextView)convertView.findViewById(R.id.template_created_date);
			holder.selected=(CheckBox)convertView.findViewById(R.id.template_check_box);
			convertView.setTag(holder);
		}
		ViewHolder holder=(ViewHolder)convertView.getTag();
		holder.title.setText(dataModel[position]._NAME);
		holder.created_at.setText(dataModel[position]._CREATE_TIME);
		return convertView;
	}
	
	class ViewHolder{
		TextView title;
		TextView created_at;
		CheckBox selected;
	}

}
