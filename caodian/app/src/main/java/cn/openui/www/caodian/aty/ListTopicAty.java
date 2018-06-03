package cn.openui.www.caodian.aty;

import android.app.Activity;
import android.os.Bundle;

import cn.openui.www.caodian.R;
import cn.openui.www.caodian.adapter.OpenUIAdapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 2016/10/30.
 */
public class ListTopicAty extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listtopic_layout);

        RecyclerView rv = (RecyclerView) this.findViewById(R.id.recyclerview_topic);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
       // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));


        List dataList = new ArrayList();
        dataList.add("www");
        dataList.add("qqq");
        dataList.add("eee");

        rv.setAdapter(new OpenUIAdapter(dataList));
    }

}
