package com.example.junze.kanzhihu.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.junze.kanzhihu.EndlessRecyclerViewScrollListener;
import com.example.junze.kanzhihu.R;
import com.example.junze.kanzhihu.URLUtils;
import com.example.junze.kanzhihu.activities.NewsActivity;
import com.example.junze.kanzhihu.model.Story;
import com.example.junze.kanzhihu.parser.StoryParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by junze on 16-05-12.
 */
public class ZhiHuRiBaoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View layout;
    //private MainListViewAdapter adapter;
    // private ListView lv;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private RVAdapter adapter;
    private String url;
    private SwipeRefreshLayout srl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        layout = inflater.inflate(R.layout.zhihuribao_fragment, container, false);
        init();
        return layout;
    }

    private void init() {
        // lv = (ListView) layout.findViewById(R.id.zhihuribao_lv);
        // adapter = new MainListViewAdapter();
        // lv.setAdapter(adapter);
        srl = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        srl.setColorSchemeResources(R.color.colorPrimary);
        srl.setDistanceToTriggerSync(300);
        srl.setProgressBackgroundColorSchemeResource(R.color.colorAccent);
        srl.setSize(SwipeRefreshLayout.DEFAULT);
        srl.setOnRefreshListener(this);
        url = URLUtils.LATEST_NEWS;
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(true);
                new LoadingTask().execute(url);
            }
        });
        // RecyclerView
        rv = (RecyclerView) layout.findViewById(R.id.recycler_view);
        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        adapter = new RVAdapter();
        rv.setAdapter(adapter);

        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Toast.makeText(getActivity(),"load more",Toast.LENGTH_SHORT).show();
                String url = URLUtils.NEWS_BEFORE;
                String dateString = StoryParser.date;
                String year = dateString.substring(0, 4);
                String month = dateString.substring(4, 6);
                String day = dateString.substring(6);
                int y = Integer.parseInt(year) - 1900;
                int m = Integer.parseInt(month) - 1;
                int d = Integer.parseInt(day);

                Calendar cal = Calendar.getInstance();
                cal.set(y, m, d);
                cal.add(Calendar.DAY_OF_YEAR, -1);

                Log.i("YEAR", y + "");
                Log.i("MONTH", m + "");
                Log.i("DAY", d + "");

                y = cal.get(Calendar.YEAR);
                m = cal.get(Calendar.MONTH);
                d = cal.get(Calendar.DATE);
                Log.i("YEAR", y + "");
                Log.i("MONTH", m + "");
                Log.i("DAY", d + "");

                year = Integer.toString(y + 1900);
                month = Integer.toString(m + 1);
                if (m >= 0 && m <= 9) {
                    month = "0" + month;
                }
                day = Integer.toString(d);
                if (d >= 1 && d <= 9) {
                    day = "0" + day;
                }
                String dayBefore = year + month + day;
                url += dayBefore;
                Log.i("DayBefore", dayBefore);
                new LoadingTask().execute(url);
                int curSize = adapter.getItemCount();
                adapter.notifyItemRangeInserted(curSize, adapter.stories.size() - 1);
            }
        });

    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.StoryViewHolder> {
        List<Story> stories = new ArrayList<>();

        public void addStories(List<Story> stories) {
            for (Story s : stories) {
                if (!this.stories.contains(s))
                    this.stories.add(s);
            }
            notifyDataSetChanged();
        }

        public void setStories(List<Story> stories) {
            this.stories = stories;
            Log.i("LENGTH", "" + stories.size());
            notifyDataSetChanged();
        }

        @Override
        public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StoryViewHolder(getActivity().getLayoutInflater().inflate(R.layout.zhihuribao_list_view_item, parent, false));
        }

        @Override
        public void onBindViewHolder(StoryViewHolder holder, int position) {
            Story s = stories.get(position);
            holder.bindStory(s);
        }


        @Override
        public int getItemCount() {
            return stories.size();
        }

        public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView title;
            private ImageView img;
            private Story mStory;

            public void bindStory(Story story) {
                mStory = story;
                title.setText(story.getTitle());
                Picasso.with(getActivity()).load(story.getImages().get(0)).into(img);
                itemView.setOnClickListener(this);
            }

            public StoryViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.zhihuribao_lv_title);
                img = (ImageView) itemView.findViewById(R.id.zhihuribao_lv_img);
            }

            @Override
            public void onClick(View view) {
                Intent newsIntent = new Intent(getActivity(), NewsActivity.class);
                newsIntent.putExtra("id", mStory.getId());
                startActivity(newsIntent);
            }
        }
    }

    @Override
    public void onRefresh() {
        new LoadingTask().execute(url);
    }

    //    private class MainListViewAdapter extends BaseAdapter {
//        List<Story> stories = new ArrayList<>();
//
//        public void setStories(List<Story> stories) {
//            this.stories = stories;
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public int getCount() {
//            return stories.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup viewGroup) {
//            if (convertView == null) {
//                convertView = getActivity().getLayoutInflater().inflate(R.layout.zhihuribao_list_view_item, viewGroup, false);
//            }
//            final Story story = stories.get(position);
//            TextView title = (TextView) convertView.findViewById(R.id.zhihuribao_lv_title);
//            ImageView img = (ImageView) convertView.findViewById(R.id.zhihuribao_lv_img);
//            title.setText(story.getTitle());
//            Picasso.with(getActivity()).load(story.getImages().get(0)).into(img);
//
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Uri uri = Uri.parse("http://news-at.zhihu.com/api/4/news/" + story.getId());
////                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
////                    startActivity(intent);
//                    Intent newsIntent = new Intent(getActivity(), NewsActivity.class);
//                    newsIntent.putExtra("id", story.getId());
//                    startActivity(newsIntent);
//                }
//            });
//            return convertView;
//        }
//
//    }
    private class LoadingTask extends AsyncTask<String, Integer, List<Story>> {
        HttpURLConnection connection;
        StringBuilder jsonResults;

        @Override
        protected List<Story> doInBackground(String... urls) {
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
                return new StoryParser().parse(jsonResults.toString());
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
        protected void onPostExecute(List<Story> stories) {
            super.onPostExecute(stories);
            if (stories != null) {
                adapter.addStories(stories);
            }
            srl.setRefreshing(false);
        }
    }
}
