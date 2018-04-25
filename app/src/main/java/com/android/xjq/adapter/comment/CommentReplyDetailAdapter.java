package com.android.xjq.adapter.comment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.bean.JczjMedalBean;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.view.CenterVerticalImageSpan;
import com.android.xjq.R;
import com.android.xjq.bean.comment.CommentReplyBean;
import com.android.xjq.model.comment.ObjectTypeEnum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhouyi on 2016/1/11 14:36.
 */
public class CommentReplyDetailAdapter extends MyBaseAdapter<CommentReplyBean> {
    private String objectType;
    public CommentReplyDetailAdapter(Context context, List<CommentReplyBean> list) {
        super(context, list);

    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_comment_reply, null);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        setItemView(viewHolder, position);

        return convertView;
    }

    private void setItemView(ViewHolder viewHolder, int position) {

        final CommentReplyBean bean = list.get(position);

        if (bean.isShowBlack()) {
            viewHolder.contentLayout.setBackgroundColor(Color.parseColor("#e2e2e2"));
        } else {
            viewHolder.contentLayout.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder();


        SpannableString ss = new SpannableString(bean.getLoginName() + " ");

        ssb.append(ss);

        //添加楼主以及勋章
        if (objectType!=null&&!ObjectTypeEnum.LOTTERY_PROJECT.name().equals(objectType) && bean.isLouZhu()) {

            ss = new SpannableString("作者 ");

            Drawable d = context.getResources().getDrawable(R.drawable.icon_author);

            d.setBounds(0, -3, d.getIntrinsicWidth(), d.getIntrinsicHeight() - 3);

            CenterVerticalImageSpan span = new CenterVerticalImageSpan(d, ImageSpan.ALIGN_BASELINE);

            ss.setSpan(span, 0, "作者".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ssb.append(ss);
        }

        if (bean.getUserMedalBeanList() != null && bean.getUserMedalBeanList().size() != 0) {
            for (JczjMedalBean userMedalBean : bean.getUserMedalBeanList()) {
                if ("MANAGER".equals(userMedalBean.getMedalCode())) {
                    ssb.append(DetailsHtmlShowUtils.getHtmlText(viewHolder.contentTv, "<img src=\"" + userMedalBean.getUserMedalUrl() + "\"/>"));
                    break;
                }
            }
        }

        ssb.append(" : ");

        ss = new SpannableString(ssb);

        ss.setSpan(new MyClickableSpan(bean.getUserId(), (Activity) context), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ssb = new SpannableStringBuilder();

        ssb.append(ss);

        ssb.append(DetailsHtmlShowUtils.getHtmlText(viewHolder.contentTv, bean.getContent()));

        ss = new SpannableString(" " + bean.getGmtCreate());

        ss.setSpan(new TextAppearanceSpan(context, R.style.smallDateStyle), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ssb.append(ss);

        viewHolder.contentTv.setText(ssb);

        viewHolder.contentTv.setMovementMethod(LinkMovementMethod.getInstance());

        viewHolder.contentTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                CharSequence text = ((TextView) v).getText();
                Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
                TextView widget = (TextView) v;
                int action = event.getAction();

                if (action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                    if (link.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            link[0].onClick(widget);
                        }
                        ret = true;
                    }
                }
                return ret;
            }
        });

//        viewHolder.contentTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ReplyDetailActivity) context).showReplyLayout(bean.getUserName(), bean.getCommentReplyId(),true);
//            }
//        });

//        viewHolder.userNameTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HomePageActivity.startHomepageActivity((Activity) context, bean.getUserId());
//            }
//        });
    }


    public static class MyClickableSpan extends URLSpan {

        private Activity activity;

        public MyClickableSpan(String url, Activity activity) {

            super(url);

            this.activity = activity;

        }

        @Override
        public void onClick(View widget) {

//            HomepageActivity.startHomePageActivity(activity, getURL());

        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setColor(Color.parseColor("#2f6bad"));

            ds.setUnderlineText(false);

        }
    }

    static class ViewHolder {
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.louzhuIv)
        ImageView louzhuIv;
        @BindView(R.id.contentTv)
        TextView contentTv;
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.contentLayout)
        LinearLayout contentLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
