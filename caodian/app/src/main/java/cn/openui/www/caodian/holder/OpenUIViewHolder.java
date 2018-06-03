package cn.openui.www.caodian.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.openui.www.caodian.R;

/**
 * Created by My on 2016/11/2.
 */
public class OpenUIViewHolder extends RecyclerView.ViewHolder {
    public TextView topic;
    public TextView info;
    public Button showContent;


    public OpenUIViewHolder(View itemView) {
        super(itemView);
        // holderRoot = itemView;
        topic = (TextView) itemView.findViewById(R.id.topicContent);
    }
}
