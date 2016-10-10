package com.example.devtf.devtf;

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

import bean.ArticleDetail;
import bean.DefaultParser;
import butterknife.ButterKnife;
import butterknife.InjectView;
import db.DatabaseHelper;
import listener.DataListener;

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
            fetchArticlContentAsync();
        }else{
            webview.loadUrl(mJobUrl);
        }
    }
    DefaultParser parser=new DefaultParser();
    private void fetchArticlContentAsync(){
        String reqUrl="http://www.devtf.cn/api/v1/?type=article&post_id=" + post_id;
        HttpFlinger.get(reqUrl, parser, new DataListener<String>() {
            @Override
            public void onComplete(String result) {
                loadArticletoWebView(result);
                DatabaseHelper.getInstance().saveArticleDetail(new ArticleDetail(result,post_id));
            }
        });
    }

    private void loadArticletoWebView(String htmlContent){
        webview.loadDataWithBaseURL("",HtmlUtils.wrapHtml(title,htmlContent),"text/html","utf-8","404");
    }

}
