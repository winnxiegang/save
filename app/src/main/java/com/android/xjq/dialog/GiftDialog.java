package com.android.xjq.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.coupon.BasePopupWindow;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.CommonStatusLayout;
import com.android.banana.commlib.view.MyTabLayout;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.RechargeActivity;
import com.android.xjq.adapter.main.GiftAdapter;
import com.android.xjq.adapter.main.PackageAdapter;
import com.android.xjq.bean.TreasureList;
import com.android.xjq.bean.TreasureOpenListBean;
import com.android.xjq.bean.live.main.gift.GiftConfigInfo;
import com.android.xjq.bean.live.main.gift.GiftCountInfo;
import com.android.xjq.bean.live.main.gift.GiftInfoBean;
import com.android.xjq.bean.live.main.gift.SendGiftBean;
import com.android.xjq.bean.live.main.gift.SendGiftResultBean;
import com.android.xjq.dialog.base.DialogBase;
import com.android.xjq.dialog.live.LiveGiftSupplementDialog;
import com.android.xjq.dialog.popupwindow.GiftNumSelectPop;
import com.android.xjq.dialog.popupwindow.LookWalletPop;
import com.android.xjq.dialog.popupwindow.NumKeyboardPop;
import com.android.xjq.liveanim.TreasureBoxOpenDialog2;
import com.android.xjq.model.gift.GiftCountEnum;
import com.android.xjq.model.gift.PayType;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.model.gift.PayType.GIFT_COIN;
import static com.android.xjq.model.gift.PayType.POINT_COIN;
import static com.android.xjq.utils.XjqUrlEnum.GIFT_GIVE;
import static com.android.xjq.utils.XjqUrlEnum.GIFT_PURCHASE;
import static com.android.xjq.utils.XjqUrlEnum.GIFT_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.GIFT_QUERY_BY_CHANNEL_AREA_ID;
import static com.android.xjq.utils.XjqUrlEnum.GIFT_QUERY_BY_GIFT_CONFIG_ID;

/**
 * Created by lingjiu on 2017/3/7.
 */

