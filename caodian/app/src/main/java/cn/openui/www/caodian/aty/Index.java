package cn.openui.www.caodian.aty;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.openui.www.caodian.MainActivity;
import cn.openui.www.caodian.R;
import cn.openui.www.caodian.adapter.OpenUIAdapter;
import cn.openui.www.caodian.model.Topic;
import cn.openui.www.caodian.service.MainService;

/**
 * Created by My on 2016/11/2.
 */
public class Index extends Activity {

    private MainService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.index_layout);

        RecyclerView rv = (RecyclerView) this.findViewById(R.id.recyclerview_index);
        List<Topic> data = new ArrayList<Topic>();

        Topic topic = new Topic();
        topic.setContent("test");
        data.add(topic);

        Topic topic2 = new Topic();
        topic2.setContent("test2");
        data.add(topic2);

       OpenUIAdapter adapter = new OpenUIAdapter(data);
       rv.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }
}
