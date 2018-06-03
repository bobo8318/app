package cn.openui.www.controllmyself.model;

/**
 * Created by My on 2017/12/27.
 */
public class LimitLineModel {

    private String datetype;
    private String winlose;
    private String value;
    private float level = 0.9f;//提醒阈值
    private int isuse = 0;

    public String getDatetype() {
        return datetype;
    }

    public void setDatetype(String datetype) {
        this.datetype = datetype;
    }

    public String getWinlose() {
        return winlose;
    }

    public void setWinlose(String winlose) {
        this.winlose = winlose;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public int getIsuse() {
        return isuse;
    }

    public void setIsuse(int isuse) {
        this.isuse = isuse;
    }
}
