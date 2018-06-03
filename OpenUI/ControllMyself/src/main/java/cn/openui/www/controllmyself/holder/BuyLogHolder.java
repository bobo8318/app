package cn.openui.www.controllmyself.holder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.openui.www.controllmyself.R;

/**
 * Created by My on 2017/12/24.
 */
public class BuyLogHolder extends RecyclerView.ViewHolder {

    private View root;

    private TextView buyType;
    private TextView buyDate;
    private TextView buyContent;
    private TextView price;
    private TextView win;
    private TextView syned;
    private LinearLayout background;

    public BuyLogHolder(View itemView) {
        super(itemView);
        root = itemView;

        buyDate = (TextView) itemView.findViewById(R.id.buyDate);
        buyContent = (TextView) itemView.findViewById(R.id.buyContent);
        price = (TextView) itemView.findViewById(R.id.price);
        win = (TextView) itemView.findViewById(R.id.win);
        buyType = (TextView) itemView.findViewById(R.id.buyType);
        syned = (TextView) itemView.findViewById(R.id.syned);

        background = (LinearLayout) itemView.findViewById(R.id.item_bg);

    }

    public void setBuyDate(String buyDate) {
        this.buyDate.setText(buyDate);
    }

    public void setBuyContent(String buyContent) {
        this.buyContent.setText(buyContent);
    }

    public void setPrice(String price) {
        this.price.setText(price);
    }

    public void setWin(String win) {
        this.win.setText(win);
    }

    public void setSyned(int syn) {
        if(syn==0)
            this.syned.setText("未同步");
        else if(syn==1)
            this.syned.setText("已同步");
    }

    public void setBuyType(String type){
        this.buyType.setText(type);
    }

    public void setWinBackGround(int position,boolean win){
        if(position%2==0){
            background.setBackgroundColor(Color.parseColor("#5d91c6"));
        }else if(position%2==1){
            background.setBackgroundColor(Color.parseColor("#d6d77c"));
        }
        if(win) {
            this.win.setTextColor(Color.parseColor("#ce1212"));
        }else
            this.win.setTextColor(Color.parseColor("#000000"));
    }
}
