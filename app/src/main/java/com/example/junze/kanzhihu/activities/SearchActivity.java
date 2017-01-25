package com.example.junze.kanzhihu.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.junze.kanzhihu.R;
import com.example.junze.kanzhihu.URLUtils;
import com.example.junze.kanzhihu.fragment.WebFragment;
import com.example.junze.kanzhihu.model.User;
import com.example.junze.kanzhihu.parser.UserParser;
import com.squareup.picasso.Picasso;

import static com.example.junze.kanzhihu.URLUtils.HTTPS_API_KANZHIHU_COM_SEARCHUSER;

/**
 * Created by junze on 16-05-20.
 */
public class SearchActivity extends WorkerActivity<User> {
    private Toolbar myToolbar;
    private ListView lv;
    private EditText et;
    private Button bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }
    private void init() {
        initToolbar();
        initViews();
        this.activityLayoutResourceId = R.layout.activity_search;
        this.listViewItemResourceId = R.layout.search_lv_item;
        this.listViewResourceId = R.id.search_results;
        this.parser = new UserParser();
        this.url = HTTPS_API_KANZHIHU_COM_SEARCHUSER;
        adapter = new WorkerAdapter();
        lv.setAdapter(adapter);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = et.getText().toString();
                // Check if no view has focus:
                View v = SearchActivity.this.getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                new WorkerTask(parser).execute(url + input);
//                WebFragment wf = WebFragment.newInstance("https://www.zhihu.com/search?type=people&q=" + input);
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.search_parent_container, wf);
//                ft.commit();
            }
        });
    }
    private void initToolbar() {
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
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
    private void initViews() {
        lv = (ListView) findViewById(R.id.search_results);
        et = (EditText) findViewById(R.id.search_text);
        bt = (Button) findViewById(R.id.search_button);
    }


    @Override
    protected void processViews(View convertView, User item) {
        TextView tv = (TextView) convertView.findViewById(R.id.user_name);
        tv.setText(item.getName());
        TextView answerTv = (TextView) convertView.findViewById(R.id.user_answer);
        answerTv.setText(""+item.getAnswer());
        TextView agreeTv = (TextView) convertView.findViewById(R.id.user_agree);
        agreeTv.setText(""+item.getAgree());
        TextView followerTv = (TextView) convertView.findViewById(R.id.user_follower);
        followerTv.setText(""+item.getFollower());
        ImageView img = (ImageView) convertView.findViewById(R.id.user_avatar);
        try {
            Picasso.with(this).load(item.getAvatar()).into(img);
        } catch (Exception e) {
            e.printStackTrace();
            img.setImageResource(R.color.background);
        }
    }

    @Override
    protected void onClickCallBack(User user) {
        String url = URLUtils.PEOPLE+ user.getId();
        WebFragment wf = WebFragment.newInstance(url);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.search_parent_container, wf);
        ft.commit();
    }

    @Override
    protected void postExecute(WorkerActivity<User> activity) {

    }
}
