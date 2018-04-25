package com.etiennelawlor.imagegallery.library.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.entity.Photo;
import com.etiennelawlor.imagegallery.library.util.ImageGalleryUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by etiennelawlor on 8/20/15.
 */
public class ImageGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Photo> mImages;

    private int mGridItemWidth;

    private int mGridItemHeight;

    private OnImageClickListener mOnImageClickListener;

    private View.OnClickListener mTakePhotoClickListener;

    private View.OnClickListener mSelectListener;

    private int mSelectCount;

    private int maxInsert;

    private Context context;

    private int selectType;

    private static final Uri STORAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    public ImageGalleryAdapter(ArrayList<Photo> images) {
        mImages = images;
    }

    public ImageGalleryAdapter(ArrayList<Photo> images, int maxInsert, Context context) {
        mImages = images;
        this.maxInsert = maxInsert;
        this.context = context;
    }

    public void setSelectType(int selectType) {
        this.selectType = selectType;
    }

    public void setmSelectCount(int mSelectCount) {
        this.mSelectCount = mSelectCount;
    }

    public void clearSelectImage() {
        mSelectCount = 0;
    }

    public void setSelectListener(View.OnClickListener selectListener) {
        this.mSelectListener = selectListener;
    }

    public void setTakePhotoClickListener(View.OnClickListener takePhotoClickListener) {
        this.mTakePhotoClickListener = takePhotoClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_thumbnail, viewGroup, false);

        v.setLayoutParams(getGridItemLayoutParams(v));

        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final ImageViewHolder holder = (ImageViewHolder) viewHolder;

        if (position == 0) {
            setTakePicture(holder);
        } else {
            setUpImage(holder, position - 1);
        }

    }

    private void setTakePicture(final ImageViewHolder holder) {

        holder.mImageView.setImageResource(R.drawable.icon_take_photo);
        holder.selectIv.setVisibility(View.GONE);
        holder.mFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTakePhotoClickListener != null) {
                    mTakePhotoClickListener.onClick(v);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mImages != null) {
            Log.e("当前张数", String.valueOf(mImages.size() + 1));
            return mImages.size() + 1;
        } else {
            return 0;
        }
    }

    // region Helper Methods
    public void setOnImageClickListener(OnImageClickListener listener) {
        this.mOnImageClickListener = listener;
    }

    private ViewGroup.LayoutParams getGridItemLayoutParams(View view) {

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        int screenWidth = ImageGalleryUtils.getScreenWidth(view.getContext());
        int numOfColumns;
        if (ImageGalleryUtils.isInLandscapeMode(view.getContext())) {
            numOfColumns = 4;
        } else {
            numOfColumns = 3;
        }

        mGridItemWidth = screenWidth / numOfColumns;

        mGridItemHeight = screenWidth / numOfColumns;

        layoutParams.width = mGridItemWidth;

        layoutParams.height = mGridItemHeight;

        return layoutParams;
    }

    private void setUpImage(final ImageViewHolder holder, final int position) {

        if (selectType == 2) {

            holder.selectIv.setVisibility(View.GONE);

        } else {

            holder.selectIv.setVisibility(View.VISIBLE);

        }

        final Photo image = mImages.get(position);

        ImageView imageView = holder.mImageView;

        if (image.isSelected()) {
            holder.selectIv.setImageResource(R.drawable.icon_insert_photo_selected);
        } else {
            holder.selectIv.setImageResource(R.drawable.icon_insert_photo_normal);
        }

        holder.mFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(holder, position, v);
            }
        });

        holder.selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(holder, position, v);
            }
        });

        if (!TextUtils.isEmpty(image.getPath())) {
            Picasso.with(imageView.getContext().getApplicationContext())
                    .load(contentUri(image.getId()))
                    .resize(mGridItemWidth, mGridItemHeight)
                    .centerCrop()
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.full_preview_image_empty);
        }
    }

    private void setSelected(final ImageViewHolder holder, int position, View v) {
        final Photo image = mImages.get(position);
        if (!image.isSelected() && mSelectCount == maxInsert) {
            if (0 == maxInsert) {
                Toast.makeText(holder.selectIv.getContext(), "你已经选满5张图片", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(holder.selectIv.getContext(), "最多只能选择" + maxInsert + "张", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (image.isSelected()) {
            holder.selectIv.setImageResource(R.drawable.icon_insert_photo_normal);
            mImages.get(position).setSelected(false);
            mSelectCount--;
        } else {
            holder.selectIv.setImageResource(R.drawable.icon_insert_photo_selected);
            mImages.get(position).setSelected(true);
            mSelectCount++;
        }
        if (mSelectListener != null) {
            v.setTag(position);
            mSelectListener.onClick(v);
        }

        if (mOnImageClickListener!=null)
            mOnImageClickListener.onImageClick(position);
    }

    public Uri contentUri(long id) {
        try {
            // does our uri already have an id (single image query)?
            // if so just return it
            long existingId = ContentUris.parseId(STORAGE_URI);
            return STORAGE_URI;
        } catch (NumberFormatException ex) {
            // otherwise tack on the id
            return ContentUris.withAppendedId(STORAGE_URI, id);
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageView;

        private final FrameLayout mFrameLayout;

        private final ImageView selectIv;

        public ImageViewHolder(final View view) {

            super(view);

            mImageView = (ImageView) view.findViewById(R.id.iv);

            mFrameLayout = (FrameLayout) view.findViewById(R.id.fl);

            selectIv = (ImageView) view.findViewById(R.id.selectIv);

        }

    }


}
