package cn.openui.www.controllmyself.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cn.openui.www.controllmyself.R;
import cn.openui.www.controllmyself.holder.BuyLogHolder;
import cn.openui.www.controllmyself.model.BuyLogModel;

/**
 * Created by My on 2017/12/24.
 */
public class MyListAdapter extends RecyclerView.Adapter<BuyLogHolder> {

    private List<BuyLogModel> datas = new ArrayList<>();

    private OnClickListener clickListener;

    @Override
    public BuyLogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_log_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener!=null)
                    clickListener.onClick(v);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if(clickListener!=null){
                    clickListener.onLongClick(v);
                    return true;
                }
                return false;
            }
        });
        return new BuyLogHolder(view);
    }

    @Override
    public void onBindViewHolder(BuyLogHolder holder, int position) {
        holder.setBuyDate(datas.get(position).getBuyDate());
        holder.setBuyContent(URLDecoder.decode(datas.get(position).getBuyContent()));
        holder.setPrice(datas.get(position).getPrice());
        holder.setWin(datas.get(position).getWin());
        holder.setWinBackGround(position,Integer.parseInt(datas.get(position).getWin())>=Integer.parseInt(datas.get(position).getPrice()));
        holder.setBuyType(URLDecoder.decode(datas.get(position).getType()));
        holder.setSyned(datas.get(position).getStatus());
        holder.itemView.setTag(datas.get(position).getCoder());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setData(List<BuyLogModel> data) {
        if(!this.datas.isEmpty())
            this.datas.clear();
        this.datas.addAll(data);
    }

    public void setClickListener(OnClickListener listener){
        this.clickListener = listener;
    }

    public interface OnClickListener{
        public void onClick(View v);
        public void onLongClick(View v);
    }
}
