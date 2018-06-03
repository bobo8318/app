package cn.openui.www.mytime.holder;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;

import cn.openui.www.mytime.frament.CurrentFragment;
import cn.openui.www.mytime.frament.TypeListFragment;
import cn.openui.www.mytime.model.DetailType;
import cn.openui.www.mytime.model.StudyInfo;

/**
 * Created by My on 2017/12/8.
 */
public class MainActivityHolder {

    private RadioGroup rg;//底部按钮
    private ViewPager vp;//内容显示部分
    private Toolbar toolbar;

    private TypeListFragment typeList;
    private CurrentFragment current;

    private StudyInfo currentStudy;

    public DetailType currentType;
    public boolean isStudying = false;

    public RadioGroup getRg() {
        return rg;
    }

    public void setRg(RadioGroup rg) {
        this.rg = rg;
    }

    public ViewPager getVp() {
        return vp;
    }

    public void setVp(ViewPager vp) {
        this.vp = vp;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public TypeListFragment getTypeList() {
        return typeList;
    }

    public void setTypeList(TypeListFragment typeList) {
        this.typeList = typeList;
    }

    public CurrentFragment getCurrent() {
        return current;
    }

    public void setCurrent(CurrentFragment current) {
        this.current = current;
    }

    public StudyInfo getCurrentStudy(){
        return this.currentStudy;
    }
    public void setCurrentStudy(StudyInfo currentStudy){
        this.currentStudy = currentStudy;
    }
}
