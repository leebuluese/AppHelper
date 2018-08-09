package com.lyz.helper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lyz on 2018/8/8.
 *
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rvInfo;
    private AppInfoAdapter infoAdapter;
    private ProgressBar pbProgress;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        // 安装
        if (messageEvent.getEventType() == MessageEvent.EventType.add) {
            // TODO 根据报名进行处理
            refreshData();
        }
        // 卸载
        else if (messageEvent.getEventType() == MessageEvent.EventType.remove) {
            refreshData();
        }
        // 替换
        else if (messageEvent.getEventType() == MessageEvent.EventType.replace) {
            refreshData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        refreshData();

        EventBus.getDefault().register(this);
    }

    private void initView() {
        pbProgress = findViewById(R.id.pb_progress);
        rvInfo = findViewById(R.id.rv_info);
        rvInfo.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initAdapter(List<AppInfoBean> list) {

        if (null == infoAdapter) {
            infoAdapter = new AppInfoAdapter(this);
            infoAdapter.setData(list);
            rvInfo.setAdapter(infoAdapter);
        } else {
            infoAdapter.setData(list);
            infoAdapter.notifyDataSetChanged();
        }
    }

    public void refreshData() {
        Observable.just("")
                .map(new Function<String, List<AppInfoBean>>() {

                    @Override
                    public List<AppInfoBean> apply(String s) throws Exception {
                        List<AppInfoBean> allApk = CellInfoUtil.getSdApk(getApplicationContext());
                        if (allApk.isEmpty()) {
                            throw new DataEmptyException("list is empty");
                        }
                        sortData(allApk);
                        return allApk;
                    }
                })
//                .subscribeOn(Schedulers.io()) // net
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AppInfoBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        pbProgress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(List<AppInfoBean> list) {
                        initAdapter(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO loading empty layout 空页面就不做啦，弹出个提示吧
                        Toast.makeText(MainActivity.this, "好尴尬，没数据", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        pbProgress.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 按照时间排序
     */
    private void sortData(List<AppInfoBean> mList) {
        Collections.sort(mList, new Comparator<AppInfoBean>() {
            /**
             * 比较时间排序
             * @param l
             * @param r
             * @return an integer < 0 if l is less than r, 0 if they are
             *         equal, and > 0 if l is greater than r
             */
            @Override
            public int compare(AppInfoBean l, AppInfoBean r) {
                long date1 = l.getUpdateTime();
                long date2 = r.getUpdateTime();
                // 对时间字段进行降序
                if (date1 < date2) {
                    return 1;
                }
                return -1;
            }
        });

    }

}
