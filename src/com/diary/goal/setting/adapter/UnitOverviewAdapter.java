package com.diary.goal.setting.adapter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.RichTextEditorActivity;
import com.diary.goal.setting.database.DiaryHelper;
import com.diary.goal.setting.database.DiaryHelper.Tables;
import com.diary.goal.setting.listener.OnRatingPentagramTouchUp;
import com.diary.goal.setting.model.CategoryModel;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.tools.BitmapCustomize;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Constant.SudoType;
import com.diary.goal.setting.view.RatingPentagramView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UnitOverviewAdapter extends BaseExpandableListAdapter {

	private Context context;
	private LayoutInflater m_inflater;
	private HashMap<Integer, String> categorys;
	//private HashMap<Integer, String[]>configs;
	private ArrayList<Integer> indexs;
	private HashMap<Integer, CategoryModel> configTables;

	public final static int TYPE_RATING=0;
	public final static int TYPE_CHECKBOX=1;
	public final static int TYPE_CONVENTIONAL_EDIT=2;
	public final static int TYPE_LIST=3;
	
	public UnitOverviewAdapter(Context con) {
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		categorys=new HashMap<Integer, String>();
		indexs=new ArrayList<Integer>();
		loadData();
	}
	private void loadData(){
		DateModel model=DiaryApplication.getInstance().getDateModel();
		configTables=(HashMap<Integer,CategoryModel>)DiaryApplication.getInstance().getTableCacheElement(Tables.DIARY_CONFIG, 0);
		for(Iterator<Integer> keys = configTables.keySet().iterator(); keys.hasNext();){
			int _id=keys.next();
			if(SudoType.getSudoType(configTables.get(_id).getSudoType())==model.getType()){
				indexs.add(_id);
				Cursor c=DiaryApplication.getInstance().getDbHelper().getCategory(model.getDate(),model.getType().getType(),configTables.get(_id).getCategoryIndex());
				if(c!=null&&c.getCount()!=0){
					c.moveToFirst();
					categorys.put(_id,c.getString(1));
				}
				else
					categorys.put(_id, null);
				if(c!=null)
					c.close();
			}
		}
		Collections.sort(indexs, new CategoryPriority());
		
	}
	
	@Override
	public void notifyDataSetChanged() {
		categorys.clear();
		indexs.clear();
		loadData();
		
		DateModel model=DiaryApplication.getInstance().getDateModel();
		boolean isNull=true;
		for(Entry<Integer, String> entry:categorys.entrySet()){
			if(categorys.get(entry.getKey())!=null){
				isNull=false;
				break;
			}
		}
		DiaryApplication.getInstance().getPadStatus().getPadStatus().put(model.getType(), !isNull);
		super.notifyDataSetChanged();
	}
	
	//@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return indexs.size();
	}
	//@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	//@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	//@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	//@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	//@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	//@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	//@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = m_inflater.inflate(R.layout.essay_overview_item, null);
			ViewHolder viewHolder=new ViewHolder();
			viewHolder.showType=TYPE_CHECKBOX;
			viewHolder.type_0=(RelativeLayout)convertView.findViewById(R.id.category_type_0);
			viewHolder.title_type_0=(TextView)convertView.findViewById(R.id.title_type_0);
			viewHolder.ratingStar=(RatingPentagramView)convertView.findViewById(R.id.star_type_0);
			viewHolder.type_1=(RelativeLayout)convertView.findViewById(R.id.category_type_1);
			viewHolder.title_type_1=(TextView)convertView.findViewById(R.id.title_type_1);
			viewHolder.checkBox=(CheckBox)convertView.findViewById(R.id.check_type_1);
			viewHolder.type_2=(RelativeLayout)convertView.findViewById(R.id.category_type_2);
			viewHolder.title_type_2=(TextView)convertView.findViewById(R.id.title_type_2);
			viewHolder.content=(TextView)convertView.findViewById(R.id.content_type_2);
			viewHolder.type_3=(RelativeLayout)convertView.findViewById(R.id.category_type_3);
			viewHolder.title_type_3=(TextView)convertView.findViewById(R.id.title_type_3);
			viewHolder.jump=(ImageView)convertView.findViewById(R.id.expandable_type_3);
			convertView.setTag(viewHolder);
		}
		ViewHolder holder=(ViewHolder)convertView.getTag();
		CategoryModel categoryModel=configTables.get(indexs.get(groupPosition));
		holder.showType=Integer.valueOf(categoryModel.getCategoryType());
		final int index=groupPosition;
		String text;
		switch (holder.showType) {
		case TYPE_RATING:
			holder.title_type_0.setText(switchLanguage(categoryModel.getCategoryName()));
			holder.type_0.setVisibility(View.VISIBLE);
			holder.type_1.setVisibility(View.GONE);
			holder.type_2.setVisibility(View.GONE);
			holder.type_3.setVisibility(View.GONE);

			RatingPentagramView rating=holder.ratingStar;
			text=categorys.get(indexs.get(groupPosition));
			rating.setRate(text!=null?Float.valueOf(text):0f);
			rating.setOnTouchUpListener(new OnRatingPentagramTouchUp() {
				
				public void touchUp(float rating) {
					  DateModel model=DiaryApplication.getInstance().getDateModel();
					  /*******************************set date model*************************************/
					  model.setCategory(configTables.get(indexs.get(index)).getCategoryIndex());
					  model.setConfigId(indexs.get(index));
					  /********************************************************************/
					  DiaryHelper helper=DiaryApplication.getInstance().getDbHelper();
					  Cursor c=helper.getCategory(model);
					  if(c!=null&&c.getCount()!=0)
						  helper.updateDiaryContent(model, String.valueOf(rating));
					  else
						  helper.insertDiaryContent(model, String.valueOf(rating));
					  if(c!=null)
						  c.close();
					  DiaryApplication.getInstance().getPadStatus().getPadStatus().put(model.getType(), true);
					
				}
			});
			break;
		case TYPE_CHECKBOX:
			holder.title_type_1.setText(switchLanguage(categoryModel.getCategoryName()));
			holder.type_0.setVisibility(View.GONE);
			holder.type_1.setVisibility(View.VISIBLE);
			holder.type_2.setVisibility(View.GONE);
			holder.type_3.setVisibility(View.GONE);
			CheckBox choice=holder.checkBox;
			text=categorys.get(indexs.get(groupPosition));
			choice.setChecked(text!=null&&Boolean.valueOf(text));
			choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				//@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					  DateModel model=DiaryApplication.getInstance().getDateModel();
					  /*******************************set date model*************************************/
					  model.setCategory(configTables.get(indexs.get(index)).getCategoryIndex());
					  model.setConfigId(indexs.get(index));
					  /********************************************************************/
					  DiaryHelper helper=DiaryApplication.getInstance().getDbHelper();
					  Cursor c=helper.getCategory(model);
					  if(c!=null&&c.getCount()!=0)
						  helper.updateDiaryContent(model, isChecked?"true":"false");
					  else
						  helper.insertDiaryContent(model, isChecked?"true":"false");
					  if(c!=null)
						  c.close();
					  DiaryApplication.getInstance().getPadStatus().getPadStatus().put(model.getType(), true);
				}
			});
			break;
		case TYPE_CONVENTIONAL_EDIT:
			holder.title_type_2.setText(switchLanguage(categoryModel.getCategoryName()));
			holder.type_0.setVisibility(View.GONE);
			holder.type_1.setVisibility(View.GONE);
			holder.type_2.setVisibility(View.VISIBLE);
			holder.type_3.setVisibility(View.GONE);
			TextView content=holder.content;
