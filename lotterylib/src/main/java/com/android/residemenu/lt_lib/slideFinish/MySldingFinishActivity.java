package com.android.residemenu.lt_lib.slideFinish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.android.residemenu.lt_lib.R;

/**
 * Created by lingjiu on 2016/10/10 17:09.
 */
public class MySldingFinishActivity extends AppCompatActivity {
    protected SwipeBackLayout layout;
    /**
     * 是否支持侧滑
     *
     */
    private boolean isSupportSlideFinish = true;//默认是支持侧滑
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isSupportSlideFinish) {
            layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                    R.layout.layout_slide_finish_base, null);
            layout.attachToActivity(this);
        }

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if(isSupportSlideFinish){
            overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
        }
    }

    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isSupportSlideFinish){
            overridePendingTransition(0, R.anim.base_slide_right_out);
        }
    }

    protected void setIsSupportSlideFinish(boolean isSupportSlideFinish) {
        this.isSupportSlideFinish = isSupportSlideFinish;
    }

    protected void setOnlyEdgeSlideFinish(boolean onlyEdgeSlideFinish){
        layout.setOnlyEdgeSlideFinish(onlyEdgeSlideFinish);
    }
}
