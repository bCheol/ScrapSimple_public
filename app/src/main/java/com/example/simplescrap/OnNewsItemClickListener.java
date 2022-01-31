package com.example.simplescrap;

import android.view.View;

public interface OnNewsItemClickListener {
    public void onItemClick(NewsAdapter.ViewHolder holder, View view, int position);
}
