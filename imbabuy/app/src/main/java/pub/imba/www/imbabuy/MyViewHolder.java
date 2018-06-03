package pub.imba.www.imbabuy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView item_img;
        TextView id_name;
        TextView id_type;
        TextView id_price;
        TextView id_sales;
        TextView id_discountPrice;
        TextView id_discountInfo;
        TextView id_discountEnd;
        Context context;

      public MyViewHolder(View view) {
          super(view);
          id_name = (TextView) view.findViewById(R.id.id_name);
          item_img = (ImageView) view.findViewById(R.id.item_img);
          id_type = (TextView) view.findViewById(R.id.id_type);
          id_price = (TextView) view.findViewById(R.id.id_price);
          id_sales = (TextView) view.findViewById(R.id.id_sales);
          id_discountPrice = (TextView) view.findViewById(R.id.id_discountPrice);
          id_discountInfo = (TextView) view.findViewById(R.id.id_discountInfo);
          id_discountEnd = (TextView) view.findViewById(R.id.id_discountEnd);
          context = view.getContext();
      }



 }