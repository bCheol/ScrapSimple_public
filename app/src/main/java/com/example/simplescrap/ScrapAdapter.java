package com.example.simplescrap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScrapAdapter extends RecyclerView.Adapter<ScrapAdapter.ViewHolder> implements OnScrapItemClickListener {

    ArrayList<ScrapData> items = new ArrayList<>();
    OnScrapItemClickListener listener;

    @NonNull
    @Override
    public ScrapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_scrap, parent, false);
        return new ScrapAdapter.ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrapAdapter.ViewHolder holder, int position) {
        ScrapData item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ScrapData item){
        items.add(item);
    }

    public ScrapData getItem(int position){
        return items.get(position);
    }

    public void clear(){
        items.clear();
    }

    public void setOnItemClickListener(OnScrapItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ScrapAdapter.ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView scrapTitle, newsTitle;

        public ViewHolder(@NonNull View itemView, final OnScrapItemClickListener listener) {
            super(itemView);
            scrapTitle = itemView.findViewById(R.id.scrapTitle);
            newsTitle = itemView.findViewById(R.id.newsTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition() ;
                    if(listener != null){
                        listener.onItemClick(ScrapAdapter.ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(ScrapData item){
            scrapTitle.setText(item.getScrapTitle());
            newsTitle.setText(item.getNewsTitle());
        }
    }
}