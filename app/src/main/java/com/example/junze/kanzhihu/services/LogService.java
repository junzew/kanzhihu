package com.example.junze.kanzhihu.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;

import com.example.junze.kanzhihu.db.DBHelper;
import com.example.junze.kanzhihu.fragment.KanZhiHuFragment;
import com.example.junze.kanzhihu.model.Post;
import com.example.junze.kanzhihu.parser.PostsParser;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junze on 16-05-16.
 */
public class LogService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Update Database with latest posts every 5 minutes
            while(true) {
                long endTime = System.currentTimeMillis() + 300 * 1000;
                while (System.currentTimeMillis() < endTime) {
                    synchronized (this) {
                        try {
                            wait(endTime - System.currentTimeMillis());
                            Log.i("loading", "loading ... ");
                            new LoadingTask("http://api.kanzhihu.com/getposts/");
                            Intent i = new Intent(KanZhiHuFragment.LOAD_DATA_IN_BACKGROUND_ACTION);
                            sendBroadcast(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            // stopSelf(msg.arg1);
        }
    }


    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);

        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LogService", "onStartCommand");
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("LogService", "onDestroy");
        // Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    private class LoadingTask {
        HttpURLConnection connection;
        StringBuilder jsonResults;
        List<Post> posts = new ArrayList<>();

        public LoadingTask(String u) {
            try {
                URL url = new URL(u);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if (connection.getResponseCode() != 200) {
                    throw new IOException();
                }
                jsonResults = readResponse();
                System.out.println(jsonResults.toString());
                posts = new PostsParser().parse(jsonResults.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            if (posts != null) {
                DBHelper helper = DBHelper.getInstance(getApplicationContext());
                for (int i = 0; i < posts.size(); i++) {
                    helper.addPost(posts.get(i));
                }
            }
        }
        private StringBuilder readResponse() throws IOException {
            StringBuilder jsonResults = new StringBuilder();
            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            return jsonResults;
        }
    }
}
