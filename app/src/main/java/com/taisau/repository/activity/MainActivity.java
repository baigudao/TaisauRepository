package com.taisau.repository.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.taisau.repository.R;
import com.taisau.repository.contract.MainContract;
import com.taisau.repository.presenter.MainPresenter;
import com.taisau.repository.util.CameraUtils2;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MainContract.View, Camera.ErrorCallback, SurfaceHolder.Callback {

    private MainPresenter presenter;

    //承载画面的surfaceView
    private SurfaceView surfaceView;
    //用于承载预览画面生成图片的数据的holder
    private SurfaceHolder surfaceHolder;
    //相机的辅助工具类
    public CameraUtils2 cameraUtils;

    private AlertDialog errorDialog;
    public Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
//        LogUtils.e("w:" + surfaceView.getWidth() + " h:" + surfaceView.getHeight());

        //初始化Presenter
        presenter = new MainPresenter(this);
        LogUtils.d("MainActivity   onCreate()");
    }


    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d("MainActivity   onStart()");
        surfaceView = findViewById(R.id.main_camera_preview);
        surfaceHolder = surfaceView.getHolder();
        // 为surfaceHolder添加一个回调监听器，用于监听surfaceView的变化
        surfaceHolder.addCallback(this);
        surfaceView.setKeepScreenOn(true);
        if (cameraUtils == null) {
            cameraUtils = new CameraUtils2(this, this);
        }
        if (cameraUtils.getCamera() != null) {
            try {
                cameraUtils.getCamera().stopPreview();
                //绑定摄像头到surfaceView和Holder上
                //  cameraUtils.getCamera().autoFocus(null);
                cameraUtils.getCamera().setPreviewDisplay(surfaceHolder);
                cameraUtils.getCamera().setPreviewCallback(presenter.getPreviewCallback());
                cameraUtils.getCamera().startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Camera.ErrorCallback
    @Override
    public void onError(int error, Camera camera) {
        LogUtils.d("MainActivity   onError()");
        if (error == 100) {
            errorDialog = new AlertDialog.Builder(this).setTitle("摄像头失效")
                    .setMessage("请确认摄像头是否有效，或者重新插拔摄像头点击确定键重新初始化\n提示:5秒后会自动进行初始化工作")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (errorDialog != null && errorDialog.isShowing()) {
                                errorDialog.dismiss();
                                if (cameraUtils != null) {
                                    cameraUtils.releaseCamera();
                                    cameraUtils = null;
                                    cameraUtils = new CameraUtils2(MainActivity.this, MainActivity.this);
                                    try {
                                        cameraUtils.getCamera().stopPreview();
                                        //绑定摄像头到surfaceView和Holder上
                                        //  cameraUtils.getCamera().autoFocus(null);
                                        cameraUtils.getCamera().setPreviewDisplay(surfaceHolder);
                                        cameraUtils.getCamera().setPreviewCallback(presenter.getPreviewCallback());
                                        cameraUtils.getCamera().startPreview();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                surfaceView.setVisibility(View.GONE);
                                surfaceView.setVisibility(View.VISIBLE);
                            }
                        }
                    })
                    .setNegativeButton("关闭应用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ToastUtils.showShort("关闭应用！");
//                            Intent intent = new Intent();
//                            intent.setClass(MainActivity.this, WelcomeActivity.class);
//                            intent.putExtra("exit_flag", "exit_id");
//                            MainActivity.this.startActivity(intent);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (errorDialog != null && errorDialog.isShowing()) {
                    errorDialog.dismiss();
                    if (cameraUtils != null) {
                        cameraUtils.releaseCamera();
                        cameraUtils = null;
                        cameraUtils = new CameraUtils2(MainActivity.this, MainActivity.this);
                        try {
                            cameraUtils.getCamera().stopPreview();
                            //绑定摄像头到surfaceView和Holder上
                            //  cameraUtils.getCamera().autoFocus(null);
                            cameraUtils.getCamera().setPreviewDisplay(surfaceHolder);
                            cameraUtils.getCamera().setPreviewCallback(presenter.getPreviewCallback());
                            cameraUtils.getCamera().startPreview();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    surfaceView.setVisibility(View.GONE);
                    surfaceView.setVisibility(View.VISIBLE);
                }
            }
        }, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("MainActivity   onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("MainActivity   onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d("MainActivity   onStop()");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("MainActivity   onDestroy()");
    }

    //surfaceView生成时会调用此函数
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        LogUtils.d("MainActivity   surfaceCreated()");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        if (cameraUtils.getCamera() != null) {
            try {
                cameraUtils.getCamera().stopPreview();
                //绑定摄像头到surfaceView和Holder上
                //  cameraUtils.getCamera().autoFocus(null);
                cameraUtils.getCamera().setPreviewDisplay(surfaceHolder);
                cameraUtils.getCamera().setPreviewCallback(presenter.getPreviewCallback());
                cameraUtils.getCamera().startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtils.d("MainActivity   surfaceChanged()");
    }

    //surfaceView销毁时会调用此函数此时释放摄像头
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        LogUtils.d("MainActivity   surfaceDestroyed()");
    }
}
