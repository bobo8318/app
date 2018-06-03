package cn.openui.www.caodian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.openui.www.caodian.aty.DecisionAty;
import cn.openui.www.caodian.aty.Index;
import cn.openui.www.caodian.aty.ListTopicAty;
import cn.openui.www.caodian.aty.LoginAty;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,DecisionAty.class);
        startActivity(intent);
    }
}
