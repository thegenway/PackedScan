package com.dzw.packetscan;

import static com.wonderkiln.camerakit.CameraKit.Constants.FLASH_AUTO;
import static com.wonderkiln.camerakit.CameraKit.Constants.FLASH_OFF;
import static com.wonderkiln.camerakit.CameraKit.Constants.FLASH_ON;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dzw.packetscan.base.BaseFragment;
import com.dzw.packetscan.databinding.FragmentCameraKitBinding;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaImageView;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xutil.app.FragmentUtils;
import com.xuexiang.xutil.display.ImageUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Page
public class CameraKitFragment extends BaseFragment<FragmentCameraKitBinding> {

    //相机
    private CameraView cameraView;
    //闪光灯图标
    private XUIAlphaImageView ivFlashLight;
    //拍照键
    private XUIAlphaImageView iv_take_photo;

    @NonNull
    @Override
    protected FragmentCameraKitBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentCameraKitBinding.inflate(inflater, container, false);
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initViews() {
        cameraView = findViewById(R.id.camera_view);
        ivFlashLight = findViewById(R.id.iv_flash_light);
        iv_take_photo = findViewById(R.id.iv_take_photo);

        //按下拍照键
        iv_take_photo.setOnClickListener(v -> {
            getProgressLoader("处理中...").showLoading();
            cameraView.captureImage();
        });

        switchFlashIcon(cameraView.getFlash());
        cameraView.setJpegQuality(10);
        cameraView.setZoom(0.5f);
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @IOThread
            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                getProgressLoader().dismissLoading();
                File savedPhoto = new File(FileUtils.getDiskCacheDir(), "PackedScan.jpg");

                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = ImageUtils.compressByScale(bitmap, 0.3f, 0.3f, true);
                bitmap = ImageUtils.compressByQuality(bitmap, 10, true);
                byte[] bytes = ImageUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG);

                try {
                    FileOutputStream ops = new FileOutputStream(savedPhoto.getPath());
                    ops.write(bytes);
                    ops.close();

                    openPage(DataFragment.class, "path", savedPhoto.getPath());

                } catch (IOException e) {
                    ToastUtils.toast("出错了");
                    e.printStackTrace();
                }
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        //切换闪光灯
        ivFlashLight.setOnClickListener(v -> {
            switchFlashIcon(cameraView.toggleFlash());
        });

    }

    private void switchFlashIcon(int flash) {
        switch (flash) {
            case FLASH_OFF:
                ivFlashLight.setImageResource(R.drawable.ic_flash_off);
                break;
            case FLASH_ON:
                ivFlashLight.setImageResource(R.drawable.ic_flash_on);
                break;
            case FLASH_AUTO:
                ivFlashLight.setImageResource(R.drawable.ic_flash_auto);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPause() {
        cameraView.stop();
        super.onPause();
    }

}