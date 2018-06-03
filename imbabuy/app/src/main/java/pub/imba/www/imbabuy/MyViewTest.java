package pub.imba.www.imbabuy;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by My on 2017/9/18.
 */
public class MyViewTest extends Activity {
    private String[] test = new String[]{
        "test","maos","test111","maos222222222222",
           "test","maos","test111","maos222222222222", "maos222222222222","maos222222222222" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.myviewtest);

       FlowLayout viewtest = (FlowLayout) this.findViewById(R.id.viewtest);

        LayoutInflater inflater =  LayoutInflater.from(this);
        for(int i=0;i<test.length;i++){
            TextView text  = (TextView) inflater.inflate(R.layout.label,viewtest,false);
            text.setText(test[i]);
            viewtest.addView(text);
        }

    }

}
