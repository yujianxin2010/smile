package com.example.myxx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-11-29.
 */

public class Result extends AppCompatActivity implements View.OnClickListener {
    private Button Cl;
    private TextView btn1;
    private TextView btn2;
    private TextView btn3;
    private TextView btn;
    private ImageView ima1;
    private ImageView ima2;
    private ImageView ima3;
    private ImageView ima4;
    private Bitmap mCurrentPhotoStr1;
    private Bitmap mCurrentPhotoStr2;
    private Bitmap mCurrentPhotoStr3;
    private Bitmap mCurrentPhotoStr4;
    private String mFilePath1;
    private String mFilePath2;
    private String mFilePath3;
    private String mFilePath4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        ima1=(ImageView)findViewById(R.id.ima1);
        ima2=(ImageView)findViewById(R.id.ima2);
        ima3=(ImageView)findViewById(R.id.ima3);
        ima4=(ImageView)findViewById(R.id.ima4);
        btn=(TextView)findViewById(R.id.btn);
        btn1=(TextView)findViewById(R.id.btn1);
        btn2=(TextView)findViewById(R.id.btn2);
        btn3=(TextView)findViewById(R.id.btn3);
        Cl= (Button) findViewById(R.id.button11);
        Cl.setOnClickListener(this);
        Intent intent = getIntent();
        int age = intent.getIntExtra("age", 0);
        int facecount = intent.getIntExtra("facecount", 0);
        age=age/facecount;
        if (age<=60)
        {
            btn3.setVisibility(View.VISIBLE);
        }else if (age<=75){btn2.setVisibility(View.VISIBLE);}
        else if (age<=90){btn1.setVisibility(View.VISIBLE);}
        else{btn.setVisibility(View.VISIBLE);}

        //Toast.makeText(this, ""+age,Toast.LENGTH_LONG).show();
        mFilePath1 = "/storage/emulated/0" + "/" + "1tttt.png";
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        mCurrentPhotoStr1= BitmapFactory.decodeFile(mFilePath1, options1);
        mFilePath2 = "/storage/emulated/0" + "/" + "2tttt.png";
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        mCurrentPhotoStr2= BitmapFactory.decodeFile(mFilePath2, options2);
        mFilePath3 = "/storage/emulated/0" + "/" + "3tttt.png";
        BitmapFactory.Options options3 = new BitmapFactory.Options();
        mCurrentPhotoStr3= BitmapFactory.decodeFile(mFilePath3, options3);
        mFilePath4 = "/storage/emulated/0" + "/" + "4tttt.png";
        BitmapFactory.Options options4 = new BitmapFactory.Options();
        mCurrentPhotoStr4= BitmapFactory.decodeFile(mFilePath4, options4);
        ima1.setImageBitmap(mCurrentPhotoStr1);
        ima2.setImageBitmap(mCurrentPhotoStr2);
        ima3.setImageBitmap(mCurrentPhotoStr3);
        ima4.setImageBitmap(mCurrentPhotoStr4);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button11:
                Intent intent = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
