package cn.openui.www.caodian.aty;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.openui.www.caodian.R;
import cn.openui.www.caodian.service.MainService;

/**
 * Created by My on 2016/10/30.
 */
public class LoginAty extends Activity {
    private TextView username;
    private TextView password;
    private Button loginBtn;
    private MainService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        service = new MainService(this);
        username = (TextView) this.findViewById(R.id.usename);
        password = (TextView)this.findViewById(R.id.password);

        loginBtn = (Button)this.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                service.test(username.getText().toString());
            }
        });


    }
}
