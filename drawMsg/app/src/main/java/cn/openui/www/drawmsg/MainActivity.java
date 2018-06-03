package cn.openui.www.drawmsg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.openui.www.drawmsg.view.MyView;

public class MainActivity extends AppCompatActivity {

    private Button clearbtn;
    private Button savebtn;
    private MyView myview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clearbtn = (Button) this.findViewById(R.id.clearBtn);
        savebtn = (Button) this.findViewById(R.id.saveBtn);
        myview = (MyView) this.findViewById(R.id.myview);

        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myview.clear();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               myview.save();
                myview.clear();
                Toast.makeText(getApplicationContext(),"保存成功！",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
