package com.example.junze.kanzhihu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.junze.kanzhihu.R;

/**
 * Created by junze on 16-05-12.
 */
public class WebFragment extends Fragment {

    private WebView wv;
    private View layout;

    public static WebFragment newInstance(String questionId, String answerId) {
        WebFragment wf = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("questionId", questionId);
        bundle.putString("answerId", answerId);
        bundle.putString("type", "ids");
        wf.setArguments(bundle);
        return wf;
    }

    public static WebFragment newInstance(String body, String css, String img, String title, String imgSource) {
        // body is html
        WebFragment wf = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("body", body);
        bundle.putString("css", css);
        bundle.putString("img", img);
        bundle.putString("imgSource", imgSource);
        bundle.putString("title", title);
        bundle.putString("type", "html");
        wf.setArguments(bundle);
        return wf;
    }
    public static WebFragment newInstance(String url) {
        WebFragment wf = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "url");
        bundle.putString("url", url);
        wf.setArguments(bundle);
        return wf;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        layout = inflater.inflate(R.layout.web_fragment, container, false);
        init();
        return layout;
    }
    private void init() {
        wv = (WebView) layout.findViewById(R.id.web_view);
        if (getArguments().getString("type").equals("ids")) {
            String questionid = getArguments().getString("questionId");
            String answerid = getArguments().getString("answerId");
            String url = "http://www.zhihu.com/question/" + questionid+"/answer/" + answerid;
            System.out.println("loading url:" + url);
            wv.loadUrl(url);
        } else if (getArguments().getString("type").equals("html")){
            String body = getArguments().getString("body");
            String css = getArguments().getString("css");
            String img = getArguments().getString("img");
            body = "<link rel=\"stylesheet\" href=\"" + css + "\"/>" + body;
            String imgSource = getArguments().getString("imgSource");
            String title = getArguments().getString("title");
            body = insertImage(body, img, imgSource, title);
            //int index = body.indexOf("img-place-holder");
            //String html = body.substring(0, index+18) + "<img class=\"img-place-holder\" src=\"" + img + "\"/>" + body.substring(index+18);
            String html = body;
            //System.out.println(html);
            String mime = "text/html";
            String encoding = "utf-8";
            wv.getSettings().setJavaScriptEnabled(true);
            wv.loadDataWithBaseURL(null, html, mime, encoding, null);
        }  else {
            String url = getArguments().getString("url");
            wv.loadUrl(url);
        }
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wv.loadUrl(url);
                return true;
            }
        });

    }

    private String insertImage(String body, String img, String imgSource, String title) {
        try {
            String placeHolder = "<div class=\"img-place-holder\"></div>";
            int index = body.indexOf(placeHolder);
            String insert = "<div class=\"img-wrap\">\n" +
                    "\t\t<h1 class=\"headline-title\">"+ title + "</h1>\n" +
                    "\n" +
                    "\n" +
                    "\t\t<span class=\"img-source\">"+ imgSource +"</span>\n" +
                    "\n" +
                    "\n" +
                    "\t\t<img src=\""+img+"\" alt=\"\">\n" +
                    "\t\t<div class=\"img-mask\"></div>\n" +
                    "\t</div>";
            body = body.substring(0, index) + insert + body.substring(index + placeHolder.length());
            Log.i("HTML", body);
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

}
