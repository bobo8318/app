package cn.openui.www.mytime.frament;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import cn.openui.www.mytime.MainActivity;
import cn.openui.www.mytime.R;
import cn.openui.www.mytime.activity.AddTypeActive;
import cn.openui.www.mytime.adapter.TypeAdapter;
import cn.openui.www.mytime.holder.MainActivityHolder;
import cn.openui.www.mytime.mainview.ModifyTypeView;
import cn.openui.www.mytime.mainview.ShowTypeView;
import cn.openui.www.mytime.model.DetailType;
import cn.openui.www.mytime.presenter.AddTypePresenter;
import cn.openui.www.mytime.presenter.ShowTypePresenter;

/**
 * Created by My on 2017/12/6.
 */
public class TypeListFragment extends Fragment implements ShowTypeView{

    private ShowTypePresenter stp;
    private RecyclerView rv;
    private TypeAdapter ta;

    private View rootView = null
            ;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stp = new ShowTypePresenter(this.getActivity());
        stp.setView(this);
        stp.attach(getActivity());

        ta = new TypeAdapter();
        setAdapter();

        stp.ListBigType();
        stp.setBaseView((MainActivity)getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.activity_show_type,container,false);
            //return super.onCreateView(inflater, container, savedInstanceState);
            rv = (RecyclerView) rootView.findViewById(R.id.bigTypeList);
            LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
            rv.setLayoutManager(llm);
        }

        return rootView;
    }


    @Override
    public void listBigType(List<DetailType> datalist) {

        ta.clear();
        rv.removeAllViews();

        ta.setData(datalist);
        rv.setAdapter(ta);

    }

    @Override
    public void listDetailType(List<DetailType> datalist) {

    }

    @Override
    public void ShowDetailType(List<DetailType> datalist) {
        rv.removeAllViews();
        ta.clear();
        ta.setData(datalist);
        setAdapter();//重新设置监听
        ta.notifyDataSetChanged();

    }

    @Override
    public void closeSlibeTyle(String bigType) {

    }

    public void setAdapter(){
        if(ta!=null){
            ta.setOnItemClickListener(new TypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, String tag) {

                    //判断当前点击的是大类还是小类
                    if(!stp.isDetailType(tag)){//如果是大类 是否是打开状态
                        if(stp.isOpened(tag)) {//如果是收起
                            ;
                        }else{//如果不是打开 并收起其他大项的子项
                            stp.ShowDetailType(stp.getTag(tag));
                        }
                    }else{ //如果是小类 跳转到学习界面 查看今日学习 情况 可以选择 学习 还是停止
                        //
                        MainActivity activity = (MainActivity) getActivity();
                        MainActivityHolder mhoder = activity.getMholder();

                        if( mhoder.getCurrent().isStudying()){//正在学习
                            if(tag.equals( mhoder.getCurrent().getCurrent())){//进入学习页面
                                ;
                            }else{//正在学习 与选择不相符 进行提示
                                stp.showTips("请先结束当前学习，再学习其他内容");
                            }
                        }else{
                            mhoder.getCurrent().setCurrent(stp.getTypeByTag(tag));
                            mhoder.getCurrent().NewStudy();
                        }

                        activity.changeInex(1);//设置标题
                        mhoder.getVp().setCurrentItem(1);//跳转

                    }
                }

                @Override
                public void onItemLongClick(View view,final String tag) {//长按弹出选择 或编辑对话框
                    //AlertDialog ad = new AlertDialog();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("请选择操作");
                    //dialog.setMessage("确定要删除此项吗？");

                    builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setNeutralButton("修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), AddTypeActive.class);
                            intent.putExtra("id",stp.getId(tag));
                            intent.putExtra("model", AddTypePresenter.MODIFY);
                            startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            stp.removeType(stp.getId(tag));
                            //刷新list
                            stp.ListBigType();
                        }
                    });
                    //AlertDialog  dialog = builder.create();
                    builder.show();

                }
            });
        }
    }

    public void freshData() {
        stp.ListBigType();
    }
}
