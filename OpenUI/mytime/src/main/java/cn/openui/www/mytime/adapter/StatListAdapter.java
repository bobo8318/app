package cn.openui.www.mytime.adapter;

import java.util.List;
import cn.openui.www.mytime.myview.MyBar;
/**
 * Created by My on 2018/1/19.
 */
public class StatListAdapter implements MyBar.BarDataAdapter {

    private List<Float> datas;
    private List<String> labels;

    public void setDatas(List datas, List labels){
        this.datas = datas;
        this.labels = labels;
    }
    @Override
    public float getValue(int position) {
        return datas.get(position);
    }

    @Override
    public float getMaxValue() {
        float max = 0;
        if(datas!=null) {
            for (Float value : datas) {
                if (max < value)
                    max = value;
            }
        }
        return max;
    }

    @Override
    public int getColor(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public String getLabel(int position) {
        return labels.get(position);
    }

    @Override
    public float getRate(int postion) {
        //return datas.get(postion)/(12*60);
        return datas.get(postion)/60;
    }
}
