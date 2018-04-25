package com.android.xjq.controller.live;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.emoji.EmojBean;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.adapter.live.LiveCommentAdapter2;
import com.android.xjq.adapter.live.LiveCommentMultiType;
import com.android.xjq.bean.live.ChannelUserConfigBean;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.bean.medal.CurrentUserMedalDetail;
import com.android.xjq.bean.medal.MedalInfoBean;
import com.android.xjq.bean.medal.UserFansMedalInfoEntity;
import com.android.xjq.bean.medal.UserMedalBean;
import com.android.xjq.fragment.input.BubblePanelFragment;
import com.android.xjq.fragment.input.EmojiFragment;
import com.android.xjq.fragment.input.InputCallback;
import com.android.xjq.fragment.input.MedalFragment;
import com.android.xjq.listener.live.OnMessageSendListener;
import com.android.xjq.model.BubbleColorEnum;
import com.android.xjq.model.live.LiveCommentMessageTypeEnum;
import com.android.banana.commlib.utils.SharePreferenceUtils;
import com.android.xjq.view.ImageViewWithRedPoint;
import com.android.xjq.view.expandtv.CustomMedalDrawable;
import com.android.banana.commlib.view.EmojiEditTextView;
import com.android.xjq.view.recyclerview.MyClickRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.banana.commlib.utils.SharePreferenceUtils.GET_NEW_FANS_MEDAL_ID;
import static com.android.banana.commlib.utils.SharePreferenceUtils.SOFT_KEYBOARD_HEIGHT;


/**
 * Created by zhouyi on 2017/3/9.
 */

public class InputCommentController extends BaseLiveController<LiveActivity> implements OnMessageSendListener, InputCallback {

    private ViewHolder mViewHolder;

    //是否已经设置了输入框离底部的高度。这样做主要为了解决防止整个界面被推上去
    private boolean mHasSetEditTextMargin;

    private int mScreenWidth;

    private int mScreenHeight;

    //当前屏幕的高度，假设当前屏幕分辨率为1920（高）*1080(宽)。竖屏屏幕高度为1920，横屏屏幕高度为1080
    private int mCurrentHeight;

    private int mInputEmojHeight = 0;

    private int mBottomHeight = 0;

    private ArrayList<LiveCommentBean> mList = new ArrayList<>();

    private LiveCommentAdapter2 mAdapter;

    private boolean keyBoardIsShow;

    //不止键盘（包括表情,选气泡等）,输入功能是否在显示
    private boolean inputViewIsShow;

    private OnKeyBoardStateChangeListener onKeyBoardStateChangeListener;

    private boolean dialogIsShow;

    //是否已经点击显示输入框了
    private boolean mClickInputFrame = false;

    private EmojiFragment emojiFragment;
    private BubblePanelFragment bubblePanelFragment;
    private MedalFragment medalFragment;
    //当前选择的气泡颜色
    private String fontColor = BubbleColorEnum.NORMAL_BUBBLE.getFontColor();
    //当前选择的粉丝勋章
    private UserMedalBean currentFansMedalBean;

    private static Unbinder binder;

    public void setOnKeyBoardStateChangeListener(OnKeyBoardStateChangeListener onKeyBoardStateChangeListener) {
        this.onKeyBoardStateChangeListener = onKeyBoardStateChangeListener;
    }

    public interface OnKeyBoardStateChangeListener {
        void onKeyBoardStateChange(boolean keyBoardIsShow);
    }

    public InputCommentController(LiveActivity context) {
        super(context);
    }

    public void showMedalManager() {
        // context.getHttpController().getUserFansMedalQuery();
        hideKeyboard();
        mViewHolder.showEmojIv.setChecked(false);
        mViewHolder.functionContainer.setVisibility(View.VISIBLE);
        showMedalFragment();

    }

    public void getNewFansMedal(String userMedalDetailId) {
        mViewHolder.managerMedalIv.setShowRedPoint(true);
        SharePreferenceUtils.putData(GET_NEW_FANS_MEDAL_ID, userMedalDetailId);
    }

