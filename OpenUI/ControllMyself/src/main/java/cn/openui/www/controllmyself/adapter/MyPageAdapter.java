package cn.openui.www.controllmyself.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import cn.openui.www.controllmyself.model.PageViewModel;

/**
 * Created by My on 2017/12/24.
 */
public class MyPageAdapter extends PagerAdapter {

    private List<PageViewModel> mdata;

    public void setMdata(List<PageViewModel> mdata){
        this.mdata = mdata;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mdata.get(position).getTitle();
    }

    @Override
    public int getCount() {
        if(mdata!=null)
            return mdata.size();
        else
            return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mdata.get(position).getView();
        ((ViewPager) container).addView(view);
        return view;
    }
}
