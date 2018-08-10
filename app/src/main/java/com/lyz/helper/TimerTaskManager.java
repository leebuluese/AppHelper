package com.lyz.helper;


import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by lyz on 2018/8/9.
 * 时间控制器
 */

public class TimerTaskManager {

    private TimerTaskListener timerTaskListener;
    private Object object;

    public interface TimerTaskListener{
        void onTimeOver(Object object);
        void onRefresh(int time);
    }

    private int mTime = 3;

    private Runnable mRunnable;

    private Runnable mRunnableLoop;

    private Handler mHandler = new TimerTaskHandler(this);

    private static class TimerTaskHandler extends Handler {

        private WeakReference<TimerTaskManager> reference;

        TimerTaskHandler(TimerTaskManager taskManager){
            reference = new WeakReference<>(taskManager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) return;
            TimerTaskManager taskManager = reference.get();
            if (null != taskManager.timerTaskListener) {
                if (msg.what == 1) {
                    taskManager.timerTaskListener.onRefresh(taskManager.mTime);
                } else {
                    taskManager.timerTaskListener.onTimeOver(taskManager.object);
                }
            }
        }
    }

    public TimerTaskManager setTime(int Time) {
        this.mTime = Time;
        return this;
    }

    public TimerTaskManager setObject(Object object) {
        this.object = object;
        if (mTime <= 1) {
            mTime = 1;
        }
        if (mTime >= 90) {
            mTime = 90;
        }
        return this;
    }

    public TimerTaskManager setListener(TimerTaskListener listener) {
        this.timerTaskListener = listener;
        return this;
    }

    public void start() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 0;
                mHandler.handleMessage(message);
            }
        };
        mHandler.postDelayed(mRunnable, mTime * 1000);
    }

    public void startLoop() {
        mRunnableLoop = new Runnable() {
            @Override
            public void run() {
                mTime--;
                Message message = Message.obtain();
                if (mTime >= 1) {
                    message.what = 1;
                    mHandler.handleMessage(message);
                    mHandler.postDelayed(this, 1000);
                } else {
                    message.what = 0;
                    mHandler.handleMessage(message);
                }
            }
        };
        mHandler.postDelayed(mRunnableLoop, 1000);
    }

    public void destroy() {
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mRunnable = null;
            mRunnableLoop = null;
        }
    }
}
