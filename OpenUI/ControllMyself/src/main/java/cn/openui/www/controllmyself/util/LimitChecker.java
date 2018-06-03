package cn.openui.www.controllmyself.util;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.openui.www.controllmyself.config.Config;
import cn.openui.www.controllmyself.model.LimitLineModel;

/**
 * Created by My on 2017/12/27.
 */
public class LimitChecker {

    private Map<String,LimitLineModel> ruller;
    public LimitChecker(){
        ruller = new HashMap<>();
    }

    public void addRul(String type,LimitLineModel rul){
        ruller.put(type,rul);
    }

    public void removeRul(String key){
        if(ruller.containsKey(key))
            ruller.remove(key);
    }

    public int check(double value, String key){

        int result = 0;

            if(ruller.containsKey(key)){
                LimitLineModel model = ruller.get(key);
                if(model.getIsuse()==1){
                    double limitvalue = Double.valueOf(model.getValue());
                    String winlose = model.getWinlose();
                    if(winlose.equals(Config.LIMIT_LOSE)){
                        if(value>0) result = 0;//没有损失
                        else result= compare(Math.abs(value),limitvalue,model.getLevel());
                    }else if(winlose.equals(Config.LIMIT_WIN)){
                        if(value<0) result = 0;//没赢钱 肯定到不了止盈线
                        else result = compare(value,limitvalue,model.getLevel());
                    }
                }
            }

        Log.i("check ruller",key+"-"+result);
        return result;
    }

    /**
     * 比较 a 与 b
     * 如果
     * @param a
     * @param b
     * @return
     */
    private int compare(double a,double b, float level){
        if(a>=b){//越线提醒
            return 1;
        }else if(a>=b*level&&a<b){//阈值提醒
            return 2;
        }else{
            return 0;
        }
    }

    public void clear(){
        ruller.clear();
    }

    public boolean isEmpty(){
        return ruller.isEmpty();
    }

    public void addRuls(List<LimitLineModel> rul) {
        for(LimitLineModel model:rul){
            ruller.put(model.getDatetype(),model);
        }
    }
}
