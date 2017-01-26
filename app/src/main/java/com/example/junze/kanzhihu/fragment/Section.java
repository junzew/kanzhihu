package com.example.junze.kanzhihu.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.junze.kanzhihu.R;
import com.example.junze.kanzhihu.activities.NewsActivity;
import com.example.junze.kanzhihu.model.Story;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by junze on 2017-01-24.
 */

public class Section extends StatelessSection {

    private List<Story> mStories = new ArrayList<>();
    String date;
    private Context mContext;


    public Section(List<Story> stories, String date, Context context) {
        super(R.layout.section_header, R.layout.zhihuribao_list_view_item);
        this.mStories = stories;
        this.date = date;
        this.mContext = context;
    }

    @Override
    public int getContentItemsTotal() {
        return mStories.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new StoryViewHolder(view);
    }
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        StoryViewHolder itemHolder = (StoryViewHolder) holder;
        final Story story = mStories.get(position);
        itemHolder.title.setText(story.getTitle());
        Picasso.with(mContext).load(story.getImages().get(0)).into(itemHolder.img);
        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent newsIntent = new Intent(mContext, NewsActivity.class);
                newsIntent.putExtra("id", story.getId());
                mContext.startActivity(newsIntent);
            }
        });
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindHeaderViewHolder(holder);
        HeaderViewHolder itemHolder = (HeaderViewHolder) holder;
        itemHolder.tv.setText(date);
    }

    class StoryViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView img;
        View rootView;

        public StoryViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            title = (TextView) itemView.findViewById(R.id.zhihuribao_lv_title);
            img = (ImageView) itemView.findViewById(R.id.zhihuribao_lv_img);
        }
    }
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public final TextView tv;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.tv = (TextView) itemView.findViewById(R.id.tvTitle); // TODO
        }
    }
}
