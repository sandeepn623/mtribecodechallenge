package com.mtribe.carfeedapp.ui;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Created by Sandeepn on 02-12-2017.
 */

public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
