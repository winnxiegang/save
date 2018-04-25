package com.etiennelawlor.imagegallery.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.entity.PhotoDirectory;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouyi on 2015/12/21.
 */
public class PopupWindowAdapter extends BaseAdapter {

    private Context context;

    private List<PhotoDirectory> list = new ArrayList<>();


    public PopupWindowAdapter(Context context, List<PhotoDirectory> list) {

        this.context = context;

        this.list = list;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_popupwindow, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        setItemView(viewHolder, position);

        return convertView;
    }

    private void setItemView(ViewHolder viewHolder, int position) {

        PhotoDirectory bean = list.get(position);

        viewHolder.directoryTv.setText(bean.getName() + "(" + bean.getPhotos().size() + ")");

        if(bean.getCoverPath()==null){
            return;
        }

        Picasso.with(context)
                .load(new File(bean.getCoverPath()))
                .resize(90, 90)
                .centerCrop()
                .into(viewHolder.firstPhotoIv);

        if (bean.isSelected()) {
            viewHolder.selectIv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.selectIv.setVisibility(View.GONE);
        }

    }


    public static class ViewHolder {

        ImageView firstPhotoIv;

        TextView directoryTv;

        ImageView selectIv;

        ViewHolder(View view) {

            firstPhotoIv = (ImageView) view.findViewById(R.id.firstPhotoIv);

            directoryTv = (TextView) view.findViewById(R.id.directoryTv);

            selectIv = (ImageView) view.findViewById(R.id.selectIv);

        }
    }
}
