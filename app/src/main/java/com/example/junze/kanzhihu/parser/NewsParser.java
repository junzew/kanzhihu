package com.example.junze.kanzhihu.parser;

import com.example.junze.kanzhihu.model.News;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junze on 16-05-13.
 */
public class NewsParser {
    public News parse(String jsonString) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject(jsonString);
        News n = new News();
        int type = jsonObject.optInt("type");
        n.setType(type);
        if (type == 0) {
            n.setBody(jsonObject.optString("body"));
            // TODO 有时候会外链
            n.setImageSource(jsonObject.optString("image_source"));
            n.setTitle(jsonObject.optString("title"));
            n.setImage(jsonObject.optString("image"));
            String css = jsonObject.optJSONArray("css").optString(0);
            n.setCss(css);
            n.setShare_url(jsonObject.optString("share_url"));
        } else if (type == 1) {
            n.setTitle(jsonObject.optString("title"));
            n.setShare_url(jsonObject.optString("share_url"));
        }
        return n;
    }
}
