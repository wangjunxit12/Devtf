package com.example.devtf.devtf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import adapters.ArticleAdapter;
import bean.Article;
import bean.ArticleParser;
import butterknife.ButterKnife;
import butterknife.InjectView;
import db.DatabaseHelper;
import listener.DataListener;
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
        adapter.addItems(DatabaseHelper.getInstance().getArticles());


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
        adapter=new ArticleAdapter();
         adapter.setOnItemClickListener(new OnItemClickListener<Article>() {
             @Override
             public void OnClick(Article item) {
                 if(item!=null){
                     loadArticle(item);
                 }
             }
         });
        fragmentArticleRc.setAdapter(adapter);
        fetchArticles(1);

      }
    private  void loadArticle(Article item){
        Intent intent=new Intent(getActivity(),ContentActivity.class);
        intent.putExtra("post_id",item.post_id);
        intent.putExtra("title",item.title);
        startActivity(intent);
    }
    private String prepareRequsetUrl(int page){
        return  "http://www.devtf.cn/api/v1/?type=articles&page=" + page
                + "&count=20&category=1";
    }
    ArticleParser parser=new ArticleParser();
    private void fetchArticles(int page){
        fragmentArticleSwipe.setRefreshing(true);
        HttpFlinger.get(prepareRequsetUrl(page), parser, new DataListener<List<Article>>() {
            @Override
            public void onComplete(List<Article> result) {
                Log.i("this", "fetchArticles: "+result.size());
                adapter.addItems(result);
                // 存储文章列表
                DatabaseHelper.getInstance().saveArticles(result);
                fragmentArticleSwipe.setRefreshing(false);
                if(result.size()>0){
                    mPageIndex++;
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRefresh() {
           fetchArticles(1);
    }

    @Override
    public void onLoad() {
            fragmentArticleSwipe.setRefreshing(true);
            fetchArticles(mPageIndex);
    }
}
