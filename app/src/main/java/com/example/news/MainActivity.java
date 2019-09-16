package com.example.news;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.news.service.News;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private List<News> newsList;
    private NewsAdapter adapter;
    private Handler handler;
    private ListView lv;

    String[] datas = {"A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10",
            "A11", "A12", "A13", "A14", "A15", "A16", "A17", "A18", "A19", "A20"};
    private float scaledTouchSlop;
    private float firstY = 0;
    private Toolbar toolbar;
    private ObjectAnimator animtor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //滑动菜单
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        navigationView.setCheckedItem(R.id.setting);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent(MainActivity.this,Setting.class);
                startActivity(intent);
                return true;
            }
        });
        View headview=navigationView.inflateHeaderView(R.layout.nav_header);
        headview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

        //新闻
        newsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.news_lv);
        getNews();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    adapter = new NewsAdapter(MainActivity.this,newsList);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            News news = newsList.get(position);
                            Intent intent = new Intent(MainActivity.this,NewsDisplay.class);
                            intent.putExtra("news_url",news.getNewsUrl());
                            startActivity(intent);
                        }
                    });
                }
            }
        };

        /**
         * 添加一个HeadView避免第一个Item被ToolBar遮挡
         * 必须在setAdapter之前进行设置
         */
        initHeadView();
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datas));
        //判断认为是滑动的最小距离(乘以系数调整滑动灵敏度)
        scaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop()*3.0f;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    private void getNews(){

        Log.e("Jsoup","Test");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //获取虎扑新闻5页的数据，网址格式为：https://voice.hupu.com/nba/第几页
                    for(int i = 1;i<=5;i++) {

                        Document doc = Jsoup.connect("https://voice.hupu.com/nba/" + Integer.toString(i)).get();
                        Elements titleLinks = doc.select("div.list-hd");    //解析来获取每条新闻的标题与链接地址
                        // Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
                        Elements timeLinks = doc.select("div.otherInfo");   //解析来获取每条新闻的时间与来源
                        Log.e("title",Integer.toString(titleLinks.size()));
                        for(int j = 0;j < titleLinks.size();j++){
                            String title = titleLinks.get(j).select("a").text();
                            String uri = titleLinks.get(j).select("a").attr("href");
                            //   String desc = descLinks.get(j).select("span").text();
                            String time = timeLinks.get(j).select("span.other-left").select("a").text();
                            News news = new News(title,uri,null,time);
                            newsList.add(news);
                        }
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 设置头布局，注意：这个头布局的高度要和ToolBar的高度一致
     */
    public void initHeadView() {
        View view = new View(this);
        //abc_action_bar_default_height_material获取系统ActionBar的高度
        AbsListView.LayoutParams params = new AbsListView.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT,
                        (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        view.setLayoutParams(params);
        lv.addHeaderView(view);
    }
}