    public void setUserMedalData(UserFansMedalInfoEntity userFansMedalInfoEntity) {
        int awardNum = userFansMedalInfoEntity.getAwardNum();
        setMedalIv(awardNum, userFansMedalInfoEntity.getAdornedUserMedalDetail());
        if (mViewHolder.managerMedalIv.isShowRedPoint())
            mViewHolder.managerMedalIv.setShowRedPoint(false);

        String newMedalId = (String) SharePreferenceUtils.getData(GET_NEW_FANS_MEDAL_ID, null);

        userFansMedalInfoEntity.operatorData(context, newMedalId);

        // medalFragment.setUserMedalData(userFansMedalInfoEntity);

        SharePreferenceUtils.remove(GET_NEW_FANS_MEDAL_ID);
    }

    public void setUserCurrentMedalInfo(CurrentUserMedalDetail currentUserMedalDetail) {
        currentFansMedalBean = currentUserMedalDetail.getUserMedalDetail();
        setMedalIv(currentUserMedalDetail.getAwardNum(), currentFansMedalBean);
        //medalFragment.setCurrentMedalInfo(currentFansMedalBean);
    }

    private void setMedalIv(int awardNum, UserMedalBean userMedalBean) {
        if (awardNum == 0) {
            mViewHolder.managerMedalIv.setImageResource(R.drawable.icon_without_medal);
        } else if (userMedalBean == null) {
            mViewHolder.managerMedalIv.setImageResource(R.drawable.icon_not_wear_medal);
        } else {
            String resName = "icon_fans_medal_" + userMedalBean.getCurrentMedalLevelConfigCode();
            int resId = context.getResources().getIdentifier(resName.toLowerCase(), "drawable", context.getPackageName());
            String drawTxt = userMedalBean.getMedalLabelConfigList().size() > 0 ? userMedalBean.getMedalLabelConfigList().get(0).getContent() : "";
            Drawable d = new CustomMedalDrawable(drawTxt, resId, context.getResources());
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            mViewHolder.managerMedalIv.setImageDrawable(d);
        }
    }

    public void showComment() {
        mClickInputFrame = true;
        showKeyBoard();
        mViewHolder.outerCommentLayout.setVisibility(View.VISIBLE);
        if (!mHasSetEditTextMargin) {
            mViewHolder.innerCommentLayout.setVisibility(View.GONE);
        }
//        mViewHolder.showEmojIv.setImageResource(R.drawable.selector_emoj_toggle);
        //弹出输入框,即认为键盘弹起,即使在输入表情的时候键盘收下去(也认为键盘依旧在)直到输入框(表情以及键盘)消失
        inputViewIsShow = true;
        onKeyBoardStateChangeListener.onKeyBoardStateChange(inputViewIsShow);
    }

