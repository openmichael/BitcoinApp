package com.michael.bitcoinapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Michael.
 */

//Service running at background
public class StroreDailyPrice extends IntentService {

    private static final String TAG = "service test";

    public StroreDailyPrice() {
        super("StroreDailyPrice");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //Log.i(TAG, "The service has started");

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        //Notification setup
        Context context = getApplicationContext();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, DrawerActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        String title = "BitCoin App Notification";

        String priceChanged = null;

        //Reset database time setup
        SimpleDateFormat simpleDateFormatHour = new SimpleDateFormat("HH");
        SimpleDateFormat simpleDateFormatMin = new SimpleDateFormat("ss");
        String startOfDayHour = "01";
        String startOfDayMin = "02";

        int countTime = 0;

        //Run insert data loop (every 1 minute)
        while (true){

            Date date = new Date();
            String currentTimeHour = simpleDateFormatHour.format(date.getTime());
            String currentTimeMin = simpleDateFormatMin.format(date.getTime());

            if (startOfDayHour.equals(currentTimeHour) && startOfDayMin.equals(currentTimeMin)){
                databaseHelper.resetTable();
                databaseHelper.insertYesterdayPrice();
            }

            //Determine whether percentage change enough to notify user
            try {
                Log.i(TAG, "The service has started");

                //Insert price into database every minute
                databaseHelper.insertCurrentPrice();
                countTime += 1;

                if (countTime == 30){
                    int changedCase = databaseHelper.checkPriceChange();

                    switch (changedCase){
                        case 0:
                            break;
                        case 1:
                            priceChanged = "BitCoin price has increased 1% in an hour";
                            break;
                        case 2:
                            priceChanged = "BitCoin price has decreased 1% in an hour";
                            break;
                        case 3:
                            priceChanged = "BitCoin price has decreased 1% in 30 minutes";
                            break;
                        case 4:
                            priceChanged = "BitCoin price has decreased 1% in 30 minutes";
                            break;
                        case 5:
                            priceChanged = "BitCoin price has decreased 1% in 5 minutes";
                            break;
                        case 6:
                            priceChanged = "BitCoin price has decreased 1% in 5 minutes";
                            break;
                        default:
                            break;
                    }

                    //Pop-up notification setup
                    if (changedCase > 0){
                        Notification notification = new Notification.Builder(context)
                                .setContentIntent(pendingIntent)
                                .setContentText(priceChanged)
                                .setContentTitle(title)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setContentIntent(pendingIntent)
                                .build();

                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, notification);
                    }

                    countTime = 0;
                }

                //For debug
                //Thread.sleep(20000);
                //Wait for 1 min and redo the process again
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
