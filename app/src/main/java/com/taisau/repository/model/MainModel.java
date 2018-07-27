package com.taisau.repository.model;

import android.hardware.Camera;

import com.blankj.utilcode.util.LogUtils;
import com.taisau.repository.contract.MainContract;

/**
 * Created by Devin on 2018\7\27 0027.
 */
public class MainModel implements MainContract.Model, Camera.PreviewCallback {

    private MainContract.Presenter presenter;

    public MainModel(/*Context context,*/ MainContract.Presenter presenter2) {
        this.presenter = presenter2;
//        this.mContext = context;
//        initSoundPool();
    }

    @Override
    public Camera.PreviewCallback getPreviewCallback() {
        return MainModel.this;
    }

    //摄像头数据返回
    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        LogUtils.i("___lhl___", "MainModel   onPreviewFrame()");
    }
}
