package com.etiennelawlor.imagegallery.library.fullscreen;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.entity.Photo;
import com.etiennelawlor.imagegallery.library.util.ImageGalleryUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


/**
 * Created by zhouyi on 2015/12/22.
 */
public class MyPhotoAdapter extends BaseAdapter {

    private Context context;

    private List<Photo> list = null;

    private int width;

    private int height;

    private View.OnClickListener photoDeleteClickListener = null;

    public MyPhotoAdapter(Context context, List<Photo> list,View.OnClickListener photoDeleteClickListener) {
        this.context = context;

        this.list = list;

        this.photoDeleteClickListener = photoDeleteClickListener;

        measure();
    }

    @Override
    public int getCount() {
        if (list.size() == 0) {
            return 0;
        }
        if (list.size() == 5) {
            return list.size();
        }
        return list.size() + 1;
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

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_photo, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mainLayout.setLayoutParams(new AbsListView.LayoutParams(width, height));

        setItemView(holder, position);

        return convertView;
    }

    private void setItemView(ViewHolder holder, final int position) {
        if (position == list.size() && list.size() != 5) {
            holder.addPhotoIv.setVisibility(View.VISIBLE);
            holder.photoIv.setVisibility(View.GONE);
            holder.deleteIv.setVisibility(View.GONE);
        } else {
            holder.addPhotoIv.setVisibility(View.GONE);
            holder.photoIv.setVisibility(View.VISIBLE);
            holder.deleteIv.setVisibility(View.VISIBLE);
            setUpImage(holder.photoIv, list.get(position));
            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setTag(position);
                    photoDeleteClickListener.onClick(v);
                }
            });
        }
    }

    private void setUpImage(ImageView iv, Photo imageUrl) {
        if (!TextUtils.isEmpty(imageUrl.getPath())) {
            Picasso.with(context.getApplicationContext())
                    .load(new File(imageUrl.getPath()))
                    .resize(width, height)
                    .centerCrop()
                    .into(iv);
        } else {
            iv.setImageDrawable(null);
        }
    }

    private void measure() {

        width = (int) (ImageGalleryUtils.getScreenWidth(context) - 2 * context.getResources().getDimension(R.dimen.dp10)) / 4;

        height = width;

    }


    static class ViewHolder {
        ImageView deleteIv;
        ImageView photoIv;
        ImageView addPhotoIv;
        RelativeLayout mainLayout;


        ViewHolder(View view) {
            deleteIv = (ImageView) view.findViewById(R.id.deleteIv);
            photoIv = (ImageView) view.findViewById(R.id.photoIv);
            addPhotoIv = (ImageView) view.findViewById(R.id.addPhotoIv);
            mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
        }

    }
}
