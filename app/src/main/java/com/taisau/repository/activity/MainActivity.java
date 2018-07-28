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

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.taisau.repository.R;
import com.taisau.repository.contract.MainContract;
import com.taisau.repository.presenter.MainPresenter;
import com.taisau.repository.util.CameraUtils2;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements Camera.ErrorCallback, MainContract.View {

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
        setContentView(R.layout.activity_main);

        //初始化Presenter
        presenter = new MainPresenter(this);
        surfaceView = findViewById(R.id.main_camera_preview);
        LogUtils.i("___lhl___", "MainActivity   onCreate()");
    }


    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.i("___lhl___", "MainActivity   onStart()");
        if (cameraUtils == null) {
            cameraUtils = new CameraUtils2(this, this);
        }
        if (cameraUtils.getCamera() != null) {
            try {
                cameraUtils.getCamera().stopPreview();
                //绑定摄像头到surfaceView和Holder上
                //  cameraUtils.getCamera().autoFocus(null);
                cameraUtils.getCamera().setPreviewDisplay(surfaceHolder);
                cameraUtils.getCamera().setPreviewCallback(/*MainActivity.this*/presenter.getPreviewCallback());
                cameraUtils.getCamera().startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Camera.ErrorCallback
    @Override
    public void onError(int error, Camera camera) {
        LogUtils.i("___lhl___", "MainActivity   onError()");
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
        LogUtils.i("___lhl___", "MainActivity   onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i("___lhl___", "MainActivity   onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i("___lhl___", "MainActivity   onStop()");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("___lhl___", "MainActivity   onDestroy()");
    }
}
