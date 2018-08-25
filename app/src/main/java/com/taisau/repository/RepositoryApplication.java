package com.taisau.repository;

import android.app.Application;
import android.os.Environment;

import com.GFace;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.taisau.repository.util.FileUtils;

import java.io.File;

/**
 * Created by Devin on 2018\7\24 0024.
 */
public class RepositoryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        arithmeticInit();

        //初始化Utils
        Utils.init(this);
    }

    private void arithmeticInit() {
        String LIB_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/arithmetic";
        File fileLibDir = new File(LIB_DIR);
        if (!fileLibDir.exists())
            fileLibDir.mkdir();

        FileUtils.moveConfigFile(this, R.raw.base, LIB_DIR + "/base.dat");
        FileUtils.moveConfigFile(this, R.raw.a, LIB_DIR + "/a.dat");
        FileUtils.moveConfigFile(this, R.raw.d, LIB_DIR + "/d.dat");
        FileUtils.moveConfigFile(this, R.raw.db, LIB_DIR + "/db.dat");
        FileUtils.moveConfigFile(this, R.raw.p, LIB_DIR + "/p.dat");

        int ret = GFace.loadModel(LIB_DIR + "/d.dat", LIB_DIR + "/a.dat", LIB_DIR + "/db.dat", LIB_DIR + "/p.dat");
        System.out.println("init GFace:" + ret);
        LogUtils.i("___lhl___", "init GFace", ret);
    }
}
