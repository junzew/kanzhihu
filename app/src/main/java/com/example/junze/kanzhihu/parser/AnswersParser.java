package com.example.junze.kanzhihu.parser;

import com.example.junze.kanzhihu.model.Answer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junze on 16-05-12.
 */
public class AnswersParser implements Parser {
    public List<Answer> parse(String jsonString) throws JSONException, IOException {
        List<Answer> answers = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray;
        if (jsonObject.getString("error").equals("")) {
            jsonArray = jsonObject.getJSONArray("answers");
            for (int i = 0; i < jsonArray.length(); i++) {
                answers.add(parseAnswer(jsonArray.getJSONObject(i)));
            }
        } else {
            throw new IOException();
        }
        return answers;
    }

    private Answer parseAnswer(JSONObject jsonObject) throws JSONException {
        Answer answer = new Answer();
        answer.setTitle(jsonObject.getString("title"));
        answer.setTime(jsonObject.getString("time"));
        answer.setSummary(jsonObject.getString("summary"));
        answer.setQuestionId(jsonObject.getString("questionid"));
        answer.setAnswerId(jsonObject.getString("answerid"));
        answer.setAuthorName(jsonObject.getString("authorname"));
        answer.setAuthorHash(jsonObject.getString("authorhash"));
        answer.setAvatar(jsonObject.getString("avatar"));
        answer.setVote(jsonObject.getString("vote"));
        return answer;
    }
}
