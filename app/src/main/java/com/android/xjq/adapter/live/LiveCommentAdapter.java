package com.android.xjq.adapter.live;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.frescoUtils.DraweeSpanStringBuilder;
import com.android.xjq.frescoUtils.SimpleDraweeSpanTextView;
import com.android.xjq.model.live.LiveCommentMessageTypeEnum;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.encryptUtils.StringUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.widget.text.span.BetterImageSpan;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhouyi on 2017/4/5.
 */

public class LiveCommentAdapter extends BaseAdapter {

    private List<LiveCommentBean> list;

    private Context context;

    private LayoutInflater inflater;

    public void setHasBlackBubble(boolean hasBlackBubble) {
        isHasBlackBubble = hasBlackBubble;
    }

    private boolean isHasBlackBubble;

    public LiveCommentAdapter(List<LiveCommentBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
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

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_live_comment_message, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setItemView(position, holder);
        return convertView;

    }

    private void setItemView(int position, ViewHolder holder) {
        if (isHasBlackBubble) {
            holder.contentLayout.setBackgroundResource(R.drawable.shape_chat_gray_bg);
            holder.messageTv.setTextColor(Color.WHITE);
        } else {
            holder.messageTv.setTextColor(Color.BLACK);
        }
        holder.messageLayout.setVisibility(View.GONE);
        holder.giftMessageTv.setVisibility(View.GONE);
        LiveCommentBean liveCommentBean = list.get(position);
        switch (LiveCommentMessageTypeEnum.safeValueOf(liveCommentBean.getType())) {
            case NORMAL:
            case TEXT:
                holder.messageLayout.setVisibility(View.VISIBLE);
                holder.userNameTv.setVisibility(View.VISIBLE);
                setTextMessage(liveCommentBean, holder);
                break;
            case GIFTCORE_GIFT_GIVE_TEXT:
                holder.giftMessageTv.setVisibility(View.VISIBLE);
                setGiftShow(liveCommentBean, holder);
                break;
            case SPECIAL_EFFECT_ENTER_NOTICE:
                holder.messageLayout.setVisibility(View.VISIBLE);
                holder.userNameTv.setVisibility(View.GONE);
                setSpecialEffectShow(liveCommentBean, holder);
                break;
        }
    }

    private void setSpecialEffectShow(LiveCommentBean liveCommentBean, ViewHolder holder) {
        String senderName = liveCommentBean.getSenderName();
        SpannableStringBuilder ssb = new SpannableStringBuilder("欢迎 ");
        ssb.append(SpannableStringHelper.changeTextColor(senderName, Color.parseColor("#fe6969")));
        ssb.append(" 入场");
        holder.messageTv.setText(ssb);
    }

    private void setGiftShow(LiveCommentBean liveCommentBean, ViewHolder holder) {
        LiveCommentBean.ContentBean.BodyBean body = liveCommentBean.getContent().getBody();
        holder.userNameTv.setText(liveCommentBean.getSenderName());

        DraweeSpanStringBuilder ssb = new DraweeSpanStringBuilder();

        String sendGiftName = liveCommentBean.getSenderName() + " 送出" + body.getTotalCount() + "个 " + body.getGiftConfigName() + " ";

        String tag = "[图片]";
        ssb.append(sendGiftName);
        ssb.append(tag);

        DraweeHierarchy draweeHierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
//                .setPlaceholderImage(new ColorDrawable(Color.RED))
                .build();

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(liveCommentBean.getContent().getBody().getGiftImageUrl()))
                .build();

        ssb.setImageSpan(
                context,
                draweeHierarchy,
                draweeController,
                sendGiftName.length(),
                sendGiftName.length() + tag.length() - 1,
                LibAppUtil.dip2px(context,40),
                LibAppUtil.dip2px(context,40),
                false,
                BetterImageSpan.ALIGN_CENTER);

        holder.giftMessageTv.setDraweeSpanStringBuilder(ssb);

    }

    private void setTextMessage(LiveCommentBean liveCommentBean, ViewHolder holder) {
        holder.userNameTv.setText(liveCommentBean.getSenderName() + ":");
        String text = liveCommentBean.getContent().getText();
        if(StringUtils.isBlank(text)){
            holder.messageTv.setText("");
            return;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        int i = text.indexOf("[");
        while (i != -1) {
            int j = text.indexOf("]", i + 1);
            if (j != -1) {
                String emojName = text.substring(i + 1, j);
                //获取资源id
                int resId = context.getResources().getIdentifier("emoj_" + emojName, "drawable", context.getPackageName());
                if (resId != 0) {
                    ssb.setSpan(getEmojImageSpan(holder.messageTv, resId), i, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            }
            i = text.indexOf("[", i + 1);
        }
        holder.messageTv.setText(ssb);
    }

    private ImageSpan getEmojImageSpan(TextView et, int resId) {

        Drawable d = context.getResources().getDrawable(resId);

        d.setBounds(0, 0, (int) et.getTextSize(), (int) et.getTextSize());

        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);

        return span;

    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    class ViewHolder {
        @BindView(R.id.userNameTv)
        TextView userNameTv;

        @BindView(R.id.messageTv)
        TextView messageTv;

        @BindView(R.id.messageLayout)
        View messageLayout;

        @BindView(R.id.giftMessageTv)
        SimpleDraweeSpanTextView giftMessageTv;

        @BindView(R.id.contentLayout)
        LinearLayout contentLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
