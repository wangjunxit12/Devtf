package com.example.devtf.devtf;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bean.ArticleDetail;
import butterknife.ButterKnife;
import butterknife.InjectView;
import db.DatabaseHelper;

/**
 * Created by Administrator on 2016/9/20.
 */

public class ContentActivity extends AppCompatActivity {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    @InjectView(R.id.webview)
    WebView webview;
    private String post_id;
    private String title;
    private String mJobUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finish();
            }
        });

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebSettings settings=webview.getSettings();
                settings.setBuiltInZoomControls(true);//放大缩小功能
                view.loadUrl(url);
                return true;
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(newProgress);

                if(newProgress==100){
                    progressbar.setVisibility(View.GONE);
                }

            }
        });
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null&&!bundle.containsKey("job_url")) {
            post_id = bundle.getString("post_id");
            title=bundle.getString("title");
            Log.i("Menuadapter", "onCreate: "+post_id);
        }else{
            mJobUrl=bundle.getString("job_url");
            webview.loadUrl(mJobUrl);
        }
        ArticleDetail detail=DatabaseHelper.getInstance().getArticleDetail(post_id);
        if(!TextUtils.isEmpty(detail.getContent())){
           loadArticletoWebView(detail.getContent());
        }else if(!TextUtils.isEmpty(post_id)){
            getArticleContent();
        }else{
            webview.loadUrl(mJobUrl);
        }
    }
    private void getArticleContent(){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... params) {
                HttpURLConnection urlConnection=null;
                try{
                    urlConnection= (HttpURLConnection) new URL(
                            "http://www.devtf.cn/api/v1/?type=article&post_id=" + post_id
                    ).openConnection();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()
                    ));
                    StringBuilder sb=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        sb.append(line).append("\n");
                    }
                    return  sb.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    urlConnection.disconnect();
                }
                return "";

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loadArticletoWebView(s);
                DatabaseHelper.getInstance().saveArticleDetail(new ArticleDetail(s,post_id));
            }
        }.execute();
    }
    private void loadArticletoWebView(String htmlContent){
        webview.loadDataWithBaseURL("",wrapHtml(title,htmlContent),"text/html","utf-8","404");
    }
    private static String wrapHtml(String title,String htmlcontent){
        final StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html dir=\"ltr\" lang=\"zh\">");
        sb.append("<head>");
        sb.append("<meta name=\"viewport\" content=\"width=100%; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\" />");
        sb.append("<link rel=\"stylesheet\" href='file:///android_asset/style.css' type=\"text/css\" media=\"screen\" />");
        sb.append("<link rel=\"stylesheet\" href='file:///android_asset/default.min.css' type=\"text/css\" media=\"screen\" />");
        sb.append("</head>");
        sb.append("<body style=\"padding:0px 8px 8px 8px;\">");
        sb.append("<div id=\"pagewrapper\">");
        sb.append("<div id=\"mainwrapper\" class=\"clearfix\">");
        sb.append("<div id=\"maincontent\">");
        sb.append("<div class=\"post\">");
        sb.append("<div class=\"posthit\">");
        sb.append("<div class=\"postinfo\">");
        sb.append("<h2 class=\"thetitle\">");
        sb.append("<a>");
        sb.append(title);
        sb.append("</a>");
        sb.append("</h2>");
        sb.append("<hr/>");
        sb.append("</div>");
        sb.append("<div class=\"entry\">");
        sb.append(htmlcontent);
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("<script src=\'file:///android_asset/highlight.pack.js\'></script>");
        sb.append("<script>hljs.initHighlightingOnLoad();</script>");
        sb.append("</body>");
        sb.append("</html>");
        Log.e("", "html : " + sb.toString());
        return sb.toString();
    }
}
