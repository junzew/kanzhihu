package com.example.junze.kanzhihu.parser;

import com.example.junze.kanzhihu.model.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junze on 16-05-12.
 */
public class StoryParser {
    /*
    * {
    date: "20140523",
    stories: [
        {
            title: "中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）",
            ga_prefix: "052321",
            images: [
                "http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg"
            ],
            type: 0,
            id: 3930445
        },
    ...
    ],
    top_stories: [
        {
            title: "商场和很多人家里，竹制家具越来越多（多图）",
            image: "http://p2.zhimg.com/9a/15/9a1570bb9e5fa53ae9fb9269a56ee019.jpg",
            ga_prefix: "052315",
            type: 0,
            id: 3930883
        },
    ...
    ]
}*/
    public static String date;

    public List<Story> parse(String jsonString) throws IOException, JSONException {
        List<Story> stories = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        String date = jsonObject.optString("date");
        this.date = date;
        JSONArray jsonArray = jsonObject.getJSONArray("stories");
        for (int i = 0; i < jsonArray.length(); i++) {
            stories.add(parseStory(jsonArray.getJSONObject(i)));
        }
        return stories;

    }
    public List<Story> parseTopStories(String jsonString) throws IOException, JSONException {
        List<Story> stories = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("top_stories");
        for (int i = 0; i < jsonArray.length(); i++) {
            stories.add(parseTopStory(jsonArray.getJSONObject(i)));
        }
        return stories;
    }

    public Story parseStory(JSONObject o) throws JSONException {
        Story s = new Story();
        List<String> images = new ArrayList<>();
        s.setTitle(o.getString("title"));
        s.setId(o.getInt("id"));
        s.setType(o.getInt("type"));
        //s.setGaPrefix(o.getString("ga_prefix"));
        JSONArray imagesJSONArray = o.getJSONArray("images");
        for (int i = 0; i < imagesJSONArray.length(); i++) {
            images.add(imagesJSONArray.getString(i));
        }
        s.setImages(images);
        return s;
    }
    public Story parseTopStory(JSONObject o ) throws JSONException {
        Story s = new Story();
        List<String> images = new ArrayList<>();
        s.setTitle(o.getString("title"));
        s.setId(o.getInt("id"));
        s.setType(o.getInt("type"));
        s.setGaPrefix(o.getString("ga_prefix"));
        images.add(o.getString("image"));
        s.setImages(images);
        return s;
    }
}
