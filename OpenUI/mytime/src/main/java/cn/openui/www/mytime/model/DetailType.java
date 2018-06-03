package cn.openui.www.mytime.model;

/**
 * Created by My on 2017/12/5.
 */
public class DetailType extends BaseModel{


    private String typeName;
    private String fatherType;
    private long todayStudy;

    public DetailType(){
    }
    public DetailType(int id, String fatherType, String name){
        this.id = id;
        this.fatherType = fatherType;
        this.typeName = name;
        todayStudy = 0l;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFatherType() {
        return fatherType;
    }

    public void setFatherType(String fatherType) {
        this.fatherType = fatherType;
    }

    public long getTodayStudy() {
        return todayStudy;
    }

    public void setTodayStudy(long todayStudy) {
        this.todayStudy = todayStudy;
    }
}
