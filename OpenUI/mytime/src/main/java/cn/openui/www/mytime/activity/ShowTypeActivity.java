package cn.openui.www.mytime.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import java.util.List;

import cn.openui.www.mytime.R;
import cn.openui.www.mytime.adapter.TypeAdapter;
import cn.openui.www.mytime.mainview.ShowTypeView;
import cn.openui.www.mytime.model.DetailType;
import cn.openui.www.mytime.presenter.ShowTypePresenter;

public class ShowTypeActivity extends AppCompatActivity implements ShowTypeView{

    private ShowTypePresenter stp;
    private RecyclerView rv;
    private TypeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_type);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("分类选择");
        setSupportActionBar(toolbar);*/

        initView();
    }

    private void initView(){
        rv = (RecyclerView) this.findViewById(R.id.bigTypeList);
        adapter = new TypeAdapter();
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new TypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String tag) {//
                if(stp.isFather(tag)){//父元素
                    stp.ShowDetailType(tag);
                }else{//子元素 跳转学习
                    //先查看是否正在学习

                }
            }

            @Override
            public void onItemLongClick(View view, String tag) {//长按

            }
        });



        stp = new ShowTypePresenter(this);
        stp.attach(this);
        stp.ListBigType();

    }


    @Override
    public void listBigType(List<DetailType> datalist) {
        TypeAdapter adapter = new TypeAdapter();
        adapter.setData(datalist);
        rv.setAdapter(adapter);
    }

    @Override
    public void listDetailType(List<DetailType> datalist) {

    }

    @Override
    public void ShowDetailType(List<DetailType> datalist) {
        adapter.clear();
        adapter.setData(datalist);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void closeSlibeTyle(String bigType) {

    }
}
