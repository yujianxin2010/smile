package com.example.myxx;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

import static android.graphics.ImageFormat.JPEG;

public class ThirdCamera extends AppCompatActivity {
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

    SurfaceView sView;
    SurfaceHolder surfaceHolder;
    int screenWidth, screenHeight;
    // 定义系统所用的照相机
    Camera camera;
    // 是否在预览中
    boolean isPreview = false;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_third_camera);
        // 获取窗口管理器
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        // 获取屏幕的宽和高
        display.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        // 获取界面中SurfaceView组件
        sView = (SurfaceView) findViewById(R.id.preview);
        // 获得SurfaceView的SurfaceHolder
        surfaceHolder = sView.getHolder();
        // 为surfaceHolder添加一个回调监听器
        surfaceHolder.addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height)
            {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                // 打开摄像头
                try {
                    initCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                // 如果camera不为null ,释放摄像头
                if (camera != null)
                {
                    if (isPreview) camera.stopPreview();
                    camera.release();
                    camera = null;
                }
            }
        });
    }

    private void initCamera() throws IOException {
        if (!isPreview) {
            // 此处默认打开后置摄像头。
            // 通过传入参数可以打开前置摄像头
            int CammeraIndex=FindFrontCamera();
            camera = Camera.open(CammeraIndex);  //①
            //  camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
        }

        if (camera != null && !isPreview){
            try
            {











                Camera.Parameters parameters = camera.getParameters();









                // 设置预览照片的大小
                parameters.setPreviewSize(screenWidth, screenHeight);
                // 设置预览照片时每秒显示多少帧的最小值和最大值
                parameters.setPreviewFpsRange(4, 10);
                // 设置图片格式
                parameters.setPictureFormat(JPEG);
                // 设置JPG照片的质量
                parameters.set("jpeg-quality", 100);
                // 设置照片的大小
                parameters.setPictureSize(screenWidth, screenHeight);
                // 通过SurfaceView显示取景画面*/
                camera.setPreviewDisplay(surfaceHolder);  //②
                // 开始预览
                camera.startPreview();  //③
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            isPreview = true;
        }
    }
    /**
     * 此方法在布局文件中调用
     * */
    public void capture(View source)
    {
        if (camera != null) {
            // 控制摄像头自动对焦后才拍照
            camera.autoFocus(autoFocusCallback);  //④
        }
    }

    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback()
    {
        // 当自动对焦时激发该方法
        @Override
        public void onAutoFocus(boolean success, final Camera camera)
        {
            if (success)
            {

                Camera.Parameters parameters = camera.getParameters();


                parameters.setPictureFormat(ImageFormat.JPEG);
                // mCamera.setParameters(parameters);
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                List<Camera.Size> SupportedPreviewSizes = parameters
                        .getSupportedPreviewSizes();// 获取支持预览照片的尺寸
                Camera.Size previewSize = SupportedPreviewSizes.get(0);// 从List取出Size
                parameters
                        .setPreviewSize(previewSize.width, previewSize.height);// 设置预览照片的大小
                List<Camera.Size> supportedPictureSizes = parameters
                        .getSupportedPictureSizes();// 获取支持保存图片的尺寸
                Camera.Size pictureSize = supportedPictureSizes.get(0);// 从List取出Size
                parameters
                        .setPictureSize(pictureSize.width, pictureSize.height);// 设置照片的大小*/
                // camera.setParameters(parameters);
                // takePicture()方法需要传入3个监听器参数
                // 第1个监听器：当用户按下快门时激发该监听器
                // 第2个监听器：当相机获取原始照片时激发该监听器
                // 第3个监听器：当相机获取JPG照片时激发该监听器
                camera.takePicture(new Camera.ShutterCallback()
                {
                    public void onShutter()
                    {
                        // 按下快门瞬间会执行此处代码
                    }
                }, new Camera.PictureCallback()
                {
                    public void onPictureTaken(byte[] data, Camera c)
                    {
                        // 此处代码可以决定是否需要保存原始照片信息

                    }
                }, myJpegCallback);  //⑤
            }
        }
    };

    Camera.PictureCallback myJpegCallback = new Camera.PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {



            Intent intent = new Intent(ThirdCamera.this, MainActivity3.class);
            intent.putExtra("picPath", data);
            startActivity(intent);

            finish();
            ThirdCamera.this.finish();
        }
    };



}