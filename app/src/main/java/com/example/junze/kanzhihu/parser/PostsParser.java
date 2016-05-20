package com.example.junze.kanzhihu.parser;

import com.example.junze.kanzhihu.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by junze on 16-05-11.
 */
public class PostsParser {


    public List<Post> parse(String jsonString) throws JSONException, IOException {
        List<Post> posts = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray;
        if (jsonObject.getString("error").equals("")) {
            jsonArray = jsonObject.getJSONArray("posts");
            for (int i = 0; i < jsonArray.length(); i++) {
                posts.add(parsePost(jsonArray.getJSONObject(i)));
            }
        } else {
            throw new IOException();
        }
        return posts;
    }

    private Post parsePost(JSONObject o) throws JSONException {
        Post p = new Post();
        p.setAnswerCount(o.getInt("count"));
        p.setDate(o.getString("date"));
        String excerpt = o.getString("excerpt");
        parseExcerpt(p, excerpt);
        p.setExcerpt(excerpt);
        p.setName(o.getString("name"));
        p.setPic(o.getString("pic"));
        p.setPublishTime(o.getInt("publishtime"));
        return p;
    }
    private void parseExcerpt(Post p,String excerpt) {
        List<String> titles = new ArrayList<>();

        Pattern pattern = Pattern.compile("『(.*?)』");
        Matcher matcher = pattern.matcher(excerpt);

        while(matcher.find()) {
           titles.add(matcher.group(1));
        }
        p.setTitles(titles);
    }
}
