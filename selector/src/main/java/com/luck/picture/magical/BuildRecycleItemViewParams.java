package com.luck.picture.magical;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：luck
 * @date：2021/12/17 1:19 下午
 * @describe：BuildRecycleItemViewParams
 */
public class BuildRecycleItemViewParams {

    private static final List<ViewParams> viewParams = new ArrayList<>();

    public static void clear() {
        if (viewParams.size() > 0) {
            viewParams.clear();
        }
    }

    public static ViewParams getItemViewParams(int position) {
        return viewParams.size() > position ? viewParams.get(position) : null;
    }

    public static void generateViewParams(RecyclerView recyclerView, int statusBarHeight) {
        List<View> views = new ArrayList<>();
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = recyclerView.getChildAt(i);
            if (view == null) {
                continue;
            }
            views.add(view);
        }
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        int firstPos;
        int lastPos;
        int totalCount = layoutManager.getItemCount();
        firstPos = layoutManager.findFirstVisibleItemPosition();
        lastPos = layoutManager.findLastVisibleItemPosition();
        lastPos = lastPos > totalCount ? totalCount - 1 : lastPos;
        fillPlaceHolder(views, totalCount, firstPos, lastPos);
        viewParams.clear();
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            ViewParams viewParam = new ViewParams();
            if (view == null) {
                viewParam.setLeft(0);
                viewParam.setTop(0);
                viewParam.setWidth(0);
                viewParam.setHeight(0);
            } else {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                viewParam.setLeft(location[0]);
                viewParam.setTop(location[1] - statusBarHeight);
                viewParam.setWidth(view.getWidth());
                viewParam.setHeight(view.getHeight());
            }
            viewParams.add(viewParam);
        }
    }


    private static void fillPlaceHolder(List<View> originImageList, int totalCount, int firstPos, int lastPos) {
        if (firstPos > 0) {
            for (int i = firstPos; i >= 1; i--) {
                originImageList.add(0, null);
            }
        }

        if (lastPos < totalCount) {
            for (int i = totalCount - 1 - lastPos; i >= 1; i--) {
                originImageList.add(null);
            }
        }
    }
}