public class GiftDialog extends DialogBase implements OnHttpResponseListener {

    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.payTypeRg)
    RadioGroup payTypeRg;
    @BindView(R.id.stampPayRb)
    RadioButton stampPayRb;
    @BindView(R.id.bananaPayRb)
    RadioButton bananaPayRb;
    @BindView(R.id.giftNumTv)
    TextView giftNumTv;
    @BindView(R.id.giftTypeTv)
    TextView giftTypeTv;
    @BindView(R.id.giftNumLayout)
    RelativeLayout giftNumLayout;
    @BindView(R.id.sendGiftBtn)
    Button sendGiftBtn;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id.walletCb)
    CheckBox walletCb;
    @BindView(R.id.rechargeCb)
    CheckBox rechargeCb;
    @BindView(R.id.statusLayout)
    CommonStatusLayout statusLayout;
    @BindView(R.id.empty_content_img)
    ImageView emptyImg;
    @BindView(R.id.giftTypeTab)
    MyTabLayout giftTypeTab;

    private static final int THE_GIFT_TYPE = 0;
    private static final int THE_PLAY_TYPE = 1;
    private static final int THE_PACKAGE_TYPE = 2;

    public @interface UserPackageItemType {
        //道具
        String GIFT = "GIFT";
        //宝箱
        String TREASURE_CHEST = "TREASURE_CHEST";
    }

    //当前包裹类型
    private String currentPackageType = UserPackageItemType.TREASURE_CHEST;
    //当前礼物类型
    private int currentGiftType;
    private GiftNumSelectPop pop;
    //当前的支付类型
    private PayType currentPayType = GIFT_COIN;
    private String channelId;
    private HttpRequestHelper httpRequestHelper;
    private GiftAdapter giftAdapter;
    private GiftAdapter playAdapter;
    private PackageAdapter packageAdapter;
    //礼物列表数据
    private List<GiftInfoBean> giftInfoList = new ArrayList<>();
    //玩法列表数据
    private List<GiftInfoBean> playInfoList = new ArrayList<>();
    //包裹列表数据
    private List<TreasureList.Treasure> packageInfoList = new ArrayList<>();
    //当前选中的礼物数量
    private GiftCountInfo giftCountInfo;
    private List<GiftCountInfo> giftCountInfoList = new ArrayList<>();
    private List<GiftCountInfo> packageCountList;
    //礼物被选中的礼物
    private GiftInfoBean giftInfoBean;
    //玩法被选中的礼物
    private GiftInfoBean playInfoBean;
    //包裹被选中的礼物
    private TreasureList.Treasure treasureInfoBean;
    //礼物个数配置信息
    private GiftConfigInfo configInfo;
    private long giftGiveMaxNum;
    //批量赠送礼物
    private List<SendGiftBean> sendGiftList = new ArrayList<>();
    private Handler sendGiftHandler = new Handler();
    private LookWalletPop lookWalletPop;

    private BasePopupWindow mBroadCastPopupWindow;

    public interface OnSendGiftListener {
        void sendGiftSuccess(SendGiftResultBean.ResultListBean resultListBean);
    }

    private OnSendGiftListener sendGiftListener;

    public void setSendGiftListener(OnSendGiftListener sendGiftListener) {
        this.sendGiftListener = sendGiftListener;
    }

    @Override
    public void dismiss() {
        if (sendGiftHandler != null) {
            sendGiftHandler.removeCallbacks(sendGiftRunnable);
        }
        super.dismiss();
    }

    private Runnable sendGiftRunnable = new Runnable() {
        @Override
        public void run() {
            XjqRequestContainer map = new XjqRequestContainer(GIFT_PURCHASE, true);
            map.put("platformObjectId", channelId);
            map.put("receiveUserId", CurLiveInfo.getAnchorUserId());
            map.put("giftPurchaseParams", new Gson().toJson(sendGiftList));
            httpRequestHelper.startRequest(map, false);
            sendGiftList.clear();
        }
    };

    @OnClick({R.id.sendGiftBtn, R.id.rechargeCb, R.id.walletCb, R.id.giftNumLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendGiftBtn:
                sendGift();
                break;
            case R.id.rechargeCb:
                recharge();
                break;
            case R.id.walletCb:
                checkWallet();
                break;
            case R.id.giftNumLayout:
                selectGiftNum();
                break;
        }
    }

    private void sendGift() {
        if (currentGiftType == THE_GIFT_TYPE) {
            sendNormalGift();
            //礼物动画
            ((LiveActivity) context).getGiftController().addSendGiftSuccessShow(giftInfoBean.getGiftImageUrl(), (int) giftCountInfo.getNum());
        } else if (currentGiftType == THE_PLAY_TYPE) {
            dismiss();
            showGiftSupplementDialog();
        } else if (currentGiftType == THE_PACKAGE_TYPE) {
            if (currentPackageType.equals(UserPackageItemType.TREASURE_CHEST)) {
                openTreasureBox();
            } else {
                sendPackage();
            }
        }
        if (mBroadCastPopupWindow != null && mBroadCastPopupWindow.isShowing())
            mBroadCastPopupWindow.dismiss();
    }

    private void openTreasureBox() {
        if (treasureInfoBean.currentTotalCount < giftCountInfo.getNum()) {
            giftCountInfo.setNum((long) treasureInfoBean.currentTotalCount);
            setGiftCount(giftCountInfo);
            ToastUtil.showShort(context.getApplicationContext(), "超过当前可开包裹数量");
            return;
        }

        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.USER_PACKAGE_OPEN, true);
        map.put("subTypeId", treasureInfoBean.subTypeId);
        map.put("number", giftCountInfo.getNum());
        httpRequestHelper.startRequest(map, false);
    }

    private void showTreasureOpenAnimDialog(ArrayList<TreasureOpenListBean.treasureOpenBean> treasureOpenList, TreasureList.Treasure treasure) {
        TreasureBoxOpenDialog2 openDialog2 = TreasureBoxOpenDialog2.newInstance(treasure.title, treasure.subTypeCode, treasureOpenList);
        openDialog2.setOnDismissListener(new TreasureBoxOpenDialog2.OnDismissListener() {
            @Override
            public void onDismiss() {
                reqPlayGiftListData();
            }
        });
        openDialog2.show(((BaseActivity) context).getSupportFragmentManager());
    }

    private void sendPackage() {
        XjqRequestContainer map = new XjqRequestContainer(GIFT_GIVE, true);
        map.put("channelAreaId", channelId);
        map.put("userGiftId", treasureInfoBean.id);
        map.put("totalCount", giftCountInfo.getNum());
        httpRequestHelper.addRequest(map);
    }

    private void sendNormalGift() {
        if (giftInfoBean == null || giftCountInfo == null) return;
        if (giftCountInfo == null) return;
        if (sendGiftList.size() == 0) {
            sendGiftHandler.postDelayed(sendGiftRunnable, (long) (CurLiveInfo.getGiftPurchaseInterval() * 1000));
        }
        SendGiftBean sendGiftBean = new SendGiftBean(giftInfoBean.getId(), giftCountInfo.getNum(), currentPayType.name(),
                giftInfoBean.getPrice() * giftCountInfo.getNum() + "");
        sendGiftList.add(sendGiftBean);
    }

    private void recharge() {
        RechargeActivity.startRechargeActivity((Activity) context);
        dismiss();
    }

    private void checkWallet() {
        httpRequestHelper.startRequest(getAccountInfoAndGiftInfo(), false);
        lookWalletPop = new LookWalletPop(context, walletCb);
        lookWalletPop.showUp();
    }

    private void selectGiftNum() {
        if (currentGiftType == THE_PLAY_TYPE) return;
        if (pop == null) {
            pop = new GiftNumSelectPop(context, giftNumLayout, giftNumSelectListener);
        }
        pop.setNumKeyBoardListener(numKeyBoardListener);
        List<GiftCountInfo> countInfos = currentGiftType == THE_PACKAGE_TYPE ? packageCountList : giftCountInfoList;
        pop.showUp(currentGiftType, countInfos, giftCountInfo);
    }

    public GiftDialog(Context context, int orientation, String channelId) {
        super(context, R.layout.dialog_gift, R.style.dialog_base, orientation);
        setDimAmount(0.0f);
        this.channelId = channelId;
        ButterKnife.bind(this);
        httpRequestHelper = new HttpRequestHelper(context, this);
        initView();

    }

    //请求礼物数据
    private void reqGiftListData() {
        httpRequestHelper.startRequest(getGiftInfo(), false);
    }

    //请求玩法礼物数据
    private void reqPlayGiftListData() {
        statusLayout.showLoading();
        RequestContainer map = null;
        if (currentGiftType == THE_GIFT_TYPE) {
            giftInfoList.clear();
            giftAdapter.notifyDataSetChanged();
            map = getGiftInfo();
        } else if (currentGiftType == THE_PLAY_TYPE) {
            playInfoList.clear();
            playAdapter.notifyDataSetChanged();
            map = getPlayTypeGiftInfo();
        } else if (currentGiftType == THE_PACKAGE_TYPE) {
            packageInfoList.clear();
            packageAdapter.notifyDataSetChanged();
            map = getPackageTypeInfo();
        }
        httpRequestHelper.startRequest(map, false);
    }

    private RequestContainer getPackageTypeInfo() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.MY_PACKAGE_QUERY, true);
        map.put("type", currentPackageType);
        return map;
    }


    private RequestContainer getGiftCountInfo(String id) {
        XjqRequestContainer map = new XjqRequestContainer(GIFT_QUERY_BY_GIFT_CONFIG_ID, true);
        map.put("giftConfigId", id);
        return map;
    }

    private RequestContainer getGiftInfo() {
        XjqRequestContainer map = new XjqRequestContainer(GIFT_QUERY_BY_CHANNEL_AREA_ID, true);
        map.put("payType", currentPayType.name());
        map.put("channelAreaId", channelId);
        return map;
    }

    private RequestContainer getPlayTypeGiftInfo() {
        XjqRequestContainer map = new XjqRequestContainer(GIFT_QUERY, true);
        map.put("payType", currentPayType.name());
        map.put("giftType", "PLAY");
        return map;
    }

    private XjqRequestContainer getAccountInfoAndGiftInfo() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.MY_FUND_ACCOUNT_QUERY, true);
        return map;
    }

    /**
     * 礼物数量选择回调
     */
    private GiftNumSelectPop.GiftNumSelectListener giftNumSelectListener = new GiftNumSelectPop.GiftNumSelectListener() {
        @Override
        public void giftNumSelect(int position, GiftCountInfo countInfo, boolean isAll) {
            if (isAll) countInfo.setNum((long) treasureInfoBean.currentTotalCount);
            setGiftCount(countInfo);
        }
    };

    /**
     * 数字键盘选择回调
     */
    private NumKeyboardPop.NumKeyboardSelectListener numKeyBoardListener = new NumKeyboardPop.NumKeyboardSelectListener() {
        @Override
        public void numKeyBoardSelectListener(int num) {
            long currentNum = 0;
            if (num == -1) {
                //删除
                String numStr = String.valueOf(giftCountInfo.getNum());
                if (numStr != null && numStr.length() > 1) {
                    numStr = numStr.substring(0, numStr.length() - 1);
                } else if (numStr != null && numStr.length() == 1) {
                    numStr = "0";
                }
                currentNum = Integer.valueOf(numStr);
            } else {
                currentNum = Long.valueOf("" + giftCountInfo.getNum() + num);
            }
            if (currentGiftType == THE_PACKAGE_TYPE) {
                giftCountInfo.setNum(currentNum);
                setGiftCount(giftCountInfo);
            } else {
                setGiftCount(configInfo.judgeDataRange(currentNum));
            }
        }

        @Override
        public void dismissListener() {
            if (giftCountInfo.getNum() == 0) {
//                giftCountInfo.setNum(1);
                setGiftCount(configInfo.judgeDataRange(1));
            }
        }
    };

    private void setGiftCount(GiftCountInfo countInfo) {
        giftCountInfo = countInfo;
        giftNumTv.setText(String.valueOf(giftCountInfo.getNum()));
        if (TextUtils.isEmpty(giftCountInfo.getDesc())) {
            giftTypeTv.setVisibility(View.GONE);
        } else {
            giftTypeTv.setVisibility(View.VISIBLE);
            giftTypeTv.setText(giftCountInfo.getDesc());
        }
    }

    private CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                switch (buttonView.getId()) {
                    case R.id.stampPayRb:
                        currentPayType = GIFT_COIN;
                        currentPackageType = UserPackageItemType.TREASURE_CHEST;
                        if (currentGiftType == THE_PACKAGE_TYPE)
                            sendGiftBtn.setText("打开");
                        break;
                    case R.id.bananaPayRb:
                        currentPayType = POINT_COIN;
                        currentPackageType = UserPackageItemType.GIFT;
                        if (currentGiftType == THE_PACKAGE_TYPE)
                            sendGiftBtn.setText("赠送");
                        break;
                }
                reqPlayGiftListData();
            }
        }
    };


    private void initView() {
        GiftCountEnum[] values = GiftCountEnum.values();
        packageCountList = new ArrayList<>();
        for (GiftCountEnum giftCountEnum : values) {
            GiftCountInfo giftCountInfo = new GiftCountInfo(giftCountEnum.getValue(), giftCountEnum.getMessage());
            packageCountList.add(giftCountInfo);
        }
        giftAdapter = new GiftAdapter(context, giftInfoList);
        playAdapter = new GiftAdapter(context, playInfoList);
        packageAdapter = new PackageAdapter(context, packageInfoList);
        giftAdapter.setOnItemClickListener(new GiftAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                giftInfoBean = giftInfoList.get(position);
                giftAdapter.itemUpdate(gridView, giftInfoBean.getId());
                httpRequestHelper.startRequest(getGiftCountInfo(String.valueOf(giftInfoList.get(position).getId())), false);
            }
        });
        playAdapter.setOnItemClickListener(new GiftAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                playInfoBean = playInfoList.get(position);
                playAdapter.itemUpdate(gridView, playInfoBean.getId());
                showPromptGift(position);
            }
        });
        packageAdapter.setOnItemClickListener(new PackageAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                treasureInfoBean = packageInfoList.get(position);
                packageAdapter.itemUpdate(gridView, position);
            }
        });

        stampPayRb.setOnCheckedChangeListener(checkedListener);
        bananaPayRb.setOnCheckedChangeListener(checkedListener);
        int tabMargin = isLandScape ? 5 : 25;
        giftTypeTab.addTabs(context.getString(R.string.gift_text), context.getString(R.string.play_type_text), context.getString(R.string.package_text))
                .setTabMargin(tabMargin)
                .setBottomLineColor(ContextCompat.getColor(context, R.color.line_color))
                .setTabSelectedListener(new MyTabLayout.TabSelectedListener() {
                    @Override
                    public void onTabSelected(MyTabLayout.Tab tab, boolean reSelected) {
                        switch (tab.getPosition()) {
                            case THE_GIFT_TYPE:
                                currentGiftType = THE_GIFT_TYPE;
                                sendGiftBtn.setText("赠送");
                                gridView.setAdapter(giftAdapter);
                                payTypeRg.setVisibility(View.VISIBLE);
                                stampPayRb.setText(context.getString(R.string.gift_gold));
                                bananaPayRb.setText(context.getString(R.string.banana_coin));
                                sendGiftBtn.setEnabled(true);
                                if (giftCountInfo != null) giftCountInfo.reset();
                                reqPlayGiftListData();
                                break;
                            case THE_PLAY_TYPE:
                                currentGiftType = THE_PLAY_TYPE;
                                gridView.setAdapter(playAdapter);
                                sendGiftBtn.setText("赠送");
                                payTypeRg.setVisibility(View.VISIBLE);
                                GiftDialog.this.giftCountInfo = GiftDialog.this.giftCountInfo == null ?
                                        new GiftCountInfo(1, null) : GiftDialog.this.giftCountInfo.reset();
                                setGiftCount(GiftDialog.this.giftCountInfo);
                                playAdapter.setCheckPosition(-1, -1);
                                reqPlayGiftListData();
                                stampPayRb.setText(context.getString(R.string.gift_gold));
                                bananaPayRb.setText(context.getString(R.string.banana_coin));
                                sendGiftBtn.setEnabled(false);
                                break;
                            case THE_PACKAGE_TYPE:
                                currentGiftType = THE_PACKAGE_TYPE;
                                gridView.setAdapter(packageAdapter);
                                reqPlayGiftListData();
                                if (currentPackageType == UserPackageItemType.TREASURE_CHEST)
                                    sendGiftBtn.setText("打开");
                                GiftDialog.this.giftCountInfo = GiftDialog.this.giftCountInfo == null ?
                                        new GiftCountInfo(1, null) : GiftDialog.this.giftCountInfo.reset();
                                setGiftCount(GiftDialog.this.giftCountInfo);
                                payTypeRg.setVisibility(View.VISIBLE);
                                stampPayRb.setText(context.getString(R.string.the_treasure));
                                bananaPayRb.setText(context.getString(R.string.the_props));
                                sendGiftBtn.setEnabled(true);
                                break;
                        }
                    }
                }).setSelectTab(0);
    }

    private void showPromptGift(int position) {
        View child = gridView.getChildAt(position);
        mBroadCastPopupWindow = new BasePopupWindow(context, -1, -1);
        int dialogHeight = getWindow().getAttributes().height;
        int height = (int) (dialogHeight - DimensionUtils.dpToPx(45, context));
        mBroadCastPopupWindow.setHeight(height);
        mBroadCastPopupWindow.setContentView(View.inflate(context, R.layout.layout_prompt_gift_annotation, null));
        mBroadCastPopupWindow.setFocusable(false);
        mBroadCastPopupWindow.setOutsideTouchable(false);
        final View contentView = mBroadCastPopupWindow.getContentView();
        View indicateIv = contentView.findViewById(R.id.indicateIv);
        View memoView = contentView.findViewById(R.id.memoLayout);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) memoView.getLayoutParams();
        int[] location = new int[2];
        child.getLocationInWindow(location);
        int x = location[0];
        int y = location[1];
        //根据礼物的位置控制弹层弹出箭头的左右位置
        params.topMargin = y - child.getMeasuredHeight() / 2;
        LogUtils.e("showPromptGift", "y = " + y + "  child.getMeasuredHeight()= " + child.getMeasuredHeight() + "  screenHeight=" + dialogHeight);
        if (x > DimensionUtils.getScreenWidth(context) / 2) {
            LinearLayout.LayoutParams indicateParams = (LinearLayout.LayoutParams) indicateIv.getLayoutParams();
            indicateParams.gravity = Gravity.RIGHT;
            indicateParams.rightMargin = child.getMeasuredWidth() / 2;
            params.gravity = Gravity.RIGHT;
            params.rightMargin = x;
        } else {
            params.gravity = Gravity.LEFT;
            params.leftMargin = x;
        }
        memoView.setLayoutParams(params);
        mBroadCastPopupWindow.showAsDropDown(walletCb, 0, (int) (-DimensionUtils.dpToPx(45, context) - height));
        memoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBroadCastPopupWindow.dismiss();
                dismiss();
                showGiftSupplementDialog();
            }
        });
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBroadCastPopupWindow.dismiss();
            }
        });
    }

    private void showGiftSupplementDialog() {
        LiveGiftSupplementDialog dialog = new LiveGiftSupplementDialog(context, playInfoBean, currentPayType.name(), ((LiveActivity) context).isLandscape());
        dialog.show();
    }

    private void responseSuccessAccountQuery(JSONObject jsonObject) throws JSONException {
        double goldCoinAmount = 0;
        if (jsonObject.has("goldCoinAmount")) {
            goldCoinAmount = jsonObject.getDouble("goldCoinAmount");
        }
        double pointCoinAmount = 0;
        if (jsonObject.has("pointCoinAmount")) {
            pointCoinAmount = jsonObject.getDouble("pointCoinAmount");
        }
        double giftCoinAmount = 0;
        if (jsonObject.has("giftCoinAmount")) {
            giftCoinAmount = jsonObject.getDouble("giftCoinAmount");
        }

        if (lookWalletPop != null) {
            lookWalletPop.setGoldNum(goldCoinAmount, giftCoinAmount, pointCoinAmount);
        }
    }

    private void responseSuccessGiftQueryByChannelId(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("giftGiveMaxNum")) {
            giftGiveMaxNum = jsonObject.getLong("giftGiveMaxNum");
        }
        if (jsonObject.has("giftList")) {
            statusLayout.hideStatusView();
            walletCb.setEnabled(true);
            JSONArray giftList = jsonObject.getJSONArray("giftList");
            List<GiftInfoBean> giftInfoBeanList = new Gson().fromJson(giftList.toString(), new TypeToken<List<GiftInfoBean>>() {
            }.getType());
            giftAdapter.setCurrentPayType(currentPayType);
            if (giftInfoBeanList != null && giftInfoBeanList.size() > 0) {
                giftInfoList.clear();
                giftInfoList.addAll(giftInfoBeanList);
                giftInfoBean = giftInfoBeanList.get(0);
                giftAdapter.notifyDataSetChanged();
                if (currentGiftType == THE_GIFT_TYPE)
                    httpRequestHelper.startRequest(getGiftCountInfo(String.valueOf(giftInfoBean.getId())), false);
                sendGiftBtn.setEnabled(true);
                giftNumLayout.setEnabled(true);
                gridView.setVisibility(View.VISIBLE);
                emptyImg.setVisibility(View.GONE);
                emptyImg.setBackground(getContext().getResources().getDrawable(R.drawable.gift_empty_content));
            } else {
                sendGiftBtn.setEnabled(false);
                giftNumLayout.setEnabled(false);
                gridView.setVisibility(View.GONE);
                emptyImg.setVisibility(View.VISIBLE);
            }
        }
    }

    private void responseSuccessGiftCount(JSONObject jsonObject) {
        configInfo = new Gson().fromJson(jsonObject.toString(), GiftConfigInfo.class);
        configInfo.operatorData(giftCountInfoList, giftGiveMaxNum);
        giftInfoBean.setDynamicUrl(configInfo.getGiftImageUrl());
        giftAdapter.itemUpdate(gridView, giftInfoBean.getId());
        setGiftCount(configInfo.judgeDataRange(1));
    }

    private void responseSuccessPackageInfo(JSONObject jsonObject) throws JSONException {
        packageInfoList.clear();
        statusLayout.hideStatusView();
        walletCb.setEnabled(true);
        TreasureList treasureList = new Gson().fromJson(jsonObject.toString(), TreasureList.class);
        ArrayList<TreasureList.Treasure> treasureChestList = currentPackageType == UserPackageItemType.GIFT ?
                treasureList.userGiftList : treasureList.treasureChestList;
        if (treasureChestList != null && treasureChestList.size() > 0) {
            treasureInfoBean = treasureChestList.get(0);
            packageAdapter.setCheckPosition(0, 0);
            packageInfoList.addAll(treasureChestList);
            gridView.setVisibility(View.VISIBLE);
            emptyImg.setVisibility(View.GONE);
            sendGiftBtn.setEnabled(true);
            giftNumLayout.setEnabled(true);
        } else {
            sendGiftBtn.setEnabled(false);
            giftNumLayout.setEnabled(false);
            gridView.setVisibility(View.GONE);
            emptyImg.setBackground(getContext().getResources().getDrawable(R.drawable.package_empty_content));
            emptyImg.setVisibility(View.VISIBLE);
        }
        giftCountInfo.reset();
        setGiftCount(giftCountInfo);
        packageAdapter.setCurrentPackageType(currentPackageType);
        packageAdapter.notifyDataSetChanged();
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
                case MY_FUND_ACCOUNT_QUERY:
                    responseSuccessAccountQuery(jsonObject);
                    break;
                case GIFT_QUERY_BY_CHANNEL_AREA_ID:
                    responseSuccessGiftQueryByChannelId(jsonObject);
                    break;
                case GIFT_QUERY:
                    responseSuccessGiftQuery(jsonObject);
                    break;
                case GIFT_QUERY_BY_GIFT_CONFIG_ID:
                    responseSuccessGiftCount(jsonObject);
                    break;
                case GIFT_PURCHASE:
                    SendGiftResultBean sendGiftResult = new Gson().fromJson(jsonObject.toString(), SendGiftResultBean.class);
                    //重新计算余额
                    calculateRemainMoney(sendGiftResult);
                    break;
                case MY_PACKAGE_QUERY:
                    responseSuccessPackageInfo(jsonObject);
                    break;
                case GIFT_GIVE:
                    responseSuccessGiftGive(jsonObject);
                    break;
                case USER_PACKAGE_OPEN:
                    responseSuccessOpenPackage(jsonObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessGiftGive(JSONObject jsonObject) {
        ToastUtil.showShort(context.getApplicationContext(), "赠送成功");
        updatePackageList();
    }

    private void updatePackageList() {
        treasureInfoBean.currentTotalCount = treasureInfoBean.currentTotalCount - (int) giftCountInfo.getNum();
        if (treasureInfoBean.currentTotalCount <= 0) {
            packageInfoList.remove(treasureInfoBean);
            packageAdapter.setCheckPosition(0, 0);
            if (packageInfoList.size() > 0) {
                treasureInfoBean = packageInfoList.get(0);
            }
        }
        if (packageInfoList.size() == 0) {
            sendGiftBtn.setEnabled(false);
        }
        packageAdapter.notifyDataSetChanged();
    }

    private void responseSuccessOpenPackage(JSONObject jsonObject) {
        TreasureOpenListBean treasureOpenListBean = new Gson().fromJson(jsonObject.toString(), TreasureOpenListBean.class);
        ArrayList<TreasureOpenListBean.treasureOpenBean> treasureOpenList = treasureOpenListBean == null ? null : treasureOpenListBean.treasureChestList;
        showTreasureOpenAnimDialog(treasureOpenList, treasureInfoBean);
        updatePackageList();
    }

    private void responseSuccessGiftQuery(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("giftList")) {
            statusLayout.hideStatusView();
            walletCb.setEnabled(true);
            JSONArray giftList = null;
            giftList = jsonObject.getJSONArray("giftList");
            List<GiftInfoBean> giftInfoBeanList = new Gson().fromJson(giftList.toString(), new TypeToken<List<GiftInfoBean>>() {
            }.getType());
            playAdapter.setCurrentPayType(currentPayType);
            if (giftInfoBeanList != null && giftInfoBeanList.size() > 0) {
                playInfoList.clear();
                playInfoList.addAll(giftInfoBeanList);
                playInfoBean = giftInfoBeanList.get(0);
                playAdapter.notifyDataSetChanged();
                sendGiftBtn.setEnabled(true);
                gridView.setVisibility(View.VISIBLE);
                emptyImg.setVisibility(View.GONE);
                emptyImg.setBackground(getContext().getResources().getDrawable(R.drawable.gift_empty_content));
            } else {
                sendGiftBtn.setEnabled(false);
                giftNumLayout.setEnabled(false);
                gridView.setVisibility(View.GONE);
                emptyImg.setVisibility(View.VISIBLE);
            }
        }
    }

    private void calculateRemainMoney(SendGiftResultBean sendGiftResult) {

        List<SendGiftResultBean.ResultListBean> resultList = sendGiftResult.getResultList();
        if (resultList != null && resultList.size() > 0) {
            for (SendGiftResultBean.ResultListBean resultListBean : resultList) {
                //礼物赠送成功
                if (resultListBean.isSuccess()) {
                    if (sendGiftListener != null) {
                        sendGiftListener.sendGiftSuccess(resultListBean);
                    }
                }
            }
        }

    }


    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {

        try {
            if (GIFT_PURCHASE.equals((XjqUrlEnum) requestContainer.getRequestEnum())) {
                SendGiftResultBean sendGiftResult = new Gson().fromJson(jsonObject.toString(), SendGiftResultBean.class);
                SendGiftResultBean.ErrorBean error = sendGiftResult.getError();
                //重新计算余额
                calculateRemainMoney(sendGiftResult);
                if ("DATA_LOCK_FAILED".equals(error.getName())) return;//数据锁定失败，不需要任何显示

                if ("AVAIABLE_AMOUNT_NOT_ENOUGH".equals(error.getName()) && Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    new AlertDialog.Builder(context)
                            .setTitle("提示")
                            .setMessage("余额不足")
                            .setPositiveButton("确定", null)
                            .setNegativeButton("取消", null)
                            .show();
                    return;
                }
                if ("GIFT_IS_NOT_IN_THIS_AREA".equals(error.getName())) {
                    new AlertDialog.Builder(context)
                            .setTitle("提示")
                            .setMessage("礼物更新啦~请刷新后再试")
                            .setPositiveButton("刷新", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reqGiftListData();
                                }
                            })
                            .show();
                    return;
                }
            } else if (GIFT_GIVE.equals(requestContainer.getRequestEnum())) {
                ErrorBean errorBean = new ErrorBean(jsonObject);
                //忽略下USER_GIFT_LOCK_FAILED这个code的错误
                if (TextUtils.equals(errorBean.error.getName(), "USER_GIFT_LOCK_FAILED")) return;
            }

            ((BaseActivity) context).operateErrorResponseMessage(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void executorFailed(RequestContainer requestContainer) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) requestContainer.getRequestEnum();
        if (GIFT_QUERY_BY_CHANNEL_AREA_ID == requestEnum || GIFT_QUERY_BY_GIFT_CONFIG_ID == requestEnum) {
            statusLayout.showRetry();
        }
    }

    @Override
    public void executorFinish() {

    }
}