//			Bitmap bitmap=BitmapCustomize.customizePicture(context, R.drawable.quote, 0, 0, false);
//			int[] pads = new int[]{content.getPaddingLeft(), content.getPaddingTop(), content.getPaddingRight(), content.getPaddingBottom()};
//			content.setBackgroundDrawable(new NinePatchDrawable(context.getResources(), 
//					bitmap, bitmap.getNinePatchChunk(), new Rect(), null));	
//			content.setPadding(pads[0], pads[1], pads[2], pads[3]);
			text=categorys.get(indexs.get(groupPosition));
			content.setText(text==null?"N/A":switchLanguage(text));
			content.setOnClickListener(new View.OnClickListener() {
				
				//@Override
				public void onClick(View v) {
					DateModel model=DiaryApplication.getInstance().getDateModel();
					/*******************************set date model*************************************/
					model.setCategory(configTables.get(indexs.get(index)).getCategoryIndex());
					model.setText(categorys.get(indexs.get(index)));
					model.setConfigId(indexs.get(index));
					model.setCategory_type(configTables.get(indexs.get(index)).getCategoryType());
					model.setCategory_name(configTables.get(indexs.get(index)).getCategoryName());
					/********************************************************************/
					Intent intent=new Intent();
					intent.setClass(context, RichTextEditorActivity.class);
					((Activity)context).startActivityForResult(intent, 0);
					//context.startActivity(intent);
					
				}
			});
			break;
		case TYPE_LIST:
			holder.title_type_3.setText(switchLanguage(categoryModel.getCategoryName()));
			//holder.title_type_3.setBackgroundResource(R.drawable.group_icon_selector);
			if(isExpanded)
				holder.jump.setBackgroundDrawable(new BitmapDrawable(
						BitmapCustomize.customizePicture(context, R.drawable.jump_down, 0, 0, false)));
			else{
				holder.jump.setBackgroundDrawable(new BitmapDrawable(
						BitmapCustomize.customizePicture(context, R.drawable.jump_right, 0, 0, false)));
			}
			holder.type_0.setVisibility(View.GONE);
			holder.type_1.setVisibility(View.GONE);
			holder.type_2.setVisibility(View.GONE);
			holder.type_3.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}

		return convertView;
	}
	
	//@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	//@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	class ViewHolder{
		int showType;
		RatingPentagramView ratingStar;
		RelativeLayout type_0,type_1,type_2,type_3;
		TextView title_type_0,title_type_1,title_type_2,title_type_3;
		TextView content;
		CheckBox checkBox;
		ImageView jump;
	}
    
	class CategoryPriority implements Comparator<Integer>{

		//@Override
		public int compare(Integer lhs, Integer rhs) {
		
			if(configTables.get(lhs).getCategoryIndex()<configTables.get(rhs).getCategoryIndex())
				return -1;
			else if (configTables.get(lhs).getCategoryIndex()==configTables.get(rhs).getCategoryIndex())
				return 0;
			else
				return 1;
		}
		
	}
	
	private String switchLanguage(String key){
		Integer value=Constant.stringDict.get(key);
		return value==null?key:context.getResources().getString(value);
	}
//	
//	public BitmapHolder BITMAP_FILE(String key)
//	{
//		//先从cache中取，取不到，再从preload中取，再取不到，再加载
//	/*	BitmapCache.BitmapHolder bitmapHolder;
//		
//		bitmapHolder = BitmapCache.getInstance().get(key);
//		if(bitmapHolder != null) return bitmapHolder;
//		
//		bitmapHolder = BitmapCache.getInstance().getPreload(key);
//		if(bitmapHolder != null) return bitmapHolder;
//		
//		//从文件中加载
//		bitmapHolder = loadBitmap(key);
//		if(bitmapHolder == null) return null;
//		
//		BitmapCache.getInstance().put(key,bitmapHolder);
//		return bitmapHolder; */
//		
//		String fullPath = FullFilePath(key);
//		
//		if(fullPath.endsWith(".9.png")) return loadBitmap(key);
//		else
//		{
//			NativeBitmapHolder holder;
//			if(Configuration.GetUseSDCard())
//			{
//				holder = NativeBitmapCache.getInstance().get(fullPath,false);
//			}
//			else
//			{
//				holder = NativeBitmapCache.getInstance().get(fullPath,true);
//			}
//			return new BitmapHolder(holder.b,null);
//		} 
//	}

}
