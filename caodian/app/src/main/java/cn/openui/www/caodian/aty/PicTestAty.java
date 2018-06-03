package cn.openui.www.caodian.aty;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import cn.openui.www.caodian.R;
import cn.openui.www.caodian.net.NetConnection;
import cn.openui.www.caodian.service.MainService;

/**
 * Created by My on 2016/12/15.
 */
public class PicTestAty extends Activity implements View.OnClickListener {
    private ImageView iv;
    private MainService service = new MainService(this);
    private Bitmap bitmap = null;
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictest_layout);
        //bitmap = BitmapFactory.decodeFile("/res/drawable/mn.jpg");
        bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.mn);
        iv = (ImageView) this.findViewById(R.id.pic);
        iv.setImageBitmap(bitmap);


        Button btn = (Button) this.findViewById(R.id.picBtn);
        Button detect = (Button) this.findViewById(R.id.detectBtn);
        Button drawBitmap = (Button) this.findViewById(R.id.captchBtn);

        btn.setOnClickListener(this);
        iv.setOnClickListener(this);
        detect.setOnClickListener(this);
        drawBitmap.setOnClickListener(this);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){//未选择图片 data会为null
            Uri uri;
            switch(requestCode){
                case 1:
                   uri  = data.getData();
                  ContentResolver cr = this.getContentResolver();
                     /* String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = cr.query(uri,columns,null,null,null);
                    int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String file =  cursor.getString(index);
                    if(file!=null&&!"".equals(file)) {
                        bitmap = BitmapFactory.decodeFile(file);
                        iv.setImageBitmap(bitmap);
                    }*/

                    try {
                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        iv.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Bundle bundle = data.getExtras();
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    bitmap = (Bitmap) bundle.get("data");
                   /* if (data.getData() != null) { uri = data.getData();
                    } else {
                        uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
                    }*/


                   // bitmap = BitmapFactory.decodeFile(uri.getPath());
                    bitmap = bitmapReset(bitmap,width,height);
                    iv.setImageBitmap(bitmap);

                   // iv.setScaleType(ImageView.ScaleType.FIT_XY);

                    Log.i("detect bitmap uri:","werwer");
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch(v.getId()) {
            case R.id.picBtn:
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
             break;
            case R.id.pic:
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                break;
            case  R.id.detectBtn:
                if (bitmap == null) {
                    Toast.makeText(PicTestAty.this,"please choose img",Toast.LENGTH_LONG).show();
                } else {
                try {
                        service.dectedImg(
                                new NetConnection.SuccessCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                        getResBitMap(result);
                                        iv.setImageBitmap(bitmap);
                                        Log.i("detect result:",result);
                                    }
                                },
                                new NetConnection.FailCallback() {
                                    @Override
                                    public void onFail() {

                                    }
                                }, bitmap);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.captchBtn://拍照

               // iv.setImageBitmap(getResBitMap(""));
               // Log.i("detect bitmap:",bitmap.getHeight()+" "+bitmap.getWidth());
                intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
               // File file = new File("test.jpg");
               // intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));
                startActivityForResult(intent,3);
                Log.i("detect capture:",bitmap.getHeight()+" "+bitmap.getWidth());
                break;
        }
    }


    private Bitmap getResBitMap(String rs){
        Rect rect = null;
        int width=0,top=0,left=0,height=0;
        Bitmap canvasBitmap =  Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        Canvas canvas = new Canvas(canvasBitmap);
        canvas.drawBitmap(bitmap,0,0,null);
        Bitmap face;
        try {
            JSONObject jsondata = new JSONObject(rs);
            JSONArray faces = jsondata.getJSONArray("faces");
            for(int i=0;i<faces.length();i++){
                JSONObject oneface = faces.getJSONObject(i);
                 width = oneface.getJSONObject("face_rectangle").getInt("width");
                 top = oneface.getJSONObject("face_rectangle").getInt("top");
                 left = oneface.getJSONObject("face_rectangle").getInt("left");
                 height = oneface.getJSONObject("face_rectangle").getInt("height");
                 rect =  new Rect(left,top,left+width,top+height);

                face = BitmapFactory.decodeResource(this.getResources(),R.drawable.self);
                bitmapReset(face,width,height);
                canvas.drawBitmap(face,null,rect,null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        //canvas.drawLine(0,0,1140,1800,paint);

        bitmap = canvasBitmap;
        return canvasBitmap;
    }

    private Bitmap bitmapReset(Bitmap bitmap, int width, int height){
       /* BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inJustDecodeBounds = true;
       // Bitmap orgin = BitmapFactory.decodeFile(file,options);
        int orginHeight = bitmap.getHeight();
        int orginWidth = bitmap.getWidth();

       // int rate = 1;
      // if(orginHeight>height&&orginWidth>width){
         int  rate = Math.max(orginHeight/height,orginWidth/width);
            options.inSampleSize = rate;
        Log.i("detect rate:",rate+"");
       // options.inJustDecodeBounds = false;
      // }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bo);
        byte[] bitmapbyte =bo.toByteArray();
        return  BitmapFactory.decodeByteArray(bitmapbyte,0,bitmapbyte.length,options);*/
        Bitmap target = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(target);
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, target.getWidth(), target.getHeight()), null);
        return target;
    }

}
