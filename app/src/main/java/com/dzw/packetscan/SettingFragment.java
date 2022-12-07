package com.dzw.packetscan;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dzw.packetscan.base.BaseFragment;
import com.dzw.packetscan.databinding.FragmentSettingBinding;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xutil.data.SPUtils;

@Page
public class SettingFragment extends BaseFragment<FragmentSettingBinding> {

    @NonNull
    @Override
    protected FragmentSettingBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentSettingBinding.inflate(inflater, container, false);
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initViews() {
        TextView secretIdText = findViewById(R.id.secretIdText);
        TextView secretKeyText = findViewById(R.id.secretKeyText);

        secretIdText.setText(getSecretId());
        secretKeyText.setText(getSecretKey());

        findViewById(R.id.confirm_button).setOnClickListener(v -> {
            SharedPreferences sharedPreferences = SPUtils.getDefaultSharedPreferences();
            SPUtils.putString(sharedPreferences, "secretId", secretIdText.getText().toString());
            SPUtils.putString(sharedPreferences, "secretKey", secretKeyText.getText().toString());
            popToBack();
        });
    }

    public static String getSecretId() {
        SharedPreferences sharedPreferences = SPUtils.getDefaultSharedPreferences();
        return SPUtils.getString(sharedPreferences, "secretId", null);
    }

    public static String getSecretKey() {
        SharedPreferences sharedPreferences = SPUtils.getDefaultSharedPreferences();
        return SPUtils.getString(sharedPreferences, "secretKey", null);
    }
}