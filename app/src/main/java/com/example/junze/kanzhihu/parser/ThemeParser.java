package com.example.junze.kanzhihu.parser;

import com.example.junze.kanzhihu.model.Story;
import com.example.junze.kanzhihu.model.ThemeContent;
import com.example.junze.kanzhihu.model.ThemeListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junze on 16-05-13.
 */
public class ThemeParser  implements Parser<ThemeContent> {
    public ThemeListItem parseThemeListItem(JSONObject o) throws JSONException {
        ThemeListItem item = new ThemeListItem();
        item.setId(o.getInt("id"));
        item.setName(o.getString("name"));
        item.setDescription(o.getString("description"));
        item.setThumbnail(o.getString("thumbnail"));
        return item;
    }

    /*
* {
"limit": 1000,
"subscribed": [ ],
"others": [
{
    "color": 8307764,
    "thumbnail": "http://pic4.zhimg.com/2c38a96e84b5cc8331a901920a87ea71.jpg",
    "description": "内容由知乎用户推荐，海纳主题百万，趣味上天入地",
    "id": 12,
    "name": "用户推荐日报"
},
...
]

thumbnail : 供显示的图片地址
description : 主题日报的介绍
id : 该主题日报的编号
name : 供显示的主题日报名称
}*/
    public List<ThemeListItem> parseThemeList(String jsonString) throws JSONException {
        JSONObject o = new JSONObject(jsonString);
        JSONArray others = o.getJSONArray("others");
        List<ThemeListItem> list = new ArrayList<>();
        for (int i = 0; i < others.length(); i++) {
            list.add(parseThemeListItem(others.getJSONObject(i)));
        }
        return list;
    }

    @Override
    public List<ThemeContent> parse(String jsonString) throws JSONException, IOException {
        JSONObject o = new JSONObject(jsonString);
        List<ThemeContent> results = new ArrayList<>();
        JSONArray stories = o.getJSONArray("stories");
        for (int i = 0; i < stories.length(); i++) {
            results.add(parseThemeContent(stories.getJSONObject(i)));
        }
        return results;
    }

    private ThemeContent parseThemeContent(JSONObject o) throws JSONException {
        ThemeContent s = new ThemeContent();
        //List<String> images = new ArrayList<>();
        s.setTitle(o.getString("title"));
        s.setId(o.getInt("id"));
        try {
            s.setImages(o.getJSONArray("images").getString(0));
        } catch(JSONException e) {
            e.printStackTrace();
            s.setImages("");
        }

       // System.out.println("<<<<<<<<<<>>>>>>>>>>>>>>>" + s.toString());

        return s;
    }
}
