package cn.openui.www.mytime.presenter;

import android.content.Context;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

import cn.openui.www.mytime.dao.TypeDao;
import cn.openui.www.mytime.mainview.ModifyTypeView;
import cn.openui.www.mytime.model.DetailType;

/**
 * Created by My on 2017/12/10.
 */
public class AddTypePresenter extends BasePresenter {

    private ModifyTypeView mfv;

    public static final int MODIFY = 0;
    public static  final int ADD = 1;
    private TypeDao dao;

    public AddTypePresenter(Context context, ModifyTypeView mfv) {
        super.attach(context);
        dao = new TypeDao(context);
        this.mfv = mfv;
    }

    /**
     * 按钮显示
     * @param model
     */
    public void setModel(int model, int id) {

         switch(model){
            case MODIFY:
                DetailType dt = dao.getTypeById(id);
                mfv.showModifyType(dt);
                mfv.upDateButtonText("修改");
                break;
            case ADD:
                mfv.upDateButtonText("添加");
                break;
            default:break;
        }

    }


    public void listFatherType() {
        List<DetailType> datalist = dao.getFatherList();
        if(datalist!=null){
            String[] data = new String[datalist.size()+1];
            data[0] = "无父类";
           for(int i=0;i<datalist.size();i++){
               data[i+1] = datalist.get(i).getTypeName();
           }
            mfv.listFatherType(data);
        }

    }

    public void store(DetailType dt) {
        if(dt!=null) {
            if(dt.getFatherType().equals("无父类"))
                dt.setFatherType("");
            if (dt.getId() != 0) {//修改
                dao.modifyType(dt);
                baseView.showTips("修改成功！");
            } else {//添加
                if (dao.checkType(dt.getTypeName())) {
                    baseView.showTips("该类型已存在，请重新输入！");
                } else {
                    int result = dao.addNewType(dt);
                    if (result == 1) {
                        baseView.showTips("添加成功！");
                    } else {
                        baseView.showTips("添加失败！");
                    }
                }

            }
        }
    }
}
