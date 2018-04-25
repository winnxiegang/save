package com.android.banana.commlib.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.view.URLImageParser;

import org.xml.sax.XMLReader;

/**
 * Created by zaozao on 2017/12/4.
 * 详情页显示html转化工具类（详情内容，一级回复，二级回复）
 */

public class DetailsHtmlShowUtils {
    public static String mHtmlContent = null;

    public static String getFormatHtmlContent(Context context, String content) {
        String userId = LoginInfoHelper.getInstance().getUserId();
        if (TextUtils.isEmpty(userId) || content == null)
            return content;
        return getHtmlContent(context).replace(AppParam.USER_ID, userId).replace(AppParam.SUBJECT_CONTENT, content);
    }

    /**
     * 获取替换内容
     *
     * @param context
     * @return
     */
    private static String getHtmlContent(Context context) {
        if (mHtmlContent == null) {
            mHtmlContent = LibAppUtil.getFromRaw(context, "wenzhang.htm")
                    .replace("[JCH_SERVER_URL]", AppParam.API_DOMAIN)
                    .replace("[FT_ICON_URL]", AppParam.FT_API_DOMAIN)
                    .replace("[JCH_IMAGE_URL]", AppParam.JCH_IMAGE_URL)
                    .replace("[BT_ICON_URL]", AppParam.BT_API_DOMAIN);
            return mHtmlContent;
        } else {
            return mHtmlContent;
        }
    }

    public static void setHtmlText(TextView tv, String content, boolean isClickable, SpannableStringBuilder headerSpan) {

        if (content == null) {
            tv.setText("");
            if (isClickable) {

                tv.setMovementMethod(LinkMovementMethod.getInstance());

                tv.setLinkTextColor(Color.parseColor("#559ce8"));

                setTouchListener(tv);

            }
            return;
        }

        URLImageParser p = new URLImageParser(tv, tv.getContext());
        SpannableStringBuilder htmlSpan = (SpannableStringBuilder) Html.fromHtml(content, p, new Html.TagHandler() {
            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
//                int len = output.length();
//                ImageSpan[] images = output.getSpans(0, len, ImageSpan.class);
//                if (images == null || images.length == 0)
//                    return;
//                String imgURL = images[0].getSource();
//                output.setSpan(new CustomURLSpan(tag), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        });


        htmlSpan.append(" ");

        if (headerSpan != null) {

            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append(headerSpan);

            ssb.append(htmlSpan);

            tv.setText(ssb, TextView.BufferType.SPANNABLE);
        } else {
            tv.setText(htmlSpan, TextView.BufferType.SPANNABLE);
        }

        if (isClickable) {

            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setLinkTextColor(Color.parseColor("#559ce8"));

            setTouchListener(tv);
        }

    }


    public static Spanned getHtmlText(TextView tv, String content) {

        URLImageParser p = new URLImageParser(tv, tv.getContext());

        //<script>alert(1234)</script>
        Spanned htmlSpan = Html.fromHtml(content, p, null);
        return htmlSpan;

    }

    public static void setHtmlText(TextView tv, String content, boolean isClickable) {
        setHtmlText(tv, content, isClickable, null);
    }


    public static void setTouchListener(TextView tv) {
        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                CharSequence text = ((TextView) v).getText();
                Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
                TextView widget = (TextView) v;
                int action = event.getAction();

                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
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

    }

    public static class CustomURLSpan extends URLSpan {

        public CustomURLSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {

            if (getURL().contains("userId=")) {

                String userId = getURL().substring(getURL().indexOf("userId=") + 7, getURL().length());

//                HomepageActivity.startHomePageActivity((Activity) context, userId);


            } else {

//                ToAdvertisementByWebActivity.startToAdvertisementByWebActivity((Activity) context, getURL(), "");

            }

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

    }

    protected void setFirstShowView(View view, int position) {
        if (position == 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void setHtmlText(TextView tv, String content) {
        setHtmlText(tv, content, true, null);
    }

    public static void formatAtSpan(SpannableStringBuilder htmlSpan) {

        URLSpan[] spans = htmlSpan.getSpans(0, htmlSpan.length(), URLSpan.class);

        for (URLSpan span : spans) {

            int start = htmlSpan.getSpanStart(span);

            int end = htmlSpan.getSpanEnd(span);

            htmlSpan.removeSpan(span);

            span = new CustomURLSpan(span.getURL());

            htmlSpan.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

}
