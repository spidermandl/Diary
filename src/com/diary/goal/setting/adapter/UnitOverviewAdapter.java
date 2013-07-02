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
import android.widget.TextView;

public class UnitOverviewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater m_inflater;
	private HashMap<Integer, String> categorys,configs;
	private ArrayList<Integer> indexs;
	
	public UnitOverviewAdapter(Context con) {
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		categorys=new HashMap<Integer, String>();
		configs=new HashMap<Integer, String>();
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
				Iterator<String> it=o.keys();
				String key=it.next();
				int category=o.getInt(key);
				indexs.add(category);
				configs.put(category, key);
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
	public int getCount() {
		// TODO Auto-generated method stub
		return indexs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = m_inflater.inflate(R.layout.essay_overview, null);
		}
		TextView category=(TextView)convertView.findViewById(R.id.title);
		category.setText(configs.get(indexs.get(position)));
		TextView content=(TextView)convertView.findViewById(R.id.content);

		Bitmap bitmap=BitmapCustomize.customizePicture(context, R.drawable.quote, 0, 0, false);
		int[] pads = new int[]{content.getPaddingLeft(), content.getPaddingTop(), content.getPaddingRight(), content.getPaddingBottom()};
		content.setBackgroundDrawable(new NinePatchDrawable(context.getResources(), 
				bitmap, bitmap.getNinePatchChunk(), new Rect(), null));	
		content.setPadding(pads[0], pads[1], pads[2], pads[3]);
		String text=categorys.get(indexs.get(position));
		content.setText(text==null?"N/A":text);
		final int index=position;
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
		return convertView;
	}
	
//	public Drawable DRAWABLE_FILE(String key)
//	{
//		if(key == null)
//		{
//			Log.e("game","png key = null");
//			return null;
//		}
//		BitmapHolder bitmapHolder = BITMAP_FILE(key);
//		if(bitmapHolder == null) return null;
//
//		byte[] np = bitmapHolder.b.getNinePatchChunk();
//		if((np == null) || !NinePatch.isNinePatchChunk(np))
//			return new BitmapDrawable(Resources.getSystem(),bitmapHolder.b);
//		else
//			return new NinePatchDrawable(Resources.getSystem(),bitmapHolder.b,np,bitmapHolder.pad,null);
//	}      
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
