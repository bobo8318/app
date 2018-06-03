package cn.openui.www.mytime.frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.openui.www.mytime.MainActivity;
import cn.openui.www.mytime.R;
import cn.openui.www.mytime.mainview.BaseView;
import cn.openui.www.mytime.mainview.MyInfoView;
import cn.openui.www.mytime.presenter.SetupPresenter;
import cn.openui.www.mytime.util.TextUtils;

/**
 * Created by My on 2017/12/6.
 */
public class MyInfoFragment extends Fragment implements MyInfoView,BaseView{

    private View rootView;
    private SetupPresenter presenter;
    private Button setup_save_btn;
    private EditText user_name_edit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.activity_set_up,container,false);
        }
        user_name_edit = (EditText) rootView.findViewById(R.id.username_edit);

        setup_save_btn = (Button) rootView.findViewById(R.id.setup_save_btn);
        setup_save_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String usrname = user_name_edit.getText().toString();
                if(!TextUtils.isEmpty(usrname)){
                    presenter.saveUserName(usrname);
                    presenter.showTips("保存成功！");
                }else{
                    presenter.showTips("用户名不能为空！");
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new SetupPresenter(this.getActivity(),this,this);
        presenter.attach(getContext());
        user_name_edit.setText(presenter.getUserName());
    }


    @Override
    public void showTips(String text) {
        Toast.makeText(this.getActivity(),text,Toast.LENGTH_SHORT).show();
    }
}
