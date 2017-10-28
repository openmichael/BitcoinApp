package utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael.
 */

public class DailyData {

    public static List<String> time = new ArrayList<String>();
    public static List<Float> price = new ArrayList<Float>();

    public static List<String> getTime() {
        return time;
    }

    public static void setTime(List<String> date) {
        DailyData.time = date;
    }

    public static List<Float> getPrice() {
        return price;
    }

    public static void setPrice(List<Float> price) {
        DailyData.price = price;
    }
}
