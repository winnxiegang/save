package com.android.banana.commlib.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrs on 2017/4/20.
 */

public class HtmlEmojiTextView extends AppCompatTextView {

    @EmojiType
    private int emojiType = EmojiType.SCALE_WITH_TXT;
    private CharSequence mText;
    SpannableStringBuilder mStringBuilder;

    public HtmlEmojiTextView(Context context) {
        this(context, null);
    }

    public HtmlEmojiTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HtmlEmojiTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @IntDef({EmojiType.ORIGINAL, EmojiType.SCALE_HALF, EmojiType.SCALE_WITH_TXT})
    public @interface EmojiType {
        int ORIGINAL = 0;//保持原大小
        int SCALE_WITH_TXT = 1;//缩小到跟文本大小一致
        int SCALE_HALF = 2;//缩小到原来的一半
    }

    public void setEmojiText(CharSequence htmlText) {

        setEmojiText(htmlText, EmojiType.SCALE_WITH_TXT);
    }

    public void setEmojiText(CharSequence htmlText, @EmojiType int emojiType) {
        if (TextUtils.isEmpty(htmlText)) {
            this.setText("");
            return;
        }
        this.emojiType = emojiType;
        parseEmoji(htmlText);
    }


    public void setEllipsizeText(CharSequence text) {
        mText = text;
        ellipsize();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ellipsize();
    }

    private void ellipsize() {
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        int maxLines = getMaxLines();
        int width = getWidth();
        float textWidth = getPaint().measureText(mText, 0, mText.length());
        int nowLine = (int) (textWidth / width) + 1;

        if (nowLine < maxLines) {
            setText(mText);
            return;
        }

        CharSequence newText = TextUtils.ellipsize(mText, getPaint(), getWidth() * getMaxLines(), TextUtils.TruncateAt.END);
        setText(newText);
    }


    public void setHtmlText(CharSequence htmlSpan, CharSequence tagSpans, boolean isClickable) {
        if (mStringBuilder == null)
            mStringBuilder = new SpannableStringBuilder();

        if (tagSpans != null) {
            mStringBuilder.clear();
            mStringBuilder.append(tagSpans);
            mStringBuilder.append(" ");
            mStringBuilder.append(htmlSpan);
            setEllipsizeText(mStringBuilder);
        } else {
            setEllipsizeText(htmlSpan);
        }

        if (isClickable) {
            setMovementMethod(LinkMovementMethod.getInstance());
            setLinkTextColor(Color.parseColor("#559ce8"));
            setTouchListener();
        }
    }

    private void parseEmoji(CharSequence input) {
        Pattern pattern = Pattern.compile("\\[[^\\[\\]]*\\]");
        Matcher matcher = pattern.matcher(input);
        SpannableString sp = new SpannableString(input);
        String packageName = getContext().getPackageName();
        while (matcher.find()) {
            String group = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            group = group.substring(1, group.length() - 1);
            int resid = getContext().getResources().getIdentifier("emoj_" + group, "drawable", packageName);
            if (resid != 0) {
                Drawable drawable = getDrawableByEmojiType(resid);
                if (drawable != null)
                    sp.setSpan(new CenterVerticalImageSpan(drawable), start, end, ImageSpan.ALIGN_BASELINE);
            }
        }

        setText(sp);
    }

    private Drawable getDrawableByEmojiType(int resid) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resid);
        if (emojiType == EmojiType.ORIGINAL) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        } else if (emojiType == EmojiType.SCALE_HALF) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
        } else if (emojiType == EmojiType.SCALE_WITH_TXT) {
            drawable.setBounds(0, 0, (int) getTextSize(), (int) getTextSize());
        }
        return drawable;
    }

    private void setTouchListener() {
        setOnTouchListener(new OnTouchListener() {
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

                    ClickableSpan[] links = stext.getSpans(off, off, ClickableSpan.class);
                    if (links.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            links[0].onClick(widget);
                        }
                        ret = true;
                    }
                }
                return ret;
            }
        });

    }
}
