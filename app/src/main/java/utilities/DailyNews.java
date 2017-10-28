package utilities;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael.
 */

public class DailyNews {

    public static List<String> news = new ArrayList<String>();
    public static List<String> newsLink = new ArrayList<String>();

    private String TAG = "new testing";

    public List<String> getNews() {
        return news;
    }

    public void setNews(List<String> news) {
        this.news = news;
    }

    public void getWebsiteNews(){
        new getNewsTask().execute();
    }

    public static List<String> getNewsLink() {
        return newsLink;
    }

    public static void setNewsLink(List<String> newsLink) {
        DailyNews.newsLink = newsLink;
    }

    public class getNewsTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... strings) {
            Document document = null;

            try {
                document = Jsoup.connect("https://www.coindesk.com/").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements article = document.select("h3");
            for (int i=0; i<article.size(); i++){
                news.add(article.get(i).getElementsByTag("h3").text());
                if (article.get(i).getElementsByTag("h3").select("a").attr("href") == ""){
                    newsLink.add("http://www.coindesk.com");
                } else{
                    newsLink.add(article.get(i).getElementsByTag("h3").select("a").attr("href"));
                }
//                Log.i(TAG, "this is the link in dailynews.java: "+article.get(i).getElementsByTag("h3").select("a").attr("href"));
            }

            return news;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


    }


}
