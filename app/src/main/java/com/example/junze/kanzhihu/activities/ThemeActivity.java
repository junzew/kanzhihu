package com.example.junze.kanzhihu.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.junze.kanzhihu.R;
import com.example.junze.kanzhihu.URLUtils;
import com.example.junze.kanzhihu.fragment.WebFragment;
import com.example.junze.kanzhihu.model.ThemeContent;
import com.example.junze.kanzhihu.parser.ThemeParser;
import com.squareup.picasso.Picasso;

/**
 * Created by junze on 16-05-13.
 */
public class ThemeActivity extends WorkerActivity<ThemeContent> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activityLayoutResourceId = R.layout.activity_theme;
        this.listViewItemResourceId = R.layout.selected_theme_lv_item;
        this.listViewResourceId = R.id.selected_theme_lv;
        this.parser = new ThemeParser();
        int id = getIntent().getIntExtra("id", 0);
        this.url = URLUtils.THEME + id;

        setContentView(activityLayoutResourceId);
        lv = (ListView) findViewById(listViewResourceId);
        adapter = new WorkerAdapter();
        lv.setAdapter(adapter);

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

        new WorkerTask(parser).execute(url);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    protected void processViews(View convertView, ThemeContent item) {
        TextView tv = (TextView) convertView.findViewById(R.id.selected_theme_lv_item_text);
        tv.setText(item.getTitle());
        ImageView imageView = (ImageView) convertView.findViewById(R.id.selected_theme_lv_item_image);
        Log.d("ThemeActivity", item.getImages());
        imageView.setImageResource(R.color.background);
        try {
            Picasso.with(this).load(item.getImages()).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO Bug: 图片第一张复用
    }

    @Override
    protected void onClickCallBack(ThemeContent item) {
       // String url = "http://news-at.zhihu.com/api/4/news/" + item.getId();
//        WebFragment wf = WebFragment.newInstance(url);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.theme_parent_container, wf);
////              ft.addToBackStack(null);
//        ft.commit();
        Intent newsIntent = new Intent(this, NewsActivity.class);
        newsIntent.putExtra("id", item.getId());
        startActivity(newsIntent);
    }

    @Override
    protected void postExecute(WorkerActivity<ThemeContent> activity) {

    }
}
