package com.lyz.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Created by lyz on 2018/8/8.
 *
 */

public class SfToastDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvCancel;
    private TextView tvSure;

    private String mTitle;
    private String mContent;
    private String leftBtnStr, rightBtnStr;

    private OnToastDialogListener mListener;

    public SfToastDialog(@NonNull Context context) {
        super(context);
    }

    public SfToastDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SfToastDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog_toast);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_dialog_title);
        tvContent = findViewById(R.id.tv_dialog_content);
        tvCancel = findViewById(R.id.tv_dialog_cancel);
        tvSure = findViewById(R.id.tv_dialog_sure);
    }

    private void initData() {
        if (TextUtils.isEmpty(mTitle)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(mTitle);
        }
        if (TextUtils.isEmpty(mContent)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(mContent);
        }
        if (!TextUtils.isEmpty(leftBtnStr)) {
            tvCancel.setText(leftBtnStr);
        }
        if (!TextUtils.isEmpty(rightBtnStr)) {
            tvSure.setText(rightBtnStr);
        }
    }

    private void initListener() {
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
    }

    public static SfToastDialog makeDialog(Context context, String title, String content, OnToastDialogListener listener) {
        return makeDialog(context, title, content, "取消", "确定", listener);
    }

    public static SfToastDialog makeDialog(Context context, String title, String content, String leftBtnStr, String rightBtnStr, OnToastDialogListener listener) {
        SfToastDialog dialog = new SfToastDialog(context, R.style.choose_dialog);
        dialog.mContext = context;
        dialog.mTitle = title;
        dialog.mContent = content;
        dialog.leftBtnStr = leftBtnStr;
        dialog.rightBtnStr = rightBtnStr;
        dialog.mListener = listener;
        return dialog;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_dialog_cancel) {
            if (null != mListener) {
                mListener.onClick(SfToastDialog.this, OnToastDialogListener.left);
            }

        } else if (i == R.id.tv_dialog_sure) {
            if (null != mListener) {
                mListener.onClick(SfToastDialog.this, OnToastDialogListener.right);
            }

        }
    }

    public interface OnToastDialogListener {
        int left = 0;
        int right = 1;

        /**
         * click点击
         *
         * @param dialog
         * @param btn
         */
        void onClick(SfToastDialog dialog, int btn);
    }
}
