package com.example.junze.kanzhihu.model;

import java.util.List;

/**
 * Created by junze on 16-05-12.
 */
public class RecentStories {
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Story> getTopStories() {
        return topStories;
    }

    public void setTopStories(List<Story> topStories) {
        this.topStories = topStories;
    }

    private String date;
    private List<Story> stories;
    private List<Story> topStories;

}
