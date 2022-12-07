package com.dzw.packetscan.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dzw.packetscan.R;
import com.xuexiang.xui.adapter.listview.BaseListAdapter;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.InputCallback;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;
import com.xuexiang.xutil.system.ClipboardUtils;

import java.util.Collection;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;

public class ResultDataAdapter extends BaseListAdapter<Dict, ResultDataAdapter.ViewHolder> {

    public static final String TITLE_KEY = "title";
    public static final String VALUE_KEY = "value";
    private View view;

    public ResultDataAdapter(Context context, Collection<Dict> data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.titleTv = convertView.findViewById(R.id.title);
        holder.valueTv = convertView.findViewById(R.id.value);
        holder.copyIv = convertView.findViewById(R.id.copy);
        holder.editIv = convertView.findViewById(R.id.edit);
        view = convertView;
        return holder;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_result_data;
    }

    @Override
    protected void convert(ViewHolder holder, Dict item, int position) {
        String title = item.getStr(TITLE_KEY);
        String value = item.getStr(VALUE_KEY);
        holder.titleTv.setText(title);
        holder.valueTv.setText(value);

        holder.copyIv.setOnClickListener(v -> {
            if(StrUtil.isNotBlank(value)){
                ClipboardUtils.copyText(value);
                SnackbarUtils.Short(view, "已经复制进剪切板").confirm().show();
            }else{
                SnackbarUtils.Short(view, "没什么可复制的").warning().show();
            }
        });

        holder.editIv.setOnClickListener(v -> {
            InputInfo inputInfo = new InputInfo(InputType.TYPE_CLASS_TEXT, "", value, false);
            DialogLoader.getInstance().showInputDialog(
                    getContext(),
                    R.drawable.ic_edit,
                    "编辑",
                    "编辑内容",
                    inputInfo,
                    (InputCallback) (dialog, input) -> {

                    },
                    "确认",
                    (DialogInterface.OnClickListener) (dialog, i) -> {
                        KeyboardUtils.hideSoftInput(dialog);
                        dialog.dismiss();
                        if (dialog instanceof MaterialDialog) {
                            String newVal = ((MaterialDialog) dialog).getInputEditText().getText().toString();
                            holder.valueTv.setText(newVal);
                            item.set(VALUE_KEY, newVal);
                        }
                    },
                    "取消",
                    (DialogInterface.OnClickListener) (dialog, i) -> {
                        KeyboardUtils.hideSoftInput(dialog);
                        dialog.dismiss();
                    }
            );
        });
    }


    public static class ViewHolder {
        /**
         * 标题
         */
        public TextView titleTv;
        /**
         * 值
         */
        public TextView valueTv;
        /**
         * 复制
         */
        public ImageView copyIv;
        /**
         * 编辑
         */
        public ImageView editIv;
    }

}
