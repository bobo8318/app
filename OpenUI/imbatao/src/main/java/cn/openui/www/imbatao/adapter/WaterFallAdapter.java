package cn.openui.www.imbatao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.openui.www.imbatao.R;
import cn.openui.www.imbatao.customview.ScaleImageView;
import cn.openui.www.imbatao.loader.ImageLoader;
import cn.openui.www.imbatao.po.ItemPo;

/**
 * Created by My on 2018/3/12.
 */
public class WaterFallAdapter extends ArrayAdapter {

    private ImageLoader loader;

    private LayoutInflater inflater;

    private List<ItemPo> mdata;
    private String[] strdata;

    public WaterFallAdapter(Context context, int resource) {
        super(context, resource);
        loader = new ImageLoader(context);
        inflater = LayoutInflater.from(context);
    }

    public WaterFallAdapter(Context context, int resource,String[] data) {
        super(context, resource);
        loader = new ImageLoader(context);
        inflater = LayoutInflater.from(context);
        strdata = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_layout,null);
            holder.image = (ScaleImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ItemPo item = this.mdata.get(position);
        loader.displayImg(item.getImgUrl(),holder.image);
        return convertView;
       /* ViewHolder holder;

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(getContext());
            convertView = layoutInflator.inflate(R.layout.row_staggered_demo,
                    null);
            holder = new ViewHolder();
            holder.image = (ScaleImageView ) convertView .findViewById(R.id.imageView1);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        loader.displayImg(getItem(position), holder.image);

        return convertView;*/

    }

    @Override
    public String getItem(int position) {
        return strdata[position];
    }

    @Override
    public int getCount() {
        if(strdata!=null&&strdata.length!=0)
             return strdata.length;
        else if(mdata!=null)
            return mdata.size();
        else
            return 0;
    }

    public void setData(List<ItemPo> datas) {
        this.mdata = datas;
    }

    class ViewHolder{
        ScaleImageView image;
    }
}
