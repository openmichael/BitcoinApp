package com.michael.bitcoinapp;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import utilities.NetworkUtilities;


public class CurrentPriceFragment extends Fragment {


    public CurrentPriceFragment() {

    }

    private TextView mBitcoinPrice;
    private ProgressBar mLoadingBar;
    private TextView mMaxBitcoinPrice;
    private TextView mMinBitcoinPrice;
    private TextView mPercentageDaily;
    private TextView mPercentageMonthly;

    public String currentPrice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.current_price_fragment, container, false);
        mBitcoinPrice = (TextView) view.findViewById(R.id.bitcoin_price);
        mLoadingBar = (ProgressBar) view.findViewById(R.id.loading_bar);
        mMaxBitcoinPrice = (TextView) view.findViewById(R.id.max_day_price);
        mMinBitcoinPrice = (TextView) view.findViewById(R.id.min_day_price);
        mPercentageDaily = (TextView) view.findViewById(R.id.percentage_daily);
        mPercentageMonthly = (TextView) view.findViewById(R.id.percentage_monthly);

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        databaseHelper.insertCurrentPrice();

        priceUpdateThread.start();

        return view;
    }

    Thread priceUpdateThread = new Thread(){

        @Override
        public void run() {

            try{
                while (!isInterrupted()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getCurrentBitcoinPrice();
                            getMaxMinPrice();
                            getPercentage();
                        }
                    });
                    Thread.sleep(10000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    };

    class currentBitcoinQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String searchResults = null;

            try{
                searchResults = NetworkUtilities.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e){
                e.printStackTrace();
            }

            return searchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            mLoadingBar.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")){
                String parseResult = null;
                parseResult = parseCurrentPriceJson(s);
                mBitcoinPrice.setText("$ " + parseResult);
            } else{
                mBitcoinPrice.setText("Not able to get price now, try again later...");
            }
        }
    }

    private String parseCurrentPriceJson(String s){
        String price = null;
        try {
            JSONObject reader = new JSONObject(s);
            JSONObject bpi = reader.getJSONObject("bpi");
            JSONObject USD = bpi.getJSONObject("USD");
            price = USD.getString("rate");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return price;
    }

    private void getCurrentBitcoinPrice(){

        String currentBitcoinQuery = "https://api.coindesk.com/v1/bpi/currentprice.json";
        URL bitcoinUrl = null;
        try {
            bitcoinUrl = new URL(currentBitcoinQuery);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            currentPrice = new currentBitcoinQueryTask().execute(bitcoinUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //Get max and min price of today
    private void getMaxMinPrice(){

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

        mMaxBitcoinPrice.setText("Daily Max Price: $ " + databaseHelper.getDayMax());
        mMinBitcoinPrice.setText("Daily Min Price: $ " + databaseHelper.getDayMin());
    }

    //Get percentage change monthly and daily
    private void getPercentage(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

        String cur = parseCurrentPriceJson(currentPrice);
        cur = cur.replace(",", "");

        Float percentageDaily = databaseHelper.calculatePercentageChangeDaily(Float.parseFloat(cur));
        percentageDaily *= 100;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(4);
        String dailyResult = df.format(percentageDaily);

        Float percentageMonthly = databaseHelper.calculatePercentageChangeMonthly(Float.parseFloat(cur));
        percentageMonthly *= 100;
        df.setMaximumFractionDigits(4);
        String monthlyResult = df.format(percentageMonthly);

        mPercentageDaily.setText("% Daily: " + dailyResult + "%");
        mPercentageMonthly.setText("% Monthly: " + monthlyResult + "%");

    }

}
