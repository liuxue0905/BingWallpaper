package com.lx.iruanmi.bingwallpaper.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liuxue on 2015/3/12.
 */
public class LXRadioGroup extends RadioGroup {
//    private static final String TAG = "LXRadioGroup";

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;

    public LXRadioGroup(Context context) {
        super(context);
        init();
    }

    public LXRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private static List<LXRadioButton> findChildren(ViewGroup viewGroup) {
        List<LXRadioButton> children = new ArrayList<>();

        int N = viewGroup.getChildCount();
        for (int i = 0; i < N; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof LXRadioButton) {
                children.add((LXRadioButton) child);
            } else if (child instanceof ViewGroup) {
                children.addAll(findChildren((ViewGroup) child));
            }
        }

        return children;
    }

    /**
     * Generate a value suitable for use in {@link #setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
//        // the user listener is delegated to our pass-through listener
//        mPassThroughListener.mOnHierarchyChangeListener = listener;
//    }

    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    /**
     * <p>Register a callback to be invoked when the checked radio button
     * changes in this group.</p>
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
        mOnCheckedChangeListener = listener;
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
//        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        //@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void onChildViewAdded(View parent, View child) {
//            Log.d(TAG, "child:" + child);
            if (parent == LXRadioGroup.this && child instanceof LXRadioButton) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
//                    id = View.generateViewId();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        id = View.generateViewId();
                    } else {
                        id = generateViewId();
                    }
                    child.setId(id);
                }
                ((LXRadioButton) child).setOnCheckedChangeWidgetListener(
                        mChildOnCheckedChangeListener);
            } else if (parent == LXRadioGroup.this && child instanceof ViewGroup) {
                List<LXRadioButton> children = findChildren((ViewGroup) child);
//                Log.d(TAG, "children:" + children);
                for (LXRadioButton view : children) {
                    ((LXRadioButton) view).setOnCheckedChangeWidgetListener(
                            mChildOnCheckedChangeListener);
                }
            }

//            if (mOnHierarchyChangeListener != null) {
//                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
//            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            if (parent == LXRadioGroup.this && child instanceof LXRadioButton) {
                ((LXRadioButton) child).setOnCheckedChangeWidgetListener(null);
            } else if (parent == LXRadioGroup.this && child instanceof ViewGroup) {
                List<LXRadioButton> children = findChildren((ViewGroup) child);
                for (LXRadioButton view : children) {
                    ((LXRadioButton) view).setOnCheckedChangeWidgetListener(
                            null);
                }
            }

//            if (mOnHierarchyChangeListener != null) {
//                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
//            }
        }
    }
}
