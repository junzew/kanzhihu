package com.example.junze.kanzhihu.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.junze.kanzhihu.R;
import com.example.junze.kanzhihu.URLUtils;
import com.example.junze.kanzhihu.activities.ThemeActivity;
import com.example.junze.kanzhihu.model.ThemeListItem;
import com.example.junze.kanzhihu.parser.ThemeParser;
import com.squareup.picasso.Picasso;

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
public class ThemeFragment extends Fragment {
    private View layout;
    private MainListViewAdapter adapter;
    private ListView lv;
    private String url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        layout = inflater.inflate(R.layout.theme_fragment, container, false);
        init();
        return layout;
    }
    private void init() {
        lv = (ListView) layout.findViewById(R.id.theme_list);
        adapter = new MainListViewAdapter();
        lv.setAdapter(adapter);

        url = URLUtils.THEMES;
        new LoadingTask().execute(url);
    }
    private class MainListViewAdapter extends BaseAdapter {
        List<ThemeListItem> themeListItems = new ArrayList<>();

        public void setThemeListItems(List<ThemeListItem> themeListItems) {
            this.themeListItems = themeListItems;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return themeListItems.size();
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.theme_list_view_item, viewGroup, false);
            }
            final ThemeListItem story = themeListItems.get(position);
            TextView title = (TextView) convertView.findViewById(R.id.theme_lv_title);
            ImageView img = (ImageView) convertView.findViewById(R.id.theme_lv_img);
            title.setText(story.getName());
            Picasso.with(getActivity()).load(story.getThumbnail()).into(img);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Uri uri = Uri.parse("http://news-at.zhihu.com/api/4/news/" + story.getId());
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                    Intent newsIntent = new Intent(getActivity(), NewsActivity.class);
//                    newsIntent.putExtra("id", story.getId());
//                    startActivity(newsIntent);
                    Intent intent = new Intent(getActivity(), ThemeActivity.class);
                    intent.putExtra("id", story.getId());
                    startActivity(intent);
                }
            });
            return convertView;
        }

    }
    private class LoadingTask extends AsyncTask<String, Integer, List<ThemeListItem>> {
        HttpURLConnection connection;
        StringBuilder jsonResults;

        @Override
        protected List<ThemeListItem> doInBackground(String... urls) {
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
                return new ThemeParser().parseThemeList(jsonResults.toString());
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
        protected void onPostExecute(List<ThemeListItem> stories) {
            super.onPostExecute(stories);
            if (stories != null) {
                adapter.setThemeListItems(stories);
            }
        }
    }

}
