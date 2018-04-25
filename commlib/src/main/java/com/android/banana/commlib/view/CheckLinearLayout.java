package com.android.banana.commlib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * 类似checkbox的linearLayout可以使用selector的drawable但是文字不会改变颜色
 */
public class CheckLinearLayout extends LinearLayout implements Checkable {

    private boolean mChecked;

    private static final String TAG = CheckLinearLayout.class.getCanonicalName();

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    public CheckLinearLayout(final Context context) {
        super(context);
        init();
    }

    public CheckLinearLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CheckLinearLayout(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){
        setClickable(true);
        setLongClickable(true);

    }

    @Override
    public void setChecked(final boolean checked) {
        if (checked != mChecked) {
            mChecked = checked;
            refreshDrawableState();
        }

    }

    @Override
    protected int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final Drawable drawable = getBackground();
        if (drawable != null) {
            final int[] myDrawableState = getDrawableState();
            drawable.setState(myDrawableState);
            invalidate();
        }
    }

    @Override
    public boolean performClick() {
        setChecked(!mChecked);
        return super.performClick();
    }

    @Override
    public boolean performLongClick() {
        return super.performLongClick();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState savedState = new SavedState(superState);
        savedState.checked = isChecked();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(final Parcelable state) {
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setChecked(savedState.checked);
        requestLayout();
    }

    // /////////////
    // SavedState //
    // /////////////
    private static class SavedState extends BaseSavedState {
        boolean checked;
        @SuppressWarnings("unused")
        public static final Creator<SavedState> CREATOR;

        static {
            CREATOR = new Creator<SavedState>() {
                @Override
                public SavedState createFromParcel(final Parcel in) {
                    return new SavedState(in);
                }

                @Override
                public SavedState[] newArray(final int size) {
                    return new SavedState[size];
                }
            };
        }

        SavedState(final Parcelable superState) {
            super(superState);
        }

        private SavedState(final Parcel in) {
            super(in);
            checked = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return TAG + ".SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " checked=" + checked + "}";
        }
    }
}