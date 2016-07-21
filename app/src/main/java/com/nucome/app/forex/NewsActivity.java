package com.nucome.app.forex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.nucome.app.forex.helper.NewsInfo;
import com.nucome.app.forex.helper.SwipeNewsListAdapter;
public class NewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = NewsActivity.class.getSimpleName();
    private String URL_NEWS_INFO = "http://www.wuzhenweb.com:8089/json?operation=getfxnews";
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected SwipeNewsListAdapter adapter;
    protected ListView listView;
    protected List<NewsInfo> newsList;
    protected int offSet = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        listView = (ListView) findViewById(R.id.newsListView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        newsList = new ArrayList<>();
        adapter = new SwipeNewsListAdapter(this, newsList);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchNews(URL_NEWS_INFO);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;

    }

    @Override
    public void onRefresh() {
        fetchNews(URL_NEWS_INFO);
    }
    private void fetchNews(String url) {
        swipeRefreshLayout.setRefreshing(true);
        StringRequest req = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                if (response.length() > 0) {
                    try {
                    JSONObject newsObjs = new JSONObject(response.toString());
                    JSONArray rates = newsObjs.getJSONArray("content");
                        for (int i = 0; i < rates.length(); i++) {
                                JSONObject newsObj = rates.getJSONObject(i);
                                String title = newsObj.getString("title");
                                String description = newsObj.getString("description");
                                String pubDate = newsObj.getString("pubDate");
                                String source = newsObj.getString("source");

                            NewsInfo info = new NewsInfo(title, description, pubDate, source);
                                newsList.add(info);
                        }
                    adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                    }

                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        TradeApplication.getInstance().addToRequestQuest(req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.news:
                Intent registerIntent = new Intent(getApplicationContext(), NewsActivity.class);
                startActivity(registerIntent);
                return  true;
            case R.id.wsj:
                Intent loginIntent = new Intent(getApplicationContext(), WsjActivity.class);
                startActivity(loginIntent);
                return  true;
            case R.id.analysis:
                Intent calendarIntent = new Intent(getApplicationContext(), AnalysisActivity.class);
                startActivity(calendarIntent);
                return  true;
            case R.id.gold:
                Intent learningIntent = new Intent(getApplicationContext(), GoldActivity.class);
                startActivity(learningIntent);
                return  true;
            case R.id.future:
                Intent openAccountIntent = new Intent(getApplicationContext(), FutureActivity.class);
                startActivity(openAccountIntent);
                return true;
            case R.id.marketwatch:
                Intent userInfoIntent = new Intent(getApplicationContext(), MarketWatchActivity.class);
                startActivity(userInfoIntent);
                return true;
            case R.id.ForexLive:
                Intent forexLiveIntent = new Intent(getApplicationContext(), ForexLiveActivity.class);
                startActivity(forexLiveIntent);
                return true;
            case R.id.FxStreet:
                Intent fxStreetIntent = new Intent(getApplicationContext(), FxStreetActivity.class);
                startActivity(fxStreetIntent);
            default:
                return super.onOptionsItemSelected(item);

        }
    }


}
