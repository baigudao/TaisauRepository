package com.taisau.repository.presenter;

import android.hardware.Camera;

import com.taisau.repository.contract.MainContract;
import com.taisau.repository.model.MainModel;

/**
 * Created by Devin on 2018\7\27 0027.
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private MainContract.Model model;

    public MainPresenter(MainContract.View view) {
        this.view = view;
        model = new MainModel(this);
    }

    @Override
    public Camera.PreviewCallback getPreviewCallback() {
        return model.getPreviewCallback();
    }
}
