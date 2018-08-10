package com.lyz.helper;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by lyz on 2018/8/8.
 *
 */

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.AppInfoViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final ItemDeleteListener mItemDeleteListener;
    private Context mContext;
    private List<AppInfoBean> mData;

    public void setData(List<AppInfoBean> data) {
        this.mData = data;
    }

    public AppInfoAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItemDeleteListener = new ItemDeleteListener();
    }

    @NonNull
    @Override
    public AppInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AppInfoViewHolder(mLayoutInflater.inflate(R.layout.item_app_info , viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppInfoViewHolder holder, int position) {
        AppInfoBean bean = mData.get(position);
        if (null == bean.getIcon()) {
            holder.ivIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher_round));
        } else {
            holder.ivIcon.setImageDrawable(bean.getIcon());
        }
        holder.tvName.setText(bean.getName());
        holder.tvVersion.setText(bean.getVersionName());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mItemDeleteListener);
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }

    private class ItemDeleteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            final AppInfoBean appInfoBean = mData.get(position);
//            SfToastDialog.makeDialog(mContext, null, "确认卸载改App吗?", new SfToastDialog.OnToastDialogListener() {
//                @Override
//                public void onClick(SfToastDialog dialog, int btn) {
//                    if (btn == right) {
//                        CellInfoUtil.uninstallApk(mContext, appInfoBean.getPkgName());
//                    }
//                    dialog.dismiss();
//                }
//            }).show();


            TimerTaskDialog.Builder builder = new TimerTaskDialog.Builder();
            builder.setContent("确认卸载改App吗?").setLeftContent("取消").setRightContent("确定");
            TimerTaskDialog.makeDialog(mContext, builder, new TimerTaskDialog.OnDialogClickListener() {
                @Override
                public void onClick(TimerTaskDialog dialog, int btn) {
                    if (btn == left) {
                        Toast.makeText(mContext, "left", Toast.LENGTH_SHORT).show();
                    } else {
                        CellInfoUtil.uninstallApk(mContext, appInfoBean.getPkgName());
                        Toast.makeText(mContext, "right", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onTimeOver() {
                    Toast.makeText(mContext, "onTimeOver", Toast.LENGTH_SHORT).show();
                }
            }, 3,  true).show();
        }
    }
    class AppInfoViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivIcon;
        private final TextView tvName;
        private final TextView tvVersion;

        public AppInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvVersion = itemView.findViewById(R.id.tv_version);
        }
    }
}
