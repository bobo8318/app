package cn.openui.www.mytime;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import cn.openui.www.mytime.activity.AddTypeActive;
import cn.openui.www.mytime.adapter.FragAdapter;
import cn.openui.www.mytime.frament.CurrentFragment;
import cn.openui.www.mytime.frament.MyInfoFragment;
import cn.openui.www.mytime.frament.StatFragment;
import cn.openui.www.mytime.frament.TypeListFragment;
import cn.openui.www.mytime.holder.MainActivityHolder;
import cn.openui.www.mytime.mainview.BaseView;
import cn.openui.www.mytime.model.StudyInfo;
import cn.openui.www.mytime.myview.MySwipeRefreshLayout;
import cn.openui.www.mytime.presenter.AddTypePresenter;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,BaseView{

    private RadioGroup rg;//底部按钮
    private ViewPager vp;//内容显示部分
    private  Toolbar toolbar;
    private MySwipeRefreshLayout srl;

    private TypeListFragment typeList;
    private CurrentFragment current;

    private MainActivityHolder mholder;

    private Menu aMenu;

    private int currentIndex;



    public MainActivityHolder getMholder(){
        return this.mholder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Intent intent = new Intent(this, ShowTypeActivity.class);
        //startActivity(intent);
        toolbar = (Toolbar) this.findViewById(R.id.title_bar);
        toolbar.setTitle("MyTime");
        this.setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        rg = (RadioGroup) this.findViewById(R.id.btnGroup);
        rg.setOnCheckedChangeListener(this);
        vp = (ViewPager) this.findViewById(R.id.pager_content);

        srl = (MySwipeRefreshLayout) this.findViewById(R.id.main_srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//下拉刷新数据
            @Override
            public void onRefresh() {
                freshData(currentIndex);
                srl.setRefreshing(false);
            }
        });
        ;

        mholder = new MainActivityHolder();
        mholder.setRg(rg);
        mholder.setToolbar(toolbar);
        mholder.setVp(vp);

        FragAdapter fragAdapter = new FragAdapter(this.getSupportFragmentManager());
        typeList = new TypeListFragment();
        mholder.setTypeList(typeList);
        fragAdapter.addFragment(typeList);
        current = new CurrentFragment();
        mholder.setCurrent(current);
        fragAdapter.addFragment(current);

        fragAdapter.addFragment(new StatFragment());
        fragAdapter.addFragment(new MyInfoFragment());

        vp.setAdapter(fragAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeInex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void freshData(int currentIndex) {
        switch(currentIndex) {
            case 0://类型刷新
                typeList.freshData();
                break;
            default:break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.type_btn:
                changeInex(0);
                break;
            case R.id.current_btn:
                changeInex(1);;
                break;
            case R.id.stat_btn:
                changeInex(2);
                break;
            case R.id.my_btn:
                changeInex(3);
                break;
            default:break;
        }
    }

    public void changeInex(int index){
        currentIndex = index;

        switch(index){
            case 0:
                vp.setCurrentItem(0);
                toolbar.setTitle("分类选择");
                aMenu.getItem(0).setVisible(true);//显示添加分类按钮

                break;
            case 1:
                vp.setCurrentItem(1);
                toolbar.setTitle("当前学习");
                aMenu.getItem(0).setVisible(false);
                break;
            case 2:
                vp.setCurrentItem(2);
                toolbar.setTitle("统计信息");
                aMenu.getItem(0).setVisible(false);
                break;
            case 3:
                vp.setCurrentItem(3);
                toolbar.setTitle("我的信息");
                aMenu.getItem(0).setVisible(false);
                break;
            default:break;
        }
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add://添加新的分类
                    Intent intent = new Intent(MainActivity.this,AddTypeActive.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("model", ""+AddTypePresenter.ADD);
                    intent.putExtras(bundle);
                    startActivity(intent);
                  /* final String[] items = new String[]{"1","2","3"};
                    dialog_view = new EditText(MainActivity.this);
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("添加类型-在以下列表中选择父类")
                            .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this,items[which],Toast.LENGTH_LONG).show();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setView(dialog_view)
                            .setPositiveButton("确定",  new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   // dialog_view.findViewById();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();*/

                    break;
            }
            return true;
        }
    };



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        aMenu = menu;
        getMenuInflater().inflate(R.menu.title_menu , menu);//加载menu文件到布局
        return true;
    }

    @Override
    public void showTips(String text) {
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}
