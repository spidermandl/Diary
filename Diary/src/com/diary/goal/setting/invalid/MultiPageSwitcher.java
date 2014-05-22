package com.diary.goal.setting.invalid;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Scroller;

/**
 * 
 * 
 * @author weibinke
 * 
 * 
 */

public class MultiPageSwitcher extends AdapterView<BaseAdapter> {
	private BaseAdapter mAdapter = null;
	private Scroller mScroller;
	private int mTouchSlop;
	private float mTouchStartX;
	private float mLastMotionX;
	private final static String TAG = "MultiPageSwitcher";
	private int mLastScrolledOffset = 0;

	/** User is not touching the list */

	private static final int TOUCH_STATE_RESTING = 0;

	/** User is scrolling the list */

	private static final int TOUCH_STATE_SCROLL = 2;
	private int mTouchState = TOUCH_STATE_RESTING;
	private int mHeightMeasureSpec;
	private int mWidthMeasureSpec;
	private int mSelectedPosition;
	private int mFirstPosition; // ��һ���ɼ�view��position
	private int mCurrentSelectedPosition;
	private VelocityTracker mVelocityTracker;
	private static final int SNAP_VELOCITY = 600;
	protected RecycleBin mRecycler = new RecycleBin();
	private OnPostionChangeListener mOnPostionChangeListener = null;

