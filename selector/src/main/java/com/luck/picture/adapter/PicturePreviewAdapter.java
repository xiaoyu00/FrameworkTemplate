package com.luck.picture.adapter;

import android.util.LruCache;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.R;
import com.luck.picture.adapter.holder.BasePreviewHolder;
import com.luck.picture.adapter.holder.PreviewVideoHolder;
import com.luck.picture.config.InjectResourceSource;
import com.luck.picture.config.PictureMimeType;
import com.luck.picture.entity.LocalMedia;

import java.util.List;

/**
 * @author：luck
 * @date：2021/11/23 1:11 下午
 * @describe：PicturePreviewAdapter2
 */
public class PicturePreviewAdapter extends RecyclerView.Adapter<BasePreviewHolder> {

    private final List<LocalMedia> mData;
    private final BasePreviewHolder.OnPreviewEventListener onPreviewEventListener;
    private final LruCache<Integer, BasePreviewHolder> mHolderLruCache = new LruCache<>(6);

    public BasePreviewHolder getCurrentHolder(int position) {
        return mHolderLruCache.get(position);
    }

    public PicturePreviewAdapter(List<LocalMedia> list, BasePreviewHolder.OnPreviewEventListener listener) {
        this.mData = list;
        this.onPreviewEventListener = listener;
    }

    @NonNull
    @Override
    public BasePreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResourceId = 0;
        if (viewType == BasePreviewHolder.ADAPTER_TYPE_VIDEO) {
            layoutResourceId = InjectResourceSource.getLayoutResource(parent.getContext(), InjectResourceSource.PREVIEW_ITEM_VIDEO_LAYOUT_RESOURCE);
            return BasePreviewHolder.generate(parent, viewType, layoutResourceId != 0 ? layoutResourceId : R.layout.ps_preview_video);
        } else {
            layoutResourceId = InjectResourceSource.getLayoutResource(parent.getContext(), InjectResourceSource.PREVIEW_ITEM_IMAGE_LAYOUT_RESOURCE);
            return BasePreviewHolder.generate(parent, viewType, layoutResourceId != 0 ? layoutResourceId : R.layout.ps_preview_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BasePreviewHolder holder, int position) {
        holder.setOnPreviewEventListener(onPreviewEventListener);
        LocalMedia media = mData.get(position);
        mHolderLruCache.put(position, holder);
        holder.bindData(media, position);
    }


    @Override
    public int getItemViewType(int position) {
        if (PictureMimeType.isHasVideo(mData.get(position).getMimeType())) {
            return BasePreviewHolder.ADAPTER_TYPE_VIDEO;
        } else {
            return BasePreviewHolder.ADAPTER_TYPE_IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BasePreviewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof PreviewVideoHolder) {
            PreviewVideoHolder videoHolder = (PreviewVideoHolder) holder;
            videoHolder.releaseVideo();
        }
    }

    /**
     * 释放当前视频相关
     */
    public void destroyVideo(int position) {
        BasePreviewHolder holder = mHolderLruCache.get(position);
        if (holder instanceof PreviewVideoHolder) {
            PreviewVideoHolder videoHolder = (PreviewVideoHolder) holder;
            videoHolder.releaseVideo();
        }
    }
}
