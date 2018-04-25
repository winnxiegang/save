package com.android.banana.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.R;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.emoji.EmojBean;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.EmojiEditTextView;
import com.android.banana.groupchat.bean.ChatConfigBean;
import com.android.banana.groupchat.chatenum.ForbiddenActionType;
import com.android.banana.groupchat.ilistener.EmojClickListener;
import com.android.banana.pullrecycler.Conf;
import com.android.banana.utils.IMInputFilter;
import com.android.banana.utils.KeyboardHelper;
import com.android.httprequestlib.loop.Loop;
import com.android.httprequestlib.loop.LoopCallback;

/**
 * Created by qiaomu on 2017/6/21.
 */

public class ImLayout extends LinearLayout implements MenuLayout.onMenuItemClickListener, EmojClickListener, View.OnClickListener, com.android.banana.commlib.view.EmojiEditTextView.textChangedListener {
    public RadioButton emoji;
    public EmojiEditTextView emojiEditv;
    public TextView sendMsg;
    public RadioButton more;
    public RadioGroup inputLayout;
    public MenuLayout menuLayout;

    private View imLayout;
    private OnClickListener listener;
    private MenuLayout.onMenuItemClickListener menuItemClickListener;
    private Loop loop;
    private boolean enableTouchEvent = true;
    private String newMsg;
    private Toast mCurrentToast;

    public ImLayout(Context context) {
        super(context);
        init(context);
    }

