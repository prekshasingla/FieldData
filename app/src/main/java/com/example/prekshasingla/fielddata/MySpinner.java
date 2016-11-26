package com.example.prekshasingla.fielddata;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.Spinner;

/**
 * Created by prekshasingla on 7/25/2016.
 */
public class MySpinner  extends Spinner {
    private int lastSelected = 0;

    public MySpinner(Context context)
    { super(context); }

    public MySpinner(Context context, AttributeSet attrs)
    { super(context, attrs); }

    public MySpinner(Context context, AttributeSet attrs, int defStyle)
    { super(context, attrs, defStyle); }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(this.lastSelected == this.getSelectedItemPosition() && getOnItemSelectedListener() != null)
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), this.getSelectedItemPosition(), getSelectedItemId());
        if(!changed)
            lastSelected = this.getSelectedItemPosition();

        super.onLayout(changed, l, t, r, b);
    }
}
