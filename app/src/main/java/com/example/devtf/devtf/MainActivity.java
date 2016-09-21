package com.example.devtf.devtf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @InjectView(R.id.main_user_icon)
    ImageView mainUserIcon;
    @InjectView(R.id.main_username)
    TextView mainUsername;
    @InjectView(R.id.main_rc)
    RecyclerView mainRc;
    @InjectView(R.id.drawer_layout)
    LinearLayout drawerLayout;
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

        Menuadapter adapter = new Menuadapter(items);
        adapter.setOnItemClickListener(new OnItemClickListener<MenuItem>() {
            @Override
            public void OnClick(MenuItem item) {
                clickMenuItem(item);
            }
        });
        mainRc.setAdapter(adapter);
        mFragmentManager.beginTransaction().add(R.id.main_framelayout, mArticleFragment)
                .commitNowAllowingStateLoss();
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
        private void isQuit(){


    }
}


