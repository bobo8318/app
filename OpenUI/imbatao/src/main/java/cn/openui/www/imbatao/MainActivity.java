package cn.openui.www.imbatao;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import java.util.List;

import cn.openui.www.imbatao.adapter.WaterFallAdapter;
import cn.openui.www.imbatao.customview.MyFreshLayout;
import cn.openui.www.imbatao.customview.StaggeredGridView;
import cn.openui.www.imbatao.interfaceview.ShowPic;
import cn.openui.www.imbatao.po.ItemPo;
import cn.openui.www.imbatao.presenter.MyPresenter;

public class MainActivity extends AppCompatActivity implements ShowPic{

    private StaggeredGridView staggeredGridView;
    private MyPresenter presenter;
    private WaterFallAdapter adapter;
    MyFreshLayout srf;
    Toolbar toolbar;

    private String key = "";
    private String type = "";
    private int currentpage = 1;


    private String urls[] = {
            "http://farm7.staticflickr.com/6101/6853156632_6374976d38_c.jpg",
            "http://farm8.staticflickr.com/7232/6913504132_a0fce67a0e_c.jpg",
            "http://farm5.staticflickr.com/4133/5096108108_df62764fcc_b.jpg",
            "http://farm5.staticflickr.com/4074/4789681330_2e30dfcacb_b.jpg",
            "http://farm9.staticflickr.com/8208/8219397252_a04e2184b2.jpg",
            "http://farm9.staticflickr.com/8483/8218023445_02037c8fda.jpg",
            "http://farm9.staticflickr.com/8335/8144074340_38a4c622ab.jpg",
            "http://farm9.staticflickr.com/8060/8173387478_a117990661.jpg",
            "http://farm9.staticflickr.com/8056/8144042175_28c3564cd3.jpg",
            "http://farm9.staticflickr.com/8183/8088373701_c9281fc202.jpg",
            "http://farm9.staticflickr.com/8185/8081514424_270630b7a5.jpg",
            "http://farm9.staticflickr.com/8462/8005636463_0cb4ea6be2.jpg",
            "http://farm9.staticflickr.com/8306/7987149886_6535bf7055.jpg",
            "http://farm9.staticflickr.com/8444/7947923460_18ffdce3a5.jpg",
            "http://farm9.staticflickr.com/8182/7941954368_3c88ba4a28.jpg",
            "http://farm9.staticflickr.com/8304/7832284992_244762c43d.jpg",
            "http://farm9.staticflickr.com/8163/7709112696_3c7149a90a.jpg",
            "http://farm8.staticflickr.com/7127/7675112872_e92b1dbe35.jpg",
            "http://farm8.staticflickr.com/7111/7429651528_a23ebb0b8c.jpg",
            "http://farm9.staticflickr.com/8288/7525381378_aa2917fa0e.jpg",
            "http://farm6.staticflickr.com/5336/7384863678_5ef87814fe.jpg",
            "http://farm8.staticflickr.com/7102/7179457127_36e1cbaab7.jpg",
            "http://farm8.staticflickr.com/7086/7238812536_1334d78c05.jpg",
            "http://farm8.staticflickr.com/7243/7193236466_33a37765a4.jpg",
            "http://farm8.staticflickr.com/7251/7059629417_e0e96a4c46.jpg",
            "http://farm8.staticflickr.com/7084/6885444694_6272874cfc.jpg"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this,QRactivity.class));
       /* presenter = new MyPresenter(this);
        initView();
        initAdapter();*/
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.notice);

        SearchView searchView = (SearchView) this.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true);//设置展开后图标的样式,这里只有两种,一种图标在搜索框外,一种在搜索框内
        searchView.onActionViewExpanded();// 写上此句后searchView初始是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能出现输入框,也就是设置为ToolBar的ActionView，默认展开
        searchView.requestFocus();//输入焦点
        searchView.setSubmitButtonEnabled(true);//添加提交按钮，监听在OnQueryTextListener的onQueryTextSubmit响应
        searchView.setFocusable(true);//将控件设置成可获取焦点状态,默认是无法获取焦点的,只有设置成true,才能获取控件的点击事件
        searchView.setIconified(false);//输入框内icon不显示
        searchView.requestFocusFromTouch();//模拟焦点点击事件

        searchView.setFocusable(false);//禁止弹出输入法，在某些情况下有需要
        searchView.clearFocus();//禁止弹出输入法，在某些情况下有需要

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                key = query;
                currentpage = 1;
                Log.i("openui","onQueryTextSubmit:load key data"+key);
                presenter.loadData(currentpage,key,type);
                adapter.notifyDataSetChanged();
                srf.setRefreshing(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




        staggeredGridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView);
        int margin = getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
        staggeredGridView.setItemMargin(margin);
        staggeredGridView.setPadding(margin,0,margin,0);

        srf = (MyFreshLayout) this.findViewById(R.id.action_fresh);
        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadData(currentpage,key,type);
            }
        });
        srf.setMoreItem(new MyFreshLayout.MoreItem(){

            @Override
            public void moreData() {
                currentpage += 1;
                presenter.loadData(currentpage,key,type);
            }
        });

        presenter.setView(this);
        presenter.loadData(currentpage,key,type);
    }


    private void initAdapter(){
        adapter = new WaterFallAdapter(MainActivity.this, R.id.imageView);
        //adapter = new WaterFallAdapter(this,0);


        staggeredGridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
            @Override
            public void onItemClick(StaggeredGridView staggeredGridView, View view, int i, long l) {//
                Log.i("item click:",""+i);

            }

        });
        staggeredGridView.setAdapter(adapter);


    }

    @Override
    public void showPic(List<ItemPo> datas) {
        if(adapter!=null) {
            adapter.setData(datas);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void stopFresh() {
        srf.setRefreshing(false);
    }
}
