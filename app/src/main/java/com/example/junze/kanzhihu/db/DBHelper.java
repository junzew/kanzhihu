package com.example.junze.kanzhihu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.junze.kanzhihu.model.Answer;
import com.example.junze.kanzhihu.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junze on 16-05-16.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String STRING = " VARCHAR(255) ";
    private static final String INT = " INT ";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Contract.PostEntry.TABLE_NAME + " (" +
                    Contract.PostEntry._ID + " INTEGER PRIMARY KEY," +
                    Contract.PostEntry.DATE + STRING + COMMA_SEP +
                    Contract.PostEntry.NAME + STRING + COMMA_SEP +
                    Contract.PostEntry.PIC + STRING + COMMA_SEP +
                    Contract.PostEntry.PUBLISH_TIME + INT + COMMA_SEP +
                    Contract.PostEntry.ANSWER_COUNT + INT + COMMA_SEP +
                    Contract.PostEntry.EXCERPT + STRING +
            " )";
    private static final String SQL_CREATE_ANSWERS_TABLE =
            "CREATE TABLE " + Contract.AnswerEntry.TABLE_NAME + " (" +
                    Contract.AnswerEntry._ID + " INTEGER PRIMARY KEY," +
                    Contract.AnswerEntry.TITLE + STRING + COMMA_SEP +
                    Contract.AnswerEntry.TIME + STRING + COMMA_SEP +
                    Contract.AnswerEntry.SUMMARY + STRING + COMMA_SEP +
                    Contract.AnswerEntry.QUESTION_ID + STRING + COMMA_SEP +
                    Contract.AnswerEntry.ANSWER_ID + STRING + COMMA_SEP +
                    Contract.AnswerEntry.AUTHOR_ID + STRING + COMMA_SEP +
                    Contract.AnswerEntry.AUTHOR_NAME + STRING + COMMA_SEP +
                    Contract.AnswerEntry.AUTHOR_HASH + STRING + COMMA_SEP +
                    Contract.AnswerEntry.AVATAR + STRING + COMMA_SEP +
                    Contract.AnswerEntry.VOTE + STRING +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Contract.PostEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES_ANSWER =
            "DROP TABLE IF EXISTS " + Contract.AnswerEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME =  "Posts.db";

    // singleton pattern
    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ANSWERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES_ANSWER);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    public void addPost(Post post) {

        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Contract.PostEntry.DATE, post.getDate());
            values.put(Contract.PostEntry.NAME, post.getName());
            values.put(Contract.PostEntry.EXCERPT, post.getExcerpt());
            values.put(Contract.PostEntry.PIC, post.getPic());
            values.put(Contract.PostEntry.PUBLISH_TIME, post.getPublishTime());
            values.put(Contract.PostEntry.ANSWER_COUNT, post.getAnswerCount());

            Cursor c = db.rawQuery("SELECT * FROM " + Contract.PostEntry.TABLE_NAME+ " WHERE " + Contract.PostEntry.PUBLISH_TIME+
                    "=" + post.getPublishTime(), null);
            if (c.getCount() > 0) {
                //Log.i("addpost", post.getName());
                db.update(Contract.PostEntry.TABLE_NAME, values, Contract.PostEntry.PUBLISH_TIME + " = ? ",
                        new String[] {post.getPublishTime()+""});
            } else {
                // Insert the new row, returning the primary key value of the new row
                db.insert(Contract.PostEntry.TABLE_NAME, null, values);
            }
            c.close();

            db.setTransactionSuccessful();
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
    public List<Post> queryPosts() {
        ArrayList<Post> posts = new ArrayList<Post>();
        Cursor c = queryTheCursor();
        try {
            while (c.moveToNext()) {
                Post p = new Post();
                p.setDate(c.getString(c.getColumnIndex(Contract.PostEntry.DATE)));
                p.setName(c.getString(c.getColumnIndex(Contract.PostEntry.NAME)));
                p.setPic(c.getString(c.getColumnIndex(Contract.PostEntry.PIC)));
                p.setPublishTime(c.getInt(c.getColumnIndex(Contract.PostEntry.PUBLISH_TIME)));
                p.setAnswerCount(c.getInt(c.getColumnIndex(Contract.PostEntry.ANSWER_COUNT)));
                p.setExcerpt(c.getString(c.getColumnIndex(Contract.PostEntry.EXCERPT)));
                posts.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return posts;
    }

    /**
     * queryPosts all posts, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.PostEntry.TABLE_NAME +
                " ORDER BY " + Contract.PostEntry.DATE + " DESC", null);
        return c;
    }

    public void addAnswer(Answer a) {
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Contract.AnswerEntry.TITLE, a.getTitle());
            values.put(Contract.AnswerEntry.TIME, a.getTime());
            values.put(Contract.AnswerEntry.SUMMARY, a.getSummary());
            values.put(Contract.AnswerEntry.QUESTION_ID, a.getQuestionId());
            values.put(Contract.AnswerEntry.ANSWER_ID, a.getAnswerId());
            values.put(Contract.AnswerEntry.AUTHOR_ID, a.getAuthorId());
            values.put(Contract.AnswerEntry.AUTHOR_NAME, a.getAuthorName());
            values.put(Contract.AnswerEntry.AUTHOR_HASH, a.getAuthorHash());
            values.put(Contract.AnswerEntry.AVATAR, a.getAvatar());
            values.put(Contract.AnswerEntry.VOTE, a.getVote());

            Cursor c = db.rawQuery("SELECT * FROM " + Contract.AnswerEntry.TABLE_NAME+ " WHERE " + Contract.AnswerEntry.TITLE+
                    "=" + "'" + a.getTitle() + "'", null);
            if (c.getCount() > 0) {
                //Log.i("addpost", post.getName());
                db.update(Contract.AnswerEntry.TABLE_NAME, values, Contract.AnswerEntry.TITLE + " = ? ",
                        new String[] {a.getTitle()+""});
            } else {
                // Insert the new row, returning the primary key value of the new row
                db.insert(Contract.AnswerEntry.TABLE_NAME, null, values);
            }
            c.close();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public List<Answer> getAnswers() {
        List<Answer> answers = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.AnswerEntry.TABLE_NAME, null);
        try {
            while (c.moveToNext()) {
                Answer a = new Answer();
                a.setTitle(c.getString(c.getColumnIndex(Contract.AnswerEntry.TITLE)));
                a.setTime(c.getString(c.getColumnIndex(Contract.AnswerEntry.TIME)));
                a.setSummary(c.getString(c.getColumnIndex(Contract.AnswerEntry.SUMMARY)));
                a.setQuestionId(c.getString(c.getColumnIndex(Contract.AnswerEntry.QUESTION_ID)));
                a.setAnswerId(c.getString(c.getColumnIndex(Contract.AnswerEntry.ANSWER_ID)));
                a.setAuthorId(c.getString(c.getColumnIndex(Contract.AnswerEntry.AUTHOR_ID)));
                a.setAuthorName(c.getString(c.getColumnIndex(Contract.AnswerEntry.AUTHOR_NAME)));
                a.setAuthorHash(c.getString(c.getColumnIndex(Contract.AnswerEntry.AUTHOR_HASH)));
                a.setVote(c.getString(c.getColumnIndex(Contract.AnswerEntry.VOTE)));
                a.setAvatar(c.getString(c.getColumnIndex(Contract.AnswerEntry.AVATAR)));
                answers.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return answers;
    }
    public boolean removeAnswer(Answer a) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(Contract.AnswerEntry.TABLE_NAME, Contract.AnswerEntry.TITLE + "=" + "'" + a.getTitle()+"'", null) > 0;
    }
}
