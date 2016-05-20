package com.example.junze.kanzhihu.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.junze.kanzhihu.parser.Parser;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junze on 16-05-13.
 */
public abstract class WorkerActivity<T> extends AppCompatActivity {

    protected ListView lv;
    protected WorkerAdapter adapter;

    protected int activityLayoutResourceId;
    protected int listViewResourceId;
    protected int listViewItemResourceId;

    protected String url;
    protected Parser parser;

    protected abstract void processViews(View convertView, T item);
    protected abstract void onClickCallBack(T item);
    protected abstract void postExecute(WorkerActivity<T> activity);

    protected class WorkerAdapter extends BaseAdapter {
        List<T> items = new ArrayList<>();

        public void setItems(List<T> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(listViewItemResourceId, viewGroup, false);
            }
            final T item = items.get(position);
            processViews(convertView, item);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickCallBack(item);
                }
            });
            return convertView;
        }
    }

    protected class WorkerTask extends AsyncTask<String, Integer, List<T>> {
        HttpURLConnection connection;
        StringBuilder jsonResults;
        Parser parser;

        public WorkerTask(Parser parser) {
            this.parser = parser;
        }

        @Override
        protected List<T> doInBackground(String... urls) {
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
                return parser.parse(jsonResults.toString());
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
        protected void onPostExecute(List<T> items) {
            super.onPostExecute(items);
            if (items != null) {
                adapter.setItems(items);
                postExecute(WorkerActivity.this);
            }
        }
    }
}
