package utilities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import com.michael.bitcoinapp.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael.
 */

public class HistoryData {

    public static List<String> date = new ArrayList<String>();
    public static List<Float> price = new ArrayList<Float>();

    public static List<String> getDate() {
        return date;
    }

    public static void setDate(List<String> date) {
        HistoryData.date = date;
    }

    public static List<Float> getPrice() {
        return price;
    }

    public static void setPrice(List<Float> price) {
        HistoryData.price = price;
    }

    /*public static String[] date = {
            "2017-10-26",
            "2017-10-27"
    };

    public static Float[] price = {
            (float) 10.26,
            (float) 10.27
    };*/

    /*private int id;
    private String date;
    private float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        String floatPrice = Float.toString(price);
        return floatPrice;
    }

    public void setPrice(float price) {
        this.price = price;
    }*/
}
