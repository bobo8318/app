package cn.openui.www.caodian.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.openui.www.caodian.R;
import cn.openui.www.caodian.holder.OpenUIViewHolder;
import cn.openui.www.caodian.holder.TopicHolder;
import cn.openui.www.caodian.model.Msg;
import cn.openui.www.caodian.model.Topic;

/**
 * Created by My on 2016/11/2.
 */
public class OpenUIAdapter<T extends  RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    private List dataList;
    private int Adapter_type_list_topic = 0;
    private int Adapter_type_list_msg = 1;
    private int currentType;

    public OpenUIAdapter(List dataList){
        this.dataList = dataList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == Adapter_type_list_topic) {
            view = View.inflate(parent.getContext(), R.layout.topicitem_layout, null);
            TopicHolder holder = new TopicHolder(view);
            return holder;
       }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof TopicHolder) {
            OpenUIViewHolder mh = (OpenUIViewHolder) holder;
            Topic topic = (Topic) dataList.get(position);
            mh.topic.setText(topic.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.get(position) instanceof Topic)
            return Adapter_type_list_topic;
        else if(dataList.get(position) instanceof Msg){
            return Adapter_type_list_msg;
        }
       return super.getItemViewType(position);
    }
}


