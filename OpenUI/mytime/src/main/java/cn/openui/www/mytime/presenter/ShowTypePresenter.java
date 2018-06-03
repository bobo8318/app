package cn.openui.www.mytime.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.openui.www.mytime.dao.StudyDao;
import cn.openui.www.mytime.dao.TypeDao;
import cn.openui.www.mytime.frament.TypeListFragment;
import cn.openui.www.mytime.mainview.ShowTypeView;
import cn.openui.www.mytime.model.DetailType;
import cn.openui.www.mytime.util.TextUtils;

/**
 * Created by My on 2017/12/5.
 */
public class ShowTypePresenter extends BasePresenter {

    private String TAG = "ShowTypePresenter";
    private ShowTypeView stv;
    private List<DetailType> datalist;//实际显示数据
    private List<DetailType> fatherlist;//父类型
    private TypeDao typedao;
    private StudyDao studydao;
    private List<String[]> todayStudayCount;
    public ShowTypePresenter(Context context){
        attach(context);

        typedao = new TypeDao(context);
        studydao = new StudyDao(context);

        datalist = new ArrayList<>();


    }

    public void setView(ShowTypeView stv){
        this.stv = stv;
    }

    public void ListBigType(){

        fatherlist = typedao.getFatherList();//所有父类，开始时显示
        todayStudayCount = studydao.getTodayStudyCount(this.getTodayMillion());
        Log.i("todayStudayCount","size"+todayStudayCount.size());
        for(String[] bigtype:todayStudayCount){
            //if(bigtype.length==3){
                String fatherType = bigtype[0];
                //String detailType = bigtype[1];
                String studyTime = bigtype[2];
                Log.i("ListBigType",fatherType+"-"+studyTime);
                for(DetailType father:fatherlist){
                    if(fatherType.equals(father.getTypeName())){
                        father.setTodayStudy(father.getTodayStudy()+Long.valueOf(studyTime));
                        break;
                    }
                }
           // }
        }
        datalist.clear();
        if(fatherlist!=null)
            datalist.addAll(fatherlist);//显示存储数据

        //Log.i(TAG,"datalist:"+datalist.size());

        stv.listBigType(datalist);
    }

    /**
     * 判断十大类还是小类
     * @param typename
     * @return
     */
    public boolean isDetailType(String typename){

        if(typename!=null&&!"".equals(typename))
        for(DetailType type:datalist){
            if(getIdByViewTag(typename)==type.getId()){
                //System.out.println(type.getFatherType());
                if(!TextUtils.isEmpty(type.getFatherType()))
                    return true;
            }
        }
        return false;
    }
    public boolean isOpened(String typename){
        return false;
    }

    /**
     * 显示大类下的小类
     * @param bigType
     */
    public void ShowDetailType(String bigType){

        List<DetailType> childrens = typedao.getChildrenByFather(bigType);

        for(String[] bigtype:todayStudayCount){
            if(bigtype.length==3){
                //String fatherType = bigtype[0];
                String detailType = bigtype[1];
                String studyTime = bigtype[2];

                for(DetailType detail:childrens){
                    if(detailType.equals(detail.getTypeName())){
                        detail.setTodayStudy(detail.getTodayStudy()+Long.valueOf(studyTime));
                        break;
                    }
                }
            }
        }

        if(childrens!=null&&!childrens.isEmpty()){
            datalist.clear();//清空显示数据

           for(int i=0;i<fatherlist.size();i++){
               DetailType type = fatherlist.get(i);
               datalist.add(type);
               if(bigType.equals(type.getTypeName())){
                   datalist.addAll(childrens);
               }
           }

            stv.ShowDetailType(datalist);
        }


    }


    public boolean isFather(String tag) {
        //Log.i(TAG,tag+fatherlist.size());
        boolean result = false;
        Iterator<DetailType> it = fatherlist.iterator();
        while (it.hasNext()){
            DetailType father = it.next();
            if(father.getTypeName().equals(tag)) {
                result = true;
                break;
            }
        }
        return  result;
    }

    public String getTag(String source){
        if(!TextUtils.isEmpty(source)){
           String[] result = source.split(":");
            if(result!=null&&result.length>0)
                return result[0];
        }
            return "";
    }
    public int getId(String source){

        if(!TextUtils.isEmpty(source)){
            String[] result = source.split(":");
            if(result!=null&&result.length>1)
                return Integer.parseInt(result[1]);
        }
        return 0;
    }

    public void removeType(int id) {
        //如果是大分类 需要先删除小分类

        //删除分类
        //Log.i(TAG,"type id:"+id);
        studydao.removeStudyLogByTypeId(id);
        typedao.removeType(id);
        //大分类为空可以删除小分类

    }

    public DetailType getTypeByTag(String tag) {
        if(!TextUtils.isEmpty(tag)){
            String[] result = tag.split(":");
            if(result!=null&&result.length>1){
                DetailType dt  = new DetailType( Integer.parseInt(result[1]),"",result[0]);
                return dt;
            }

        }
        return null;
    }


}