    public void setChatListShow(boolean dialogIsShow) {
        this.dialogIsShow = dialogIsShow;
        if (dialogIsShow) {
            mViewHolder.outerCommentLayout.setVisibility(View.VISIBLE);
            mViewHolder.innerCommentLayout.setVisibility(View.GONE);
            notifyData();
        } else {
            mViewHolder.outerCommentLayout.setVisibility(View.GONE);
            mViewHolder.innerCommentLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setBaseConfig(ChannelUserConfigBean channelUserConfigBean) {
        int chatWords = channelUserConfigBean.getChatWords();
        //mViewHolder.inputCommentEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(chatWords)});
        mViewHolder.inputCommentEt.setMaxLength(chatWords);
        //bubblePanelFragment.setBubbleData(channelUserConfigBean.getChannelBubbleConfigList());
    }

    public void changeOrientation(boolean isLandscape) {
        mClickInputFrame = false;
        mHasSetEditTextMargin = false;
        removeGlobeChangeListener();
        mViewHolder.functionContainer.setVisibility(View.GONE);
        mViewHolder.outerCommentLayout.setVisibility(View.GONE);
       /* mViewHolder.innerCommentLayout.setVisibility(View.GONE);
        mViewHolder.functionContainer.setVisibility(View.GONE);*/
        if (isLandscape) {
            addGlobeChangeListener();
            mCurrentHeight = mScreenWidth;
            setLandscapeView();
        } else {
            addGlobeChangeListener();
            mCurrentHeight = mScreenHeight;
            setPortraitView();
        }
    }

    @Override
    public void init(View view) {

        if (mViewHolder == null) {
            mViewHolder = new ViewHolder(view);
            Display defaultDisplay = context.getWindowManager().getDefaultDisplay();
            mScreenHeight = defaultDisplay.getHeight();
            mScreenWidth = defaultDisplay.getWidth();
            mCurrentHeight = mScreenHeight;
            setListener();
            changeOrientation(false);
            setListView();
            setFragment();
           /* mInputEmojHeight = (Integer) SharePreferenceUtils.getData(context, SOFT_KEYBOARD_HEIGHT, 0);
            if (mInputEmojHeight != 0) {
                //设置多功能面板高度
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mViewHolder.functionContainer.getLayoutParams();
                lp.height = mInputEmojHeight;
            }*/

            if (SharePreferenceUtils.getData(GET_NEW_FANS_MEDAL_ID, null) != null) {
                mViewHolder.managerMedalIv.setShowRedPoint(true);
            }
        }
    }

    private void setFragment() {
        emojiFragment = new EmojiFragment();
        emojiFragment.setCallback(this);
        //bubblePanelFragment = new BubblePanelFragment();
        //bubblePanelFragment.setCallback(this);
        // medalFragment = new MedalFragment();
        // medalFragment.setCallback(this);

        FragmentManager fm = context.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.functionContainer, emojiFragment);
        // ft.add(R.id.functionContainer, bubblePanelFragment);
        // ft.add(R.id.functionContainer, medalFragment);
        ft.commitAllowingStateLoss();
    }

    private void setListView() {
        mAdapter = new LiveCommentAdapter2(context, mList, 0, new LiveCommentMultiType());
        mAdapter.setHasBlackBubble(true);
        mViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mViewHolder.recyclerView.setAdapter(mAdapter);
        mViewHolder.recyclerView.setFocusable(false);
    }

    public void showMessage(LiveCommentBean bean) {
        if (bean.isShowFloatingLayer())
            return;
        //如果礼物同一组连击里面,大的连击消息先到了，那么小的消息就废弃掉不要显示
        if (bean.isRuleEffect() && mAdapter.judgeIsFilterSameGroupGift(bean)) return;
        if (mList.size() >= 100) {
            mList.remove(0);
            mAdapter.notifyItemRemoved(0);
        }
        mList.add(bean);
        //只在竖屏键盘弹出的时候刷新适配器
        if (!context.isLandscape() && keyBoardIsShow || dialogIsShow || functionIsShow()) {
            mAdapter.notifyItemInserted(mAdapter.getItemCount());
            mViewHolder.recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    private boolean functionIsShow() {
        return mViewHolder.functionContainer.getVisibility() == View.VISIBLE;
    }

    private void notifyData() {
        if (mList == null || mList.size() == 0) {
            return;
        }
        mAdapter.notifyDataSetChanged();
        mViewHolder.recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void setListener() {
        mViewHolder.showBubbleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                mViewHolder.showEmojIv.setChecked(false);
                mViewHolder.functionContainer.setVisibility(View.VISIBLE);
                bubblePanelFragment.setCurrentBubble(fontColor);
                showPanelFragment();
            }
        });

        mViewHolder.showEmojIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewHolder.showEmojIv.isChecked()) {
                    hideKeyboard();
                    mViewHolder.functionContainer.setVisibility(View.VISIBLE);
                    showEmojiFragment();
                } else {
                    showComment();
                }
            }
        });

        mViewHolder.recyclerView.setOnClickListener(new MyClickRecyclerView.OnClickListener() {
            @Override
            public void onClickListener() {
                hideKeyboard();
                closeInputLayout();
                mViewHolder.showEmojIv.setChecked(false);
                //如果只是切换表情,这时候可以看做键盘依旧弹出(礼物等效果显示与键盘弹出一致)
                inputViewIsShow = false;
                onKeyBoardStateChangeListener.onKeyBoardStateChange(inputViewIsShow);
            }
        });

        mViewHolder.outerCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                closeInputLayout();
                mViewHolder.showEmojIv.setChecked(false);
                //如果只是切换表情,这时候可以看做键盘依旧弹出(礼物等效果显示与键盘弹出一致)
                inputViewIsShow = false;
                onKeyBoardStateChangeListener.onKeyBoardStateChange(inputViewIsShow);
            }
        });

        mViewHolder.sendCommentIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mViewHolder.inputCommentEt.getText().toString())) {
                    return;
                }
                LogUtils.e(TAG, " ===" + mViewHolder.inputCommentEt.getText().toString());
                context.getHttpController().groupMessageSend(mViewHolder.inputCommentEt.getText().toString(), fontColor);
            }
        });

        mViewHolder.inputCommentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //返回true不会软键盘
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (TextUtils.isEmpty(mViewHolder.inputCommentEt.getText().toString())) {
                        return true;
                    }
                    context.getHttpController().groupMessageSend(mViewHolder.inputCommentEt.getText().toString(), fontColor);
                    return true;
                }
                return false;
            }
        });

        mViewHolder.managerMedalIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMedalManager();
            }
        });
    }

    private void showEmojiFragment() {
        setPanelHeight();
        FragmentManager fm = context.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //ft.hide(bubblePanelFragment);
        // ft.hide(medalFragment);
        ft.show(emojiFragment);
        ft.commit();
    }

    private void showPanelFragment() {
        setPanelHeight();
        FragmentManager fm = context.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(emojiFragment);
        //ft.hide(medalFragment);
        //ft.show(bubblePanelFragment);
        ft.commit();
    }

    private void showMedalFragment() {
        setPanelHeight();
        FragmentManager fm = context.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(emojiFragment);
        //ft.hide(bubblePanelFragment);
        //ft.show(medalFragment);
        ft.commit();
    }


    public void setPanelHeight() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mViewHolder.functionContainer.getLayoutParams();
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = mInputEmojHeight;
    }

    private void hideKeyboard() {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
        notifyData();
    }

    @Override
    void setView() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onKeyBoardStateChangeListener = null;
        mViewHolder = null;
        globeChangeListener = null;
        binder.unbind();
    }

    private void setPortraitView() {
        mViewHolder.showEmojIv.setVisibility(View.VISIBLE);
        mViewHolder.recyclerView.setVisibility(View.VISIBLE);
        // mViewHolder.showBubbleIv.setVisibility(View.VISIBLE);
        //mViewHolder.managerMedalIv.setVisibility(View.VISIBLE);
        // mViewHolder.lineView.setVisibility(View.VISIBLE);
        mViewHolder.innerCommentLayout.setBackgroundColor(Color.WHITE);
        mViewHolder.inputBgLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_white_solid_gray_stroke_radius));
        mViewHolder.inputCommentEt.setBackgroundColor(Color.WHITE);
        mViewHolder.sendCommentIv.setImageResource(R.drawable.icon_send_comment_portrait);
        mViewHolder.inputCommentEt.setTextColor(Color.BLACK);
        mViewHolder.inputCommentEt.setHintTextColor(Color.GRAY);
    }

    private void setLandscapeView() {
        mViewHolder.showEmojIv.setVisibility(View.GONE);
        mViewHolder.recyclerView.setVisibility(View.GONE);
        //mViewHolder.showBubbleIv.setVisibility(View.GONE);
        //mViewHolder.managerMedalIv.setVisibility(View.GONE);
        // mViewHolder.lineView.setVisibility(View.GONE);
        mViewHolder.innerCommentLayout.setBackgroundResource(R.color.alphBalck);
        mViewHolder.inputBgLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_black_alpha_solid_white_stroke));
        mViewHolder.inputCommentEt.setBackgroundColor(Color.TRANSPARENT);
        mViewHolder.sendCommentIv.setImageResource(R.drawable.selector_send_comment_landscape);
        mViewHolder.inputCommentEt.setTextColor(Color.WHITE);
        mViewHolder.inputCommentEt.setHintTextColor(Color.WHITE);
    }

    @Override
    public void onEmojAdd(EmojBean emojBean) {
        mViewHolder.inputCommentEt.insertEmoj(emojBean);
    }

    @Override
    public void onEmojDelete() {
        mViewHolder.inputCommentEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }

    @Override
    public void onBubbleSelected(String fontColor) {
        this.fontColor = fontColor;
    }

    @Override
    public void onFansMedalSelected(UserMedalBean userMedalBean) {
    }

    private void addGlobeChangeListener() {
        context.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(globeChangeListener);
    }

    ViewTreeObserver.OnGlobalLayoutListener globeChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            final Rect r = new Rect();
            context.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            /*LogUtils.e(TAG, "r.bottom=" + r.bottom +
                    "    mCurrentHeight=" + mCurrentHeight +
                    "    mHasSetEditTextMargin=" + mHasSetEditTextMargin);*/
            /* 还没有设置过输入框高度，且当前弹出了输入法。没有弹出输入法时r.bottom==mCurrentHeight。
            经过测试，红米note2上的MIUI8 7.4.13开发版 Android5.0.2。如果第一次弹出输入法之后，再横竖屏切换也会出现r.bottom!=mCurrentHeight。
            导致这边计算错误。之后再进行切换又正常了。*/
            if (r.bottom != mCurrentHeight && !mHasSetEditTextMargin && mClickInputFrame) {
                mHasSetEditTextMargin = true;
                mClickInputFrame = false;
                //先判断之前保存的键盘高度是否发生变化(用户可能切换了输入法),如果变化了,则再保存最新的键盘高度
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mViewHolder.innerCommentLayout.getLayoutParams();
                layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
                layoutParams.bottomMargin = mCurrentHeight - r.bottom;
//                    mViewHolder.functionContainer.setVisibility(View.INVISIBLE);
                if (!context.isLandscape()) {
                    SharePreferenceUtils.putData(SOFT_KEYBOARD_HEIGHT, mInputEmojHeight);
                    mInputEmojHeight = mCurrentHeight - r.bottom;
                }
                mViewHolder.innerCommentLayout.setVisibility(View.VISIBLE);
            }

            mViewHolder.inputCommentEt.requestFocus();
            if (r.bottom < mBottomHeight) {
                keyBoardIsShow = true;
            } else if (r.bottom > mBottomHeight) {
                keyBoardIsShow = false;
                //如果只是切换表情,这时候可以看做键盘依旧弹出(礼物等效果显示与键盘弹出一致)
                /*if (keyBoardIsShow && !emojIsShow()) {
                    keyBoardIsShow = false;
                    onKeyBoardStateChangeListener.onKeyBoardStateChange(keyBoardIsShow);
                }*/
                if (!functionIsShow()) {
                    inputViewIsShow = false;
                    onKeyBoardStateChangeListener.onKeyBoardStateChange(inputViewIsShow);
                }
            }
            if (r.bottom > mBottomHeight && mBottomHeight != 0 && !functionIsShow()) {
                closeInputLayout();
            }
            if (r.bottom < mBottomHeight && functionIsShow()) {
                mViewHolder.functionContainer.setVisibility(View.GONE);
                mViewHolder.showEmojIv.setChecked(false);
            }
            mBottomHeight = r.bottom;
        }
    };

    public void closeInputLayout() {
        mViewHolder.outerCommentLayout.setVisibility(View.GONE);
        mViewHolder.functionContainer.setVisibility(View.GONE);
    }

    private void removeGlobeChangeListener() {
        ViewTreeObserver viewTreeObserver = context.getWindow().getDecorView().getViewTreeObserver();
        if (Build.VERSION.SDK_INT < 16) {
            removeLayoutListenerPre16(viewTreeObserver, globeChangeListener);
        } else {
            removeLayoutListenerPost16(viewTreeObserver, globeChangeListener);
        }
    }

    @SuppressWarnings("deprecation")
    private void removeLayoutListenerPre16(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeGlobalOnLayoutListener(listener);
    }

    @TargetApi(16)
    private void removeLayoutListenerPost16(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeOnGlobalLayoutListener(listener);
    }

    private LiveCommentBean createMessage(List<String> roleCodesList,
                                          List<String> fontColorList,
                                          String userVotedContent,
                                          List<MedalInfoBean> medalInfoBeanList) {
        LiveCommentBean liveCommentBean = new LiveCommentBean();
        LiveCommentBean.UserPropertiesBean propertiesBean = new LiveCommentBean.UserPropertiesBean();
       /* String medalName = "";
        String levelCode = "";
        if (currentFansMedalBean != null && currentFansMedalBean.isAdorned()) {
            medalName = currentFansMedalBean.getMedalLabelConfigList().size() > 0 ?
                    currentFansMedalBean.getMedalLabelConfigList().get(0).getContent() : "";
            levelCode = currentFansMedalBean.getCurrentMedalLevelConfigCode();
        }*/
        propertiesBean.setUserVotedContent(userVotedContent);
        propertiesBean.setMedalInfoBeanList(medalInfoBeanList);
        propertiesBean.setHorseList(roleCodesList);
        liveCommentBean.setProperties(propertiesBean);
        liveCommentBean.setSenderName(LoginInfoHelper.getInstance().getNickName());
        liveCommentBean.setSenderId(LoginInfoHelper.getInstance().getUserId());
        LiveCommentBean.ContentBean contentBean = new LiveCommentBean.ContentBean();
        liveCommentBean.setType(LiveCommentMessageTypeEnum.TEXT.name());
        contentBean.setText(mViewHolder.inputCommentEt.getText().toString());
        contentBean.setFontColor(fontColor);
        liveCommentBean.setContent(contentBean);
        return liveCommentBean;
    }

    @Override
    public void onMessageSendSuccess(List<String> roleCodesList, List<String> fontColorList, String userVotedContent, List<MedalInfoBean> medalInfoBeanList) {
        context.getPushMessageController().addMeMessage(createMessage(roleCodesList, fontColorList, userVotedContent, medalInfoBeanList));
        mViewHolder.inputCommentEt.setText("");
    }

    @Override
    public void onMessageSendFailed() {

    }


    static class ViewHolder {
        @BindView(R.id.inputCommentEt)
        EmojiEditTextView inputCommentEt;
        @BindView(R.id.showEmojIv)
        CheckBox showEmojIv;
        @BindView(R.id.innerCommentLayout)
        LinearLayout innerCommentLayout;
        @BindView(R.id.outerCommentLayout)
        FrameLayout outerCommentLayout;
        @BindView(R.id.sendCommentIv)
        ImageView sendCommentIv;
        @BindView(R.id.inputBgLayout)
        LinearLayout inputBgLayout;
        @BindView(R.id.recyclerView)
        MyClickRecyclerView recyclerView;
        @BindView(R.id.functionContainer)
        FrameLayout functionContainer;
        @BindView(R.id.showBubbleIv)
        ImageView showBubbleIv;
        @BindView(R.id.managerMedalIv)
        ImageViewWithRedPoint managerMedalIv;
        @BindView(R.id.lineView)
        View lineView;

        ViewHolder(View view) {
            binder = ButterKnife.bind(this, view);
        }
    }
}
