package pub.imba.www.imbabuy;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import pub.imba.www.util.ItemPo;
import pub.imba.www.util.Service;
import pub.imba.www.view.MySurfaceView;

public class MainActivity extends AppCompatActivity {


    private final int FIRST_LOAD = 0;
    private final int LOAD_MORE = 1;

    RecyclerView mRecyclerView;

    private List<ItemPo> mDatas;
    private HomeAdapter adapter;
    private Service service;
    private Handler handler;

    private String key = "";
    private int currentpage = 1;

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;

    boolean isLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySurfaceView game = new MySurfaceView(this);
        setContentView(game);

        //Intent intent = new Intent(MainActivity.this,SensorActivity.class);
       // startActivity(intent);

        /*
        setContentView(R.layout.activity_main);
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
                loaddata(currentpage,key,"",FIRST_LOAD);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        mRecyclerView = (RecyclerView) this.findViewById(R.id.id_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.sfl);

        service = new Service();

        adapter = new HomeAdapter();
        adapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, mDatas.get(position).getName(), Toast.LENGTH_LONG).show();
                //Log.i("openui", mDatas.get(position).getTbkurl());

                 Intent intent = new Intent();
                 intent.setAction("android.intent.action.VIEW");
                 Uri uri = Uri.parse( mDatas.get(position).getTbkurl());
                 intent.setData(uri);
                 startActivity(intent);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);
        loaddata(currentpage,key,"",FIRST_LOAD);
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh() {
                //
                Log.i("openui","fresh load data...");
                currentpage = 1;
                loaddata(currentpage,key,"",FIRST_LOAD);
                //数据重新加载完成后，提示数据发生改变，并且设置现在不在刷新
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //初始化数据

        //上拉加载更多
        mRecyclerView.addOnScrollListener(new EndLessOnScrollListener(llm){
            @Override
            public void onLoadMore() {
                currentpage++;
                Log.i("openui","end load data..."+currentpage);
                loaddata(currentpage,key,"",LOAD_MORE);
                //adapter.notifyDataSetChanged();
               // swipeRefreshLayout.setRefreshing(false);
            }
        });*/
    }
    public static List<ItemPo>  parseJsonData(String jsondata){
        List<ItemPo> mDatas = new ArrayList<ItemPo>();
        try {
            JSONObject data = JSON.parseObject(jsondata);//{}
            JSONArray ja = JSON.parseArray(data.getString("data").toString());//"[]"
            Iterator<Object> it = ja.iterator();
            while(it.hasNext()){
                JSONObject ob = (JSONObject) it.next();
                ItemPo item = new ItemPo();

                item.setName(ob.getString("name"));
                item.setImgUrl(ob.getString("pic"));
                item.setDiscountEnd("优惠截止日期:"+ob.getString("discountEnd"));
                item.setDiscountInfo("优惠券:"+ob.getString("discountInfo"));
                item.setDiscountPrice("优惠价格:"+ob.getString("discountPrice")+"元");
                item.setSales("销量:"+ob.getString("sales"));
                item.setTbkurl(ob.getString("tbkurl"));
                item.setType("分类："+ob.getString("type"));
                item.setPrice("价格:"+ob.getString("price")+"元");
                //Log.i("openui",ob.getString("name"));
                mDatas.add(item);
            }
        }catch (Exception e){
            Log.i("openui","catch!!!!!!!!!!!!!!!!!!!!");
            return null;
        }
        return mDatas;
    }
    private void loaddata(final int page,final String key,final String type, final int status){
        mDatas = new ArrayList<ItemPo>();
        //
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                // 处理消息时需要知道是成功的消息还是失败的消息
                String json = msg.obj.toString();
                Log.i("openui","net data:"+json);
                switch (msg.what) {
                    case 1:
                        mDatas = MainActivity.parseJsonData(json);
                        if(mDatas!=null&&mDatas.size()>0) {

                            //ItemPo po = JSON.parseObject(json,ItemPo.class);
                            if(status == FIRST_LOAD)
                                adapter.setmDatas(mDatas);
                            else if(status == LOAD_MORE)
                                adapter.addmDatas(mDatas);


                            mRecyclerView.setAdapter(adapter);
                        }else{//没有数据
                            Toast.makeText(MainActivity.this,"数据加载失败，请检查网络",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this,"数据加载失败，请检查网络",Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }

            }
        };
        //
        Thread t = new Thread() {
            @Override
            public void run() {
               String json =  service.getJsonData(page,key,type);
                if(json!=null){
                    Message msg = new Message();
                    // 消息对象可以携带数据
                    msg.obj = json;
                    msg.what = 1;
                    handler.sendMessage(msg);
                }else{
                    Message msg = new Message();
                    // 消息对象可以携带数据
                    msg.obj = "timeout";
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        };
        t.start();
    }

}
