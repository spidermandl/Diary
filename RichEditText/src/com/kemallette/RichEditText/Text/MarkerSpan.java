package com.kemallette.RichEditText.Text;


import android.text.Editable;
import android.text.Spannable;
import android.text.style.UpdateAppearance;


/**
 * Just a lighter weight version of BaseSpan for marking points
 * 
 */
public class MarkerSpan implements UpdateAppearance{
    
    public int position;
    public int type;
    

    public MarkerSpan(int position, int type) {

        super();
        this.position = position;
        this.type = type;
    }
    

    public void setSpan(Editable e) {

        if (position < 0)
            position = 0;
        
        e.setSpan(this, position, position,
                  Spannable.SPAN_MARK_MARK);
    }
    

}
