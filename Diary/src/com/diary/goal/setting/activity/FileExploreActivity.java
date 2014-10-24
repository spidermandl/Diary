package com.diary.goal.setting.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.R;

/**
 * 文件目录activity
 * @author desmond.duan
 *
 */
public class FileExploreActivity extends SherlockActivity{

	
	private ListView listView;
	private FileAdapter fileAdapter;
	private Bitmap mIconBack,mIconFolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.export);
		
		final ActionBar ab = getSupportActionBar();
		
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(true);
		ab.setTitle(R.string.export_title);
		

		initView();
		initFunctionality();
		
		super.onCreate(savedInstanceState);
	}
	
	private void initView(){
		listView=(ListView)this.findViewById(R.id.export_list);
		mIconBack = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.icon_menu_back);
		mIconFolder = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.icon_folder_small);
	}
	
	private void initFunctionality(){
		fileAdapter=new FileAdapter(this, Environment.getExternalStorageDirectory().getAbsolutePath());
		listView.setAdapter(fileAdapter);
	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==0){
					fileAdapter.backToRoot();
				}else{
					fileAdapter.goToChild(position);
				}
				
			}
	    	
	    });
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case android.R.id.home:
		    this.finish();
			//this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
			break;
		default:
			break;
		}
		return true;
	}
	
	class FileAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private String rootPath;
		private List<String> items;

		public FileAdapter(Context context,String path) {
			mInflater = LayoutInflater.from(context);
			rootPath=path;
			updateItems();
		}
		
		private void updateItems(){
			if(items==null)
				items=new ArrayList<String>();
			else
				items.clear();
			
			File file=new File(rootPath);
			for(File f:file.listFiles()){
				if (f.isDirectory()) {
					items.add(f.getName());
				}
			}
		}

		/**
		 * 返回父目录
		 */
		public void backToRoot(){
			File file=new File(rootPath);
			rootPath=file.getParentFile().getPath();
			updateItems();
			notifyDataSetChanged();
		}
		
		/**
		 * 进入子目录
		 * @param position
		 */
		public void goToChild(int position){
			rootPath+=File.separator+items.get(position);
			updateItems();
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return items.size()+1;
		}

		@Override
		public Object getItem(int position) {
			if(position!=0)
				return items.get(position-1);
			else
				return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.export_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.export_name);
				holder.icon = (ImageView) convertView.findViewById(R.id.export_folder);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position==0) {
				File root=new File(rootPath);
				holder.text.setText("返回"+root.getName());
				holder.icon.setImageBitmap(mIconBack);
			} else {
				holder.text.setText(items.get(position-1));
				holder.icon.setImageBitmap(mIconFolder);
			}
			return convertView;
		}

		private class ViewHolder {
			TextView text;
			ImageView icon;
		}

	}
	
}
