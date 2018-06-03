package cn.openui.www.controllmyself;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cn.openui.www.controllmyself.adapter.MyListAdapter;
import cn.openui.www.controllmyself.adapter.MyPageAdapter;
import cn.openui.www.controllmyself.behavior.BottomBehavior;
import cn.openui.www.controllmyself.behavior.EasyBehavior;
import cn.openui.www.controllmyself.behavior.MyAppBarLayout;
import cn.openui.www.controllmyself.config.Config;
import cn.openui.www.controllmyself.customView.CustomDialog;
import cn.openui.www.controllmyself.customView.MySwipeRefreshLayout;
import cn.openui.www.controllmyself.customView.MyViewPager;
import cn.openui.www.controllmyself.model.BuyLogModel;
import cn.openui.www.controllmyself.model.LimitLineModel;
import cn.openui.www.controllmyself.model.PageViewModel;
import cn.openui.www.controllmyself.model.StatInfoModel;
import cn.openui.www.controllmyself.presenter.MyPresenter;
import cn.openui.www.controllmyself.service.MusicService;
import cn.openui.www.controllmyself.viewInterface.BuyLogView;
import cn.openui.www.controllmyself.viewInterface.LimitView;
import cn.openui.www.controllmyself.viewInterface.StatView;
import cn.openui.www.controllmyself.viewInterface.UserInfoView;

public class MainActivity extends AppCompatActivity implements BuyLogView,StatView,LimitView,UserInfoView{

    public final static int FOOTBALL = 1;
    public final static int BASKETBALL = 2;

    private TabLayout mTabLayout;
    private MyViewPager mViewPager;
    private MyPageAdapter adapter;
    private MyPresenter presenter;

    private CustomDialog selfDialog;

    private LayoutInflater mLi;

    private View buyLogView;
    private View statView;
    private View savehandsView;
    private View userInfoView;

    RecyclerView loglist;
    MyListAdapter listadapter = new MyListAdapter();

    private  MySwipeRefreshLayout logswesh;
    private  MySwipeRefreshLayout statFresh;

    private List<PageViewModel> mdata = new ArrayList<PageViewModel>();

    private int day_checkedid;
    private int week_checkedid;
    private int month_checkedid;

    private Toolbar toolbar;

    private int typeFilter;
    private int statTypeFilter;

    private boolean serviceBound = false;



