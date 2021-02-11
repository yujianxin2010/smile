package com.example.myxx;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import android.graphics.PaintFlagsDrawFilter;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;

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



public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {


    private ImageView mPhoto;

    private Button mDetect;

    private TextView mTip;
    private View mWaitting;


    int count=0;

    private Button m;


    private Bitmap mPhotoImg;

    private Paint mPaint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        initViews();
        initEvent();
         mPaint =new Paint();

     Intent intent = getIntent();
        if(intent !=null) {



                byte [] bis=intent.getByteArrayExtra("picPath");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;


            double ratio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);
            options.inSampleSize = (int) Math.ceil(ratio);
            options.inJustDecodeBounds = false;
            mPhotoImg=BitmapFactory.decodeByteArray(bis,0,bis.length,options);

            Matrix matrix = new Matrix();
            matrix.setRotate(-90);
            // 设置想要的大小
            int newWidth = 750;
            int newHeight = 750;
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / mPhotoImg.getWidth();
            float scaleHeight = ((float) newHeight) / mPhotoImg.getHeight();

            matrix.postScale(scaleWidth, scaleHeight);
            mPhotoImg= Bitmap.createBitmap(mPhotoImg, 0, 0, mPhotoImg.getWidth(), mPhotoImg.getHeight(), matrix, true);

                mPhoto.setImageBitmap(mPhotoImg);


        }

    }
    private void initEvent() {
        mDetect.setOnClickListener(this);
        m.setOnClickListener(this);
    }

    private void initViews() {
        m= (Button) findViewById(R.id.button10);
        mPhoto = (ImageView) findViewById(R.id.id_photo);

        mDetect = (Button) findViewById(R.id.id_detect);
        mTip = (TextView) findViewById(R.id.id_tip);

        mWaitting = findViewById(R.id.id_waitting);


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
            if(faceCount==0){
                Toast.makeText(this, "请正对摄像头重新拍摄",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,FirstCamera.class));
            finish();
            }else {m.setVisibility(View.VISIBLE);}
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
                mPaint.setAntiAlias(true);
                mPaint.setDither(true);
                canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
                //画格子
                canvas.drawLine(x-w/2,y-h/2,x-w/2,y+h/2,mPaint);
                canvas.drawLine(x-w/2,y-h/2,x+w/2,y-h/2,mPaint);
                canvas.drawLine(x+w/2,y-h/2,x+w/2,y+h/2,mPaint);
                canvas.drawLine(x-w/2,y+h/2,x+w/2,y+h/2,mPaint);

                //获得微笑和性别
                int age = face.getJSONObject("attribute").getJSONObject("smiling").getInt("value");
                String gender = face.getJSONObject("attribute").getJSONObject("gender").getString("value");

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

            case R.id.button10:
                startActivity(new Intent(this,SecondCamera.class));
                break;
            case R.id.id_detect:

                mDetect.setVisibility(View.INVISIBLE);
                mWaitting.setVisibility(View.VISIBLE);
                mDetect.setEnabled(false);

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

                break;

        }
    }





}
