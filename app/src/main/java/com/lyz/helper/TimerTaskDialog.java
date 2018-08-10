package com.lyz.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

/**
 * Created by lyz on 2018/8/9.
 * 时间控制弹窗
 * <p>
 * color : 0xffabcdef
 * size : 14f
 */

public class TimerTaskDialog extends Dialog implements View.OnClickListener, TimerTaskManager.TimerTaskListener {

    private static TimerTaskManager timerTaskManager;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvLeft;
    private TextView tvRight;
    private Builder mBuilder;

    private boolean isShow;
    private int mTime;

    private OnDialogClickListener mListener;

    public interface OnDialogClickListener {
        int left = 0;
        int right = 1;

        /**
         * click点击
         *
         * @param dialog
         * @param btn
         */
        void onClick(TimerTaskDialog dialog, int btn);
        void onTimeOver();
    }

    public TimerTaskDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_timer_task);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvLeft = findViewById(R.id.tv_left);
        tvRight = findViewById(R.id.tv_right);
    }

    private void initData() {
        if (null == mBuilder) return;

        if (null != mBuilder.getTitle()) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mBuilder.getTitle());
            if (mBuilder.getTitleColor() != 0) {
                tvTitle.setTextColor(mBuilder.getTitleColor());
            }
            if (mBuilder.getTitleSize() != 0) {
                tvTitle.setTextSize(mBuilder.getTitleSize());
            }
        } else {
            tvTitle.setVisibility(View.INVISIBLE);
        }


        if (null != mBuilder.getContent()) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(mBuilder.getContent());
            if (mBuilder.getContentColor() != 0) {
                tvContent.setTextColor(mBuilder.getContentColor());
            }
            if (mBuilder.getContentSize() != 0) {
                tvContent.setTextSize(mBuilder.getContentSize());
            }
        } else {
            tvContent.setVisibility(View.INVISIBLE);
        }

        if (null != mBuilder.getLeftContent()) {
            tvLeft.setVisibility(View.VISIBLE);
            if (isShow && mTime > 0) {
                String s = mBuilder.getLeftContent() + "(" + mTime + "S)";
                tvLeft.setText(s);
            } else {
                tvLeft.setText(mBuilder.getLeftContent());
            }
            if (mBuilder.getLeftColor() != 0) {
                tvLeft.setTextColor(mBuilder.getLeftColor());
            }
            if (mBuilder.getLeftSize() != 0) {
                tvLeft.setTextSize(mBuilder.getLeftSize());
            }
        } else {
            tvLeft.setVisibility(View.GONE);
        }

        if (null != mBuilder.getRightContent()) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(mBuilder.getRightContent());
            if (mBuilder.getRightColor() != 0) {
                tvRight.setTextColor(mBuilder.getRightColor());
            }
            if (mBuilder.getRightSize() != 0) {
                tvRight.setTextSize(mBuilder.getRightSize());
            }
        } else {
            tvRight.setVisibility(View.GONE);
        }

    }

    private void initListener() {
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    public TimerTaskDialog setBuilder(Builder builder) {
        this.mBuilder = builder;
        return this;
    }

    public static TimerTaskDialog makeDialog(Context context, Builder builder,
                                             @NonNull OnDialogClickListener listener, int time, boolean show) {
        TimerTaskDialog dialog = new TimerTaskDialog(context, R.style.choose_dialog);
        dialog.mBuilder = builder;
        dialog.mListener = listener;
        dialog.isShow = show;
        dialog.mTime = time;
        if (time > 0) {
            timerTaskManager = new TimerTaskManager();
            timerTaskManager.setTime(time).setObject(dialog);
        }
        return dialog;
    }

    @Override
    public void onTimeOver(Object object) {
        this.dismiss();
        if (null != mListener) {
            mListener.onTimeOver();
        }
    }

    @Override
    public void onRefresh(int time) {
        if (tvLeft.getVisibility() == View.GONE) return;
        if (null != mBuilder && null != mBuilder.getLeftContent()) {
            String s = mBuilder.getLeftContent() + "(" + time + "S)";
            tvLeft.setText(s);
        } else {
            String s = tvLeft.getText().toString() + "(" + time + "S)";
            tvLeft.setText(s);
        }
    }

    @Override
    public void show() {
        super.show();
        if (null != timerTaskManager) {
            if (isShow) {
                timerTaskManager.setListener(this).startLoop();
            } else {
                timerTaskManager.setListener(this).start();
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (null != timerTaskManager) {
            timerTaskManager.destroy();
            timerTaskManager = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                if (null != mListener) {
                    mListener.onClick(TimerTaskDialog.this, OnDialogClickListener.left);
                }
                break;
            case R.id.tv_right:
                if (null != mListener) {
                    mListener.onClick(TimerTaskDialog.this, OnDialogClickListener.right);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 属性设置
     */
    public static class Builder {

        private String title;
        private float titleSize;
        private int titleColor;

        private String content;
        private float contentSize;
        private int contentColor;

        private String leftContent;
        private float LeftSize;
        private int leftColor;

        private String rightContent;
        private float rightSize;
        private int rightColor;


        public String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public float getTitleSize() {
            return titleSize;
        }

        public Builder setTitleSize(float titleSize) {
            this.titleSize = titleSize;
            return this;
        }

        public int getTitleColor() {
            return titleColor;
        }

        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public String getContent() {
            return content;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public float getContentSize() {
            return contentSize;
        }

        public Builder setContentSize(float contentSize) {
            this.contentSize = contentSize;
            return this;
        }

        public int getContentColor() {
            return contentColor;
        }

        public Builder setContentColor(int contentColor) {
            this.contentColor = contentColor;
            return this;
        }

        public String getLeftContent() {
            return leftContent;
        }

        public Builder setLeftContent(String leftContent) {
            this.leftContent = leftContent;
            return this;
        }

        public float getLeftSize() {
            return LeftSize;
        }

        public Builder setLeftSize(float leftSize) {
            LeftSize = leftSize;
            return this;
        }

        public int getLeftColor() {
            return leftColor;
        }

        public Builder setLeftColor(int leftColor) {
            this.leftColor = leftColor;
            return this;
        }

        public String getRightContent() {
            return rightContent;
        }

        public Builder setRightContent(String rightContent) {
            this.rightContent = rightContent;
            return this;
        }

        public float getRightSize() {
            return rightSize;
        }

        public Builder setRightSize(float rightSize) {
            this.rightSize = rightSize;
            return this;
        }

        public int getRightColor() {
            return rightColor;
        }

        public Builder setRightColor(int rightColor) {
            this.rightColor = rightColor;
            return this;
        }
    }
}
