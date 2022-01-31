package com.example.simplescrap;

import android.view.View;

public interface OnScrapItemClickListener {
    public void onItemClick(ScrapAdapter.ViewHolder holder, View view, int position);
}
