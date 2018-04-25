package com.android.xjq.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.coupon.BasePopupWindow;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.model.comment.ObjectTypeEnum;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import static com.android.xjq.model.comment.ObjectTypeEnum.DEFAULT;

/**
 * Created by lingjiu on 2017/7/10 15:07.
 */
public class SharePopUpWindowHelper {

    private BasePopupWindow popWindow;

    private Activity activity;

    private Builder builder;

    private View view;

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            LibAppUtil.showTip(activity, "分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            LibAppUtil.showTip(activity, " 分享失败");

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            LibAppUtil.showTip(activity, " 分享取消");

        }
    };

    private SharePopUpWindowHelper(Builder builder) {
        this.builder = builder;
        this.activity = builder.activity;

        popWindow = new BasePopupWindow(builder.activity, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        if (builder.isLandscape) {
            view = View.inflate(builder.activity, R.layout.view_share_choose_landscape, null);
        } else {
            view = View.inflate(builder.activity, R.layout.view_share_choose, null);
            popWindow.setSupportShadow(true);
        }


        setListener(view);
        popWindow.setContentView(view);
       /* popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));*/
        popWindow.setAnimationStyle(R.style.dialog_anim_bottom);
    }

    public void setListener(View view) {
        View QQLayout = view.findViewById(R.id.QQLayout);
        View QzoneLayout = view.findViewById(R.id.QzoneLayout);
        View WxLayout = view.findViewById(R.id.WxLayout);
        View WxCircleLayout = view.findViewById(R.id.WxCircleLayout);
        View SinaLayout = view.findViewById(R.id.SinaLayout);
        View copyPasteLayout = view.findViewById(R.id.copyPasteLayout);
        LinearLayout cancel_ll = (LinearLayout) view.findViewById(R.id.cancel_ll);
        setListener(QQLayout, SHARE_MEDIA.QQ);
        setListener(QzoneLayout, SHARE_MEDIA.QZONE);
        setListener(WxLayout, SHARE_MEDIA.WEIXIN);
        setListener(WxCircleLayout, SHARE_MEDIA.WEIXIN_CIRCLE);
        setListener(SinaLayout, SHARE_MEDIA.SINA);
        if (builder.isShowCopyPasteLayout()) {
            copyPasteLayout.setVisibility(View.VISIBLE);
            copyPasteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    //将文本数据复制到剪贴板
                    cm.setText(builder.getShareUrl());
                    popWindow.dismiss();
                    LibAppUtil.showTip(activity, "复制成功");
                }
            });
        } else {
            copyPasteLayout.setVisibility(View.GONE);
        }
        TextView titleTv = (TextView) view.findViewById(R.id.titleTv);

        if (builder.showTitle) {
            titleTv.setVisibility(View.VISIBLE);
        } else {
            titleTv.setVisibility(View.GONE);
        }

        cancel_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });

    }

    private void setListener(final View view, final SHARE_MEDIA platform) {

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UMShareAPI mShareAPI = UMShareAPI.get(activity);
                if (SHARE_MEDIA.WEIXIN == platform && !mShareAPI.isInstall(activity, SHARE_MEDIA.WEIXIN) ||
                        SHARE_MEDIA.WEIXIN_CIRCLE == platform && !mShareAPI.isInstall(activity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
                    LibAppUtil.showTip(activity.getApplicationContext(), "您还未安装微信客户端");
                    return;
                } else if (SHARE_MEDIA.QQ == platform && !mShareAPI.isInstall(activity, SHARE_MEDIA.QQ) ||
                        SHARE_MEDIA.QZONE == platform && !mShareAPI.isInstall(activity, SHARE_MEDIA.QZONE)) {
                    LibAppUtil.showTip(activity.getApplicationContext(), "您还未安装QQ客户端");
                    return;
                }

                int resId = 0;

                switch (builder.objectType) {
                    case CMS_NEWS:
                        resId = R.drawable.icon_qiubao_news;
                        break;
                    case SUBJECT://当前话题只有文章，之后可能要区分是不是文章
                        resId = R.drawable.icon_article_subject_logo;
                        break;
                    case LOTTERY_PROJECT:
                        resId = R.drawable.icon_qiubao_order;
                        break;
                }

                ShareAction shareAction = new ShareAction(activity)
                        .setPlatform(platform)
                        .setCallback(umShareListener)
                        .withText("在这里，发现一个好东西，强烈推荐给你。");
//                        .withText(builder.getSummary());
                UMWeb umWeb = new UMWeb(builder.getShareUrl());
                shareAction.withMedia(umWeb);
                umWeb.setTitle(builder.title);
                if (builder.hostImageUrl != null) {
                    umWeb.setThumb(new UMImage(activity, builder.hostImageUrl));
                    umWeb.setDescription(builder.hostName + "正在香蕉球直播,快来围观吧~");
                } else {
                    umWeb.setThumb(new UMImage(activity, resId));
                    umWeb.setDescription(builder.content);
                }

                shareAction.share();
                popWindow.dismiss();

            }
        });
    }

    public void show() {
        popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public Builder getBuilder() {
        return builder;
    }

    public static class Builder {

        private String shareUrl;

        private String content;

        private String createAlias;

        private String summary;

        private String title;

        private Activity activity;

        private boolean showCopyPasteLayout = true;

        //是否横屏
        private boolean isLandscape;
        //主播头像
        private String hostImageUrl;

        //主播名称
        private String hostName;

        private ObjectTypeEnum objectType = DEFAULT;

        private boolean showTitle = true;

        public Builder setHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public Builder setObjectType(ObjectTypeEnum objectType) {
            this.objectType = objectType;
            return this;
        }

        public Builder setShowTitle(boolean showTitle) {
            this.showTitle = showTitle;
            return this;
        }

        public Builder setHostImageUrl(String imageUrl) {
            this.hostImageUrl = imageUrl;
            return this;
        }

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public String getSummary() {
            return summary;
        }

        public String getTitle() {
            return title;
        }

        public Builder setLandscape(boolean landscape) {
            isLandscape = landscape;
            return this;
        }

        public boolean isShowCopyPasteLayout() {
            return showCopyPasteLayout;
        }

        public Builder setShowCopyPasteLayout(boolean showCopyPasteLayout) {
            this.showCopyPasteLayout = showCopyPasteLayout;
            return this;
        }

        public Builder setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
            return this;
        }

        public Builder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setCreateAlias(String createAlias) {
            this.createAlias = createAlias;
            return this;
        }

        public String getCreateAlias() {
            return createAlias;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public SharePopUpWindowHelper builder() {

            return new SharePopUpWindowHelper(this);

        }

    }
}
