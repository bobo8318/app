package cn.openui.www.mytime.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import cn.openui.www.mytime.R;
import cn.openui.www.mytime.mainview.ModifyTypeView;
import cn.openui.www.mytime.model.DetailType;
import cn.openui.www.mytime.presenter.AddTypePresenter;
import cn.openui.www.mytime.util.TextUtils;

/**
 * Created by My on 2017/12/9.
 */
public class AddTypeActive extends AppCompatActivity implements ModifyTypeView{

    private EditText newType;
    private Spinner fatherType;
    private String fatherTypeStr = "";
    private String[] typeList;
    private Button modify_type_btn;
    private int model;
    private AddTypePresenter presenter;
    private Toolbar addtype_title;
    private int id = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_type);

        Bundle bundle = getIntent().getExtras();

        model = Integer.parseInt(bundle.getString("model"));//读出数据
        if(!TextUtils.isEmpty(bundle.getString("id")))
            id = Integer.parseInt(bundle.getString("id"));//类型id值
        initView();
    }

    private void initView() {

        addtype_title = (Toolbar) this.findViewById(R.id.addtype_title);
        setSupportActionBar(addtype_title);
        addtype_title.setTitle("ttttttt");

        //System.out.println( addtype_title.getTitle());

        presenter = new AddTypePresenter(this,this);

        modify_type_btn = (Button) this.findViewById(R.id.modify_type_btn);//修改按钮
        modify_type_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typename = newType.getText().toString();//新类型
                if(TextUtils.isEmpty(typename)){
                    Toast.makeText(AddTypeActive.this,"请输入类型名",Toast.LENGTH_LONG).show();
                }else{
                    DetailType dt = new DetailType();

                    dt.setFatherType(fatherTypeStr);
                    dt.setTypeName(typename);
                    dt.setId(id);

                    presenter.store(dt);
                }


            }
        });

        newType = (EditText) this.findViewById(R.id.new_type_name);
        fatherType = (Spinner) this.findViewById(R.id.father_type_select);

        presenter.setModel(model,id);
        presenter.listFatherType();

    }


    @Override
    public void upDateButtonText(String text) {
        modify_type_btn.setText(text);
        addtype_title.setTitle(text+"类信息");

    }

    @Override
    public void listFatherType(String[] datalist) {
        typeList = datalist;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fatherType.setAdapter(adapter);
        fatherType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fatherTypeStr = typeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fatherTypeStr = "";
            }
        });
    }

    @Override
    public void showModifyType(DetailType dt) {
        fatherType.setSelection(1);
    }
}
