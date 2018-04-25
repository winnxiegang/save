package com.android.xjq.view.expandtv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.compat.BuildConfig;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.android.banana.commlib.view.CenterVerticalImageSpan;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lingjiu on 2017/8/16.
 */

public class ChatTextView extends TextView {
    @EmojiType
    private int emojiType = EmojiType.SCALE_WITH_TXT;

    @IntDef({EmojiType.ORIGINAL, EmojiType.SCALE_HALF, EmojiType.SCALE_WITH_TXT})
    public @interface EmojiType {
        int ORIGINAL = 0;//保持原大小
        int SCALE_WITH_TXT = 1;//缩小到跟文本大小一致
        int SCALE_HALF = 2;//缩小到原来的一半
    }

    public ChatTextView(Context context) {
        this(context, null);
    }

    public ChatTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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


    public void setSpannableString(SpannableStringBuilder text,
                                   String url,
                                   int startIndex,
                                   int emojiType) {
        this.emojiType = emojiType;
        text = parseEmoji(text);
        setText(text, BufferType.SPANNABLE);
        //getImageDrawable(text, url, startIndex);
    }

    public void setEmojiText(CharSequence text, int emojiType) {
        this.emojiType = emojiType;
        text = parseEmoji(text);
        setText(text, BufferType.SPANNABLE);
    }


    class MyTarget implements Target {
        private SpannableStringBuilder text;
        private int startIndex;

        MyTarget(SpannableStringBuilder text, int startIndex) {
            this.text = text;
            this.startIndex = startIndex;
        }

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            int height = (int) getTextSize();
            int width = (int) (((float) drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight()) * height);
            drawable.setBounds(0, 0, width, height);
            LogUtils.e("onBitmapLoaded", drawable.getIntrinsicWidth() + "---" + drawable.getIntrinsicHeight() + "--" + LibAppUtil.getScreenHeight(getContext()));
            //这里留个空格,用于设置与前面马甲的间距
            SpannableString ss = new SpannableString("[图片] ");
            ss.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0, ss.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.insert(startIndex, ss);
            setText(text, BufferType.SPANNABLE);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }


    private void getImageDrawable(final SpannableStringBuilder text, String url, final int startIndex) {
        if (url == null) {
            return;
        }
        Picasso.with(getContext()).load(url).into(new MyTarget(text, startIndex));
    }

    public SpannableStringBuilder parseEmoji(CharSequence input) {
        long l = System.currentTimeMillis();
        Pattern pattern = Pattern.compile("\\[[^\\[\\]]*\\]");
        Matcher matcher = pattern.matcher(input);
        SpannableStringBuilder sp = new SpannableStringBuilder(input);
        String packageName = getContext().getPackageName();
        while (matcher.find()) {
            String group = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            group = group.substring(1, group.length() - 1);
            int resid = getContext().getResources().getIdentifier("emoj_" + group, "drawable", packageName);
            /*if (resid == 0) {
                group = EmojUtils.getReplaceDrawableName(group);
                resid = getContext().getResources().getIdentifier("emoj_" + group, "drawable", packageName);
            }*/
            if (resid != 0) {
                Drawable drawable = getDrawableByEmojiType(resid);
                sp.setSpan(new CenterVerticalImageSpan(drawable), start, end, ImageSpan.ALIGN_BASELINE);
            }
        }
        if (BuildConfig.DEBUG)
            Log.e("parseEmoji", "parseEmoji: " + (System.currentTimeMillis() - l));
        return sp;
    }
}
