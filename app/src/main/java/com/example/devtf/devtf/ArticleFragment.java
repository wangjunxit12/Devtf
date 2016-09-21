package com.example.devtf.devtf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import adapters.ArticleAdapter;
import bean.Article;
import butterknife.ButterKnife;
import butterknife.InjectView;
import db.DatabaseHelper;
import listener.OnItemClickListener;
import view.AutoLoadRecyclerview;

/**
 * Created by Administrator on 2016/9/13.
 */
public class ArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
, AutoLoadRecyclerview.OnLoadListener{
    @InjectView(R.id.fragment_article_rc)
    AutoLoadRecyclerview fragmentArticleRc;
    @InjectView(R.id.fragment_article_swipe)
    SwipeRefreshLayout fragmentArticleSwipe;
    ArticleAdapter adapter;
    List<Article> mDataSet=new ArrayList<>();
    private int mPageIndex=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        ButterKnife.inject(this, view);
        initRefreshView();
        initAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataSet.addAll(DatabaseHelper.getInstance().getArticles());
        adapter.notifyDataSetChanged();
    }

    private void initRefreshView(){
        fragmentArticleSwipe.setRefreshing(true);
        fragmentArticleSwipe.setOnRefreshListener(this);
        fragmentArticleRc.setLayoutManager(new LinearLayoutManager(getActivity()
                .getApplicationContext()));
        fragmentArticleRc.setHasFixedSize(true);
        fragmentArticleRc.setVisibility(View.VISIBLE);
        fragmentArticleRc.setLoadListener(this);
    }
    private void initAdapter(){
        adapter=new ArticleAdapter(mDataSet);
         adapter.setOnItemClickListener(new OnItemClickListener<Article>() {
             @Override
             public void OnClick(Article item) {
                 if(item!=null){
                     loadArticle(item);
                 }
             }
         });
        fragmentArticleRc.setAdapter(adapter);
        getArticles(1);

      }
    private  void loadArticle(Article item){
        Intent intent=new Intent(getActivity(),ContentActivity.class);
        intent.putExtra("post_id",item.post_id);
        intent.putExtra("title",item.title);
        getActivity().startActivity(intent);
    }
    private void getArticles(int page){
        new AsyncTask<Void,Void,List<Article>>(){
            @Override
            protected void onPreExecute() {
                fragmentArticleSwipe.setRefreshing(true);
            }

            @Override
            protected List<Article> doInBackground(Void... params) {
                return performRequest(mPageIndex);
            }

            @Override
            protected void onPostExecute(List<Article> articles) {
                articles.removeAll(mDataSet);
                mDataSet.addAll(articles);
                adapter.notifyDataSetChanged();
                fragmentArticleSwipe.setRefreshing(false);
//                  // 存储文章列表
                DatabaseHelper.getInstance().saveArticles(articles);
                if(articles.size()>0){
                    mPageIndex++;
                }
            }
        }.execute();

    }
    private List<Article> performRequest(int page){
        HttpURLConnection urlConnection=null;
        try{
            String getUrl =
                    "http://www.devtf.cn/api/v1/?type=articles&page=" + mPageIndex
                            + "&count=20&category=1";
            urlConnection= (HttpURLConnection) new URL(getUrl).openConnection();
            urlConnection.connect();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()
            ));
            StringBuilder sb=new StringBuilder();
            String line=null;
            while((line=bufferedReader.readLine())!=null){
                sb.append(line).append("\n");
            }
            String result=sb.toString();
            return parse(new JSONArray(result));//将json解析为文章列表
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return new ArrayList<Article>();
    }
    private List<Article> parse(JSONArray jsonArray){
        List<Article> articleList=new LinkedList<>();
        int count=jsonArray.length();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for(int i=0;i<count;i++){
            JSONObject object=jsonArray.optJSONObject(i);
            Article article=new Article();
            article.title=object.optString("title");
            article.author=object.optString("author");
            article.post_id=object.optString("post_id");
            article.publishTime=formatDate(dateFormat,object.optString("date"));
            String category=object.optString("category");
            article.category= TextUtils.isEmpty(category)?0:Integer.valueOf(category);
            articleList.add(article);
        }
        return  articleList;
    }
    private String formatDate(SimpleDateFormat dateFormat,String date){
        try{
            Date datee=dateFormat.parse(date);
            return dateFormat.format(datee);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRefresh() {
           getArticles(1);
    }

    @Override
    public void onLoad() {
            fragmentArticleSwipe.setRefreshing(true);
            getArticles(mPageIndex);
    }
}
