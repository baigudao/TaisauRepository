package com.taisau.repository.model;

import android.graphics.Bitmap;
import android.hardware.Camera;

import com.GFace;
import com.blankj.utilcode.util.LogUtils;
import com.taisau.repository.contract.MainContract;
import com.taisau.repository.util.ImgUtils;
import com.taisau.repository.util.YUVUtils;

import java.util.List;

/**
 * Created by Devin on 2018\7\27 0027.
 */
public class MainModel implements MainContract.Model, Camera.PreviewCallback {

    private MainContract.Presenter presenter;

    private static boolean isRun = false;
    private static int noFaceCount = 0;
    //是否检测人脸
    public static volatile boolean runDetect = false;
    //是否检测到人脸
    private static boolean hasFace = false;
    //是否进行人脸比对
    private static boolean runCompare = false;

    //检测后的人脸信息数据
    private GFace.FaceInfo aa;

    //    private float[] modFea;
    private List<float[]> modFeasList;

    public MainModel(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Camera.PreviewCallback getPreviewCallback() {
        return MainModel.this;
    }

    private byte[] temp = null;
    private byte[] ret = null;

    //摄像头数据返回
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        LogUtils.i("___lhl___", "MainModel   onPreviewFrame()");
//        LogUtils.e("data的长度为   >>>" + data.length);//1382400

        Bitmap bitmap = ImgUtils.getUtils().Bytes2Bitmap(data);
        //压缩yuv图片
        temp = YUVUtils.scaleYUV300_200(data, 1280, 720);
        ret = GFace.detectFace(temp, 300, 200);
        if (ret != null && ret[0] > 0) {
            aa = GFace.getFaceInfo(ret);
//            aa = adjustFaceInfo(aa, true);
//            //生成人脸框
////                if (SubstationApplication.getApplication().isOpenFaceFrame()) {
//            presenter.updateFaceFrame(changeSituation(aa.rc[0].left, aa.rc[0].top
//                    , aa.rc[0].right, aa.rc[0].bottom), 640, 360);
////                }
//            hasFace = true;
//            if (isSlow) {
//                isSlow = false;
//            }
            LogUtils.e("___lhl___", "has face");
        } else {
////                presenter.updateFaceFrame(changeSituation(0, 0, 0, 0), 640, 360);//step1
//            if (noFaceCount < noFace * 10)
//                noFaceCount++;
//            if (noFaceCount == noFace * 10) {
//                clearMode();
//                if (!isSlow) {
//                    isSlow = true;
//                }
//            }
//            hasFace = false;
            LogUtils.e("___lhl___", "no face");
        }
    }
}
