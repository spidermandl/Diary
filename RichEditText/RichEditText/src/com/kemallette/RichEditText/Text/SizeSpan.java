package com.kemallette.RichEditText.Text;

import android.os.Parcel;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ScaleXSpan;
import android.util.Log;

public class SizeSpan extends ScaleXSpan implements ISpan {

	int	startPosition, endPosition, flag;
	
	public SizeSpan(float proportion,final int startPostion,
			final int endPostion,
			final int flag) {
		super(proportion);
		
		startPosition = startPostion;
		endPosition = endPostion;
		this.flag = flag;
	}
	
	public SizeSpan(Parcel src,final int startPostion,
			final int endPostion,
			final int flag) {
		super(src);

		startPosition = startPostion;
		endPosition = endPostion;
		this.flag = flag;
	}

	@Override
	public void setSpan(Editable e) {
		if (startPosition < 0)
			startPosition = 0;

		if (!(e.length() < startPosition))
			if (startPosition > endPosition)
				Log.e(	"SPAN",
						"StartPosition was after End Position - couldn't set.");
			else
				e.setSpan(	this,
							startPosition,
							endPosition,
							flag);
		else
			Log.e(	"SPAN",
					"DID NOT SET: Start position past EditText length.");

	}

	@Override
	public int getStartPosition(){

		return startPosition;
	}


	@Override
	public int getEndPosition(){

		return endPosition;
	}


	@Override
	public void setStartPosition(final int startPos){

		startPosition = startPos;

	}


	@Override
	public void setEndPosition(final int endPos){

		endPosition = endPos;
	}


	@Override
	public void setFlag(final int flag){

		this.flag = flag;
	}


	@Override
	public int getFlag(){

		return flag;
	}


	@Override
	public void setType(final int type){

		// Do nothing, we're an underline type
	}


	@Override
	public int getType(){

		return UNDERLINE;
	}


	@Override
	public boolean isStartInclusive(){

		if (flag == Spanned.SPAN_INCLUSIVE_EXCLUSIVE
			|| flag == Spanned.SPAN_INCLUSIVE_INCLUSIVE)
			return true;
		else
			return false;
	}


	@Override
	public boolean isEndInclusive(){

		if (flag == Spanned.SPAN_EXCLUSIVE_INCLUSIVE
			|| flag == Spanned.SPAN_INCLUSIVE_INCLUSIVE)
			return true;
		else
			return false;
	}


	@Override
	public void removeSpan(Editable text){

		text.removeSpan(this);
	}


	@Override
	public void dump(){

		BaseSpan.dump(this);
	}

}
