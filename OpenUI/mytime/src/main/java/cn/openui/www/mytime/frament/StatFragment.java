package cn.openui.www.mytime.frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.openui.www.mytime.MainActivity;
import cn.openui.www.mytime.R;
import cn.openui.www.mytime.activity.ListLogActivy;
import cn.openui.www.mytime.adapter.StatListAdapter;
import cn.openui.www.mytime.mainview.StatView;
import cn.openui.www.mytime.model.StudyInfo;
import cn.openui.www.mytime.myview.MyBar;
import cn.openui.www.mytime.presenter.StatPresenter;

/**
 * Created by My on 2017/12/6.
 */
public class StatFragment extends Fragment implements StatView{

    private View rootview = null;
    private MyBar bar;
    private StatPresenter presenter;
    private StatListAdapter adapter;

    private String statdate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // TextView test = new TextView(container.getContext());
        //test.setText("统计");
        //return super.onCreateView(inflater, container, savedInstanceState);
        if(rootview == null)
            rootview = inflater.inflate(R.layout.stat_layout,container,false);

        bar = (MyBar) rootview.findViewById(R.id.stat_bar);

        adapter = new StatListAdapter();



        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.i("create frag activity",bar==null?"null":"no");
        final MainActivity activity = (MainActivity) this.getActivity();
        presenter = new StatPresenter(activity,this);
        presenter.setBaseView(activity);
        presenter.showTodayStat();

        if(bar!=null){
            bar.setAdapter(adapter);
            bar.setOnClickListener(new MyBar.ClickListener() {
                @Override
                public void onClick(String tag,float value) {
                    Intent intent = new Intent(activity,ListLogActivy.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type",tag);
                    bundle.putString("date",statdate);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void showTodayStat(Map map) {
        List<Float> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        Iterator<Map.Entry<String,StudyInfo>> iterator = map.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,StudyInfo> entry =  iterator.next();
            StudyInfo info = entry.getValue();
            if(!TextUtils.isEmpty(info.getEndTime())&&!TextUtils.isEmpty(info.getStartTime())){
                float timelong = (Long.valueOf(info.getEndTime())-Long.valueOf(info.getStartTime()))/(1000*60);
                values.add(timelong);
                labels.add(info.getType());

            }

        }

        adapter.setDatas(values,labels);

    }
}
