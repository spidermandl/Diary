package com.diary.goal.setting.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.RichTextEditorActivity;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.tools.BitmapCustomize;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UnitOverviewAdapter extends BaseExpandableListAdapter {

	private Context context;
	private LayoutInflater m_inflater;
	private HashMap<Integer, String> categorys;
	private HashMap<Integer, String[]>configs;
	private ArrayList<Integer> indexs;
	
	private final static int TYPE_CHECKBOX=1;
	private final static int TYPE_CONVENTIONAL_EDIT=2;
	private final static int TYPE_LIST=3;
	
	public UnitOverviewAdapter(Context con) {
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		categorys=new HashMap<Integer, String>();
		configs=new HashMap<Integer, String[]>();
		indexs=new ArrayList<Integer>();
		loadData();
	}
	private void loadData(){
		DateModel model=DiaryApplication.getInstance().getDateModel();
		JSONObject json=DiaryApplication.getInstance().getSudoConfig();
		try {
			JSONArray array=json.getJSONArray(SudoType.getTypeString(model.getType()).toLowerCase());
			for (int i=0;i<array.length();i++){
				JSONObject o=array.optJSONObject(i);
				int category=o.getInt("index");
				String name=o.getString("name");
				int type=o.getInt("type");
				indexs.add(category);
				configs.put(category, new String[]{name,String.valueOf(type)});
				Cursor c=DiaryApplication.getInstance().getDbHelper().getCategory(model.getDate(),model.getType().getType(),category);
				if(c!=null&&c.getCount()!=0){
					c.moveToFirst();
					categorys.put(c.getInt(0),c.getString(1));
				}
				else
					categorys.put(category, null);
				if(c!=null)
					c.close();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		categorys.clear();
		configs.clear();
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
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return indexs.size();
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
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
			convertView = m_inflater.inflate(R.layout.essay_overview, null);
			ViewHolder viewHolder=new ViewHolder();
			viewHolder.showType=TYPE_CHECKBOX;
			viewHolder.type_1=(LinearLayout)convertView.findViewById(R.id.category_type_1);
			viewHolder.title_type_1=(TextView)convertView.findViewById(R.id.title_type_1);
			viewHolder.checkBox=(CheckBox)convertView.findViewById(R.id.check_type_1);
			viewHolder.type_2=(LinearLayout)convertView.findViewById(R.id.category_type_2);
			viewHolder.title_type_2=(TextView)convertView.findViewById(R.id.title_type_2);
			viewHolder.content=(TextView)convertView.findViewById(R.id.content_type_2);
			viewHolder.type_3=(LinearLayout)convertView.findViewById(R.id.category_type_3);
			viewHolder.title_type_3=(TextView)convertView.findViewById(R.id.title_type_3);
			convertView.setTag(viewHolder);
		}
		ViewHolder holder=(ViewHolder)convertView.getTag();
		holder.showType=Integer.valueOf(configs.get(indexs.get(groupPosition))[1]);
		switch (holder.showType) {
		case TYPE_CHECKBOX:
			holder.title_type_1.setText(configs.get(indexs.get(groupPosition))[0]);
			holder.type_1.setVisibility(View.VISIBLE);
			holder.type_2.setVisibility(View.INVISIBLE);
			holder.type_3.setVisibility(View.INVISIBLE);
			break;
		case TYPE_CONVENTIONAL_EDIT:
			holder.title_type_2.setText(configs.get(indexs.get(groupPosition))[0]);
			holder.type_1.setVisibility(View.INVISIBLE);
			holder.type_2.setVisibility(View.VISIBLE);
			holder.type_3.setVisibility(View.INVISIBLE);
			TextView content=holder.content;
			Bitmap bitmap=BitmapCustomize.customizePicture(context, R.drawable.quote, 0, 0, false);
			int[] pads = new int[]{content.getPaddingLeft(), content.getPaddingTop(), content.getPaddingRight(), content.getPaddingBottom()};
			content.setBackgroundDrawable(new NinePatchDrawable(context.getResources(), 
					bitmap, bitmap.getNinePatchChunk(), new Rect(), null));	
			content.setPadding(pads[0], pads[1], pads[2], pads[3]);
			String text=categorys.get(indexs.get(groupPosition));
			content.setText(text==null?"N/A":text);
			final int index=groupPosition;
			content.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DiaryApplication.getInstance().getDateModel().setCategory(indexs.get(index));
					DiaryApplication.getInstance().getDateModel().setText(categorys.get(indexs.get(index)));
					Intent intent=new Intent();
					intent.setClass(context, RichTextEditorActivity.class);
					((Activity)context).startActivityForResult(intent, 0);
					//context.startActivity(intent);
					
				}
			});
			break;
		case TYPE_LIST:
			holder.title_type_3.setText(configs.get(indexs.get(groupPosition))[0]);
			holder.type_1.setVisibility(View.INVISIBLE);
			holder.type_2.setVisibility(View.INVISIBLE);
			holder.type_3.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}

		return convertView;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	class ViewHolder{
		int showType;
		LinearLayout type_1,type_2,type_3;
		TextView title_type_1,title_type_2,title_type_3;
		TextView content;
		CheckBox checkBox;
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
