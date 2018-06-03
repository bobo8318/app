package cn.openui.www.imbatao;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dtr.zxing.activity.CaptureActivity;

public class QRactivity extends AppCompatActivity {

    private TextView qrresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qractivity_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        qrresult = (TextView) this.findViewById(R.id.qrresult);
    }

    public void scanQR(View view){
        startActivityForResult(new Intent(QRactivity.this,CaptureActivity.class),0);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            qrresult.setText(result);
        }
    }
}
