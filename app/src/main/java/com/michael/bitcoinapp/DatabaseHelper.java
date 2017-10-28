package com.michael.bitcoinapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import utilities.DailyData;
import utilities.HistoryData;
import utilities.NetworkUtilities;

/**
 * Created by Michael.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DATABASE_NAME = "bitcoin.db";

    public static final String TABLE_NAME1 = "daily_price";
    public static final String daliy_price_id = "id";
    public static final String daily_price_col1 = "time";
    public static final String daily_price_col2 = "price";

    public static final String TABLE_NAME2 = "history_price";
    public static final String history_price_id = "id";
    public static final String history_price_col1 = "date";
    public static final String history_price_col2 = "price";

    public String historyResult;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    }

    //Run first time creating database
    //Insert monthly history data from bitcoin api
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " +
                TABLE_NAME1 + " (" + daliy_price_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                daily_price_col1 + " TEXT, " +
                                daily_price_col2 + " REAL)");

        sqLiteDatabase.execSQL("CREATE TABLE " +
                TABLE_NAME2 + " (" + history_price_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                history_price_col1 + " TEXT, " +
                                history_price_col2 + " REAL)");

        String historyBicoinQuery = "https://api.coindesk.com/v1/bpi/historical/close.json";
        URL historyBitcoinUrl = null;
        try {
            historyBitcoinUrl = new URL(historyBicoinQuery);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            historyResult = new historyBitcoinQueryTask().execute(historyBitcoinUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ContentValues cv = new ContentValues();

        try {
            JSONObject reader = new JSONObject(historyResult);
            JSONObject bpi = reader.getJSONObject("bpi");

            Iterator keys = bpi.keys();
            while (keys.hasNext()){
                Object key = keys.next();
                String date = key.toString();
                String closePrice = bpi.getString(key.toString());

                cv.put("date", date);
                cv.put("price", Float.parseFloat(closePrice));
                sqLiteDatabase.insert("history_price", null, cv);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(sqLiteDatabase);
    }

    public class historyBitcoinQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            if (s != null && !s.equals("")){
                historyResult = s;
            }
        }
    }

    //Insert current bitcoin price and time to database
    public void insertCurrentPrice(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:ss");
        String currentTime = simpleDateFormat.format(date.getTime());

        String currentResult = getCurrentBitcoinPrice();
        String currentPrice = parseCurrentPriceJson(currentResult);

        cv.put("time", currentTime);
        cv.put("price", currentPrice);
        sqLiteDatabase.insert("daily_price", null, cv);
    }

    //Insert yesterday's price to database history table
    public void insertYesterdayPrice(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String yesterdayResult = getYesterdayBitcoinPrice();

        String yesterdayDate = null;
        String yesterdayPrice = null;
        try {
            JSONObject reader = new JSONObject(yesterdayResult);
            JSONObject bpi = reader.getJSONObject("bpi");

            Iterator keys = bpi.keys();
            Object key = keys.next();
            yesterdayDate = key.toString();
            yesterdayPrice = bpi.getString(key.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cv.put("date", yesterdayDate);
        cv.put("price", Float.parseFloat(yesterdayPrice));
        sqLiteDatabase.insert("history_price", null, cv);

    }

    private String getCurrentBitcoinPrice(){

        String currentResult = null;
        String currentBitcoinQuery = "https://api.coindesk.com/v1/bpi/currentprice.json";
        URL bitcoinUrl = null;
        try {
            bitcoinUrl = new URL(currentBitcoinQuery);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            currentResult = new currentBitcoinQueryTask().execute(bitcoinUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return currentResult;
    }

    public class currentBitcoinQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            if (s != null && !s.equals("")){

            }
        }
    }

    private String getYesterdayBitcoinPrice(){

        String currentResult = null;
        String currentBitcoinQuery = "https://api.coindesk.com/v1/bpi/historical/close.json?for=yesterday";
        URL bitcoinUrl = null;
        try {
            bitcoinUrl = new URL(currentBitcoinQuery);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            currentResult = new currentBitcoinQueryTask().execute(bitcoinUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return currentResult;
    }

    public class yesterdayBitcoinQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            if (s != null && !s.equals("")){

            }
        }
    }

    //Parse Json object retrieve from bitcoin api
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

    //Reset daily table in database
    public void resetTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //Drop table and recreate again to reset id
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        sqLiteDatabase.execSQL("CREATE TABLE " +
                TABLE_NAME1 + " (" + daliy_price_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                daily_price_col1 + " TEXT, " +
                daily_price_col2 + " REAL)");


    }

    //Check if price change in 5, 30, 60 minutes
    public int checkPriceChange(){

        SQLiteDatabase db = this.getWritableDatabase();

        String countRowQuery = "SELECT * FROM " + TABLE_NAME1;
        Cursor countCursor = db.rawQuery(countRowQuery, null);
        int row = countCursor.getCount();

        //If price change over 1% in 5 minutes
        if (row > 5){

            String selectQuery = "SELECT * FROM " + TABLE_NAME1 + " ORDER BY " + daliy_price_id + " DESC LIMIT 5";
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            String latestDBPriceString = cursor.getString(cursor.getColumnIndex("price"));
            latestDBPriceString = latestDBPriceString.replace(",", "");
            Float latestDBPrice = Float.parseFloat(latestDBPriceString);

            cursor.moveToLast();
            String fiveMinAgoDBPriceString = cursor.getString(cursor.getColumnIndex("price"));
            fiveMinAgoDBPriceString = fiveMinAgoDBPriceString.replace(",", "");
            Float fiveMinAgoDBPrice = Float.parseFloat(fiveMinAgoDBPriceString);

            if ((Math.abs(latestDBPrice - fiveMinAgoDBPrice)/fiveMinAgoDBPrice) > 0.01 && latestDBPrice > fiveMinAgoDBPrice){
                return 5;
            } else if ((Math.abs(latestDBPrice - fiveMinAgoDBPrice)/fiveMinAgoDBPrice) > 0.01 && latestDBPrice < fiveMinAgoDBPrice){
                return 6;
            }
        }

        //If price change over 1% in 30 minutes
        if (row > 30){

            String selectQuery = "SELECT * FROM " + TABLE_NAME1 + " ORDER BY " + daliy_price_id + " DESC LIMIT 30";
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            String latestDBPriceString = cursor.getString(cursor.getColumnIndex("price"));
            latestDBPriceString = latestDBPriceString.replace(",", "");
            Float latestDBPrice = Float.parseFloat(latestDBPriceString);

            cursor.moveToLast();
            String thirtyMinAgoDBPriceString = cursor.getString(cursor.getColumnIndex("price"));
            thirtyMinAgoDBPriceString = thirtyMinAgoDBPriceString.replace(",", "");
            Float thirtyMinAgoDBPrice = Float.parseFloat(thirtyMinAgoDBPriceString);

            if ((Math.abs(latestDBPrice - thirtyMinAgoDBPrice)/thirtyMinAgoDBPrice) > 0.01 && latestDBPrice > thirtyMinAgoDBPrice){
                return 3;
            } else if ((Math.abs(latestDBPrice - thirtyMinAgoDBPrice)/thirtyMinAgoDBPrice) > 0.01 && latestDBPrice < thirtyMinAgoDBPrice){
                return 4;
            }

        }

        //If price change over 1% in 60 minutes
        if (row > 60){

            String selectQuery = "SELECT * FROM " + TABLE_NAME1 + " ORDER BY " + daliy_price_id + " DESC LIMIT 60";
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            String latestDBPriceString = cursor.getString(cursor.getColumnIndex("price"));
            latestDBPriceString = latestDBPriceString.replace(",", "");
            Float latestDBPrice = Float.parseFloat(latestDBPriceString);

            cursor.moveToLast();
            String hourAgoDBPriceString = cursor.getString(cursor.getColumnIndex("price"));
            hourAgoDBPriceString = hourAgoDBPriceString.replace(",", "");
            Float hourAgoDBPrice = Float.parseFloat(hourAgoDBPriceString);

            if ((Math.abs(latestDBPrice - hourAgoDBPrice)/hourAgoDBPrice) > 0.01 && latestDBPrice > hourAgoDBPrice){
                return 1;
            } else if ((Math.abs(latestDBPrice - hourAgoDBPrice)/hourAgoDBPrice) > 0.01 && latestDBPrice < hourAgoDBPrice){
                return 2;
            }

        }

        return 0;

    }

    //Return daily max price
    public String getDayMax(){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String maxValueQuery = "SELECT MAX(price) FROM " + TABLE_NAME1;
        Cursor cursor = sqLiteDatabase.rawQuery(maxValueQuery, null);
        cursor.moveToFirst();

        return cursor.getString(0);
    }

    //Return daily min price
    public String getDayMin(){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String minValueQuery = "SELECT MIN(price) FROM " + TABLE_NAME1;
        Cursor cursor = sqLiteDatabase.rawQuery(minValueQuery, null);
        cursor.moveToFirst();

        return cursor.getString(0);
    }

    //Return daily percentage change
    public float calculatePercentageChangeDaily(Float currentPrice){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME2 + " ORDER BY " + history_price_id + " DESC LIMIT 1";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        String yesterdayDBPriceString = cursor.getString(cursor.getColumnIndex("price"));
        yesterdayDBPriceString = yesterdayDBPriceString.replace(",", "");
        Float yesterdayDBPrice = Float.parseFloat(yesterdayDBPriceString);

        Float percentage = (Math.abs(currentPrice - yesterdayDBPrice)/yesterdayDBPrice);

        if (currentPrice > yesterdayDBPrice){
            return percentage;
        }

        return (-1)*percentage;

    }

    //Return monthly percentage change
    public float calculatePercentageChangeMonthly(Float currentPrice){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME2 + " ORDER BY " + history_price_id + " DESC LIMIT 1";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int curId = cursor.getInt(0);
        cursor.close();
        int monthId = curId - 30;
        String selectMonthQuery = "SELECT * FROM " + TABLE_NAME2 + " WHERE id=" + monthId + " LIMIT 1";
        cursor = sqLiteDatabase.rawQuery(selectMonthQuery, null);
        cursor.moveToFirst();

        String lastMonthDBPriceString = cursor.getString(cursor.getColumnIndex("price"));
        lastMonthDBPriceString = lastMonthDBPriceString.replace(",", "");
        Float lastMonthDBPrice = Float.parseFloat(lastMonthDBPriceString);

        Float percentage = (Math.abs(currentPrice - lastMonthDBPrice)/lastMonthDBPrice);

        if (currentPrice > lastMonthDBPrice){
            return percentage;
        }

        return (-1)*percentage;

    }

    //Set history data for further use
    public void retrieveHistoryData(){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME2 + ";", null);;

        List<String> date = new ArrayList<String>();
        List<Float> price = new ArrayList<Float>();

        if (cursor.moveToFirst()){
            do {
                date.add(cursor.getString(cursor.getColumnIndex("date")));
                price.add(Float.parseFloat(cursor.getString(cursor.getColumnIndex("price"))));

            } while (cursor.moveToNext());
        }

        HistoryData historyData = new HistoryData();
        historyData.setDate(date);
        historyData.setPrice(price);

        cursor.close();
    }

    //Set daily data for further use
    public void retrieveDailyData(){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME1 + ";", null);;

        List<String> time = new ArrayList<String>();
        List<Float> price = new ArrayList<Float>();

        if (cursor.moveToFirst()){
            do {
                time.add(cursor.getString(cursor.getColumnIndex("time")));
                price.add(Float.parseFloat(cursor.getString(cursor.getColumnIndex("price")).replace(",", "")));

            } while (cursor.moveToNext());
        }

        DailyData dailyData = new DailyData();
        dailyData.setTime(time);
        dailyData.setPrice(price);

        cursor.close();
    }

}
