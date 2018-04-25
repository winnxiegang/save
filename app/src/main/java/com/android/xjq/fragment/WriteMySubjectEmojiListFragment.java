package com.android.xjq.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.ShowSimpleMessageDialog;
import com.android.banana.commlib.emoji.EmojBean;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.SoftKeyboardStateHelper;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.utils.KeyboardHelper;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.bean.medal.UserMedalBean;
import com.android.xjq.fragment.input.EmojiFragment;
import com.android.xjq.fragment.input.InputCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WriteMySubjectEmojiListFragment extends Fragment implements IHttpResponseListener, InputCallback {

    private WrapperHttpHelper httpOperate = null;

    private static final String COMMENT_ID = "comment_id";

    private static final String COMMENT_REPLY_ID = "comment_reply_id";

    private final int MAX_INPUT_COUNT = 140;

    private final int MIN_INPUT_COUNT = 1;

    @BindView(R.id.shareHomePageCb)
    CheckBox shareHomePageCb;

    @BindView(R.id.addEmojIv)
    ImageView addEmojIv;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.sendCommentReplyTv)
    Button sendCommentReplyTv;

    @BindView(R.id.emojiFramLayout)
    FrameLayout emojLayout;

    @BindView(R.id.replyShowLayout)
    LinearLayout replyShowLayout;

    private String commentId;

    private String commentReplyId;

    private String userName;

    private OnFragmentInteractionListener mListener;

    private boolean showEmoj = false;

    public WriteMySubjectEmojiListFragment() {
    }

    public static WriteMySubjectEmojiListFragment newInstance() {

        WriteMySubjectEmojiListFragment fragment = new WriteMySubjectEmojiListFragment();

        return fragment;

    }

    @OnClick(R.id.sendCommentReplyTv)
    public void sendComment() {

        ImageSpan[] spans = editText.getText().getSpans(0, editText.getText().length(), ImageSpan.class);

        int count = 0;

        for (ImageSpan span : spans) {
            count += Integer.parseInt(span.getSource());
        }

        int length = editText.getText().length() + count;

        if (length < MIN_INPUT_COUNT) {
            LibAppUtil.showTip(getActivity(), "内容不能为空，随便说点什么吧~");
            return;
        }
        if (length > MAX_INPUT_COUNT) {
            LibAppUtil.showTip(getActivity(), "不能超过" + MAX_INPUT_COUNT + "个字");
            return;
        }

        commentReplyCreate();

    }


    @OnClick(R.id.addEmojIv)
    public void showEmoj() {
        if (emojLayout.getVisibility() == View.VISIBLE) {
            showEmoj = false;
            emojLayout.setVisibility(View.GONE);
            KeyboardHelper.showSoftInput(editText);
        } else {
            if (KeyboardHelper.isActive(getActivity())) {
                KeyboardHelper.hideSoftInput(editText);
            } else {
                emojLayout.setVisibility(View.VISIBLE);
            }
            showEmoj = true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            commentId = getArguments().getString(COMMENT_ID);
            commentReplyId = getArguments().getString(COMMENT_REPLY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_write_mysubject, container, false);

        httpOperate = new WrapperHttpHelper(this);

        ButterKnife.bind(this, view);

        initView();

        shareHomePageCb.setChecked(false);

        return view;
    }

    private void initView() {
        EmojiFragment emojiFragment = (EmojiFragment) getChildFragmentManager().findFragmentByTag("emojiFragment");
        emojiFragment.setCallback(this);
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(getActivity(), replyShowLayout);

        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                if (emojLayout.getVisibility() == View.VISIBLE) {
                    emojLayout.setVisibility(View.GONE);
                    showEmoj = false;
                }
            }

            @Override
            public void onSoftKeyboardClosed() {
                if (showEmoj) {
                    emojLayout.setVisibility(View.VISIBLE);
                }

            }
        });

    }


    //创建评论
    private void commentReplyCreate() {
        String content = editText.getText().toString();
        LogUtils.e("commentReplyCreate== ", "content=" + content);
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.COMMENT_REPLY_CREATE, true);
        formBody.put("commentId", commentId);
        if (!TextUtils.isEmpty(commentReplyId)) {
            formBody.put("parentReplyId", commentReplyId);
            content = "回复 " + userName + ":" + content;
        }
        formBody.put("content", content);
        formBody.put("createTopic", shareHomePageCb.isChecked() ? "1" : "0");

        httpOperate.startRequest(formBody);

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }


    private void ShowGagDialog(JSONObject jo, String content) {

        try {
            String gmtExpired = null;
            String forbiddenReason = null;
            if (jo.has("gmtExpired")) {
                gmtExpired = jo.getString("gmtExpired");
            }
            if (jo.has("forbiddenReason")) {
                forbiddenReason = jo.getString("forbiddenReason");
            }
            ShowSimpleMessageDialog simpleMessageDialog = new ShowSimpleMessageDialog(getActivity(), content + "\n" + "禁言原因:" + forbiddenReason + "\n" + "解封时间:" + gmtExpired);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        if (mListener != null) {
            mListener.onFragmentInteraction(true);
        }

        hideLayout();

        editText.setText("");

        LibAppUtil.showTip(getActivity(), "回复成功");
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        try {
            ErrorBean bean = new ErrorBean(jsonObject);
            if (bean.getError() == null) {
                return;
            }
            if ("COMMENT_NOT_EXISTS".equals(bean.getError().getName()) || "COMMENT_HAS_BEEN_DELETED".equals(bean.getError().getName())) {
                LibAppUtil.showTip(getActivity(), "抱歉,原内容已删除,无法进行回复");
            } else if ("SUBJECT_NOT_EXISTS".equals(bean.getError().getName()) || "SUBJECT_HAS_BEEN_DELETED".equals(bean.getError().getName())) {
                LibAppUtil.showTip(getActivity(), "抱歉,原内容已删除,无法进行回复");
            } else if ("CMS_HAS_BEEN_DELETED".equals(bean.getError().getName()) || "CMS_NOT_EXISTS".equals(bean.getError().getName())) {
                LibAppUtil.showTip(getActivity(), "抱歉,原内容已删除,无法进行回复");
            } else if ("POST_FORBIDDEN".equals(bean.getError().getName())) {
                ShowGagDialog(jsonObject, "您已被禁言");
            } else {
                ((BaseActivity) getActivity()).operateErrorResponseMessage(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEmojAdd(EmojBean bean) {
        if (bean.getMessage() == null) {
            deleteChar();
        } else {
            insertEmoj(bean);
        }
    }

    @Override
    public void onEmojDelete() {
        deleteChar();
    }

    @Override
    public void onBubbleSelected(String fontColor) {

    }

    @Override
    public void onFansMedalSelected(UserMedalBean userMedalBean) {

    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(boolean sendResult);

    }

    public void insertEmoj(EmojBean bean) {

        Editable editable = editText.getText();

        int index = editText.getSelectionStart();

        editable.insert(index, getSpannableString(bean.getUploadMessage(), bean.getMessage(), getResources().getDrawable(bean.getResId())));

    }

    public void deleteChar() {

        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));

    }

    private SpannableString getSpannableString(String hideString, String message, Drawable drawable) {

        SpannableString spanString = new SpannableString(hideString);

        Drawable d = drawable;

        d.setBounds(0, 0, (int) editText.getTextSize(), (int) editText.getTextSize());

        ImageSpan span = new ImageSpan(d, String.valueOf(-hideString.length() + message.length()), ImageSpan.ALIGN_BOTTOM);

        spanString.setSpan(span, 0, hideString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;

    }

    public void setValue(String commentId, String commentReplyId, String userName) {

        LibAppUtil.showSoftKeyboard(getActivity(), editText);

        editText.setHint("回复:" + userName);

        this.commentId = commentId;

        this.commentReplyId = commentReplyId;

        this.userName = userName;

    }

    public void setValue(String commentId, String commentReplyId, String userName, boolean showKeyboard) {

        if (showKeyboard) {

            LibAppUtil.showSoftKeyboard(getActivity(), editText);

        } else {

            LibAppUtil.hideSoftKeyboard(getActivity());
        }

        editText.setHint("回复:" + userName);

        this.commentId = commentId;

        this.commentReplyId = commentReplyId;

        this.userName = userName;

    }

    public void hideLayout() {

        if (emojLayout.getVisibility() == View.VISIBLE) {

            emojLayout.setVisibility(View.GONE);
        }
        LibAppUtil.hideSoftKeyboard(getActivity());
    }
}
