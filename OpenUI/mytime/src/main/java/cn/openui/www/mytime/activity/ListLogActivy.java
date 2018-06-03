package cn.openui.www.mytime.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.openui.www.mytime.R;
import cn.openui.www.mytime.mainview.BaseView;
import cn.openui.www.mytime.mainview.ListLogView;
import cn.openui.www.mytime.model.StudyInfo;
import cn.openui.www.mytime.presenter.ListLogPresenter;

/**
 * Created by My on 2018/1/24.
 */
public class ListLogActivy extends AppCompatActivity implements ListLogView,BaseView {

    private ListLogPresenter presenter;
    private Toolbar toolbar;
    private RecyclerView rv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_log_layout);
        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("type");
        String date = bundle.getString("date");

        toolbar = (Toolbar) this.findViewById(R.id.listLogTitle);
        toolbar.setTitle("学习列表");
        setSupportActionBar(toolbar);

        rv = (RecyclerView) this.findViewById(R.id.loglist);

        presenter = new ListLogPresenter(this,this,this);
        presenter.showList(type,date);
    }

    @Override
    public void showTips(String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showList(List<StudyInfo> datas) {
       /* StringBuffer sb = new StringBuffer();
        for(StudyInfo info:datas){
            sb.append(info.getType()).append("\r\n");
            Log.i("showList",info.getType());
        }*/
        LogListAdapter listAdapter = new LogListAdapter();
        listAdapter.setData(datas);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(listAdapter);
        
    }
    public class LogListHolder extends RecyclerView.ViewHolder{

        private View root;
        private TextView type;
        private TextView start;
        private TextView continueTime;

        public LogListHolder(View itemView) {
            super(itemView);
            root = itemView;
            type = (TextView) itemView.findViewById(R.id.log_item_type);
            start = (TextView) itemView.findViewById(R.id.log_item_start);
            continueTime = (TextView) itemView.findViewById(R.id.log_item_continue);
        }

        public void init(String type,String start,String continueTime,int position){
            if(position%2==0){
                root.setBackgroundColor(Color.parseColor("#FFC0CB"));
            }else{
                root.setBackgroundColor(Color.parseColor("#FFD700"));
            }
            this.type.setText(type);
            this.start.setText(start);
            this.continueTime.setText(continueTime);
        }
    }
    public class LogListAdapter extends RecyclerView.Adapter<LogListHolder>{

        private List<StudyInfo> datas;

        @Override
        public LogListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_lor_item, parent, false);
             LogListHolder holder = new LogListHolder(itemview);
            return holder;
        }

        @Override
        public void onBindViewHolder(LogListHolder holder, int position) {
            if(datas!=null){
                StudyInfo info = datas.get(position);
                Long cotinues =  Long.valueOf(info.getEndTime())-Long.valueOf(info.getStartTime());
                String cotinuesStr = presenter.getTimeDiffStr(cotinues);
                holder.init(info.getType(),presenter.LongToDate(Long.valueOf(info.getStartTime())),cotinuesStr,position);

            }
        }

        @Override
        public int getItemCount() {
            return datas==null?0:datas.size();
        }

        public void setData(List<StudyInfo> datas) {
            this.datas = datas;
        }
    }
}
