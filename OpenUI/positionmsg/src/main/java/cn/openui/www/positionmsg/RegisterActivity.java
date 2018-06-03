package cn.openui.www.positionmsg;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import cn.openui.www.positionmsg.model.Conference;
import cn.openui.www.positionmsg.util.BaseService;
import cn.openui.www.positionmsg.view.FlowLayout;
import cn.openui.www.positionmsg.view.OpenTitle;


public class RegisterActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private BaseService service;

    private EditText form_name;
    private EditText form_info;
    private EditText form_phone;

    private Button captch_img_btn;//拍照
    private Button choose_img_btn;//选择相片
    private ImageView img_view;//显示图片
    private Bitmap bmp;

    private RadioGroup form_type;

    private int type = Conference.PCS;
    private String imgName = "";

    private Conference conference = null;

    private OpenTitle title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conference = (Conference) getIntent().getSerializableExtra("conference");
        //System.out.println("conference"+conference.getName());

        service = new BaseService(this);
        setContentView(R.layout.activity_register);
        initView();
    }
    private void initView() {
        this.form_name = (EditText) this.findViewById(R.id.add_form_name);
        if(conference!=null) this.form_name.setText(conference.getName());

        this.form_info = (EditText) this.findViewById(R.id.add_form_info);
        if(conference!=null) this.form_info.setText(conference.getInfo());

        this.form_phone = (EditText)this.findViewById(R.id.add_form_phone);
        if(conference!=null) this.form_phone.setText(conference.getPhone());

        captch_img_btn = (Button) this.findViewById(R.id.catch_pic_btn);
        choose_img_btn = (Button) this.findViewById(R.id.choose_pic_btn);

        img_view = (ImageView) this.findViewById(R.id.add_form_imgview);
        if(conference!=null) this.img_view.setImageBitmap(service.getImg(conference.getImg()));

        form_type = (RadioGroup) this.findViewById(R.id.add_form_type);
        if(conference!=null){
            if(conference.getType()==Conference.FENJU){
                RadioButton rbtn = (RadioButton) this.findViewById(R.id.add_form_type_fj);
                type = Conference.FENJU;
                rbtn.setChecked(true);
            } else if(conference.getType()==Conference.PCS){
                RadioButton rbtn = (RadioButton) this.findViewById(R.id.add_form_type_pcs);
                type = Conference.PCS;
                rbtn.setChecked(true);
            }
        }

        form_type.setOnCheckedChangeListener(this);

        title = (OpenTitle) this.findViewById(R.id.add_update_title);
        title.setRightText("保存");
        if(conference!=null){
            title.setTitle("修改会议室信息");
            title.setLeftText("删除");


            title.setOnClickListener(new OpenTitle.OnClickListener() {
                @Override
                public void onLeftClick(View v) {//删除按钮
                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(RegisterActivity.this);
                    normalDialog.setTitle("确定删除？");
                    normalDialog.setMessage("你要点击哪一个按钮呢?");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //...To-do
                                    if(conference!=null)
                                    service.delete(conference.getId());
                                    conference = null;
                                    //重新加载active
                                    dialog.cancel();
                                }
                            });
                    normalDialog.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //...To-do
                                    dialog.cancel();
                                }
                            });
                    // 显示
                    normalDialog.show();
                }

                @Override
                public void onRightClick(View v) {//保存按钮
                    String name =  form_name.getText().toString();
                    String info =  form_info.getText().toString();
                    String phone =  form_phone.getText().toString();
                    if(name==null||"".equals(name)){
                        form_name.setError("会员室名称不能为空！");
                    }else
                    if(info==null||"".equals(info)){
                        form_info.setError("会议室信息不能为空！");
                    }else
                    if(phone==null||"".equals(phone)){
                        form_phone.setError("联系电话不能为空！");
                    }else{//保存数据
                        if(conference == null)
                            conference = new Conference();
                        conference.setName(name);
                        conference.setInfo(info);
                        conference.setPhone(phone);
                        conference.setType(type);

                        //将拍照或选取的相片存到app img目录下
                        service.storeBitMap(bmp,service.getRandomName()+".jpg");
                        conference.setImg("");//设置图片

                        if(conference.getId()==0) {//添加
                            service.add(conference);
                        }else {//修改
                            service.update(conference);
                        }


                    }
                }
            });
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.add_form_type_pcs:type=Conference.PCS;break;
            case R.id.add_form_type_fj:type=Conference.FENJU;break;
            default:;

        }
    }

    public void choosePic(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    public void captch_img(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imgName = "tmp.jpg";
        File file = new File(service.getImgUrl(), imgName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) if (requestCode == 1) {//拍照
           /* Bundle bundle = data.getExtras();//缩略图
            Bitmap bitmap = (Bitmap) bundle.get("data");*/
            bmp = service.getImg("tmp.jpg");
            img_view.setImageBitmap(bmp);
            //
        } else if (requestCode == 2) {//选取相片
            Uri photoUri = data.getData();
            File myFile = new File(photoUri.getPath());
            Uri selectedImage = service.getImageContentUri(RegisterActivity.this,myFile);
            if(bmp != null)//如果不释放的话，不断取图片，将会内存不够
                 bmp.recycle();

            String[] filePathColumn={MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
            cursor.moveToFirst();
            String photoPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();

            bmp = BitmapFactory.decodeFile(photoPath);
            img_view.setImageBitmap(bmp);
        }
    }
}
