package com.android.xjq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.emoji.EmojBean;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.view.HtmlEmojiTextView;
import com.android.banana.commlib.view.expandtv.NumLimitEditText;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.medal.UserMedalBean;
import com.android.xjq.fragment.input.EmojiFragment;
import com.android.xjq.fragment.input.InputCallback;
import com.android.xjq.utils.SubjectUtils;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qiaomu on 2018/3/1.
 */

public class TransformTransitionActivity extends BaseActivity4Jczj implements InputCallback, IHttpResponseListener, View.OnClickListener {

    @BindView(R.id.num_limit_edit)
    NumLimitEditText mNumLimitEdit;
    @BindView(R.id.transform_iv)
    ImageView mTransformIv;
    @BindView(R.id.transform_title)
    TextView mTransformTitle;
    @BindView(R.id.transform_content)
    HtmlEmojiTextView mTransformContent;
    @BindView(R.id.transCardLayout)
    LinearLayout mTransCardLayout;
    @BindView(R.id.transform_emoji)
    ImageView mTransformEmoji;
    @BindView(R.id.emojiFramLayout)
    FrameLayout emojiFramLayout;

    private Unbinder unbinder;
    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    private String objectId, objectType, title, content, firstArticleUrl, author;
    private EmojiFragment mEmojiFragment;
    private int defaultIcon;

    /**
     * 转发话题：objectId为话题id，objectType为SUBJECT
     * 转发PK：objectId为PK游戏局id，objectType为PK_GAME_BOARD
     * 转发极限手速：objectId为期次id，objectType为HAND_SPEED
     *
     * @param from
     * @param firstArticleUrl 转发话题的图标logo，没有就给个本地的资源id
     * @param transObjectId   转发话题的objectId
     * @param transObjectType 转发话题的Type 看上面
     * @param transTitle      转发话题的标题
     * @param transContent    转发话题的摘要
     */
    public static void startTransformTransitionActivity(Context from, String author, String firstArticleUrl, String transObjectId, String transObjectType, String transTitle, String transContent, int defaultIconRes) {
        Intent intent = new Intent(from, TransformTransitionActivity.class);
        intent.putExtra("trans_author", author);
        intent.putExtra("trans_logo_url", firstArticleUrl);
        intent.putExtra("trans_objectId", transObjectId);
        intent.putExtra("trans_objectType", transObjectType);
        intent.putExtra("trans_title", transTitle);
        intent.putExtra("trans_content", transContent);
        intent.putExtra("trans_default_icon", defaultIconRes);
        from.startActivity(intent);
    }

    @Override
    protected void initEnv() {
        objectId = getIntent().getStringExtra("trans_objectId");
        objectType = getIntent().getStringExtra("trans_objectType");
        author = getIntent().getStringExtra("trans_author");
        title = getIntent().getStringExtra("trans_title");
        content = getIntent().getStringExtra("trans_content");
        firstArticleUrl = getIntent().getStringExtra("trans_logo_url");
        defaultIcon = getIntent().getIntExtra("trans_default_icon", R.drawable.image_empty);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_transform_transition);
        unbinder = ButterKnife.bind(this);

        mEmojiFragment = (EmojiFragment) getSupportFragmentManager().findFragmentById(R.id.emoji_fragment);
        mEmojiFragment.setCallback(this);

        mTransformEmoji.setOnClickListener(this);
    }

    @Override
    protected void setUpView() {
        setUpToolbar(getString(R.string.transform_title), R.menu.menu_completed, MODE_BACK);
        mToolbar.getMenu().getItem(0).setTitle(R.string.transform_publish);

        mNumLimitEdit.setMaxLength(500);
        mNumLimitEdit.setCountLocation(NumLimitEditText.LOCATION_RIGHT_BOTTOM);
        mTransCardLayout.setVisibility(TextUtils.isEmpty(title) && TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void setUpData() {
        PicUtils.load(this, mTransformIv, firstArticleUrl, defaultIcon);
        mTransformTitle.setText(title);
        mTransformTitle.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
        mTransformContent.setHtmlText(SubjectUtils.getSubjectSummarySpan(mTransformContent, content), null, false);
        mTransformContent.setMaxLines(TextUtils.isEmpty(title) ? 2 : 1);

        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.TRANSMIT_CONTENT_QUERY_BEFORE_CREATE, true);
        formBody.put("objectId", objectId);
        formBody.put("objectType", objectType);
        mHttpHelper.startRequest(formBody);
    }

    @Override
    public void onClick(View v) {
        emojiFramLayout.setVisibility(emojiFramLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (TextUtils.isEmpty(mNumLimitEdit.getText()))
            return false;

        showProgressDialog();
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.TRANSMIT_CREATE_SUBJECT, true);
        formBody.put("objectId", objectId);
        formBody.put("objectType", objectType);
        formBody.put("content", mNumLimitEdit.getText().toString());
        mHttpHelper.startRequest(formBody);

        return false;
    }

    @Override
    public void onEmojAdd(EmojBean emojBean) {
        if (emojBean.getMessage() == null) {
            mNumLimitEdit.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            Editable editable = mNumLimitEdit.getText();
            int index = mNumLimitEdit.getSelectionStart();
            SpannableString spanString = new SpannableString(emojBean.getUploadMessage());
            Drawable d = getResources().getDrawable(emojBean.getResId());
            d.setBounds(0, 0, (int) mNumLimitEdit.getTextSize(), (int) mNumLimitEdit.getTextSize());
            ImageSpan span = new ImageSpan(d, String.valueOf(-emojBean.getUploadMessage().length() + emojBean.getMessage().length()), ImageSpan.ALIGN_BOTTOM);
            spanString.setSpan(span, 0, emojBean.getUploadMessage().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            editable.insert(index, spanString);
        }
    }

    @Override
    public void onEmojDelete() {
        mNumLimitEdit.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }

    @Override
    public void onBubbleSelected(String fontColor) {

    }

    @Override
    public void onFansMedalSelected(UserMedalBean userMedalBean) {

    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        closeProgressDialog();
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case TRANSMIT_CONTENT_QUERY_BEFORE_CREATE:
                if (((JSONObject) o).has("content")) {
//                    int selection = mNumLimitEdit.getText() == null ? 0 : mNumLimitEdit.getText().length();
                    String content = ((JSONObject) o).optString("content");
//                    mNumLimitEdit.getText().append("@//" + author + ":" + content);
                    DetailsHtmlShowUtils.setHtmlText(mNumLimitEdit, mNumLimitEdit.getText() + "@//" + author + ":" + content);
                    mNumLimitEdit.setSelection(0);
                }
                break;
            case TRANSMIT_CREATE_SUBJECT:
                showToast(getString(R.string.transform_success));
                this.finish();
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        closeProgressDialog();
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case TRANSMIT_CONTENT_QUERY_BEFORE_CREATE:
                break;
            case TRANSMIT_CREATE_SUBJECT:
                operateErrorResponseMessage(jsonObject);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
