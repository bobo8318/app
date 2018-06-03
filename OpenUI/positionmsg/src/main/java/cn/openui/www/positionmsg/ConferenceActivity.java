package cn.openui.www.positionmsg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.openui.www.positionmsg.model.Conference;
import cn.openui.www.positionmsg.util.BaseService;
import cn.openui.www.positionmsg.util.FragmentHolder;
import cn.openui.www.positionmsg.view.FlowLayout;
import cn.openui.www.positionmsg.view.ImgInfo_fragment;
import cn.openui.www.positionmsg.view.OpenTitle;
import cn.openui.www.positionmsg.view.TextInfo_fragment;

public class ConferenceActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private BaseService service;

    private FrameLayout img_content;//frament内容
    private FlowLayout layout;//派出所标签列表
    private DrawerLayout drawerLayout;//侧滑栏
    private OpenTitle title;//标题栏

    private RadioGroup rg;//底部按钮

    private FragmentManager fm;
    private FragmentTransaction ft;

    private Conference current_pcs = null;

    private ImgInfo_fragment imgfrag;
    private TextInfo_fragment infofrag;


    private Toolbar toolbar;

    private Handler handler;
    private Handler textHandler;

    private FragmentHolder holer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference);
        service = new BaseService(this);
        initView();

        toolbar = (Toolbar) this.findViewById(R.id.toolbar_top);
        toolbar.setTitle("房山分局视频会信息查询");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mToggle.syncState();//图标
        drawerLayout.setDrawerListener(mToggle);//监听器


        service.getConferenceList();//初始化数据

        String[] pcs = service.getConferenceNameList();
        layout.adapterData(pcs);

        //初始化
        imgfrag = new ImgInfo_fragment();
        infofrag = new TextInfo_fragment();
        holer.setImgfrag(imgfrag);
        holer.setTextinfo(infofrag);
       ft = fm.beginTransaction();
       ft.add(R.id.img_content,imgfrag);
       ft.add(R.id.img_content,infofrag);
       ft.commit();
       drawerLayout.openDrawer(Gravity.LEFT);
       //current_pcs = service.getConferenceByName("大安山");

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        layout.setOnTagClickListener(new FlowLayout.OnTagClickListener(){

            @Override
            public void onTagClick(View view) {
                String tag = ((TextView)view).getText().toString();
                current_pcs = service.getConferenceByName(tag);
                title.setTitle(current_pcs.getName());

                holer.setModel(current_pcs);
                holer.showImg(service.getImg(current_pcs.getImg()));
                /*Message message = Message.obtain();

                Bitmap bitmap = service.getImg(current_pcs.getImg());
                message.obj = bitmap;
                handler.sendMessage(message);*/

                ft = fm.beginTransaction();
                if(infofrag!=null)
                    ft.hide(infofrag);
                if(imgfrag!=null)
                    ft.show(imgfrag);
                ft.commit();
                showDrawerLayout();
            }
        });
    }



    private void showDrawerLayout() {
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }


    private void initView(){
        holer = new FragmentHolder();
        title = (OpenTitle) this.findViewById(R.id.pcs_title);
        layout  = (FlowLayout) this.findViewById(R.id.pcs_selected);
        drawerLayout = (DrawerLayout) findViewById(R.id.v4_drawerlayout);

        rg = (RadioGroup)findViewById(R.id.bottom_btn);
        rg.setOnCheckedChangeListener(this);

        fm = this.getSupportFragmentManager();

        title.setOnClickListener(new OpenTitle.OnClickListener() {
            @Override
            public void onLeftClick( View v) {

            }

            @Override
            public void onRightClick( View v) {//修改
                if(current_pcs!=null){
                    Intent intent = new Intent(ConferenceActivity.this,RegisterActivity.class);
                    intent.putExtra("conference", current_pcs);
                    startActivity(intent);
                }
            }
        });
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    public void setTextHandler(Handler handler) {
        this.textHandler = handler;
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
       if(current_pcs!=null){
            ft = fm.beginTransaction();
            Message message = Message.obtain();

            switch (checkedId){
                case R.id.conference_photo://显示图片
                    ft = fm.beginTransaction();
                    if(infofrag!=null)
                        ft.hide(infofrag);
                    if(imgfrag!=null)
                        ft.show(imgfrag);
                    ft.commit();
                    /*message.what = 1;
                    message.obj = service.getImg(current_pcs.getImg());
                    handler.sendMessage(message);*/
                    break;
                case R.id.conference_info://显示派出所会议信息
                    ft = fm.beginTransaction();
                    if(imgfrag!=null)
                        ft.hide(imgfrag);
                    if(infofrag!=null)
                        ft.show(infofrag);
                    ft.commit();
                    /*message.what = 2;
                    message.obj = current_pcs.getInfo();
                    textHandler.sendMessage(message);*/
                    holer.showConferenceInfo();
                    break;
                case R.id.conference_phone://显示联系电话
                    ft = fm.beginTransaction();
                    if(imgfrag!=null)
                        ft.hide(imgfrag);
                    if(infofrag!=null)
                        ft.show(infofrag);
                    ft.commit();
                    /*message.what = 3;
                    message.obj = current_pcs.getPhone();
                    textHandler.sendMessage(message);*/
                    holer.showConferencePhone();
                    break;
                default:;
            }

        }
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(item.getItemId() == R.id.action_edit){//添加按钮
                Intent intent = new Intent(ConferenceActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
            return false;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//加载menu文件到布局
        return true;
    }
}
