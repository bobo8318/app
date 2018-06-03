package cn.openui.www.caodian.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.openui.www.caodian.R;
import cn.openui.www.caodian.service.MainService;

/**
 * Created by My on 2016/11/8.
 */
public class DecisionAty extends Activity {

    private MainService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.decision_index_layout);
        service = new MainService(this);

        int result = service.getRandom(2);

        Button commonBtn = (Button) this.findViewById(R.id.commonBtn);
        Button middleBtn = (Button) this.findViewById(R.id.middleBtn);
        Button hightBtn = (Button) this.findViewById(R.id.highBtn);

        commonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecisionAty.this,PicTestAty.class);
                startActivity(intent);
            }
        });

    }
}
