package com.android.banana.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.dialog.DialogBase;


/**
 * Created by zaozao on 2017/6/7.
 */

public class DeleteGroupChatMemberDialog extends DialogBase {

    private TextView messageTv,cancelBtn,okBtn;

    private LinearLayout selectLayout;

    private ImageView selectIv;

    private boolean selected = false;


    public DeleteGroupChatMemberDialog(Context context, final SelectListener selectListener) {

        super(context, R.layout.dialog_delete_member);

        selectLayout = (LinearLayout)window.findViewById(R.id.selectLayout);

        selectIv = (ImageView)window.findViewById(R.id.selectIv);

        cancelBtn = (TextView)window.findViewById(R.id.cancelBtn);

        okBtn = (TextView)window.findViewById(R.id.okBtn);

        selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected = !selected;

                setSelectIv();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectListener.select(selected);
                dialog.dismiss();
            }
        });

    }
    private void setSelectIv(){
        if(selected){
            selectIv.setImageResource(R.drawable.icon_contact_selected);
        }else{
            selectIv.setImageResource(R.drawable.icon_contact_normal);
        }
    }

    public interface SelectListener{
        void select(boolean selected);
    }
}
