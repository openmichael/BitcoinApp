package com.michael.bitcoinapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import utilities.HistoryData;

/**
 * Created by Michael.
 */

class HistoryPriceAdapter extends RecyclerView.Adapter{
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_price_list_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return HistoryData.date.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mItemDate;
        private TextView mItemPrice;

        public ListViewHolder(View itemView){
            super(itemView);
            mItemDate = (TextView) itemView.findViewById(R.id.item_date);
            mItemPrice = (TextView) itemView.findViewById(R.id.item_price);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            mItemDate.setText(HistoryData.date.get(position));
            mItemPrice.setText("$"+HistoryData.price.get(position).toString());
        }

        public void onClick(View view){

        }

    }
}
