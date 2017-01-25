package com.example.junze.kanzhihu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.junze.kanzhihu.R;
import com.example.junze.kanzhihu.db.DBHelper;
import com.example.junze.kanzhihu.fragment.WebFragment;
import com.example.junze.kanzhihu.model.Answer;
import com.example.junze.kanzhihu.parser.AnswersParser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.junze.kanzhihu.URLUtils.HTTPS_API_KANZHIHU_COM_GETPOSTANSWERS;

/**
 * Created by junze on 16-05-12.
 */
public class PostActivity extends WorkerActivity<Answer>  {

    private List<Answer> favorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        this.parser = new AnswersParser();
        this.activityLayoutResourceId = R.layout.activity_post;
        this.listViewResourceId = R.id.post_lv;
        this.listViewItemResourceId = R.layout.kanzhihu_post_list_view_item;

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        this.url = HTTPS_API_KANZHIHU_COM_GETPOSTANSWERS + date + "/" + name;

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
    protected void processViews(View convertView, final Answer item) {
        TextView title = (TextView) convertView.findViewById(R.id.post_lv_title);
        TextView summary = (TextView) convertView.findViewById(R.id.post_lv_content);
        ImageView img = (ImageView) convertView.findViewById(R.id.post_lv_img);
        title.setText(item.getTitle());
        summary.setText(item.getSummary());
        try{
            Picasso.with(PostActivity.this).load(item.getAvatar()).into(img);
        } catch(Exception e) {
            e.printStackTrace();
            img.setImageResource(R.color.background);
        }

        final ImageView favorite = (ImageView) convertView.findViewById(R.id.favorite);
        final DBHelper helper = DBHelper.getInstance(PostActivity.this);
        favorites = helper.getAnswers();

        if (favorites.contains(item)) {
            favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorites = helper.getAnswers();
                if (favorites.contains(item)) {
                    favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    helper.removeAnswer(item);

                } else {
                    favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
//                    favorites.add(item);
                    helper.addAnswer(item);

                }

            }
        });
    }

    @Override
    protected void onClickCallBack(Answer item) {
        WebFragment wf = WebFragment.newInstance(item.getQuestionId(),item.getAnswerId());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.parent_container, wf);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void postExecute(WorkerActivity<Answer> activity) {
       // List<Answer> l  = activity.adapter.items;
    }
}
