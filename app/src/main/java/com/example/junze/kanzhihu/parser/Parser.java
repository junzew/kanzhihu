package com.example.junze.kanzhihu.parser;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by junze on 16-05-13.
 */
public interface Parser<T> {
     List<T> parse(String jsonString) throws JSONException, IOException;
}
