package com.dzw.packetscan;

import android.app.Application;

import com.dzw.packetscan.base.BaseActivity;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.StringUtils;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        XUI.init(this);
        XUI.debug(MyApp.isDebug());

        XUtil.init(this);
        XUtil.debug(MyApp.isDebug());

        PageConfig.getInstance().debug("PageLog").setContainActivityClazz(BaseActivity.class).init(this);

        XAOP.init(this);
        XAOP.debug(MyApp.isDebug());
        XAOP.setOnPermissionDeniedListener(permissionsDenied -> {
            XToastUtils.error("权限申请被拒绝:" + StringUtils.listToString(permissionsDenied, ","));
        });

        XHttpSDK.init(this);
        XHttpSDK.setBaseUrl("https://blog.csdn.net/");
        if(MyApp.isDebug()) XHttpSDK.debug();

        if(MyApp.isDebug()){
            XRouter.openLog();
            XRouter.openDebug();
        }
        XRouter.init(this);

    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

}
