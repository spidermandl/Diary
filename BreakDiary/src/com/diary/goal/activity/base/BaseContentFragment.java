package com.diary.goal.activity.base;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;

public class BaseContentFragment extends SherlockFragment {
	private String ID;
	
    public BaseContentFragment(){
		
	} 
	
	public String getIdentity() {
		return ID;
	}

	public void setIdentity(String id) {
		this.ID = id;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
}
