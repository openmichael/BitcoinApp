package com.michael.bitcoinapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Michael.
 */

public class HistoryPriceFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_price_list_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.history_list_recycle_view);

        HistoryPriceAdapter historyPriceAdapter = new HistoryPriceAdapter();

        recyclerView.setAdapter(historyPriceAdapter);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

}
