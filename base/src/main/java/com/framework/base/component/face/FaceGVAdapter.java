package com.framework.base.component.face;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.framework.base.R;
import com.framework.base.component.face.core.Emoji;

import java.util.List;

public class FaceGVAdapter extends BaseAdapter {
    private List<Emoji> list;
    private Context mContext;

    public FaceGVAdapter(List<Emoji> list, Context mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Emoji emoji = list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_face, null);
            holder.iv = convertView.findViewById(R.id.face_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (emoji != null) {
            holder.iv.setImageBitmap(emoji.getIcon());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
    }
}