    private Messenger clientMsg = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                default:break;
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) this.findViewById(R.id.toolbar_top);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        presenter = new MyPresenter(this,this,this,this);
        mLi = LayoutInflater.from(this);
        initData();
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        presenter.firstRun();
    }

    @Override
    protected void onStart() {
        super.onStart();
        doBindService();
    }

    private void doBindService() {
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
        serviceBound = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnBindService();
    }

    private void doUnBindService() {
        if(serviceBound){
            unbindService(sc);
            serviceBound = false;
        }

    }

    private void initData() {

        PageViewModel pvm1 = new PageViewModel();
        pvm1.setTitle("购彩记录");
        getBuyLogView();
        Log.i("addviewpage",this.buyLogView.getTag().toString());
        pvm1.setView(this.buyLogView);
        mdata.add(pvm1);

        PageViewModel pvm2 = new PageViewModel();
        pvm2.setTitle("盈亏统计");
        pvm2.setView(getStatView());
        mdata.add(pvm2);

        PageViewModel pvm3 = new PageViewModel();
        pvm3.setTitle("防剁手");
        pvm3.setView(getSaveHandsView());
        mdata.add(pvm3);

        PageViewModel pvm4 = new PageViewModel();
        pvm4.setTitle("用户信息");
        pvm4.setView(getUsrInfoView());
        mdata.add(pvm4);
    }



    private void initView() {
        mTabLayout = (TabLayout) this.findViewById(R.id.tab_FindFragment_title);
        mViewPager = (MyViewPager) this.findViewById(R.id.vp_FindFragment_pager);

        adapter = new MyPageAdapter();
        adapter.setMdata(mdata);

        mViewPager.setAdapter(adapter);
        mTabLayout.setTabsFromPagerAdapter(adapter);//设置Tab的标题来自PagerAdapter.getPageTitle()
        mTabLayout.setupWithViewPager(mViewPager);//tablayout 与 viewpage 联动

        AppBarLayout appbar = (AppBarLayout) this.findViewById(R.id.appbar);
        CoordinatorLayout.LayoutParams params2 = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.setBehavior(new MyAppBarLayout());
        appbar.setLayoutParams(params2);


        // mTabLayout.setOnTabSelectedListener();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==3){
                    freshUserInfo();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //behavior = BottomBehavior.from(design_bottom_sheet);
    }

    private void freshUserInfo() {
       PageViewModel userInfoView =  mdata.get(3);
        userInfoView.setView(getUsrInfoView());
    }


    public View getBuyLogView() {
        buyLogView =  mLi.inflate(R.layout.buy_log_layout,null ,true);




        buyLogView.setTag("buy log View init");
        loglist = (RecyclerView) buyLogView.findViewById(R.id.buyLog);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        loglist.setLayoutManager(llm);
        loglist.setAdapter(listadapter);



       logswesh = (MySwipeRefreshLayout) buyLogView.findViewById(R.id.logswesh);
        logswesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.showBuyLog(true,typeFilter);
                logswesh.setRefreshing(false);
            }
        });

        Button addBtn = (Button) buyLogView.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check  = presenter.checkLimit();
                Log.i("click check", ""+check);
                if(check!=0){//已经截止线
                    presenter.showTips("已过剁手警戒线，别买了");
                }else{//显示添加
                    presenter.showDetailDialog(null,Config.ADD_MODE);

                }
            }
        });

        TextView log_type = (TextView) buyLogView.findViewById(R.id.log_type);
        log_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog.Builder typefiltDialog = new AlertDialog.Builder(MainActivity.this);
                typefiltDialog.setTitle("分类筛选");
                typefiltDialog.setPositiveButton("全部",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeFilter = 0;
                        presenter.showBuyLog(false,typeFilter);
                        dialog.dismiss();
                    }
                });
                typefiltDialog.setNeutralButton("足球",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeFilter = MainActivity.FOOTBALL;
                        presenter.showBuyLog(false,typeFilter);
                        dialog.dismiss();
                    }
                });
                typefiltDialog.setNegativeButton("篮球",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeFilter = MainActivity.BASKETBALL;
                        presenter.showBuyLog(false,typeFilter);
                        dialog.dismiss();
                    }
                });
                typefiltDialog.create().show();
            }
        });

        presenter.showBuyLog(false,typeFilter);



       /* TextView follow_test = (TextView) buyLogView.findViewById(R.id.follow);
        EasyBehavior behavior = new EasyBehavior();*/
        //CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) follow_test.getLayoutParams();
       // CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,CoordinatorLayout.LayoutParams.MATCH_PARENT);

        //buyLogView.setLayoutParams(params);
        BottomBehavior bbehavior = new BottomBehavior();
        CoordinatorLayout.LayoutParams addBtnParams = (CoordinatorLayout.LayoutParams) addBtn.getLayoutParams();
        addBtnParams.setBehavior(bbehavior);


        return buyLogView;
    }

    private View getSaveHandsView() {
        savehandsView = mLi.inflate(R.layout.save_hands_layout, null);
        savehandsView.setTag("savehandsView View init");
        presenter.showLimit();

        return savehandsView;
    }
    private View getUsrInfoView() {
        userInfoView = mLi.inflate(R.layout.user_info, null);
        userInfoView.setTag("userInfoView View init");
        ImageView image = (ImageView) userInfoView.findViewById(R.id.user_head);
        image.setImageBitmap(presenter.getUserHead());
        TextView user_name = (TextView) userInfoView.findViewById(R.id.user_name);
        presenter.showUserInfo(user_name,image);

        Button exit_btn = (Button) userInfoView.findViewById(R.id.exit_btn);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.exit();
            }
        });
        return userInfoView;
    }
    private View getStatView() {
        statView =  mLi.inflate(R.layout.buy_stat_layout, null);
        statView.setTag("statView View init");
        RadioGroup stat_type_group = (RadioGroup) statView.findViewById(R.id.stat_type_group);
        stat_type_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.stat_type_all:
                        statTypeFilter = 0;
                        break;
                    case R.id.stat_type_basketball:
                        statTypeFilter = MainActivity.BASKETBALL;
                        break;
                    case R.id.stat_type_football:
                        statTypeFilter = MainActivity.FOOTBALL;
                        break;
                }
                presenter.showStat(statTypeFilter);
            }
        });


        statFresh = (MySwipeRefreshLayout) statView.findViewById(R.id.statFresh);
        statFresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.showStat(statTypeFilter);
                statFresh.setRefreshing(false);
            }
        });

        presenter.showStat(0);
        return statView;
    }

    @Override
    public void showBuyLog(List<BuyLogModel> datas) {
        loglist.removeAllViews();//清除旧的

        listadapter.setData(datas);
        listadapter.setClickListener(new MyListAdapter.OnClickListener() {
            @Override
            public void onClick(View v) {//点击修改
                String coder = v.getTag().toString();
                presenter.showDetailDialog(coder,Config.UPDATE_MODE);
            }

            @Override
            public void onLongClick(View v) {//
                final String coder = v.getTag().toString();
                BuyLogModel model = presenter.getModelByCoder(coder);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("删除对话框")
                        .setMessage("确认删除"+ URLDecoder.decode(model.getBuyContent())+"吗？")
                        //相当于点击确认按钮
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                presenter.removeLog(coder);
                                presenter.showTips("删除成功");
                                presenter.showBuyLog(false,typeFilter);
                            }
                        });
                dialog.show();

            }
        });
        listadapter.notifyDataSetChanged();
    }

    @Override
    public void unableLogFresh() {
        this.logswesh.setRefreshing(false);
    }

    @Override
    public void showTips(String test) {
        Toast.makeText(MainActivity.this, test, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(final BuyLogModel model) {
        String title = "添加购彩信息";

        selfDialog = new CustomDialog(MainActivity.this);

        if(model!=null){
            title = "修改彩票信息";
            selfDialog.setCoder(model.getCoder());
        }
        selfDialog.setTitle(title);
         selfDialog.setYesOnclickListener("确定", new CustomDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                BuyLogModel newmodel = selfDialog.getStoreInfo();
                if(newmodel==null)
                    Toast.makeText(MainActivity.this,"输入内容不正确",Toast.LENGTH_LONG).show();
                else{
                    if(model!=null)
                        presenter.updateLog(newmodel);
                    else
                        presenter.storeLog(newmodel);

                    presenter.showBuyLog(false,typeFilter);
                    if(Double.valueOf(newmodel.getWin()) > Double.valueOf(newmodel.getPrice())){
                        presenter.showTips("赚钱了？是不是要请客吃个饭啊！");
                    }else
                        presenter.showTips("别灰心，请个客吧");
                    selfDialog.dismiss();
                }

            }
        });

        selfDialog.setNoOnclickListener("取消", new CustomDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                selfDialog.dismiss();
            }
        });

        selfDialog.show();
        if(model!=null){
            selfDialog.setOldModel(model);
        }
    }

    @Override
    public void showUserInputDialog(String user) {

        final EditText et = new EditText(this);
        et.setText(user);

        new AlertDialog.Builder(this).setTitle("输入用户名")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            presenter.showTips("用户名不能为空！" + input);
                        }
                        else {
                            presenter.setUser(input);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();



    }

    @Override
    public void jumpToLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void showStat(List<StatInfoModel> statInfoList) {

        TextView today = (TextView) statView.findViewById(R.id.stat_today);
        TextView week = (TextView) statView.findViewById(R.id.stat_week);
        TextView month = (TextView) statView.findViewById(R.id.stat_month);
        TextView year = (TextView) statView.findViewById(R.id.stat_year);

        for(StatInfoModel model:statInfoList ){

            String msg = "投入："+model.getCost()+" 赢得："+model.getWin()+" 盈亏："+(Integer.valueOf(model.getWin())-Integer.valueOf(model.getCost()));

            switch (model.getType()){
                case Config.STAT_TYPE_DAY:
                    today.setText(msg);
                    break;
                case Config.STAT_TYPE_WEEK:
                    week.setText(msg);
                    break;
                case Config.STAT_TYPE_MONTH:
                    month.setText(msg);
                    break;
                case Config.STAT_TYPE_YEAR:
                    year.setText(msg);
                    break;
                default:break;
            }

        }

    }

    @Override
    public void unableStatFresh() {
        statFresh.setRefreshing(false);
    }

    @Override
    public void showLimit(List<LimitLineModel> modelList) {

        final EditText limit_date = (EditText) savehandsView.findViewById(R.id.limit_date);
        final RadioGroup day_RadioGroup = (RadioGroup) savehandsView.findViewById(R.id.limit_dateType);

        final CheckBox day_checkbox = (CheckBox) savehandsView.findViewById(R.id.limit_date_on);

       final  EditText limit_week = (EditText) savehandsView.findViewById(R.id.limit_week);
        final RadioGroup week_RadioGroup = (RadioGroup) savehandsView.findViewById(R.id.limit_weekType);

        final CheckBox week_checkbox = (CheckBox) savehandsView.findViewById(R.id.limit_week_on);

        final EditText limit_month = (EditText) savehandsView.findViewById(R.id.limit_month);
        final RadioGroup month_RadioGroup = (RadioGroup) savehandsView.findViewById(R.id.limit_monthType);

       final  CheckBox month_checkbox = (CheckBox) savehandsView.findViewById(R.id.limit_month_on);

        Button save = (Button) savehandsView.findViewById(R.id.limit_save);
        save.setOnClickListener(new View.OnClickListener() {//保存
            @Override
            public void onClick(View v) {

                day_checkedid = day_RadioGroup.getCheckedRadioButtonId();
                week_checkedid = week_RadioGroup.getCheckedRadioButtonId();
                month_checkedid = month_RadioGroup.getCheckedRadioButtonId();

                LimitLineModel day_model = new LimitLineModel();
                day_model.setDatetype(Config.LIMIT_DAY);
                day_model.setValue(limit_date.getText().toString());
                if(day_checkedid==R.id.limit_date_lose)
                    day_model.setWinlose(Config.LIMIT_LOSE);
                else if(day_checkedid==R.id.limit_date_win)
                    day_model.setWinlose(Config.LIMIT_WIN);
                if(day_checkbox.isChecked())
                    day_model.setIsuse(1);


                LimitLineModel week_model = new LimitLineModel();
                week_model.setDatetype(Config.LIMIT_WEEK);
                week_model.setValue(limit_week.getText().toString());
                if(week_checkedid==R.id.limit_week_lose)
                    week_model.setWinlose(Config.LIMIT_LOSE);
                else if(week_checkedid==R.id.limit_week_win)
                    week_model.setWinlose(Config.LIMIT_WIN);
                if(week_checkbox.isChecked())
                     week_model.setIsuse(1);

                LimitLineModel month_model = new LimitLineModel();
                month_model.setDatetype(Config.LIMIT_MONTH);
                month_model.setValue(limit_date.getText().toString());
                if(month_checkedid==R.id.limit_month_lose)
                    month_model.setWinlose(Config.LIMIT_LOSE);
                else if(month_checkedid==R.id.limit_month_win)
                    month_model.setWinlose(Config.LIMIT_WIN);
                if(month_checkbox.isChecked())
                    month_model.setIsuse(1);

                presenter.putRul(day_model);
                presenter.putRul(week_model);
                presenter.putRul(month_model);
                Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });

        if(modelList!=null){
            for(LimitLineModel model:modelList){
                if(model.getDatetype().equals(Config.LIMIT_DAY)){//日线
                    limit_date.setText(model.getValue());
                    if(model.getIsuse()==1){
                        day_checkbox.setChecked(true);
                    }else{
                        day_checkbox.setChecked(false);
                    }
                    if(model.getWinlose().equals(Config.LIMIT_LOSE)){
                        day_RadioGroup.check(R.id.limit_date_lose);
                    }else{
                        day_RadioGroup.check(R.id.limit_date_win);
                    }

                }
                if(model.getDatetype().equals(Config.LIMIT_WEEK)){//周线
                    limit_week.setText(model.getValue());
                    if(model.getIsuse()==1){
                        week_checkbox.setChecked(true);
                    }else{
                        week_checkbox.setChecked(false);
                    }
                    if(model.getWinlose().equals(Config.LIMIT_LOSE)){
                        week_RadioGroup.check(R.id.limit_week_lose);
                    }else{
                        week_RadioGroup.check(R.id.limit_week_win);
                    }
                }
                if(model.getDatetype().equals(Config.LIMIT_MONTH)){//月线
                    limit_month.setText(model.getValue());
                    if(model.getIsuse()==1){
                        month_checkbox.setChecked(true);
                    }else{
                        month_checkbox.setChecked(false);
                    }
                    if(model.getWinlose().equals(Config.LIMIT_LOSE)){
                        month_RadioGroup.check(R.id.limit_month_lose);
                    }else{
                        month_RadioGroup.check(R.id.limit_month_win);
                    }
                }
            }
        }
    }


    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(item.getItemId() == R.id.action_setup){//添加按钮
                presenter.showUserInputDialog();
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//加载menu文件到布局
        return true;
    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            clientMsg = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void showUserInfo(String username, Bitmap header) {

    }
}
