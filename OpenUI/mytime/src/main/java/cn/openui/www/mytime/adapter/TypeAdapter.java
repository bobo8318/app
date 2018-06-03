package cn.openui.www.mytime.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.openui.www.mytime.R;
import cn.openui.www.mytime.holder.TypeHolder;
import cn.openui.www.mytime.model.DetailType;

/**
 * Created by My on 2017/12/5.
 */
public class TypeAdapter extends  RecyclerView.Adapter implements View.OnClickListener,View.OnLongClickListener {

    private List<DetailType> mDatas = null;
    private OnItemClickListener mOnItemClickListener = null;

    private final int  Big_Type = 0;
    private final int  Detail_Type = 1;

    public void setData(List<DetailType> data){
        if( this.mDatas == null){
            this.mDatas = new ArrayList<DetailType>();
        }
        this.mDatas.addAll(data) ;

    }

    @Override
    public int getItemViewType(int position) {
        DetailType dt = this.mDatas.get(position);
        if("".equals(dt.getFatherType())||dt.getFatherType()==null){
            return Big_Type;
        }else{
            return Detail_Type;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == Detail_Type)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_type, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type, parent, false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return new TypeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TypeHolder th  = (TypeHolder) holder;
        th.setTypeName(mDatas.get(position).getTypeName());
        th.setTodayStudy(mDatas.get(position).getTodayStudy());
        th.itemView.setTag(mDatas.get(position).getTypeName()+":"+mDatas.get(position).getId());
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(v,v.getTag().toString());
        }
    }

    public void clear() {
        if(mDatas!=null)
            mDatas.clear();
    }

    @Override
    public boolean onLongClick(View v) {
        if(mOnItemClickListener!=null){
            mOnItemClickListener.onItemLongClick(v,v.getTag().toString());
            return true;
        }
        return false;
    }


    public static interface OnItemClickListener {
        void onItemClick(View view , String tag);
        void onItemLongClick(View view , String tag);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


}
