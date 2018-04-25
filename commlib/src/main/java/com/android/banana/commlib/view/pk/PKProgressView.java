package com.android.banana.commlib.view.pk;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ajiao on 2018\3\13 0013.
 */

public class PKProgressView extends LinearLayout {

    private Context mCtx;
    private CircleImageView leftImg;
    private CircleImageView rightImg;
    private PKSeekBar pkSeekBar;
    private TextView leftMoneyTxt;
    private TextView rightMoneyTxt;

    public PKProgressView(Context context) {
        this(context, null);
    }

    public PKProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PKProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mCtx = context;
        LayoutInflater.from(context).inflate(R.layout.widget_pk_progress_view, this);

        leftImg = (CircleImageView)findViewById(R.id.left_img);
        rightImg = (CircleImageView)findViewById(R.id.right_img);
        pkSeekBar = (PKSeekBar)findViewById(R.id.pk_seekBar);
        leftMoneyTxt = (TextView)findViewById(R.id.left_pk_money);
        rightMoneyTxt = (TextView)findViewById(R.id.right_pk_money);

        requestDisallowInterceptTouchEvent(true);

    }


    public void setLeftImageUrl(String imgUrl) {
        Picasso.with(mCtx).load(imgUrl).error(R.drawable.user_default_logo).into(leftImg);
    }

    public void setRightImageUrl(String imgUrl) {
        Picasso.with(mCtx).load(imgUrl).error(R.drawable.user_default_logo).into(rightImg);
    }

    public void setSeekBarProgress(int hostMoney, int guestMoney) {
        if (hostMoney == 0 && guestMoney == 0) {
            pkSeekBar.setProgress(50);
        } else if (hostMoney == 0 && guestMoney != 0) {
            pkSeekBar.setProgress(0);
        } else if (hostMoney != 0 && guestMoney == 0) {
            pkSeekBar.setProgress(100);
        } else {
            Log.e("setSeekBarProgress: ", (hostMoney * 1.0f / (hostMoney + guestMoney)) + "");
            float progress = (hostMoney * 1.0f / (hostMoney + guestMoney)) * 100;
            pkSeekBar.setProgress((int) progress);
        }
    }

    public void setLeftMoney(int money) {
        leftMoneyTxt.setText(money + "礼物");
    }

    public void setRightMoney(int money) {
        rightMoneyTxt.setText(money + "礼物");
    }

}
