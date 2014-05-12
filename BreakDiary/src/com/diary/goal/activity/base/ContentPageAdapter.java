package com.diary.goal.activity.base;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ContentPageAdapter extends PagerAdapter{

	private List<BaseContentFragment> pageList = new ArrayList<BaseContentFragment>();
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	
	public ContentPageAdapter(List<BaseContentFragment> list,FragmentManager manager){
		this.pageList = list;
		this.fragmentManager = manager;
		transaction = manager.beginTransaction();
	}
	
    //获取当前窗体界面数
	@Override
	public int getCount() {
		return pageList.size();
	}

	//判断是否是由对象生成的View
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return ((Fragment) object).getView() == view;
	}

	//这个方法，return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中 
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		 if(transaction==null){
			 transaction = fragmentManager.beginTransaction();
		 }
		 //先判斷之前是否有attach過這個Fragment,有的話直接重新attach綁定
		 String tag = pageList.get(position).getIdentity();
		 BaseContentFragment fragment = (BaseContentFragment) fragmentManager.findFragmentByTag(tag);
		 if(fragment!=null){
			 transaction.attach(fragment);
		 }else{
			 //沒有attach過直接add
			 fragment = pageList.get(position);
			 transaction.add(container.getId(),fragment,fragment.getIdentity());
		 }
		 return fragment;
	}
	
	 /**
     * 此方法是移当前Object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (transaction == null) {
        	transaction = fragmentManager.beginTransaction();
        }
        //detach解除綁定
        transaction.detach((Fragment) object);
    }
   
   /**
     *  在UI更新完成后的动作
     */
   @Override
   public void finishUpdate(ViewGroup container) {
	  if(transaction!=null){
		  //提交
		  transaction.commitAllowingStateLoss();
		  transaction = null;
          //立即執行事務
          fragmentManager.executePendingTransactions();
	  }
   }
}
