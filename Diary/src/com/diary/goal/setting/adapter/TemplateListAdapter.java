package com.diary.goal.setting.adapter;

import java.util.ArrayList;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.TemplateOperateActivity;
import com.diary.goal.setting.database.DiaryHelper;
import com.diary.goal.setting.database.DiaryHelper.DiaryTemplateModel;
import com.diary.goal.setting.tools.Constant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
	private int initChoosenPos;//起始选中序号
	private int currentChoosenPos;//当前选中序号
	
	public TemplateListAdapter(Context con){
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dataModel=DiaryApplication.getInstance().getDbHelper().getFixedDiaryTemplates(
				DiaryApplication.getInstance().getMemCache().get(Constant.SERVER_USER_ID));
		int index=0;
		for(DiaryTemplateModel model:dataModel){
			if(model._SELECTED.equals("1")){
				currentChoosenPos=initChoosenPos=index;
				break;
			}
			index++;
		}
	}
	/**
	 * 模板选中项改变
	 * @return
	 */
	public boolean selectionChanged(){
		return !(initChoosenPos==currentChoosenPos);
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
		holder.selected.setChecked(dataModel[position]._SELECTED.equals("0")?false:true);
		final int pos=position;
		holder.selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			/**
			 * 模板单选功能
			 */
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ArrayList<Long> addList=((TemplateOperateActivity)context).getAddIDs();
				if(isChecked){//选中
					for(DiaryTemplateModel model:dataModel){
						if(model._SELECTED.equals("1")){//上一次原先选中的模板
							model._SELECTED="0";
							if(!addList.contains(Long.valueOf(model._ID))&&!model._SYNC.equals("-1")){
								model._SYNC="2";
							}
							DiaryApplication.getInstance().getDbHelper().updateDiaryTemplate(model);
						}
					}
					dataModel[pos]._SELECTED="1";
					currentChoosenPos=pos;
					if(!addList.contains(Long.valueOf(dataModel[pos]._ID))&&!dataModel[pos]._SYNC.equals("-1")){
						dataModel[pos]._SYNC="2";
					}
					DiaryApplication.getInstance().getDbHelper().updateDiaryTemplate(dataModel[pos]);
				}else{

				}
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		TextView title;
		TextView created_at;
		CheckBox selected;
	}

}
