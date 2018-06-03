package cn.openui.www.controllmyself.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by My on 2017/12/24.
 */
public class BuyLogModel {

    private int id;
    private String buyDate;
    private String buyContent;
    private String price;
    private String win;
    private String type;
    private String addtime;
    private String coder;
    private int status;
    private String user;

    Calendar cl = Calendar.getInstance();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cl.setTime(dateFormat.parse(buyDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getBuyContent() {
        return buyContent;
    }

    public void setBuyContent(String buyContent) {
        this.buyContent = buyContent;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getCoder() {
        return coder;
    }

    public void setCoder(String coder) {
        this.coder = coder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getYear() {
        return cl.get(Calendar.YEAR);
    }

    public int getMonth() {
        return cl.get(Calendar.MONTH)+1;
    }

    public int getDay() {
        return cl.get(Calendar.DAY_OF_MONTH);
    }
}
