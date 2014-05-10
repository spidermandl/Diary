package com.diary.goal.setting.adapter;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;


import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.database.DiaryHelper;
import com.diary.goal.setting.database.DiaryHelper.Tables;
import com.diary.goal.setting.dialog.CategoryEditDialog;
import com.diary.goal.setting.invalid.RichTextEditorActivity;
import com.diary.goal.setting.listener.OnCategoryChange;
import com.diary.goal.setting.listener.OnRatingPentagramTouchUp;
import com.diary.goal.setting.model.CategoryModel;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.tools.BitmapCustomize;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Function;
import com.diary.goal.setting.tools.Constant.SudoType;
import com.diary.goal.setting.view.CategoryTextView;
import com.diary.goal.setting.view.RatingPentagramView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UnitOverviewAdapter extends BaseExpandableListAdapter {

	private Context context;
	private LayoutInflater m_inflater;

	private ArrayList<Integer> indexs;
	private HashMap<Integer, String> categorys;//the key is correspond to the values in indexs declared above
	private HashMap<Integer, CategoryModel> configTables;//the key is correspond to the values in indexs declared above
	private HashMap<Integer, JSONObject> listCategorys;//the key is correspond to the values in indexs declared above

	private final static int TYPE_TITLE=-1;
	public final static int TYPE_RATING=0;
	public final static int TYPE_CHECKBOX=1;
	public final static int TYPE_CONVENTIONAL_EDIT=2;
	public final static int TYPE_LIST=3;
	
	
	public UnitOverviewAdapter(Context con) {
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		categorys=new HashMap<Integer, String>();
		indexs=new ArrayList<Integer>();
		listCategorys=new HashMap<Integer, JSONObject>();
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
					String text=c.getString(1);
					categorys.put(_id,text);
					if(configTables.get(_id).getCategoryType()==TYPE_LIST){
						JSONObject json;
						try {
							json = new JSONObject(text);
							listCategorys.put(_id, json);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
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
		listCategorys.clear();
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
		int index=indexs.get(groupPosition);
		CategoryModel model=configTables.get(index);
		if(model.getCategoryType()==TYPE_LIST){
			JSONObject json=listCategorys.get(index);
			if(json==null){
				return 1;
			}else{
				return json.length()==0?1:json.length();
			}
			
		}else
			return 0;
		//return 1;
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
			viewHolder.type__1=(RelativeLayout)convertView.findViewById(R.id.category_type__1);
			viewHolder.title_type__1=(TextView)convertView.findViewById(R.id.overview_title);
			viewHolder.back=convertView.findViewById(R.id.back_sudo);
			viewHolder.forward=convertView.findViewById(R.id.forward_sudo);
			viewHolder.type_0=(RelativeLayout)convertView.findViewById(R.id.category_type_0);
			viewHolder.title_type_0=(CategoryTextView)convertView.findViewById(R.id.title_type_0);
			viewHolder.ratingStar=(RatingPentagramView)convertView.findViewById(R.id.star_type_0);
			viewHolder.type_1=(RelativeLayout)convertView.findViewById(R.id.category_type_1);
			viewHolder.title_type_1=(CategoryTextView)convertView.findViewById(R.id.title_type_1);
			viewHolder.checkBox=(CheckBox)convertView.findViewById(R.id.check_type_1);
			viewHolder.type_2=(RelativeLayout)convertView.findViewById(R.id.category_type_2);
			viewHolder.title_type_2=(CategoryTextView)convertView.findViewById(R.id.title_type_2);
			viewHolder.content=(TextView)convertView.findViewById(R.id.content_type_2);
			viewHolder.type_3=(RelativeLayout)convertView.findViewById(R.id.category_type_3);
			viewHolder.title_type_3=(CategoryTextView)convertView.findViewById(R.id.title_type_3);
			viewHolder.jump=(ImageView)convertView.findViewById(R.id.expandable_type_3);
			convertView.setTag(viewHolder);
		}
		ViewHolder holder=(ViewHolder)convertView.getTag();
		CategoryModel categoryModel=configTables.get(indexs.get(groupPosition));
		holder.showType=Integer.valueOf(categoryModel.getCategoryType());
		final int index=groupPosition;
		View.OnLongClickListener category_listener=new View.OnLongClickListener() {
			
			public boolean onLongClick(View v) {
				editCategoryName(index);
				return true;
			}
		};
		String text;
		/**
		 * switch items
		 */
		switch (holder.showType) {
		case TYPE_TITLE:
			holder.type__1.setVisibility(View.VISIBLE);
			holder.type_0.setVisibility(View.GONE);
			holder.type_1.setVisibility(View.GONE);
			holder.type_2.setVisibility(View.GONE);
			holder.type_3.setVisibility(View.GONE);
			
			
			text=
					context.getResources().getString(Constant.stringDict.get(Constant.SudoType.getTypeString(DiaryApplication.getInstance().getDateModel().getType())));
			text+="("+getMonthString(DiaryApplication.getInstance().getDateModel())+")";
			holder.title_type__1.setText(text);
			holder.back.setOnClickListener(backAndForthListener);
			holder.forward.setOnClickListener(backAndForthListener);
			break;
		case TYPE_RATING:
			holder.type__1.setVisibility(View.GONE);
			holder.type_0.setVisibility(View.VISIBLE);
			holder.type_1.setVisibility(View.GONE);
			holder.type_2.setVisibility(View.GONE);
			holder.type_3.setVisibility(View.GONE);

			CategoryTextView cTextView=holder.title_type_0;
			cTextView.setText(switchLanguage(categoryModel.getCategoryName()));
			cTextView.setOnLongClickListener(category_listener);
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
			holder.type__1.setVisibility(View.GONE);
			holder.type_0.setVisibility(View.GONE);
			holder.type_1.setVisibility(View.VISIBLE);
			holder.type_2.setVisibility(View.GONE);
			holder.type_3.setVisibility(View.GONE);
			
			cTextView=holder.title_type_1;
			cTextView.setText(switchLanguage(categoryModel.getCategoryName()));
			cTextView.setOnLongClickListener(category_listener);
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
			holder.type__1.setVisibility(View.GONE);
			holder.type_0.setVisibility(View.GONE);
			holder.type_1.setVisibility(View.GONE);
			holder.type_2.setVisibility(View.VISIBLE);
			holder.type_3.setVisibility(View.GONE);
			cTextView=holder.title_type_2;
			cTextView.setText(switchLanguage(categoryModel.getCategoryName()));
			cTextView.setOnLongClickListener(category_listener);
			TextView content=holder.content;
//			Bitmap bitmap=BitmapCustomize.customizePicture(context, R.drawable.quote, 0, 0, false);
//			int[] pads = new int[]{content.getPaddingLeft(), content.getPaddingTop(), content.getPaddingRight(), content.getPaddingBottom()};
//			content.setBackgroundDrawable(new NinePatchDrawable(context.getResources(), 
//					bitmap, bitmap.getNinePatchChunk(), new Rect(), null));	
//			content.setPadding(pads[0], pads[1], pads[2], pads[3]);
			content.setText("");
			content.setHint(categoryModel.getHint());
			text=categorys.get(indexs.get(groupPosition));
			if(text!=null&&!text.equals(""))
				content.setText(switchLanguage(text));
			content.setOnClickListener(new View.OnClickListener() {
				
				//@Override
				public void onClick(View v) {
					enterRichTextEdit(indexs.get(index), -1);
				}
			});
			break;
		case TYPE_LIST:
			holder.type__1.setVisibility(View.GONE);
			holder.type_0.setVisibility(View.GONE);
			holder.type_1.setVisibility(View.GONE);
			holder.type_2.setVisibility(View.GONE);
			holder.type_3.setVisibility(View.VISIBLE);

			cTextView=holder.title_type_3;
			cTextView.setText(switchLanguage(categoryModel.getCategoryName()));
			//cTextView.setOnLongClickListener(category_listener);
			//holder.title_type_3.setBackgroundResource(R.drawable.group_icon_selector);
			if(isExpanded)
				holder.jump.setBackgroundDrawable(new BitmapDrawable(
						BitmapCustomize.customizePicture(context, R.drawable.jump_down, 0, 0, false)));
			else{
				holder.jump.setBackgroundDrawable(new BitmapDrawable(
						BitmapCustomize.customizePicture(context, R.drawable.jump_right, 0, 0, false)));
			}
			break;

		default:
			break;
		}

		return convertView;
	}
	
	//@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final int index=indexs.get(groupPosition);
		final int subIndex=childPosition;
		CategoryModel model=configTables.get(index);
		if(model.getCategoryType()==TYPE_LIST){
			ChildViewHolder holder;
			if(convertView==null){
				holder=new ChildViewHolder();
				convertView=m_inflater.inflate(R.layout.essay_overview_child_item, null);
				holder.text=(TextView)convertView.findViewById(R.id.child_text);
				convertView.setTag(holder);
			}
			holder=(ChildViewHolder)convertView.getTag();
			JSONObject json=listCategorys.get(index);
			holder.text.setText("");
			String text=model.getHint();
			if(json!=null&&json.length()!=0){
				try {
					text=json.getString(String.valueOf(childPosition));
					text=text.equals("")?"N/A":text;
				} catch (JSONException e) {
					text="N/A";
				}
				holder.text.setText(text);
			}else{
				holder.text.setHint(text);
			}
			holder.text.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					enterRichTextEdit(index, subIndex);
					//context.startActivity(intent);
					
				}
			});
			holder.text.setOnLongClickListener(new View.OnLongClickListener() {
				
				public boolean onLongClick(View v) {
					new AlertDialog.Builder(context)
		            .setTitle(R.string.sub_category_operation)
		            .setItems(R.array.sub_category_operation, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int which) {
		                	switch (which) {
							case 0://add
								JSONObject json=listCategorys.get(index);
								try {
									boolean isInsert=false;
									if(json==null||json.length()==0){
										json=new JSONObject();
										json.put("0", "");
										listCategorys.put(index, json);
										isInsert=true;
									}
									json.put(String.valueOf(json.length()), "");
									DateModel model=DiaryApplication.getInstance().getDateModel();
									/*******************************set date model*************************************/
									model.setCategory(configTables.get(index).getCategoryIndex());
									model.setText(categorys.get(index));
									model.setConfigId(index);
									model.setCategory_type(configTables.get(index).getCategoryType());
									model.setCategory_name(configTables.get(index).getCategoryName());
									model.setCategorySubIndex(subIndex);
									/********************************************************************/
									if(isInsert)
										DiaryApplication.getInstance().getDbHelper().insertDiaryContent(model, json.toString());
									else
										DiaryApplication.getInstance().getDbHelper().updateDiaryContent(model, json.toString());
									UnitOverviewAdapter.this.notifyDataSetChanged();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							case 1://edit
								enterRichTextEdit(index, subIndex);
								dialog.dismiss();
								break;
							case 2://delete
								json=listCategorys.get(index);
								try {
									if(json!=null||json.length()!=1){
										int length=json.length();
										for(int i=0;i<length-index;i++){
											json.put(String.valueOf(index+i), json.get(String.valueOf(index+i+1)));
										}
										json.remove(String.valueOf(length-1));
										DateModel model=DiaryApplication.getInstance().getDateModel();
										/*******************************set date model*************************************/
										model.setCategory(configTables.get(index).getCategoryIndex());
										model.setText(categorys.get(index));
										model.setConfigId(index);
										model.setCategory_type(configTables.get(index).getCategoryType());
										model.setCategory_name(configTables.get(index).getCategoryName());
										model.setCategorySubIndex(subIndex);
										/********************************************************************/
										DiaryApplication.getInstance().getDbHelper().updateDiaryContent(model, json.toString());
										UnitOverviewAdapter.this.notifyDataSetChanged();
									}
								    
									json.put(String.valueOf(json.length()), "");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							default:
								break;
							}
		                }
		            })
		            .create()
		            .show();
					return true;
				}
			});
			return convertView;
		}else
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
		RelativeLayout type__1,type_0,type_1,type_2,type_3;
		CategoryTextView title_type_0,title_type_1,title_type_2,title_type_3;
		TextView title_type__1,content;
		CheckBox checkBox;
		ImageView jump;
		View back,forward;
	}
    
	class ChildViewHolder{
		TextView text;
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
	/**
	 * switch to RichTextEditorActivity
	 * @param categoryIndex 
	 *        main index of the category
	 * @param subCategoryIndex
	 *        sub index of the category with list type
	 */
	private void enterRichTextEdit(int categoryIndex,int subCategoryIndex){
		DateModel model=DiaryApplication.getInstance().getDateModel();
		/*******************************set date model*************************************/
		model.setCategory(configTables.get(categoryIndex).getCategoryIndex());
		model.setText(categorys.get(categoryIndex));
		model.setConfigId(categoryIndex);
		model.setCategory_type(configTables.get(categoryIndex).getCategoryType());
		model.setCategory_name(configTables.get(categoryIndex).getCategoryName());
		model.setCategorySubIndex(subCategoryIndex);
		/********************************************************************//********************************************************************/
		Intent intent=new Intent();
//		if (Build.VERSION.SDK_INT < 14)
			intent.setClass(context, RichTextEditorActivity.class);
//		else
//			intent.setClass(context, RichTextEditor4_0Activity.class);
		((Activity)context).startActivityForResult(intent, 0);
		((Activity)context).overridePendingTransition(R.anim.right_enter,R.anim.left_exit);
	}
	
	private String switchLanguage(String key){
		Integer value=Constant.stringDict.get(key);
		return value==null?key:context.getResources().getString(value);
	}
	
	private void editCategoryName(int index){
		DateModel model=DiaryApplication.getInstance().getDateModel();
		/*******************************set date model*************************************/
		model.setCategory(configTables.get(indexs.get(index)).getCategoryIndex());
		model.setText(categorys.get(indexs.get(index)));
		model.setConfigId(indexs.get(index));
		model.setCategory_type(configTables.get(indexs.get(index)).getCategoryType());
		model.setCategory_name(configTables.get(indexs.get(index)).getCategoryName());
		/*******************************show edit dialog************************/
		CategoryEditDialog dialog=new CategoryEditDialog(context);
		dialog.setOnCategoryChangeListener(new OnCategoryChange() {
			
			public void changeCategoryName(String name) {
				DateModel model=DiaryApplication.getInstance().getDateModel();
				model.setCategory_name(name);
				DiaryApplication.getInstance().getDbHelper().updateConfigCategoryName(model);
				DiaryApplication.getInstance().clearTableCacheElement(DiaryHelper.Tables.DIARY_CONFIG);
				notifyDataSetChanged();
			}
		});
		dialog.create().show();
	}
	
	/**
	 * construct string with month and day
	 * @return
	 */
	private String getMonthString(DateModel model){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(model.getDate());
		int month=calendar.get(Calendar.MONTH)+1;
		int day=calendar.get(Calendar.DAY_OF_MONTH);
//		Log.e("getMonthString month", month+"");
//		Log.e("getMonthString day", day+"");
		return Function.getAbbrMonth(this.context,month)+day+context.getResources().getString(R.string.day);
	}
	
	private void moveSudoStep(int step){
		DateModel model=DiaryApplication.getInstance().getDateModel();
		int sudo_type=model.getType().getType();
		sudo_type=(sudo_type+step)%9;
		if(sudo_type==5){
			if(step>0)
				sudo_type=6;
			else
				sudo_type=4;
		}
		sudo_type=sudo_type==0?9:sudo_type;
		model.setType(SudoType.getSudoType(sudo_type));
	}
	OnItemLongClickListener itemLongClickListener=new AdapterView.OnItemLongClickListener() {

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			CategoryModel model=configTables.get(indexs.get(arg2));
			if(model.getCategoryType()==TYPE_LIST){
				editCategoryName(arg2);
			}
			return true;
		}
	};
	
	OnClickListener backAndForthListener=new OnClickListener() {
		
		public void onClick(View v) {
			if(v.getId()==R.id.back_sudo){
				moveSudoStep(-1);
				notifyDataSetChanged();
			}
			if(v.getId()==R.id.forward_sudo){
				moveSudoStep(1);
				notifyDataSetChanged();
			}
			
		}
	};
	
	
	public void setListener(ExpandableListView listView){
		listView.setOnItemLongClickListener(itemLongClickListener);
	}
	
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
