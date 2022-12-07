package com.dzw.packetscan;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.dzw.packetscan.adapter.ResultDataAdapter;
import com.dzw.packetscan.base.BaseFragment;
import com.dzw.packetscan.databinding.FragmentDataBinding;
import com.dzw.packetscan.vo.WaybillOCR;
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
import com.xuexiang.xhttp2.reflect.TypeToken;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.utils.GsonUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.XUIWrapContentListView;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xutil.display.ImageUtils;

 import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;

@Page
public class DataFragment extends BaseFragment<FragmentDataBinding> {

    private AppCompatImageView iv_photo;

    @NonNull
    @Override
    protected FragmentDataBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentDataBinding.inflate(inflater, container, false);
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initViews() {
        iv_photo = findViewById(R.id.iv_photo);

        Bundle bundle = getArguments();
        if(bundle != null){
            String path = bundle.getString("path");

            Bitmap bitmap = ImageUtils.getBitmap(path);
            String base64 = bitmaptoString(bitmap);
            System.out.println(base64);

            iv_photo.setImageURI(Uri.parse(path));
            loadData(base64);
        }
    }

    private void loadData(String base64) {
        getProgressLoader("正在解析...").showLoading();
        new Thread(() -> {
            String jsonString = null;
            try{
                // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
                // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
                Credential cred = new Credential(SettingFragment.getSecretId(), SettingFragment.getSecretKey());
                // 实例化一个http选项，可选的，没有特殊需求可以跳过
                HttpProfile httpProfile = new HttpProfile();
                httpProfile.setEndpoint("ocr.tencentcloudapi.com");
                // 实例化一个client选项，可选的，没有特殊需求可以跳过
                ClientProfile clientProfile = new ClientProfile();
                clientProfile.setHttpProfile(httpProfile);
                // 实例化要请求产品的client对象,clientProfile是可选的
                OcrClient client = new OcrClient(cred, "ap-shanghai", clientProfile);
                // 实例化一个请求对象,每个接口都会对应一个request对象
                WaybillOCRRequest req = new WaybillOCRRequest();
                req.setImageBase64(base64);

                // 返回的resp是一个WaybillOCRResponse的实例，与请求对象对应
                WaybillOCRResponse resp = client.WaybillOCR(req);

                // 输出json格式的字符串回包
                jsonString = WaybillOCRResponse.toJsonString(resp);
                notifyChange(2001, jsonString);
            } catch (TencentCloudSDKException e) {
                System.out.println(e.toString());
                notifyChange(5001, e.getMessage());
            }
        }).start();

    }

    private void notifyChange(int what, String data) {
        Message msg = new Message();
        msg.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        msg.setData(bundle);
        DataFragment.this.handler.sendMessage(msg);
    }

    public String bitmaptoString(Bitmap bitmap) {
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    private void initResultLayout(String jsonString) {
        WaybillOCR ocr = new WaybillOCR();
        Map<String, Object> map = GsonUtils.fromJson(jsonString, new TypeToken<Map<String, Object>>() {}.getType());
        System.out.println(map);
        Map<String, Object> TextDetectionsMap = MapUtil.get(map, "TextDetections", Map.class);
        if(TextDetectionsMap == null) return;

        Map<String, Object> RecAddrMap = MapUtil.get(TextDetectionsMap, "RecAddr", Map.class);
        if(RecAddrMap != null){
            ocr.setRecAddr(MapUtil.getStr(RecAddrMap, "Text"));
        }

        Map<String, Object> RecNameMap = MapUtil.get(TextDetectionsMap, "RecName", Map.class);
        if(RecNameMap != null){
            ocr.setRecName(MapUtil.getStr(RecNameMap, "Text"));
        }

        Map<String, Object> RecNumMap = MapUtil.get(TextDetectionsMap, "RecNum", Map.class);
        if(RecNumMap != null){
            ocr.setRecNum(MapUtil.getStr(RecNumMap, "Text"));
        }

        Map<String, Object> SenderAddrMap = MapUtil.get(TextDetectionsMap, "SenderAddr", Map.class);
        if(SenderAddrMap != null){
            ocr.setSenderAddr(MapUtil.getStr(SenderAddrMap, "Text"));
        }

        Map<String, Object> SenderNameMap = MapUtil.get(TextDetectionsMap, "SenderName", Map.class);
        if(SenderNameMap != null){
            ocr.setSenderName(MapUtil.getStr(SenderNameMap, "Text"));
        }

        Map<String, Object> SenderNumMap = MapUtil.get(TextDetectionsMap, "SenderNum", Map.class);
        if(SenderNumMap != null){
            ocr.setSenderNum(MapUtil.getStr(SenderNumMap, "Text"));
        }

        Map<String, Object> WaybillNumMap = MapUtil.get(TextDetectionsMap, "WaybillNum", Map.class);
        if(WaybillNumMap != null){
            ocr.setWaybillNum(MapUtil.getStr(WaybillNumMap, "Text"));
        }

        List<Dict> dictList = ocr.getList();
        XUIWrapContentListView recyclerView = findViewById(R.id.list_view);
        ResultDataAdapter adapter = new ResultDataAdapter(getContext(), dictList);
        recyclerView.setAdapter(adapter);
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //移除加载框
            getProgressLoader("正在解析...").dismissLoading();

            if(msg.what == 5001){
                DialogLoader.getInstance().showTipDialog(getContext(), "提示", msg.getData().getString("data"), "确定");
            }else if(msg.what == 2001){
                initResultLayout(msg.getData().getString("data"));
            }
        }
    };

}