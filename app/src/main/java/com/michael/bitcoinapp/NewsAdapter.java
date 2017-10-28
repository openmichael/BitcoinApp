package com.michael.bitcoinapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import utilities.DailyNews;

/**
 * Created by Michael.
 */

public class NewsAdapter extends RecyclerView.Adapter{

    private String TAG = "Debugggggggg";

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new NewsAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((NewsAdapter.ListViewHolder) holder).bindView(position);

    }

    @Override
    public int getItemCount() {
        return DailyNews.news.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mItemNews;

        public ListViewHolder(View itemView){
            super(itemView);
            mItemNews = (TextView) itemView.findViewById(R.id.item_news);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            mItemNews.setText(DailyNews.news.get(position));
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "the id is: "+view.getVerticalScrollbarPosition());
            Log.i(TAG, "the url is ");
            String url = DailyNews.newsLink.get(view.getVerticalScrollbarPosition());
            Log.i(TAG, "the url is: "+url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            view.getContext().startActivity(intent);

        }
    }


}
