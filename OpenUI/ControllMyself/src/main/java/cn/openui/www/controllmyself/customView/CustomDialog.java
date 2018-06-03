package cn.openui.www.controllmyself.customView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.URLDecoder;
import java.net.URLEncoder;

import cn.openui.www.controllmyself.R;
import cn.openui.www.controllmyself.model.BuyLogModel;
import cn.openui.www.controllmyself.util.CommonUtils;
import cn.openui.www.controllmyself.util.TextUtils;

/**
 * Created by My on 2017/12/24.
 */
public class CustomDialog extends Dialog {

        private Button yes;//确定按钮
        private Button no;//取消按钮
        private TextView titleTv;//消息标题文本
        private String titleStr;//从外界设置的title文本
        //确定文本和取消文本的显示内容
        private String yesStr, noStr;

        private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
        private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

        private DatePicker buyDate;
        private EditText buyContent;
        private EditText price;
        private EditText win;
        private Spinner type;

        String[] typearray = new String[]{"足球","篮球"};
        private String checkType = "";
        private String coder;

    public CustomDialog(Context context) {
            super(context, R.style.MyDialog);
        }

        public CustomDialog(Context context, int theme) {
            super(context, theme);
        }

        /**
         * 设置取消按钮的显示内容和监听
         *
         * @param str
         * @param onNoOnclickListener
         */
        public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
            if (str != null) {
                noStr = str;
            }
            this.noOnclickListener = onNoOnclickListener;
        }

        /**
         * 设置确定按钮的显示内容和监听
         *
         * @param str
         * @param onYesOnclickListener
         */
        public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
            if (str != null) {
                yesStr = str;
            }
            this.yesOnclickListener = onYesOnclickListener;
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_buy_log);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    private void initEvent() {
         //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            yes.setText(yesStr);
        }
        if (noStr != null) {
            no.setText(noStr);
        }
    }

    private void initView() {
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        titleTv = (TextView) findViewById(R.id.title);

        buyDate = (DatePicker) findViewById(R.id.add_date);
        buyContent = (EditText) findViewById(R.id.add_content);
        price = (EditText) findViewById(R.id.add_price);
        win = (EditText) findViewById(R.id.add_win);
        type = (Spinner) findViewById(R.id.add_type);


        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,typearray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkType = typearray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void setTitle(String title) {
        titleStr = title;
    }

    public BuyLogModel getStoreInfo(){
        BuyLogModel model = new BuyLogModel();
        String month_fill = "";
        String day_fill = "";
        int month = buyDate.getMonth()+1;

        if(month<10)
            month_fill = "0";
        else
            month_fill = "";
        if(buyDate.getDayOfMonth()<10)
            day_fill = "0";
        else
            day_fill = "";
        String date = buyDate.getYear()+"-"+(month_fill+month)+"-"+day_fill+buyDate.getDayOfMonth();

        String content = buyContent.getText().toString();

        String price = this.price.getText().toString();
        String win = this.win.getText().toString();

        if(TextUtils.isEmpty(content)||TextUtils.isEmpty(price)){
            return null;
        }
        if(TextUtils.isEmpty(win)){
            win = "0";
        }
        try {
            model.setBuyDate(date);
            model.setBuyContent(URLEncoder.encode(content));
            model.setPrice(price);
            model.setWin(win);
            model.setType(URLEncoder.encode(checkType));
            model.setAddtime(""+System.currentTimeMillis());
            model.setStatus(0);



            if(TextUtils.isEmpty(coder))
                model.setCoder(CommonUtils.getRandomCoder(10));
            else
                model.setCoder(coder);

            Integer.valueOf(price);
            Integer.valueOf(win);

        }catch (NumberFormatException e){
            return null;
        }

        //Log.i("BuyLogModel win:",model.getWin());

        return model;
    }

    public void setCoder(String coder) {
        this.coder = coder;
    }

    public void setOldModel(BuyLogModel model) {
        if (model != null) {
            //Log.i("DatePicker",buyDate==null?"空":"非空");
            buyDate.init(model.getYear(), model.getMonth()-1, model.getDay(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    setTitle(year + "-" + (monthOfYear+1) +  "-" + dayOfMonth);
                }
            });
            buyContent.setText(URLDecoder.decode(model.getBuyContent()));
            price.setText(model.getPrice());
            win.setText(model.getWin());
            type.setSelection(getSpinnerIndex(model.getType()));
        }

    }

    private int getSpinnerIndex(String type) {
        int result = 0;
        if(!TextUtils.isEmpty(type)){
            for(int i=0;i<typearray.length;i++){
                    if(type.equals(typearray[i])){
                        result = i;
                        break;
                    }
            }
        }

        return result;
    }


    /**
         * 设置确定按钮和取消被点击的接口
         */
        public interface onYesOnclickListener {
            public void onYesClick();
        }

        public interface onNoOnclickListener {
            public void onNoClick();
        }
}
