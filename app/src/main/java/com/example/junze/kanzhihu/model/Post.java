package com.example.junze.kanzhihu.model;

import java.util.List;

/**
 * Created by junze on 16-05-11.
 */
public class Post {
    private String date; // 发表日期（yyyy-mm-dd）
    private String name; // 文章名称（yesterday, recent, archive）
    private String pic;  // 抬头图url
    private int publishTime; // 发表时间戳
    private int answerCount; // 文章包含答案数量
    private String excerpt; // 摘要文字

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        if (publishTime != post.publishTime) return false;
        if (answerCount != post.answerCount) return false;
        if (!date.equals(post.date)) return false;
        if (!name.equals(post.name)) return false;
        if (!pic.equals(post.pic)) return false;
        if (!excerpt.equals(post.excerpt)) return false;
        return titles != null ? titles.equals(post.titles) : post.titles == null;

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + pic.hashCode();
        result = 31 * result + publishTime;
        result = 31 * result + answerCount;
        result = 31 * result + excerpt.hashCode();
        result = 31 * result + (titles != null ? titles.hashCode() : 0);
        return result;
    }

    private List<String> titles; // 摘录文章标题

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public Post() {}

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(int publishTime) {
        this.publishTime = publishTime;
    }
}
