package com.diary.goal.setting.view.wheel.adapters;

import java.text.SimpleDateFormat;

import com.diary.goal.setting.R;
import com.diary.goal.setting.calendar.MonthView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class AbstractWheelCalendarAdapter extends AbstractWheelAdapter {

	/** calendar view resource. Used as a default view for adapter. */
    public static final int CALENDAR_VIEW_ITEM_RESOURCE = -1;
    
    /** No resource constant. */
    protected static final int NO_RESOURCE = 0;
    
    
    // Current context
    protected Context context;
    // Layout inflater
    protected LayoutInflater inflater;
    
    // Items resources
    protected int itemResourceId;
    
    // Empty items resources
    protected int emptyItemResourceId;
	
    /**
     * Constructor
     * @param context the current context
     */
    protected AbstractWheelCalendarAdapter(Context context) {
        this(context, CALENDAR_VIEW_ITEM_RESOURCE);
    }


    
    /**
     * Constructor
     * @param context the current context
     * @param itemResource the resource ID for a layout file containing a TextView to use when instantiating items views
     */
    protected AbstractWheelCalendarAdapter(Context context, int itemResource) {
        this.context = context;
        itemResourceId = itemResource;
        
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    
    /**
     * Gets resource Id for items views
     * @return the item resource Id
     */
    public int getItemResource() {
        return itemResourceId;
    }
    
    /**
     * Sets resource Id for items views
     * @param itemResourceId the resource Id to set
     */
    public void setItemResource(int itemResourceId) {
        this.itemResourceId = itemResourceId;
    }
    

    /**
     * Gets resource Id for empty items views
     * @return the empty item resource Id
     */
    public int getEmptyItemResource() {
        return emptyItemResourceId;
    }

    /**
     * Sets resource Id for empty items views
     * @param emptyItemResourceId the empty item resource Id to set
     */
    public void setEmptyItemResource(int emptyItemResourceId) {
        this.emptyItemResourceId = emptyItemResourceId;
    }
    
    
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index >= 0 && index < getItemsCount()) {
            if (convertView == null) {
//                convertView = MonthView.create(parent, 
//                		inflater, 
//                		new SimpleDateFormat(context.getString(R.string.day_name_format)),
//                		new CellClickedListener(), today);
            	convertView=getView(index, convertView, parent);
            }
            if (convertView != null) {
            }
            return convertView;
        }
    	return null;
    }

    
    public View getEmptyItem(View convertView, ViewGroup parent) {
            
        return convertView;
	}


    /**
     * Loads view from resources
     * @param resource the resource Id
     * @return the loaded view or null if resource is not set
     */
    abstract protected View getView(int index, View convertView, ViewGroup parent);
    
    abstract protected void updateView();

}
