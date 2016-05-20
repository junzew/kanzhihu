package com.example.junze.kanzhihu.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.junze.kanzhihu.EndlessScrollListener;
import com.example.junze.kanzhihu.R;
import com.example.junze.kanzhihu.activities.PostActivity;
import com.example.junze.kanzhihu.db.DBHelper;
import com.example.junze.kanzhihu.model.Post;
import com.example.junze.kanzhihu.parser.PostsParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junze on 16-05-12.
 */
public class KanZhiHuFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String LOAD_DATA_IN_BACKGROUND_ACTION = "LOAD_DATA_IN_BACKGROUND_ACTION";
    private MainListViewAdapter adapter;
    private String url;
    private ListView lv;

    private View layout;
    private SwipeRefreshLayout srl;
    private BroadcastReceiver receiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        layout = inflater.inflate(R.layout.kanzhihu_fragment, container, false);
        init();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.receiver = new KanZhiHuFragment.DataReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter(LOAD_DATA_IN_BACKGROUND_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    private void init() {
        lv = (ListView) layout.findViewById(R.id.main_lv);
        adapter = new MainListViewAdapter();
        lv.setAdapter(adapter);

        url = "http://api.kanzhihu.com/getposts/";

        DBHelper helper = DBHelper.getInstance(getActivity());
        adapter.addPosts(helper.queryPosts());

        srl = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        srl.setColorSchemeResources(R.color.colorPrimary);
        srl.setDistanceToTriggerSync(300);
        srl.setProgressBackgroundColorSchemeResource(R.color.colorAccent);
        srl.setSize(SwipeRefreshLayout.LARGE);
        srl.setOnRefreshListener(this);
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(true);
                doRefresh();
            }
        });

        lv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                int size = adapter.posts.size();
                Post lastPosted = adapter.posts.get(size-1);
                int lastPostedPublishtime = lastPosted.getPublishTime();
                String url = "http://api.kanzhihu.com/getposts/" + lastPostedPublishtime;
                new LoadingTask().execute(url);
                return true;
            }
        });
    }


    @Override
    public void onRefresh() {
        doRefresh();
    }
    private void doRefresh() {
        new LoadingTask().execute(url);
    }

    static class ViewHolder {
        TextView title;
        TextView excerpt;
        ImageView  img;

    }
    private class MainListViewAdapter extends BaseAdapter {
        List<Post> posts = new ArrayList<>();

        public void setPosts(List<Post> posts) {
            this.posts = posts;
            notifyDataSetChanged();
        }
        public void addPosts(List<Post> posts) {
            for (Post p : posts) {
                if (!this.posts.contains(p)) {
                    this.posts.add(p);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return posts.size();
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
            // use ViewHolder to improve performance
            ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.kanzhihu_main_list_view_item, viewGroup, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.lv_item_title);
                holder.excerpt =  (TextView) convertView.findViewById(R.id.lv_item_excerpt);
                holder.img = (ImageView) convertView.findViewById(R.id.lv_item_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Post post = posts.get(position);
//            TextView title = (TextView) convertView.findViewById(R.id.lv_item_title);
//            TextView excerpt = (TextView) convertView.findViewById(R.id.lv_item_excerpt);
//            ImageView img = (ImageView) convertView.findViewById(R.id.lv_item_img);
            holder.title.setText(formatTitle(post.getDate(), post.getName()));

            if (post.getTitles() != null) {
                holder.excerpt.setText(formatExcerpt(post));
            } else {
                holder.excerpt.setText(post.getExcerpt());
            }
            try {
                Picasso.with(getActivity()).load(post.getPic()).into(holder.img);
            } catch(Exception e) {
                e.printStackTrace();
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postIntent = new Intent(getActivity(), PostActivity.class);
                    postIntent.putExtra("name", post.getName());
                    postIntent.putExtra("date", formatDate(post.getDate()));
                    startActivity(postIntent);
                }
            });
            return convertView;
        }
        private String formatDate(String date) {
            // 2014-09-27  20140927
            // 0123456789
            String result = date.substring(0,4) + date.substring(5,7)+date.substring(8);
            System.out.println(result);
            return result;
        }
        private String formatExcerpt(Post post) {
            StringBuilder acc = new StringBuilder("摘录了: \n");

            List<String> titles = post.getTitles();
            if (titles != null) {
                for (String title : titles) {
                    acc.append("「").append(title).append("」").append("\n");
                }
            }

            acc.append("等问题下的").append(Integer.toString(post.getAnswerCount())).append("个答案");
            return acc.toString();
        }

        private String formatTitle (String date, String type) {
            StringBuilder sb = new StringBuilder();
            sb.append(date + " ");
            if (type.equals("recent")) {
                sb.append("近日热门");
            } else if (type.equals("yesterday")) {
                sb.append("昨日更新");
            } else if (type.equals("archive")) {
                sb.append("历史精华");
            }
            return sb.toString();
        }
    }
    private class LoadingTask extends AsyncTask<String, Integer, List<Post>> {
        HttpURLConnection connection;
        StringBuilder jsonResults;

        @Override
        protected List<Post> doInBackground(String... urls) {
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
                return new PostsParser().parse(jsonResults.toString());
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
        protected void onPostExecute(List<Post> posts) {
            super.onPostExecute(posts);
            if (posts != null) {
                adapter.addPosts(posts);
                DBHelper helper = DBHelper.getInstance(getActivity());
                for (int i = 0; i < posts.size(); i++) {
                    helper.addPost(posts.get(i));
                }
            }
            srl.setRefreshing(false);
        }
    }
    public class DataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("onReceive", "receiving");
            DBHelper helper = DBHelper.getInstance(getActivity());
            //adapter.setPosts(helper.queryPosts());
        }
    }
}