	public MultiPageSwitcher(Context context, AttributeSet attrs) {

		super(context, attrs);
		mScroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mAdapter == null) {
			return;
		}
		recycleAllViews();
		detachAllViewsFromParent();
		mRecycler.clear();
		fillAllViews();

	}

	/**
	 * 
	 * �ӵ�ǰ�ɼ��view��������
	 */

	private void fillToGalleryLeft() {
		int itemSpacing = 0;
		int galleryLeft = 0;
		// Set state for initial iteration
		View prevIterationView = getChildAt(0);
		int curPosition;
		int curRightEdge;
		if (prevIterationView != null) {
			curPosition = mFirstPosition - 1;
			curRightEdge = prevIterationView.getLeft() - itemSpacing;
		} else {
			// No children available!
			curPosition = 0;
			curRightEdge = getRight() - getLeft();
		}

		while (curRightEdge > galleryLeft && curPosition >= 0) {
			prevIterationView = makeAndAddView(curPosition, curPosition- mSelectedPosition,curRightEdge, false);
			// Remember some state
			mFirstPosition = curPosition;
			// Set state for next iteration
			curRightEdge = prevIterationView.getLeft() - itemSpacing;
			curPosition--;

		}

	}

	private void fillToGalleryRight() {
		int itemSpacing = 0;
		int galleryRight = getRight() - getLeft();
		int numChildren = getChildCount();
		int numItems = mAdapter.getCount();
		// Set state for initial iteration
		View prevIterationView = getChildAt(numChildren - 1);
		int curPosition;
		int curLeftEdge;
		if (prevIterationView != null) {
			curPosition = mFirstPosition + numChildren;
			curLeftEdge = prevIterationView.getRight() + itemSpacing;
		} else {
			mFirstPosition = curPosition = numItems - 1;
			curLeftEdge = 0;
		}

		while (curLeftEdge < galleryRight && curPosition < numItems) {
			prevIterationView = makeAndAddView(curPosition, curPosition	- mSelectedPosition,curLeftEdge, true);
			// Set state for next iteration
			curLeftEdge = prevIterationView.getRight() + itemSpacing;
			curPosition++;
		}
	}

	/**
	 * 
	 * ���view
	 */

	private void fillAllViews() {

		// �ȴ�����һ��view��ʹ�������ʾ
		if (mSelectedPosition >= mAdapter.getCount() && mSelectedPosition > 0) {
			// ���?��¼��ɾ���µ�ǰѡ��λ�ó�����¼������
			mSelectedPosition = mAdapter.getCount() - 1;
			if (mOnPostionChangeListener != null) {
				mCurrentSelectedPosition = mSelectedPosition;
				mOnPostionChangeListener.onPostionChange(this,mCurrentSelectedPosition);
			}
		}
		mFirstPosition = mSelectedPosition;
		mCurrentSelectedPosition = mSelectedPosition;
		View child = makeAndAddView(mSelectedPosition, 0, 0, true);
		int offset = getWidth() / 2 - (child.getLeft() + child.getWidth() / 2);
		child.offsetLeftAndRight(offset);
		fillToGalleryLeft();
		fillToGalleryRight();

	}

	/**
	 * 
	 * Obtain a view, either by pulling an existing view from the recycler or by
	 * getting a new one from the adapter. If we are animating, make sure there
	 * is enough information in the view's layout parameters to animate from the
	 * old to new positions.
	 * @param position
	 *            Position in the gallery for the view to obtain
	 * @param offset
	 *            Offset from the selected position
	 * @param x
	 *            X-coordintate indicating where this view should be placed.
	 *            This will either be the left or right edge of the view, depending
	 *            on the fromLeft paramter
	 * 
	 * @param fromLeft
	 *            Are we posiitoning views based on the left edge? (i.e.,building from left to right)?
	 * 
	 * @return A view that has been added to the gallery
	 */

	private View makeAndAddView(int position, int offset, int x, boolean fromLeft) {

		View child;
		// child = mRecycler.get(position);
		// if (child != null) {
		// // Position the view
		// setUpChild(child, offset, x, fromLeft);
		//
		// return child;
		// }
		//
		// // Nothing found in the recycler -- ask the adapter for a view

		child = mAdapter.getView(position, null, this);
		// Position the view
		setUpChild(child, offset, x, fromLeft);
		return child;

	}

	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		/*
		 * Gallery expects Gallery.LayoutParams.
		 */
		return new Gallery.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
		ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 
	 * Helper for makeAndAddView to set the position of a view and fill out its layout paramters.
	 * @param child
	 *            The view to position
	 * @param offset
	 *            Offset from the selected position
	 * @param x
	 *            X-coordintate indicating where this view should be placed.
	 *            This will either be the left or right edge of the view, depending
	 *            on the fromLeft paramter
	 * 
	 * @param fromLeft
	 *            Are we posiitoning views based on the left edge? (i.e., building from left to right)?
	 */

	private void setUpChild(View child, int offset, int x, boolean fromLeft) {
		// Respect layout params that are already in the view. Otherwise
		// make some up...
		Gallery.LayoutParams lp = (Gallery.LayoutParams)child.getLayoutParams();

		if (lp == null) {
			lp = (Gallery.LayoutParams) generateDefaultLayoutParams();
		}
		addViewInLayout(child, fromLeft ? -1 : 0, lp);
		child.setSelected(offset == 0);
		// Get measure specs
		int childHeightSpec = ViewGroup.getChildMeasureSpec(mHeightMeasureSpec,0, lp.height);
		int childWidthSpec = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec,0, lp.width);
		// Measure child
		child.measure(childWidthSpec, childHeightSpec);
		int childLeft;
		int childRight;
		// Position vertically based on gravity setting
		int childTop = 0;
		int childBottom = childTop + child.getMeasuredHeight();
		int width = child.getMeasuredWidth();
		if (fromLeft) {
			childLeft = x;
			childRight = childLeft + width;
		} else {
			childLeft = x - width;
			childRight = x;
		}
		child.layout(childLeft, childTop, childRight, childBottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidthMeasureSpec = widthMeasureSpec;
		mHeightMeasureSpec = heightMeasureSpec;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAdapter.getCount();

	}

	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		return mAdapter;

	}

	@Override
	public void setAdapter(BaseAdapter adapter) {
		// TODO Auto-generated method stub
		mAdapter = adapter;
		removeAllViewsInLayout();
		requestLayout();
	}

	@Override
	public View getSelectedView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelection(int position) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (!mScroller.isFinished()) {
			return true;
		}

		final int action = event.getAction();
		if (MotionEvent.ACTION_DOWN == action) {
			startTouch(event);
			return false;

		} else if (MotionEvent.ACTION_MOVE == action) {
			return startScrollIfNeeded(event);
		} else if (MotionEvent.ACTION_UP == action || MotionEvent.ACTION_CANCEL == action) {
			mTouchState = TOUCH_STATE_RESTING;
			return false;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mScroller.isFinished()) {
			return true;
		}
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		final int action = event.getAction();
		final float x = event.getX();
		if (MotionEvent.ACTION_DOWN == action) {
			startTouch(event);
		} else if (MotionEvent.ACTION_MOVE == action) {
			if (mTouchState == TOUCH_STATE_RESTING) {
				startScrollIfNeeded(event);
			} else if (mTouchState == TOUCH_STATE_SCROLL) {
				int deltaX = (int) (x - mLastMotionX);
				mLastMotionX = x;
				scrollDeltaX(deltaX);
			}
		} else if (MotionEvent.ACTION_UP == action|| MotionEvent.ACTION_CANCEL == action) {
			if (mTouchState == TOUCH_STATE_SCROLL) {
				onUp(event);
			}
		}
		return true;
	}

	private void scrollDeltaX(int deltaX) {
		// �Ȱ����е�view����ƶ�
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).offsetLeftAndRight(deltaX);
		}
		boolean toLeft = (deltaX < 0);
		detachOffScreenChildren(toLeft);
		if (deltaX < 0) {
			// sroll to right
			fillToGalleryRight();
		} else {
			fillToGalleryLeft();
		}
		invalidate();
		int position = calculteCenterItem() + mFirstPosition;
		if (mCurrentSelectedPosition != position) {
			mCurrentSelectedPosition = position;
			if (mOnPostionChangeListener != null) {
				mOnPostionChangeListener.onPostionChange(this,mCurrentSelectedPosition);
			}
		}
	}

	private void onUp(MotionEvent event) {
		final VelocityTracker velocityTracker = mVelocityTracker;
		velocityTracker.computeCurrentVelocity(1000);
		int velocityX = (int) velocityTracker.getXVelocity();
		if (velocityX < -SNAP_VELOCITY&& mSelectedPosition < mAdapter.getCount() - 1) {
			if (scrollToChild(mSelectedPosition + 1)) {
				mSelectedPosition++;
			}

		} else if (velocityX > SNAP_VELOCITY && mSelectedPosition > 0) {
			if (scrollToChild(mSelectedPosition - 1)) {
				mSelectedPosition--;
			}
		} else {
			int position = calculteCenterItem();
			int newpostion = mFirstPosition + position;
			if (scrollToChild(newpostion)) {
				mSelectedPosition = newpostion;
			}
		}

		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
		mTouchState = TOUCH_STATE_RESTING;

	}

	/**
	 * 
	 * ������ӽ����ĵ��view
	 * 
	 * @return
	 */

	private int calculteCenterItem() {
		View child = null;
		int lastpostion = 0;
		int lastclosestDistance = 0;
		int viewCenter = getLeft() + getWidth() / 2;
		for (int i = 0; i < getChildCount(); i++) {
			child = getChildAt(i);
			if (child.getLeft() < viewCenter && child.getRight() > viewCenter) {
				lastpostion = i;
				break;
			} else {
				int childClosestDistance = Math.min(Math.abs(child.getLeft() - viewCenter),Math.abs(child.getRight() - viewCenter));
				if (childClosestDistance < lastclosestDistance) {
					lastclosestDistance = childClosestDistance;
					lastpostion = i;
				}
			}
		}
		return lastpostion;
	}

	public void moveNext() {
		if (!mScroller.isFinished()) {
			return;
		}

		if (0 <= mSelectedPosition
				&& mSelectedPosition < mAdapter.getCount() - 1) {
			if (scrollToChild(mSelectedPosition + 1)) {
				mSelectedPosition++;
			} else {
				makeAndAddView(mSelectedPosition + 1, 1, getWidth(), true);
				if (scrollToChild(mSelectedPosition + 1)) {
					mSelectedPosition++;
				}
			}
		}
	}
	public void movePrevious() {
		if (!mScroller.isFinished()) {
			return;
		}
		if (0 < mSelectedPosition && mSelectedPosition < mAdapter.getCount()) {
			if (scrollToChild(mSelectedPosition - 1)) {
				mSelectedPosition--;
			} else {
				makeAndAddView(mSelectedPosition - 1, -1, 0, false);
				mFirstPosition = mSelectedPosition - 1;
				if (scrollToChild(mSelectedPosition - 1)) {
					mSelectedPosition--;
				}
			}
		}

	}

	private boolean scrollToChild(int position) {

		View child = getChildAt(position - mFirstPosition);
		if (child != null) {
			int distance = getWidth() / 2- (child.getLeft() + child.getWidth() / 2);
			mLastScrolledOffset = 0;
			mScroller.startScroll(0, 0, distance, 0, 200);
			invalidate();
			return true;

		}
		return false;

	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			int scrollX = mScroller.getCurrX();
			// Mlog.d("MuticomputeScroll ," + scrollX);
			scrollDeltaX(scrollX - mLastScrolledOffset);
			mLastScrolledOffset = scrollX;
			postInvalidate();
		}

	}

	private void startTouch(MotionEvent event) {
		mTouchStartX = event.getX();
		mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTING: TOUCH_STATE_SCROLL;
		mLastMotionX = mTouchStartX;
	}

	private boolean startScrollIfNeeded(MotionEvent event) {
		final int xPos = (int) event.getX();
		mLastMotionX = event.getX();
		if (xPos < mTouchStartX - mTouchSlop || xPos > mTouchStartX + mTouchSlop) {
			// we've moved far enough for this to be a scroll
			mTouchState = TOUCH_STATE_SCROLL;
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Detaches children that are off the screen (i.e.: Gallery bounds).
	 * @param toLeft
	 *            Whether to detach children to the left of the Gallery, or to the right.
	 */

	private void detachOffScreenChildren(boolean toLeft) {
		int numChildren = getChildCount();
		int start = 0;
		int count = 0;
		int firstPosition = mFirstPosition;
		if (toLeft) {
			final int galleryLeft = 0;
			for (int i = 0; i < numChildren; i++) {
				final View child = getChildAt(i);
				if (child.getRight() >= galleryLeft) {
					break;
				} else {
					count++;
					mRecycler.put(firstPosition + i, child);
				}
			}
		} else {
			final int galleryRight = getWidth();
			for (int i = numChildren - 1; i >= 0; i--) {
				final View child = getChildAt(i);
				if (child.getLeft() <= galleryRight) {
					break;
				} else {
					start = i;
					count++;
					mRecycler.put(firstPosition + i, child);
				}
			}
		}
		detachViewsFromParent(start, count);
		if (toLeft) {
			mFirstPosition += count;
		}

		mRecycler.clear();

	}

	public void setOnPositionChangeListen(OnPostionChangeListener onPostionChangeListener) {
		mOnPostionChangeListener = onPostionChangeListener;
	}

	public int getCurrentSelectedPosition() {
		return mCurrentSelectedPosition;
	}

	/**
	 * 
	 * ˢ����ݣ���������AdapterView.AdapterDataSetObserver������ʵ�ֵģ���������߼���ֲ�Ƚ��鷳������ʱ����������
	 */

	public void updateData() {
		requestLayout();
	}

	private void recycleAllViews() {
		int childCount = getChildCount();
		final RecycleBin recycleBin = mRecycler;
		// All views go in recycler
		for (int i = 0; i < childCount; i++) {
			View v = getChildAt(i);
			int index = mFirstPosition + i;
			recycleBin.put(index, v);
		}
	}

	class RecycleBin {
		private SparseArray<View> mScrapHeap = new SparseArray<View>();
		public void put(int position, View v) {
			if (mScrapHeap.get(position) != null) {
				Log.e(TAG, "RecycleBin put error.");
			}
			mScrapHeap.put(position, v);
		}

		View get(int position) {
			// System.out.print("Looking for " + position);
			View result = mScrapHeap.get(position);
			if (result != null) {
				mScrapHeap.delete(position);

			} else {
			}

			return result;

		}

		View peek(int position) {
			// System.out.print("Looking for " + position);
			return mScrapHeap.get(position);

		}

		void clear() {
			final SparseArray<View> scrapHeap = mScrapHeap;
			final int count = scrapHeap.size();
			for (int i = 0; i < count; i++) {
				final View view = scrapHeap.valueAt(i);
				if (view != null) {
					removeDetachedView(view, true);
				}
			}

			scrapHeap.clear();

		}

	}

	public interface OnPostionChangeListener {

		abstract public void onPostionChange(View v, int position);

	}

}