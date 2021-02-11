package com.example.myxx;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainA extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_CODE = 0x110;
    private ImageView mPhoto;
    private Button mGetImage;
    private Button customCamera;
    private Button mDetect;
    private Button button;
    private Button btn;
    private Button btnvdio;
    private TextView mTip,ma;
    private View mWaitting;
    private static int REQ_2 = 2;
    private int faceCount1=0;
    private int age1=0;

    int count=0;
    int coun=0;
    int coun1;
    private String mFilePath;
    private Button startCamera;
    private String mCurrentPhotoStr;
    private FileInputStream fis;

    private Bitmap mPhotoImg;

    private Paint mPaint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maina);
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        mFilePath = mFilePath + "/" + "temp.png";
        initViews();
        initEvent();
         mPaint =new Paint();

     Intent intent = getIntent();
        if(intent !=null) {
            String path = getIntent().getStringExtra("picPath");

            if (path != null) {
                   // FileInputStream fis = new FileInputStream(path);
                   // Bitmap bitmap = BitmapFactory.decodeStream(fis);
                   /* int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    // 设置想要的大小
                    int newWidth = 800;
                    int newHeight = 800;
                    // 计算缩放比例
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    Bitmap mbitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);*/

                mCurrentPhotoStr=path;
                resizePhoto();
                Matrix matrix = new Matrix();
                matrix.setRotate(270);
                mPhotoImg = Bitmap.createBitmap(mPhotoImg, 0, 0, mPhotoImg.getWidth(), mPhotoImg.getHeight(), matrix, true);

                    mPhoto.setImageBitmap(mPhotoImg);
             /*   File f = new File("/sdcard/" + "tttt" + ".png");
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPhotoImg.compress(Bitmap.CompressFormat.PNG, 60, fOut);
                try {
                    fOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCurrentPhotoStr=f.getAbsolutePath();*/
            }
        }

    }
    private void initEvent() {
        mGetImage.setOnClickListener(this);
        mDetect.setOnClickListener(this);
        startCamera.setOnClickListener(this);
        customCamera.setOnClickListener(this);
        button.setOnClickListener(this);
        btnvdio.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    private void initViews() {
ma= (TextView) findViewById(R.id.ts);
        mPhoto = (ImageView) findViewById(R.id.id_photo);
        mGetImage = (Button) findViewById(R.id.id_getImage);
        mDetect = (Button) findViewById(R.id.id_detect);
        mTip = (TextView) findViewById(R.id.id_tip);
        startCamera = (Button) findViewById(R.id.startCamera);
        mWaitting = findViewById(R.id.id_waitting);
        customCamera =(Button) findViewById(R.id.customCamera);
        button=(Button)findViewById(R.id.button);
        btnvdio=(Button)findViewById(R.id.btnvdio);
        btn=(Button)findViewById(R.id.btn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == PICK_CODE) {
            if (intent != null) {
                Uri uri = intent.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                mCurrentPhotoStr = cursor.getString(idx);
                cursor.close();
                resizePhoto();

                mPhoto.setImageBitmap(mPhotoImg);
                mTip.setText("Click Detect==>");


            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_2) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFilePath);
                   // Bitmap bitmap = BitmapFactory.decodeStream(fis);
                  /*  int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    // 设置想要的大小
                    int newWidth = 800;
                    int newHeight = 800;
                    // 计算缩放比例
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    Bitmap mbitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);*/

                   // mPhoto.setImageBitmap(bitmap);
                    mCurrentPhotoStr=mFilePath;
                    resizePhoto();
                    Matrix matrix = new Matrix();
                    matrix.setRotate(0);
                    // 设置想要的大小
                    int newWidth = 700;
                    int newHeight = 700;
                    // 计算缩放比例
                    float scaleWidth = ((float) newWidth) / mPhotoImg.getWidth();
                    float scaleHeight = ((float) newHeight) / mPhotoImg.getHeight();

                    matrix.postScale(scaleWidth, scaleHeight);
                    mPhotoImg= Bitmap.createBitmap(mPhotoImg, 0, 0, mPhotoImg.getWidth(), mPhotoImg.getHeight(), matrix, true);
                    //mPhotoImg= Bitmap.createBitmap(mPhotoImg, 0, 0, newWidth, newHeight, matrix, true);
                    mPhoto.setImageBitmap(mPhotoImg);
               //     mPhoto.setImageBitmap(mPhotoImg);
                    mTip.setText("Click Detect==>");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }



        }

    }
    private void resizePhoto() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoStr, options);
        double ratio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);
        options.inSampleSize = (int) Math.ceil(ratio);
        options.inJustDecodeBounds = false;

        mPhotoImg = BitmapFactory.decodeFile(mCurrentPhotoStr, options);

    }
    private static final int MSG_SUCESS =0x111;
    private static final int MSG_ERROR =0x112;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SUCESS:
                    mWaitting.setVisibility(View.GONE);
                    JSONObject rs = (JSONObject) msg.obj;
                    prepareRsBitmap(rs);
                    mPhoto.setImageBitmap(mPhotoImg);
                    break;
                case MSG_ERROR:
                    mWaitting.setVisibility(View.GONE);
                    String errorMsg =(String) msg.obj;
                    if (TextUtils.isEmpty(errorMsg))
                    {
                        mTip.setText("Error");
                    }else
                    {
                        mTip.setText(errorMsg);
                    }
                    break;

            }

            super.handleMessage(msg);
        }
    };

    private void prepareRsBitmap(JSONObject rs) {
        Bitmap bitmap = Bitmap.createBitmap(mPhotoImg.getWidth(),mPhotoImg.getHeight(),mPhotoImg.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mPhotoImg,0,0,null);
        try {
            JSONArray faces = rs.getJSONArray("face");

            int faceCount = faces.length();
            faceCount1=faceCount1+faceCount;
            if (faceCount!=0)
            { ma.setText("");
                coun1=++coun;
            if (coun1==1){ma.setText("请拍摄第二张！露出你的笑容！");}
            if (coun1==2){ma.setText("请拍摄第三张！露出八颗牙齿！");}
            if (coun1==3){ma.setText("请拍摄第四章！自信些更棒哦！");}
            if (coun1==4){
                mDetect.setVisibility(View.INVISIBLE);
                startCamera.setVisibility(View.INVISIBLE);
                btn.setVisibility(View.VISIBLE);}}else{Toast.makeText(this, "请重新拍摄",Toast.LENGTH_LONG).show();}
            mTip.setText("find"+faceCount);
            for (int i=0;i<faceCount;i++)
            {
                //拿到单独face对象
                JSONObject face = faces.getJSONObject(i);
                JSONObject posObj = face.getJSONObject("position");
                float x = (float) posObj.getJSONObject("center").getDouble("x");
                float y = (float)posObj.getJSONObject("center").getDouble("y");
                float w = (float)posObj.getDouble("width");
                float h = (float)posObj.getDouble("height");
                x=x/100 * bitmap.getWidth();
                y=y/100 * bitmap.getHeight();

                w=w/100 * bitmap.getWidth();
                h=h/100 * bitmap.getHeight();
                mPaint.setColor(0xffffffff);
                mPaint.setStrokeWidth(3);
                //画格子
                canvas.drawLine(x-w/2,y-h/2,x-w/2,y+h/2,mPaint);
                canvas.drawLine(x-w/2,y-h/2,x+w/2,y-h/2,mPaint);
                canvas.drawLine(x+w/2,y-h/2,x+w/2,y+h/2,mPaint);
                canvas.drawLine(x-w/2,y+h/2,x+w/2,y+h/2,mPaint);

                //获得微笑和性别
                int age = face.getJSONObject("attribute").getJSONObject("smiling").getInt("value");
                String gender = face.getJSONObject("attribute").getJSONObject("gender").getString("value");
                age1=age1+age;
                Bitmap ageBitmap = buildAgeBitmap(age, "Male".equals(gender));

                int ageWidth = ageBitmap.getWidth();
                int ageHeight = ageBitmap.getHeight();
                if (bitmap.getWidth()<mPhoto.getWidth()&&bitmap.getHeight()<mPhoto.getHeight())
                {
                    float ratio = Math.max(bitmap.getWidth()*1.0f/mPhoto.getWidth(),bitmap.getHeight()*1.0f/mPhoto.getHeight());
                    ageBitmap = Bitmap.createScaledBitmap(ageBitmap,(int)(ageWidth*ratio),(int)(ageHeight*ratio),false);
                }
                canvas.drawBitmap(ageBitmap,x-ageBitmap.getWidth()/2,y-h/2-ageBitmap.getHeight(),null);

                mPhotoImg = bitmap;








            }
            File f = new File("/storage/emulated/0/" + ++count+"tttt" + ".png");
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap buildAgeBitmap(int age, boolean isMale) {
        TextView tv = (TextView)mWaitting.findViewById(R.id.id_age_and_gender);
        tv.setText(age+"'");
        if (isMale)
        {
            tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male),null,null,null);
        }else {
            tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female), null, null, null);
        }
            tv.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(tv.getDrawingCache());

            tv.destroyDrawingCache();

            return bitmap;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.customCamera:
