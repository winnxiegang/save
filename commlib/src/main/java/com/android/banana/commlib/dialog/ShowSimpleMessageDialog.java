package com.android.banana.commlib.dialog;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.R;

/**
 * Created by 周毅 on 2015/6/5 11:27.
 */
public class ShowSimpleMessageDialog extends DialogBase {

    public ShowSimpleMessageDialog(Context context, String message) {
        super(context, R.layout.dialog_normal_message);
        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);
        messageTv.setText(message);
        TextView okTv = (TextView) window.findViewById(R.id.ok);
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public ShowSimpleMessageDialog(Context context, String message, final OnMyClickListener clickListener, int textGravity) {
        super(context, R.layout.dialog_normal_message);
        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);
        messageTv.setGravity(textGravity);
        messageTv.setText(message);
        TextView okTv = (TextView) window.findViewById(R.id.ok);
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v);
                dialog.dismiss();
            }
        });
    }

    public ShowSimpleMessageDialog(Context context, String message, final OnMyClickListener clickListener) {
        super(context, R.layout.dialog_normal_message);
        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);
        messageTv.setText(message);
        TextView okTv = (TextView) window.findViewById(R.id.ok);
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // 这个错误原因是因此一些对话框在activity转向之前，并没有销毁，导致其引用的context为空，因此引发内存泄漏。
                dialog.dismiss();
                clickListener.onClick(v);
            }
        });
    }

    public ShowSimpleMessageDialog(Context context, SpannableStringBuilder message, int textGravity) {
        super(context, R.layout.dialog_normal_message);
        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);
        messageTv.setGravity(textGravity);
        messageTv.setText(message);
        TextView okTv = (TextView) window.findViewById(R.id.ok);
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    public ShowSimpleMessageDialog(Context context, final OnMyClickListener clickListener, int textGravity) {
        super(context, R.layout.dialog_forbid_message);
        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);
        messageTv.setGravity(textGravity);
        TextView okTv = (TextView) window.findViewById(R.id.ok);
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v);
                dialog.dismiss();
            }
        });
    }

    public ShowSimpleMessageDialog(Context context, int resId, CharSequence positiveMessage, final OnMyClickListener ok, CharSequence message) {
        super(context, R.layout.simple_dialog_layout_whith_image, R.style.MyDialog);
        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);
        messageTv.setText(message);
        TextView okBtn = (TextView) window.findViewById(R.id.okBtn);
        ImageView headIv = (ImageView) window.findViewById(R.id.headIv);
        if (resId != 0) {
            headIv.setBackgroundResource(resId);
        }
        if (positiveMessage != null) {
            okBtn.setText(positiveMessage);
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ok!=null)
                    ok.onClick(v);
                dialog.dismiss();

            }
        });
    }

}
