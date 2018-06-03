package pub.imba.www.imbabuy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pub.imba.www.util.ItemPo;

/**
 * Created by My on 2017/8/30.
 */
public class HomeAdapter extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener {
    private OnItemClickListener mOnItemClickListener = null;
    private List<ItemPo> mDatas;

    private Context context;

    public void setmDatas( List<ItemPo> mDatas){

        this.mDatas = mDatas;
    }

    public void addmDatas( List<ItemPo> mDatas){
        if( this.mDatas == null){
            this.mDatas = new ArrayList<ItemPo>();
        }
        this.mDatas.addAll(mDatas) ;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_item, parent,
                    false);
            return new MyViewHolder(view);



       /* MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(this);
        return holder;*/
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ItemPo item =  mDatas.get(position);

        if (holder instanceof MyViewHolder) {
            //holder.tv.setText(data.get(position));
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });

               /* holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        mOnItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });*/
            }
        }

        holder.id_name.setText(item.getName());
        holder.id_discountEnd.setText(item.getDiscountEnd());
        holder.id_discountInfo.setText(item.getDiscountInfo());
        holder.id_discountPrice.setText(item.getDiscountPrice());
        holder.id_price.setText(item.getPrice());
        holder.id_sales.setText(item.getSales());
        holder.id_type.setText(item.getType());

       Picasso.with(holder.context).load(item.getImgUrl()).resize(300, 250).into(holder.item_img);

        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }



    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
