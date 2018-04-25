package com.android.xjq.controller.message;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.bean.message.SystemNotifyBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kokuma on 2017/11/28.
 */

public class DefaultShowController {

    private Context context;

    public DefaultShowController(Context context) {

        this.context = context;

    }

    public void setDefaultShow(ViewHolder holder, SystemNotifyBean.NoticesBean bean){

        holder.portraitIv.setImageResource(R.drawable.icon_message_notify);

        holder.timeTv.setText(bean.getGmtCreate());

        holder.defaultShowLayout.setVisibility(View.VISIBLE);

        holder.defaultShowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibAppUtil.showTip(context,"暂不支持内容的查看");
            }
        });

    }


    public static class ViewHolder {

        @BindView(R.id.portraitIv)
        CircleImageView portraitIv;

        @BindView(R.id.vipIv)
        ImageView vipIv;

        @BindView(R.id.usernameTv)
        TextView usernameTv;

        @BindView(R.id.timeTv)
        TextView timeTv;

        @BindView(R.id.messageTypeTv)
        TextView messageTypeTv;

        @BindView(R.id.warnMessageTv)
        TextView warnMessageTv;

        public LinearLayout defaultShowLayout;

        public ViewHolder(View view) {

            defaultShowLayout = (LinearLayout) view.findViewById(R.id.defaultShowLayout);

            ButterKnife.bind(this, defaultShowLayout);

        }
    }
}
