package com.taisau.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.GFace;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.squareup.leakcanary.LeakCanary;
import com.taisau.repository.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devin on 2018\7\24 0024.
 */
public class RepositoryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Utils
        Utils.init(this);
        initLog();
        initCrash();

        initLeakCanary();

        //初始化算法
        arithmeticInit();

        requestPermission();
    }

    // init it in ur application
    public void initLog() {
        LogUtils.getConfig()
                .setLogSwitch(BuildConfig.DEBUG)// 设置 log 总开关，包括输出到控制台和文件，默认开
                .setConsoleSwitch(BuildConfig.DEBUG)// 设置是否输出到控制台开关，默认开
                .setGlobalTag("_lhl_")// 设置 log 全局标签，默认为空
                // 当全局标签不为空时，我们输出的 log 全部为该 tag，
                // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                .setLogHeadSwitch(true)// 设置 log 头信息开关，默认为开
                .setLog2FileSwitch(false)// 打印 log 时是否存到文件的开关，默认关
                .setDir("")// 当自定义路径为空时，写入应用的/cache/log/目录中
                .setFilePrefix("log")// 当文件前缀为空时，默认为"util"，即写入文件为"util-yyyy-MM-dd.txt"
                .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
                .setSingleTagSwitch(true)// 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
                .setConsoleFilter(LogUtils.V)// log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(LogUtils.V)// log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setStackDeep(1)// log 栈深度，默认为 1
                .setStackOffset(0)// 设置栈偏移，比如二次封装的话就需要设置，默认为 0
                .setSaveDays(3)// 设置日志可保留天数，默认为 -1 表示无限时长
                // 新增 ArrayList 格式化器，默认已支持 Array, Throwable, Bundle, Intent 的格式化输出
                .addFormatter(new LogUtils.IFormatter<ArrayList>() {
                    @Override
                    public String format(ArrayList list) {
                        return "LogUtils Formatter ArrayList { " + list.toString() + " }";
                    }
                });
        LogUtils.e(LogUtils.getConfig().toString());
    }

    @SuppressLint("MissingPermission")
    private void initCrash() {
        CrashUtils.init(new CrashUtils.OnCrashListener() {
            @Override
            public void onCrash(String crashInfo, Throwable e) {
                LogUtils.e(crashInfo);
                AppUtils.relaunchApp();
            }
        });
    }

    private void initLeakCanary() {
        // 内存泄露检查工具
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
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
        LogUtils.d("init GFace", ret);
    }

    private void requestPermission() {
        PermissionUtils.permission(PermissionConstants.CAMERA)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
//                        DialogHelper.showRationaleDialog(shouldRequest);
                        LogUtils.w("rationale");
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
//                        updateAboutPermission();
                        LogUtils.w(permissionsGranted);
                        LogUtils.w("onGranted");
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
//                            DialogHelper.showOpenAppSettingDialog();
                            LogUtils.w("here..."+permissionsDeniedForever, permissionsDenied);
                        }
                        LogUtils.w(permissionsDeniedForever, permissionsDenied);
                    }
                })
                .theme(new PermissionUtils.ThemeCallback() {
                    @Override
                    public void onActivityCreate(Activity activity) {
                        ScreenUtils.setFullScreen(activity);
                    }
                })
                .request();
    }
}
