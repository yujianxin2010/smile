package com.example.myxx;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class CustomCamera1 extends Activity implements SurfaceHolder.Callback{

    private int FindFrontCamera(){
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
            if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_FRONT ) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }
    private int FindBackCamera(){
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
            if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_BACK ) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    //后加
    private final String TAG = "main";
    private EditText et_path1;
    private SurfaceView sv1;
    private Button btn_play1, btn, btn_replay1, btn_stop1;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar1;
    private int currentPosition = 0;
    private boolean isPlaying;



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setStartPreview(mCamera,mHolder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
     mCamera.stopPreview();
        setStartPreview(mCamera,mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    releaseCamera();
    }

   // File f;
   /* private void saveMyBitmap(String bitName, Bitmap bitmap) {
//将图像保存到SD卡中
    }*/
    private MediaRecorder mediarecorder;
    private SurfaceView mPreview;
    private Camera mCamera;
    private SurfaceHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom1);
        mPreview = (SurfaceView) findViewById(R.id.preview);
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mPreview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCamera.autoFocus(null);
            }
        } );

        //后加
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        sv1 = (SurfaceView) findViewById(R.id.sv1);
       // et_path1 = (EditText) findViewById(R.id.et_path1);
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(click);
        btn_play1 = (Button) findViewById(R.id.btn_play1);
        btn_play1.setOnClickListener(click);
        sv1.getHolder().addCallback(callback);
    }

    private View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_play1:
                    play(0);
                    btn.setVisibility(View.VISIBLE);
                    btn_play1.setVisibility(View.INVISIBLE);
                    break;
                case R.id.btn:
                    startActivity(new Intent(CustomCamera1.this,MainA.class));
                    finish();
                    break;
              /* case R.id.btn_pause:
                    pause();
                    break;
                case R.id.btn_replay:
                    replay();
                    break;
                case R.id.btn_stop:
                    stop();
                    break;
                default:
                    break;*/
            }
        }
    };
    protected void play(final int msec) {
        // 获取视频文件地址
        String m=Environment.getExternalStorageDirectory().getPath();
        String path = m+"/love3.mp4";
                //et_path1.getText().toString().trim();
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, "视频文件路径错误",Toast.LENGTH_LONG).show();
            return;
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            mediaPlayer.setDataSource(file.getAbsolutePath());
            // 设置显示视频的SurfaceHolder
            mediaPlayer.setDisplay(sv1.getHolder());
            Log.i(TAG, "开始装载");
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.i(TAG, "装载完成");
                    mediaPlayer.start();
                    // 按照初始位置播放
                    mediaPlayer.seekTo(msec);
                    // 设置进度条的最大进度为视频流的最大播放时长
                    seekBar1.setMax(mediaPlayer.getDuration());
                    // 开始线程，更新进度条的刻度
                    new Thread() {

                        @Override
                        public void run() {
                            try {
                                isPlaying = true;
                                while (isPlaying) {
                                    int current = mediaPlayer
                                            .getCurrentPosition();
                                    seekBar1.setProgress(current);

                                    sleep(500);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    btn_play1.setEnabled(false);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被回调
                    btn_play1.setEnabled(true);
                    startActivity(new Intent(CustomCamera1.this,MainA.class));
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 发生错误重新播放
                    replay();
                    isPlaying = false;
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        // SurfaceHolder被修改的时候回调
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(TAG, "SurfaceHolder 被销毁");
            // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                currentPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "SurfaceHolder 被创建");
            if (currentPosition > 0) {
                // 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
                play(currentPosition);
                currentPosition = 0;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Log.i(TAG, "SurfaceHolder 大小被改变");
        }

    };

    //
    public void capture (View view)
    {
         //  Camera.Parameters parameters = mCamera.getParameters();
      //  parameters.setPictureFormat(ImageFormat.JPEG);

       // parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        //parameters.setPreviewSize(3200,1800);
       // parameters.setPictureSize(3200,1800);
       // mCamera.setParameters(parameters);

       /* mCamera.autoFocus(new Camera.AutoFocusCallback(){
            @Override
            public void onAutoFocus(boolean success,Camera camera){
               if (success){
                   mCamera.takePicture(null,null,mPictureCallback);

               }
            }

        });*/
        replay();
        mediarecorder = new MediaRecorder();// 创建mediarecorder对象
        // 设置录制视频源为Camera(相机)
        mCamera.stopPreview();
        mCamera.unlock();

        mediarecorder.setCamera(mCamera);
         mediarecorder.setOrientationHint(0);
        mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4

        mediarecorder
                .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 设置录制的视频编码h263 h264
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
        mediarecorder.setVideoSize(640, 480);
        // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
        mediarecorder.setVideoFrameRate(20);
        mediarecorder.setPreviewDisplay(mHolder.getSurface());
        String m=Environment.getExternalStorageDirectory().getPath();
        // 设置视频文件输出的路径
        mediarecorder.setOutputFile(m+"/love.mp4");
        try {
            // 准备录制
            mediarecorder.prepare();
            // 开始录制
            mediarecorder.start();
        } catch (IllegalStateException e)
        {e.printStackTrace();} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


public void ca (View v){
    if (mediarecorder != null) {
        // 停止录制
        mediarecorder.stop();
        mediarecorder.reset();
        // 释放资源
        mediarecorder.release();
        mediarecorder = null;
        finish();
        startActivity(new Intent(this,MainActivity1.class));

    }
}



    @Override
    protected void onResume(){
      super.onResume();
        if (mCamera == null){
            mCamera = getCamera();
            if (mHolder!=null)
            {
                setStartPreview(mCamera,mHolder);
            }
        }
    }
        @Override
    protected void onPause(){
            super.onPause();
            releaseCamera();
        }

        private Camera  getCamera()
        {
            int CammeraIndex=FindFrontCamera();
            Camera camera;
            try{
                camera = Camera.open(CammeraIndex);
            }catch (Exception e){
                camera=null;
            e.printStackTrace();
        }
            return camera;
        }
        private void setStartPreview(Camera camera,SurfaceHolder holder){
           try {
               camera.setPreviewDisplay(holder);
               //调整预览角度
               camera.setDisplayOrientation(90);
               camera.startPreview();

           }catch (IOException e){
               e.printStackTrace();
           }

    }

    private void releaseCamera()
    {
        if (mCamera!=null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();

            mCamera.release();
            mCamera=null;
        }

    }
    protected void replay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            Toast.makeText(this, "重新播放",Toast.LENGTH_LONG).show();

            return;
        }
        isPlaying = false;
        play(0);


    }
    }

