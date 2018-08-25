package com.taisau.repository.contract;

import android.hardware.Camera;

/**
 * Created by Devin on 2018\7\27 0027.
 */
public interface MainContract {

    interface Model {
        Camera.PreviewCallback getPreviewCallback();
    }

    interface View {

    }

    interface Presenter {
        Camera.PreviewCallback getPreviewCallback();
    }
}
