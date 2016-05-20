package com.example.junze.kanzhihu.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.junze.kanzhihu.R;
import com.example.junze.kanzhihu.db.DBHelper;
import com.example.junze.kanzhihu.fragment.WebFragment;
import com.example.junze.kanzhihu.model.Answer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by junze on 16-05-17.
 */
public class FavoriteActivity  extends AppCompatActivity {
    private List<Answer> answers;
    private FavoriteAdapter adapter;
    private ListView lv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        DBHelper helper = DBHelper.getInstance(this);
        answers = helper.getAnswers();
        lv = (ListView) findViewById(R.id.favorite_lv);
        adapter = new FavoriteAdapter();
        lv.setAdapter(adapter);
    }
    private class FavoriteAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return answers.size();
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
                convertView = getLayoutInflater().inflate(R.layout.favorite_lv_item, viewGroup, false);
            }
            final Answer item = answers.get(position);
//            processViews(convertView, item);

            TextView title = (TextView) convertView.findViewById(R.id.post_lv_title);
            TextView summary = (TextView) convertView.findViewById(R.id.post_lv_content);
            ImageView img = (ImageView) convertView.findViewById(R.id.post_lv_img);
            title.setText(item.getTitle());
            summary.setText(item.getSummary());
            Picasso.with(FavoriteActivity.this).load(item.getAvatar()).into(img);

            final ImageView favorite = (ImageView) convertView.findViewById(R.id.favorite);
            final DBHelper helper = DBHelper.getInstance(FavoriteActivity.this);

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    answers = helper.getAnswers();
                    if (answers.contains(item)) {
                        helper.removeAnswer(item);
                        favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    } else {
                        helper.addAnswer(item);
                        favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WebFragment wf = WebFragment.newInstance(item.getQuestionId(),item.getAnswerId());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.parent_container, wf);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            return convertView;
        }
    }
}
