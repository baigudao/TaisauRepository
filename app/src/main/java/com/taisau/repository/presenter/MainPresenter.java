package com.taisau.repository.presenter;

import android.hardware.Camera;

import com.taisau.repository.contract.MainContract;
import com.taisau.repository.model.MainModel;

/**
 * Created by Devin on 2018\7\27 0027.
 */
public class MainPresenter implements MainContract.Model, MainContract.Presenter {

    private MainContract.View view;
    private MainContract.Model model;
//    private Context mContext;

    public MainPresenter(/*Context context,*/ MainContract.View view) {
        this.view = view;
//        this.mContext = context;
        model = new MainModel(/*mContext,*/this);
    }

    @Override
    public Camera.PreviewCallback getPreviewCallback() {
        return model.getPreviewCallback();
    }
}