//                mDetect.setEnabled(true);
//                startActivity(new Intent(this,Zuan.class));
//                finish();
//                break;
//            case R.id.button:
//                startActivity(new Intent(this,CustomCamera11.class));
//                   finish();
//                break;

            case R.id.btnvdio:
                startActivity(new Intent(this,CustomCamera1.class));
                finish();
                break;
            case R.id.startCamera:
                mDetect.setEnabled(true);
                btnvdio.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                customCamera.setVisibility(View.INVISIBLE);
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = Uri.fromFile(new File(mFilePath));
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent1, REQ_2);
                break;
            case R.id.id_getImage:
                mDetect.setEnabled(true);
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType("image/*");
                startActivityForResult(intent2, PICK_CODE);

                break;
            case R.id.id_detect:
                mWaitting.setVisibility(View.VISIBLE);
                mDetect.setEnabled(false);
                if(!(mCurrentPhotoStr!=null&&!mCurrentPhotoStr.trim().equals("")))
               /*{
                   resizePhoto();
                }
                else*/
                {
                    mPhotoImg = BitmapFactory.decodeResource(getResources(),R.drawable.t4);
               }
                FaceppDetect.detect(mPhotoImg, new FaceppDetect.CallBack() {
                    @Override
                    public void success(JSONObject result)
                    {
                         Message msg = Message.obtain();
                        msg.what = MSG_SUCESS;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void error(FaceppParseException exception)
                    {
                        Message msg = Message.obtain();
                        msg.what = MSG_ERROR;
                        msg.obj = exception.getErrorMessage();
                        mHandler.sendMessage(msg);
                    }


                });
                //count++;
               // Toast.makeText(this,count+++"",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn:
                Intent intent = new Intent(MainA.this,Result.class);
                intent.putExtra("age", age1);
                intent.putExtra("facecount", faceCount1);
                startActivity(intent);
                break;
        }
    }





}
