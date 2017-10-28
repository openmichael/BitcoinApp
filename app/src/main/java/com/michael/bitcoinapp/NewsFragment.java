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

public class NewsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.news_list_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.news_list_recycle_view);

        NewsAdapter newsAdapter = new NewsAdapter();

        recyclerView.setAdapter(newsAdapter);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

}
