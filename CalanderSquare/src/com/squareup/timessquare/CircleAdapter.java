package com.squareup.timessquare;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CircleAdapter<E> extends BaseAdapter {

	/**
	 * 队列容量
	 */
	protected int capacity=12;
    protected CircleLinkList dataLinks;
    protected ListIterator<E> dataIterator;
    /**
     * 数据绑定map，key为显示view的position
     */
    protected HashMap<Integer, E> dataMap;
    
    public CircleAdapter() {
		dataLinks=new CircleLinkList();
		dataMap=new HashMap<Integer, E>();
	}
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	

	public CircleLinkList getDataLinks() {
		return dataLinks;
	}

	public void setDataLinks(CircleLinkList dataLinks) {
		this.dataLinks = dataLinks;
		this.dataIterator=this.dataLinks.listIterator();
	}
	
	public HashMap<Integer, E> getDataMap() {
		return dataMap;
	}
	public void setDataMap(HashMap<Integer, E> dataMap) {
		this.dataMap = dataMap;
	}
	
	public E getDataItem(int key){
		if(!dataMap.containsKey(key)){
			addCapacityBlock(key);
		}
		return dataMap.get(key);
	}
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		
//		return 0;
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	abstract public void addCapacityBlock(int position);
	abstract public void refreshLinks(E object);

	public class CircleLinkList extends LinkedList<E>{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7865533544418619718L;
		
	    public boolean add(E object) {
	    	if(this.size()>capacity)
	    		refreshLinks(object);
	    	else
	    		super.add(object);
	    	return true;
	    };
		
	}



}
