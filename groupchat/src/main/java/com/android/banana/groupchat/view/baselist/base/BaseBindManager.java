package com.android.banana.groupchat.view.baselist.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.utils.picasso.PicUtils;
import java.lang.ref.SoftReference;

/**
 * Created by kokuma on 2017/3/24.
 * 执行顺序  prepare、bindText...
 */
public class BaseBindManager {

    private static BaseBindManager instance;
    private Context context;
    private BaseViewHolder holder;
    private SoftReference<Activity> softActivity;

    private View root;

    private BaseBindManager(Context context) {
        this.context = context;
    }

    public static void init(Context ctx) {
        if (instance == null)
            instance = new BaseBindManager(ctx);
    }

    public static BaseBindManager getInstance() {
        if (instance == null)
            throw new IllegalArgumentException("BaseBindManager没被初始化");
        return instance;
    }

    public BaseBindManager prepare(BaseViewHolder viewHolder) {
        this.holder = viewHolder;
        if(this.softActivity!=null){
            this.softActivity.clear();
        }
        this.softActivity = null;
        this.root = null;
        return this;
    }

    public BaseBindManager prepare(SoftReference<Activity> viewHolder) {
        this.holder = null;
        if(this.softActivity!=null){
            this.softActivity.clear();
        }
        this.softActivity = viewHolder;
        this.root = null;
        return this;
    }

    public BaseBindManager prepare(View root) {
        this.holder = null;
        if(this.softActivity!=null){
            this.softActivity.clear();
        }
        this.softActivity = null;
        this.root = root;
        return this;
    }

    public BaseBindManager prepare(Object viewOrViewHolder) {
        if (viewOrViewHolder instanceof BaseViewHolder) {
            prepare((BaseViewHolder) viewOrViewHolder);
        } else if (viewOrViewHolder instanceof SoftReference) {
            prepare((SoftReference<Activity>) viewOrViewHolder);
        } else if (viewOrViewHolder instanceof View) {
            prepare((View) viewOrViewHolder);
        }
        return this;
    }

    public BaseBindManager bindText(int id, CharSequence txt) {
        setTextView(id, txt);
        return this;
    }

    public BaseBindManager bindImg(int id, int imgId) {
        setImageViewSouce(id, imgId);
        return this;
    }

    public BaseBindManager bindImg(int id, String url ,int defaultImg) {
        setImageViewUrl(id, url,defaultImg);
        return this;
    }



    //这个类生命周期很长，释放掉防止内存溢出
    public BaseBindManager release() {
        this.holder = null;
        if(this.softActivity!=null){
            this.softActivity.clear();
        }
        this.softActivity = null;
        this.root = null;
        return this;
    }


    private void setTextView(int id, CharSequence txt) {
        TextView tv = null;
        if (holder != null) {
            tv = holder.getView(id);
        } else if (softActivity != null&&softActivity.get()!=null) {
            tv = (TextView) softActivity.get().findViewById(id);
        } else if (root != null) {
            tv = (TextView) root.findViewById(id);
        }
        if (tv != null) {
            tv.setText(txt);
        }
    }

    private void setImageViewSouce(int id, int res) {
        ImageView iv = null;
        if (holder != null) {
            iv = holder.getView(id);
        } else if (softActivity != null&&softActivity.get()!=null) {
            iv = (ImageView) softActivity.get().findViewById(id);
        } else if (root != null) {
            iv = (ImageView) root.findViewById(id);
        }
        if (iv != null) {
            if (res != -1) {
                iv.setImageResource(res);
            } else {
                iv.setImageBitmap(null);
            }
        }
    }

    private void setImageViewUrl(int id, String url ,int defaultImg) {
        ImageView iv = null;
        if (holder != null) {
            iv = holder.getView(id);
        } else if (softActivity != null&&softActivity.get()!=null) {
            iv = (ImageView) softActivity.get().findViewById(id);
        } else if (root != null) {
            iv = (ImageView) root.findViewById(id);
        }
        if (iv != null) {
            PicUtils.load( context, iv,url ,defaultImg);
        }
    }
}
