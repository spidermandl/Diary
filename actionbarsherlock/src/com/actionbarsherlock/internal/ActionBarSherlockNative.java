package com.actionbarsherlock.internal;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.internal.app.ActionBarWrapper;
import com.actionbarsherlock.internal.view.menu.MenuItemWrapper;
import com.actionbarsherlock.internal.view.menu.MenuWrapper;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

@ActionBarSherlock.Implementation(api = 14)
public class ActionBarSherlockNative extends ActionBarSherlock {
    private ActionBarWrapper mActionBar;
    private ActionModeWrapper mActionMode;
    private MenuWrapper mMenu;

    public ActionBarSherlockNative(Activity activity, int flags) {
        super(activity, flags);
    }


    
    public ActionBar getActionBar() {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[getActionBar]");

        initActionBar();
        return mActionBar;
    }

    private void initActionBar() {
        if (mActionBar != null || mActivity.getActionBar() == null) {
            return;
        }

        mActionBar = new ActionBarWrapper(mActivity);
    }

    
    public void dispatchInvalidateOptionsMenu() {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[dispatchInvalidateOptionsMenu]");

        mActivity.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);

        if (mMenu != null) mMenu.invalidate();
    }

    
    public boolean dispatchCreateOptionsMenu(android.view.Menu menu) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[dispatchCreateOptionsMenu] menu: " + menu);

        if (mMenu == null || menu != mMenu.unwrap()) {
            mMenu = new MenuWrapper(menu);
        }

        final boolean result = callbackCreateOptionsMenu(mMenu);
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[dispatchCreateOptionsMenu] returning " + result);
        return result;
    }

    
    public boolean dispatchPrepareOptionsMenu(android.view.Menu menu) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[dispatchPrepareOptionsMenu] menu: " + menu);

        final boolean result = callbackPrepareOptionsMenu(mMenu);
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[dispatchPrepareOptionsMenu] returning " + result);
        return result;
    }

    
    public boolean dispatchOptionsItemSelected(android.view.MenuItem item) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[dispatchOptionsItemSelected] item: " + item.getTitleCondensed());

        MenuItem wrapped;
        if (mMenu == null) {
            if (item.getItemId() != android.R.id.home) {
                throw new IllegalStateException("Non-home action item clicked before onCreateOptionsMenu with ID " + item.getItemId());
            }
            // Create a throw-away wrapper for now.
            wrapped = new MenuItemWrapper(item);
        } else {
            wrapped = mMenu.findItem(item);
        }
        final boolean result = callbackOptionsItemSelected(wrapped);
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[dispatchOptionsItemSelected] returning " + result);
        return result;
    }

    
    public boolean hasFeature(int feature) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[hasFeature] feature: " + feature);

        final boolean result = mActivity.getWindow().hasFeature(feature);
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[hasFeature] returning " + result);
        return result;
    }

    
    public boolean requestFeature(int featureId) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[requestFeature] featureId: " + featureId);

        final boolean result = mActivity.getWindow().requestFeature(featureId);
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[requestFeature] returning " + result);
        return result;
    }

    
    public void setUiOptions(int uiOptions) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[setUiOptions] uiOptions: " + uiOptions);

        mActivity.getWindow().setUiOptions(uiOptions);
    }

    
    public void setUiOptions(int uiOptions, int mask) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[setUiOptions] uiOptions: " + uiOptions + ", mask: " + mask);

        mActivity.getWindow().setUiOptions(uiOptions, mask);
    }

    
    public void setContentView(int layoutResId) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[setContentView] layoutResId: " + layoutResId);

        mActivity.getWindow().setContentView(layoutResId);
        initActionBar();
    }

    
    public void setContentView(View view, LayoutParams params) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[setContentView] view: " + view + ", params: " + params);

        mActivity.getWindow().setContentView(view, params);
        initActionBar();
    }

    
    public void addContentView(View view, LayoutParams params) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[addContentView] view: " + view + ", params: " + params);

        mActivity.getWindow().addContentView(view, params);
        initActionBar();
    }

    
    public void setTitle(CharSequence title) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[setTitle] title: " + title);

        mActivity.getWindow().setTitle(title);
    }

    
    public void setProgressBarVisibility(boolean visible) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[setProgressBarVisibility] visible: " + visible);

        mActivity.setProgressBarVisibility(visible);
    }

    
    public void setProgressBarIndeterminateVisibility(boolean visible) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[setProgressBarIndeterminateVisibility] visible: " + visible);

        mActivity.setProgressBarIndeterminateVisibility(visible);
    }

    
    public void setProgressBarIndeterminate(boolean indeterminate) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[setProgressBarIndeterminate] indeterminate: " + indeterminate);

        mActivity.setProgressBarIndeterminate(indeterminate);
    }

    
    public void setProgress(int progress) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[setProgress] progress: " + progress);

        mActivity.setProgress(progress);
    }

    
    public void setSecondaryProgress(int secondaryProgress) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[setSecondaryProgress] secondaryProgress: " + secondaryProgress);

        mActivity.setSecondaryProgress(secondaryProgress);
    }

    
    protected Context getThemedContext() {
        Context context = mActivity;
        TypedValue outValue = new TypedValue();
        mActivity.getTheme().resolveAttribute(android.R.attr.actionBarWidgetTheme, outValue, true);
        if (outValue.resourceId != 0) {
            //We are unable to test if this is the same as our current theme
            //so we just wrap it and hope that if the attribute was specified
            //then the user is intentionally specifying an alternate theme.
            context = new ContextThemeWrapper(context, outValue.resourceId);
        }
        return context;
    }

    
    public ActionMode startActionMode(com.actionbarsherlock.view.ActionMode.Callback callback) {
        if (ActionBarSherlock.DEBUG) Log.d(TAG, "[startActionMode] callback: " + callback);

        if (mActionMode != null) {
            mActionMode.finish();
        }
        ActionModeCallbackWrapper wrapped = null;
        if (callback != null) {
            wrapped = new ActionModeCallbackWrapper(callback);
        }

        //Calling this will trigger the callback wrapper's onCreate which
        //is where we will set the new instance to mActionMode since we need
        //to pass it through to the sherlock callbacks and the call below
        //will not have returned yet to store its value.
        if (mActivity.startActionMode(wrapped) == null) {
            mActionMode = null;
        }
        if (mActivity instanceof OnActionModeStartedListener && mActionMode != null) {
            ((OnActionModeStartedListener)mActivity).onActionModeStarted(mActionMode);
        }

        return mActionMode;
    }

    private class ActionModeCallbackWrapper implements android.view.ActionMode.Callback {
        private final ActionMode.Callback mCallback;

        public ActionModeCallbackWrapper(ActionMode.Callback callback) {
            mCallback = callback;
        }

        
        public boolean onCreateActionMode(android.view.ActionMode mode, android.view.Menu menu) {
            //See ActionBarSherlockNative#startActionMode
            mActionMode = new ActionModeWrapper(mode);

            return mCallback.onCreateActionMode(mActionMode, mActionMode.getMenu());
        }

        
        public boolean onPrepareActionMode(android.view.ActionMode mode, android.view.Menu menu) {
            return mCallback.onPrepareActionMode(mActionMode, mActionMode.getMenu());
        }

        
        public boolean onActionItemClicked(android.view.ActionMode mode, android.view.MenuItem item) {
            return mCallback.onActionItemClicked(mActionMode, mActionMode.getMenu().findItem(item));
        }

        
        public void onDestroyActionMode(android.view.ActionMode mode) {
            mCallback.onDestroyActionMode(mActionMode);
            if (mActivity instanceof OnActionModeFinishedListener) {
                ((OnActionModeFinishedListener)mActivity).onActionModeFinished(mActionMode);
            }
        }
    }

    private class ActionModeWrapper extends ActionMode {
        private final android.view.ActionMode mActionMode;
        private MenuWrapper mMenu = null;

        ActionModeWrapper(android.view.ActionMode actionMode) {
            mActionMode = actionMode;
        }

        
        public void setTitle(CharSequence title) {
            mActionMode.setTitle(title);
        }

        
        public void setTitle(int resId) {
            mActionMode.setTitle(resId);
        }

        
        public void setSubtitle(CharSequence subtitle) {
            mActionMode.setSubtitle(subtitle);
        }

        
        public void setSubtitle(int resId) {
            mActionMode.setSubtitle(resId);
        }

        
        public void setCustomView(View view) {
            mActionMode.setCustomView(view);
        }

        
        public void invalidate() {
            mActionMode.invalidate();
            if (mMenu != null) mMenu.invalidate();
        }

        
        public void finish() {
            mActionMode.finish();
        }

        
        public MenuWrapper getMenu() {
            if (mMenu == null) {
                mMenu = new MenuWrapper(mActionMode.getMenu());
            }
            return mMenu;
        }

        
        public CharSequence getTitle() {
            return mActionMode.getTitle();
        }

        
        public CharSequence getSubtitle() {
            return mActionMode.getSubtitle();
        }

        
        public View getCustomView() {
            return mActionMode.getCustomView();
        }

        
        public MenuInflater getMenuInflater() {
            return ActionBarSherlockNative.this.getMenuInflater();
        }

        
        public void setTag(Object tag) {
            mActionMode.setTag(tag);
        }

        
        public Object getTag() {
            return mActionMode.getTag();
        }
    }
}
