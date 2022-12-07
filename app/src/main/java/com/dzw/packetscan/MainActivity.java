package com.dzw.packetscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.dzw.packetscan.base.BaseActivity;
import com.dzw.packetscan.databinding.ActivityMainBinding;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.WaybillOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.WaybillOCRResponse;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.utils.GsonUtils;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xutil.data.SPUtils;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.StrUtil;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.to_camera).setOnClickListener(v -> {
            if(StrUtil.isBlank(SettingFragment.getSecretId()) || StrUtil.isBlank(SettingFragment.getSecretKey())){
                SnackbarUtils.Short(v, "请点击右上角配置腾讯云数据").danger().show();
                return;
            }
            openNewPage(CameraKitFragment.class, false);
        });

        findViewById(R.id.setting).setOnClickListener(v -> {
            openNewPage(SettingFragment.class);
        });

    }
}