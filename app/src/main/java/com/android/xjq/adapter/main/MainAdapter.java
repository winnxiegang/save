package com.android.xjq.adapter.main;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.live.main.homelive.ChannelListEntity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/3/30.
 */

public class MainAdapter extends RecyclerBaseAdapter<ChannelListEntity> {

    public MainAdapter(Context context, List<ChannelListEntity> list, View header) {
        super(context, list, header);
    }

    @Override
    protected BaseViewHolder getViewHolder(Context context) {

        View view = View.inflate(context, R.layout.item_main_item, null);

        return new ViewHolder(view);
    }

    @Override
    protected void setData(RecyclerView.ViewHolder holder, int position) {
        //如果有header数据要-1
        //final ChannelListEntity messageParamBean = list.get(position - 1);

        final ChannelListEntity bean = list.get(position);

        ViewHolder viewHolder = (ViewHolder) holder;

        setImageShow(viewHolder.liveImageIv, bean.getLogoUrl(), false);

        viewHolder.peopleNumTv.setText(String.valueOf(bean.getInChannelUsers()));

        viewHolder.userNameTv.setText(bean.getAnchorName());

        viewHolder.titleTv.setText(bean.getChannelTitle());

        viewHolder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveActivity.startLiveActivity((Activity) context, bean.getId());
            }
        });

        if (bean.isAnchorPushStream()) {

            viewHolder.roomStateIv.setImageResource(R.drawable.icon_room_living);
        } else {

            viewHolder.roomStateIv.setImageResource(R.drawable.icon_room_finish);
        }

        viewHolder.couponIv.setVisibility(View.INVISIBLE);

        if (bean.isHasCoupon()) {
            viewHolder.couponIv.setVisibility(View.VISIBLE);
        }
//        setImageShow(viewHolder.couponIv,"res://" + context.getPackageName() + "/" + R.drawable.icon_home_coupon,true);
    }

    private void setImageShow(SimpleDraweeView liveImageIv, String logoUrl, boolean enable) {

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(logoUrl))
                .setAutoPlayAnimations(enable)
                .build();
        liveImageIv.setController(controller);
    }

    private void toChatRoom() {

    }


    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.liveImageIv)
        SimpleDraweeView liveImageIv;

        @BindView(R.id.userNameTv)
        TextView userNameTv;

        @BindView(R.id.peopleNumTv)
        TextView peopleNumTv;

        @BindView(R.id.titleTv)
        TextView titleTv;
        @BindView(R.id.roomStateIv)
        ImageView roomStateIv;

        @BindView(R.id.contentLayout)
        View contentLayout;
        @BindView(R.id.couponIv)
        ImageView couponIv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


}
