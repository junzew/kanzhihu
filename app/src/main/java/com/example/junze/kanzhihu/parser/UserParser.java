package com.example.junze.kanzhihu.parser;

import com.example.junze.kanzhihu.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junze on 16-05-20.
 */
public class UserParser implements Parser<User> {
    @Override
    public List<User> parse(String jsonString) throws JSONException, IOException {
        List<User> users = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        if (jsonObject.optString("error").equals("no user")) {
            return users;
        } else {
            JSONArray jsonUsers = jsonObject.optJSONArray("users");
            for (int i = 0; i < jsonUsers.length(); i++) {
                users.add(parseUser(jsonUsers.getJSONObject(i)));
            }
        }
        return users;
    }

    private User parseUser(JSONObject jsonObject) throws JSONException {
        User u = new User();
        u.setId(jsonObject.optString("id"));
        u.setName(jsonObject.optString("name"));
        u.setHash(jsonObject.optString("hash"));
        u.setAvatar(jsonObject.optString("avatar"));
        u.setSignature(jsonObject.optString("signature"));
        u.setAgree(jsonObject.optInt("agree"));
        u.setAnswer(jsonObject.optInt("answer"));
        u.setFollower(jsonObject.optInt("follower"));
        return u;
    }
}
