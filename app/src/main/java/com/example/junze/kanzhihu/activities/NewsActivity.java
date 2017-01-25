package com.example.junze.kanzhihu.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.junze.kanzhihu.R;
import com.example.junze.kanzhihu.URLUtils;
import com.example.junze.kanzhihu.fragment.WebFragment;
import com.example.junze.kanzhihu.model.News;
import com.example.junze.kanzhihu.model.RecentStories;
import com.example.junze.kanzhihu.parser.AnswersParser;
import com.example.junze.kanzhihu.parser.NewsParser;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by junze on 16-05-12.
 */
public class NewsActivity extends AppCompatActivity {
    private int id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        String url = URLUtils.NEWS + id;
        System.out.println("url is: " + url);
        new getNewsTask().execute(url);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    private class getNewsTask extends AsyncTask<String,Integer, News> {
        HttpURLConnection connection;
        StringBuilder jsonResults;

        @Override
        protected News doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if (connection.getResponseCode() != 200) {
                    throw new IOException();
                }
                jsonResults = readResponse();
                System.out.println(jsonResults.toString());
                return new NewsParser().parse(jsonResults.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        private StringBuilder readResponse() throws IOException {
            StringBuilder jsonResults = new StringBuilder();
            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            return jsonResults;
        }

        @Override
        protected void onPostExecute(News news) {
            super.onPostExecute(news);
            if (news != null) {
                if (news.getType() == 0) {
                    // 非外链
                    WebFragment wf = WebFragment.newInstance(news.getBody(), news.getCss(), news.getImage(), news.getTitle(), news.getImageSource());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.news_parent_container, wf);
//              ft.addToBackStack(null);
                    ft.commit();
                } else {
                    // 文章外链
                    Log.i("外链","外链");
                    WebFragment wf = WebFragment.newInstance(news.getShare_url());
                    Log.i("外链", news.getShare_url());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.news_parent_container, wf);
                    ft.commit();
                }

            }
        }
    }

}
