package cn.openui.www.mytime.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.openui.www.mytime.R;

/**
 * Created by My on 2017/12/5.
 */
public class TypeHolder extends RecyclerView.ViewHolder {

    private TextView typeName;
    private TextView todayStudy;
    private TextView detailStudy;


    public TypeHolder(View itemView) {
        super(itemView);
        typeName = (TextView) itemView.findViewById(R.id.type_name);
        todayStudy = (TextView) itemView.findViewById(R.id.todayStat);
        detailStudy = (TextView) itemView.findViewById(R.id.detailCount);
    }

    public void setTypeName(String name){
        typeName.setText(name);
    }

    public void setTodayStudy(long todayStudy) {
        if(this.todayStudy!=null)
            this.todayStudy.setText(""+todayStudy);
        else if(detailStudy!=null){
            this.detailStudy.setText(""+todayStudy);
        }
    }

}
