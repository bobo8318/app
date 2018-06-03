package cn.openui.www.mytime.frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.openui.www.mytime.MainActivity;
import cn.openui.www.mytime.R;
import cn.openui.www.mytime.holder.MainActivityHolder;
import cn.openui.www.mytime.mainview.ShowCurrentView;
import cn.openui.www.mytime.model.DetailType;
import cn.openui.www.mytime.model.StudyInfo;
import cn.openui.www.mytime.presenter.CurrentStudyPresenter;
import cn.openui.www.mytime.util.TextUtils;

/**
 * Created by My on 2017/12/6.
 */
public class CurrentFragment extends Fragment implements View.OnClickListener,ShowCurrentView{

    private TextView start_time;
    private TextView study_title;
    private TextView study_time;

    private Button btn_start;
    private Button btn_pause;
    private Button btn_end;

    private EditText note;
    private CurrentStudyPresenter csp;

    private MainActivityHolder holder;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        csp = new CurrentStudyPresenter(this.getActivity());
        csp.setScv(this);
        csp.setBaseView((MainActivity)this.getActivity());

        holder = ((MainActivity) this.getActivity()).getMholder();

        if(holder.getCurrentStudy()!=null&&!TextUtils.isEmpty(holder.getCurrentStudy().getStartTime()))
            start_time.setText(csp.LongToDate(Long.valueOf(holder.getCurrentStudy().getStartTime())));
        if(holder.currentType!=null)
            study_title.setText(holder.currentType.getTypeName());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.current_study,container,false);

        this.study_time = (TextView) view.findViewById(R.id.study_time);
        this.study_title = (TextView) view.findViewById(R.id.study_title);
        this.start_time = (TextView) view.findViewById(R.id.start_time);

        this.btn_start = (Button) view.findViewById(R.id.study_btn_start);
        this.btn_pause = (Button) view.findViewById(R.id.study_btn_pause);
        this.btn_end = (Button) view.findViewById(R.id.study_btn_finish);

        this.note = (EditText) view.findViewById(R.id.study_note);

        this.btn_start.setOnClickListener(this);
        this.btn_pause.setOnClickListener(this);
        this.btn_end.setOnClickListener(this);

       return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.study_btn_start:
               if(holder.getCurrentStudy()==null){
                   Toast.makeText(getActivity(),"请选择要学习的内容",Toast.LENGTH_LONG).show();
               }else{
                   long starttime = csp.getCurrentTime();
                   start_time.setText(start_time.getText()+csp.LongToDate(starttime));
                   holder.isStudying = true;
                   holder.getCurrentStudy().setStartTime(""+starttime);
                   csp.start();//启动更新时间线程
                   btn_start.setEnabled(false);//开始按钮失效
               }
                break;
            case  R.id.study_btn_finish:
                holder.isStudying = false;
                csp.finish();
                if(holder.getCurrentStudy()!=null) {
                    holder.getCurrentStudy().setNote(note.getText().toString());
                    holder.getCurrentStudy().setEndTime(""+csp.getCurrentTime());
                    csp.storeStudyInfo(holder.getCurrentStudy());
                    btn_start.setEnabled(true);//开始按钮发生作用
                    NewStudy();
                }
                break;
            default:break;
        }
    }

    public void NewStudy(){
        btn_start.setEnabled(true);//开始按钮发生作用
        study_title.setText(holder.currentType.getTypeName());
        study_time.setText("00:00:00");
        this.start_time.setText("开始时间：");
        note.setText("");

        if(holder.getCurrentStudy()==null) {
            holder.setCurrentStudy(new StudyInfo());
        }

        holder.getCurrentStudy().setType(""+holder.currentType.getId());
        holder.getCurrentStudy().setStartTime("0");
        holder.getCurrentStudy().setEndTime("0");
        holder.getCurrentStudy().setNote("");


    }

    public void setCurrent(DetailType type){
        this.holder.currentType = type;
    }


    @Override
    public void setTitle(String title) {
        study_title.setText(title);
    }

    @Override
    public void updateTime(String Time) {
        if(holder.getCurrentStudy()!=null){
            long studytime = Long.parseLong(Time) - Long.parseLong(holder.getCurrentStudy().getStartTime());
            int[] timediffer = csp.getTimeDifference(studytime);
            String showTime = timediffer[1]+":"+timediffer[2]+":"+timediffer[3];
            study_time.setText(showTime);
        }

    }

    @Override
    public void saveNote(String note) {

    }

    public DetailType getCurrent() {
        return holder.currentType;
    }

    public boolean isStudying() {
        return holder.isStudying;
    }

    public void freshData() {

    }


}
