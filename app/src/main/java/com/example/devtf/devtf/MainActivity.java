package com.example.devtf.devtf;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import adapters.Menuadapter;
import bean.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import listener.OnItemClickListener;


public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.main_drawer)
    DrawerLayout mainDrawer;
    @InjectView(R.id.main_framelayout)
    FrameLayout mainFramelayout;
    @InjectView(R.id.main_rc)
    RecyclerView mainRc;

    private FragmentManager mFragmentManager;
    private Fragment mArticleFragment = new ArticleFragment();
    private Fragment mAboutFragment;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 给左上角图标的左边加上一个返回的图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mainDrawer, toolbar, R.string.drawer_open
                , R.string.drawer_close);
        mDrawerToggle.syncState();
        mainDrawer.setDrawerListener(mDrawerToggle);
        mainRc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(getString(R.string.article), R.drawable.home));
        items.add(new MenuItem(getString(R.string.about_menu), R.drawable.about));
        items.add(new MenuItem(getString(R.string.exit), R.drawable.exit));

        Menuadapter adapter = new Menuadapter();
        adapter.addItems(items);
        Log.i("Menuadapter", "onCreate:测试 "+items.get(0)+"结尾");
        adapter.setOnItemClickListener(new OnItemClickListener<MenuItem>() {
            @Override
            public void OnClick(MenuItem item) {
                clickMenuItem(item);
            }
        });
        mainRc.setAdapter(adapter);

        mFragmentManager=getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.main_framelayout, mArticleFragment)
                .commitAllowingStateLoss();
//        点击菜单项的处理函数
    }
    private void clickMenuItem(MenuItem item) {
        mainDrawer.closeDrawers();
        switch (item.getResId()) {
            case R.drawable.home:
                mFragmentManager.beginTransaction().replace(R.id.main_framelayout, mArticleFragment)
                        .commit();
                break;
            case R.drawable.about:
                if (mAboutFragment == null) {
                    mAboutFragment = new AboutFragment();
                }
                mFragmentManager.beginTransaction().replace(R.id.main_framelayout, mAboutFragment)
                        .commit();
                break;
            case R.drawable.exit:
                isQuit();
                break;

            default:
                break;
        }
    }
    private void isQuit() {
        AlertDialog.Builder   dialog=new AlertDialog.Builder(this);
        dialog .setTitle("确认退出?").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("取消", null);
        dialog.show();
    }
}


