package com.example.junze.kanzhihu.db;

import android.provider.BaseColumns;

/**
 * Created by junze on 16-05-16.
 */
public final class Contract {
    public static abstract class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "posts";
        public static final String DATE = "date";
        public static final String NAME = "name";
        public static final String PIC = "pic";
        public static final String PUBLISH_TIME = "publish";
        public static final String ANSWER_COUNT = "answer";
        public static final String EXCERPT = "excerpt";
    }
    public static abstract class AnswerEntry implements BaseColumns {
        public static final String TABLE_NAME = "answers";
        public static final String TITLE = "title";
        public static final String TIME = "time";
        public static final String SUMMARY = "summary";
        public static final String QUESTION_ID = "questionid";
        public static final String ANSWER_ID = "answerid";
        public static final String AUTHOR_ID = "authorid";
        public static final String AUTHOR_NAME = "authorname";
        public static final String AUTHOR_HASH = "authorhash";
        public static final String AVATAR = "avatar";
        public static final String VOTE = "vote";
    }
}