    public ImLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //提供对子view的点击拦截，释放
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return enableTouchEvent ? super.onInterceptTouchEvent(ev) : true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enableTouchEvent ? super.onTouchEvent(event) : true;
    }

    public void enableTouchEvent(boolean enable) {
        this.enableTouchEvent = enable;
    }

    private void init(Context context) {
        imLayout = LayoutInflater.from(context).inflate(R.layout.activity_im_chat_input, this, true);
        emoji = (RadioButton) imLayout.findViewById(R.id.emoji);
        emojiEditv = (com.android.banana.commlib.view.EmojiEditTextView) imLayout.findViewById(R.id.editv);
        sendMsg = (TextView) imLayout.findViewById(R.id.sendMsg);
        more = (RadioButton) imLayout.findViewById(R.id.more);
        inputLayout = (RadioGroup) imLayout.findViewById(R.id.inputLayout);
        menuLayout = (MenuLayout) imLayout.findViewById(R.id.moreMenuLayout);
        emoji.setOnClickListener(this);
        more.setOnClickListener(this);
        sendMsg.setOnClickListener(this);

        menuLayout.setOnMenuItemClickListener(this);
        menuLayout.setEmojClickListener(this);
        emojiEditv.setFilters(new InputFilter[]{new IMInputFilter()});
        enableTextChanged(true);


    }

    public void enableTextChanged(boolean enable) {
        if (enable) {
            emojiEditv.addOnTextChangedListener(this);
            if (TextUtils.isEmpty(emojiEditv.getText().toString())) {
                more.setVisibility(VISIBLE);
                sendMsg.setVisibility(GONE);
            }
        } else {
            emojiEditv.removeTextChangedListener();
            sendMsg.setVisibility(VISIBLE);
            more.setVisibility(GONE);
        }
    }

    @Override
    public void onMenuMoreItemClick(int position) {
        if (menuItemClickListener != null)
            menuItemClickListener.onMenuMoreItemClick(position);

        inputLayout.clearCheck();
        menuLayout.startVisibilityAnimation(View.GONE);
    }

    @Override
    public void onEmojiClick(EmojBean bean) {
        emojiEditv.insertEmoj(bean);
    }

    public boolean changeGroupChatStatus(ChatConfigBean chatConfig) {
        if (chatConfig == null)
            return false;

        //聊天室关闭
        if (!chatConfig.groupChatOpen) {
            shutdownInput(getContext().getString(R.string.live_score_chatroom_close_1), false, Gravity.CENTER);
            return false;
        }

        //USER_ENTER_GROUP_OPERATE返回的是forbidden
        String forbiddenTip = "";
        boolean forbidden = chatConfig.forbidden && chatConfig.forbiddenAction != null
                && !TextUtils.isEmpty(chatConfig.forbiddenAction.gmtExpired)
                && TimeUtils.date1SubDate2MS(chatConfig.forbiddenAction.gmtExpired, TimeUtils.getCurrentTime()) > 0;
        boolean forbiddenForever = chatConfig.forbidden && chatConfig.forbiddenAction == null;

        boolean enable = true;
        if (forbidden) {
            String gmtExpired = chatConfig.forbiddenAction.gmtExpired;
            forbiddenTip = String.format(getContext().getString(R.string.live_score_forbidden_1), gmtExpired);
            enable = false;
            startForbiddenCountDown(gmtExpired);
        } else if (forbiddenForever) {
            forbiddenTip = getContext().getString(R.string.live_score_forbidden_3);
            enable = false;
        }

        //USER_AUTHORITY_OR_GROUP_QUERY返回是error,与上面互斥
        NormalObject error = chatConfig.error;
        if (error != null) {
            if (TextUtils.equals(error.getName(), ForbiddenActionType.Type.RELATION_GROUP_CHAT)
                    || TextUtils.equals(error.getName(), ForbiddenActionType.Type.RACE_CHAT_FORBIDDEN)) {
                if (TextUtils.isEmpty(chatConfig.gmtExpired)) {
                    //永久禁言
                    enable = false;
                    forbiddenTip = getContext().getString(R.string.live_score_forbidden_3);
                } else {
                    //非永久禁言
                    enable = false;
                    startForbiddenCountDown(chatConfig.gmtExpired);
                    forbiddenTip = String.format(getContext().getString(R.string.live_score_forbidden_1), chatConfig.gmtExpired);
                }
            } else {
                ToastUtil.show(getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT);
                String hint = getContext().getString(R.string.live_score_edit_hint);
                shutdownInput(hint, true, Gravity.CENTER | Gravity.LEFT);
                return true;
            }
        }

        if (!enable)
            shutdownInput(forbiddenTip, enable, Gravity.CENTER);
        return false;
    }


    private void shutdownInput(String hint, boolean enable, int gravity) {
        menuLayout.startVisibilityAnimation(View.GONE);
        KeyboardHelper.hideSoftInput(emojiEditv);
        setInputLayoutEnable(hint, enable, gravity);
        inputLayout.clearCheck();
    }

    public void setInputLayoutEnable(String forbiddenTip, boolean enable, int gravity) {
        emojiEditv.setGravity(gravity);
        emojiEditv.setText("");
        emojiEditv.setHint(forbiddenTip);
        emojiEditv.setEnabled(enable);
        emoji.setEnabled(enable);
        emoji.setClickable(enable);
        more.setClickable(enable);
        sendMsg.setClickable(enable);
        sendMsg.setVisibility(GONE);
        emoji.setVisibility(enable ? VISIBLE : GONE);
        more.setVisibility(enable ? VISIBLE : GONE);
    }

    private void startForbiddenCountDown(final String gmtExpired) {
        loop = Loop.creat().start(1000 * 60, new LoopCallback() {
            @Override
            public void run() {
                String forbiddenTime = TimeUtils.getForbiddenTime(gmtExpired);
                if (TextUtils.equals(forbiddenTime, "0")) {
                    loop.stop();
                    String forbiddenTip = getContext().getString(R.string.live_score_edit_hint);
                    setInputLayoutEnable(forbiddenTip, true, Gravity.LEFT | Gravity.CENTER);
                } else {
                    String forbiddenTip = String.format(getContext().getString(R.string.live_score_forbidden_2), forbiddenTime);
                    emojiEditv.setHint(forbiddenTip);
                }
            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        if (loop != null)
            loop.stop();
        super.onDetachedFromWindow();
    }


    public boolean onBackPressed() {
        if (menuLayout.getVisibility() == View.VISIBLE) {
            menuLayout.startVisibilityAnimation(View.GONE);
            inputLayout.clearCheck();
            return true;
        }
        return false;
    }


    public void addGroupMenu() {
        addMenuItem(R.string.group_chat_menu_item_pic, R.drawable.icon_live_score_pics);
        addMenuItem(R.string.group_chat_menu_item_coupon, R.drawable.icon_live_score_coupon);
    }
    public void addGroupMenuOne() {
        addMenuItem(R.string.group_chat_menu_item_pic, R.drawable.icon_live_score_pics);
    }

    public void addMenuItem(@StringRes int strRes, @DrawableRes int icon) {
        menuLayout.addMenuItem(getContext().getString(strRes), icon);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.emoji) {
            // menuLayout.showEmojiPanel(true);
        } else if (view.getId() == R.id.more) {
            // menuLayout.showMenuMore(true);
        } else if (view.getId() == R.id.sendMsg) {
            newMsg = emojiEditv.getText().toString().trim();
            if (TextUtils.isEmpty(newMsg) || !LibAppUtil.isOpenNetwork(getContext(), true))
                return;
            if (!TextUtils.isEmpty(newMsg) && newMsg.length() > Conf.MAX_INPUT_WORDS) {
                LibAppUtil.showTip(getContext(), getContext().getString(R.string.live_score_send_txt_too_long));
                return;
            }
            emojiEditv.setText("");
        }
        if (listener != null)
            listener.onClick(view);
    }

    public void setOnButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setonMenuClickListener(MenuLayout.onMenuItemClickListener menuItemClickListener) {
        this.menuItemClickListener = menuItemClickListener;
    }


    public String getNewMsg() {
        return newMsg;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        boolean empty = TextUtils.isEmpty(s);
        sendMsg.setVisibility(empty ? View.GONE : View.VISIBLE);
        more.setVisibility(empty ? View.VISIBLE : View.GONE);
    }
}
