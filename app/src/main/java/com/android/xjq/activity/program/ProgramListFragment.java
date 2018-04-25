package com.android.xjq.activity.program;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.StringUtils;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.pile.CircleImageView;
import com.android.banana.commlib.view.pile.PileLayout;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.program.ProgramEntityBean;
import com.android.xjq.dialog.InvitedGuestDialog;
import com.android.xjq.fragment.BaseListFragment;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.view.recyclerview.TitleItemDecoration;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_AREA_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_AREA_QUERY_BY_LIVE;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_AREA_USER_ORDER_CANCEL;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_AREA_USER_ORDER_CREATE;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_AREA_USER_ORDER_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.GET_CHANNEL_AREA_QUERY;


public class ProgramListFragment extends BaseListFragment<ProgramEntityBean.ChannelProgramBean> implements IHttpResponseListener<ProgramEntityBean> {

    public final static int PROGRAM_TYPE_LIVE = 0;
    public final static int PROGRAM_TYPE_ALL = 1;
    public final static int PROGRAM_TYPE_RESERVED = 2;
    public final static int PROGRAM_TYPE_ALL_Next = 3;
    private int mProgramType = PROGRAM_TYPE_LIVE;
    private boolean mIsPullDown = false;
    private String mLatestDate = "";
    private String mOldDate = "";
    private int mPullUpCurPageIndex = 1;
    private int mPullDownCurPageIndex = 1;
    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    private boolean mIsDetailShow;
    private int mItemPos = 0;
    private String raceId;
    private String raceType = "";
    private int mRecentChnlId = -1;
    private int mLatestPos = -1;

    public static ProgramListFragment newInstance(boolean isDetailShow, boolean isShowFloatTitle, int programType) {
        Bundle args = new Bundle();
        args.putBoolean("isDetailShow", isDetailShow);
        args.putInt("programType", programType);
        args.putBoolean("isShowFloatTitle", isShowFloatTitle);
        ProgramListFragment fragment = new ProgramListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProgramListFragment newInstance(boolean isDetailShow, String raceId, String raceType, boolean isShowFloatTitle) {
        Bundle args = new Bundle();
        args.putBoolean("isDetailShow", isDetailShow);
        args.putString("raceId", raceId);
        args.putString("raceType", raceType);
        args.putBoolean("isShowFloatTitle", isShowFloatTitle);
        ProgramListFragment fragment = new ProgramListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        LiveActivity.startLiveActivity(getActivity(), mDatas.get(position).getId());
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.list_item_program;
    }

    @Override
    protected void onSetUpView() {
        mIsDetailShow = getArguments().getBoolean("isDetailShow", true);
        if (!mIsDetailShow) {
            raceId = getArguments().getString("raceId");
            raceType = getArguments().getString("raceType");
            mProgramType = getArguments().getInt("programType");
        }
        if (mProgramType == PROGRAM_TYPE_LIVE) {
            setRefreshEnable(false);
            setLoadMoreEnable(false);
            mRecycler.setEnableLoadMore(false);
        } else {
            setRefreshEnable(true);
            setLoadMoreEnable(true);
            mRecycler.setEnableLoadMore(true);
        }
        mLatestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToLatestPos(false);
            }
        });

    }

    private void scrollToLatestPos(boolean isFirst) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getId() == mRecentChnlId) {
                mLatestPos = i;
            }
        }
        int firstPos = mRecycler.getFirstVisibleItemPosition();
        if (isFirst) {
            mRecycler.scrollToPosition(mLatestPos - 1);
        } else {
            if (mLatestPos > firstPos) {
                mRecycler.scrollToPosition(mLatestPos + 1);
            } else {
                mRecycler.scrollToPosition(mLatestPos - 1);
            }
        }
        mLatestImg.setVisibility(View.GONE);

    }


    @Override
    public void onRefresh(boolean refresh) {
        if (refresh) { //下拉
            mIsPullDown = true;
            requestProgramData();
        } else {
            //上拉加载最新的，传入的down=false
            mIsPullDown = false;
            requestProgramData();
        }

    }



    @Override
    public void onBindHolder(ViewHolder holder, final ProgramEntityBean.ChannelProgramBean channelProgramBean, final int position) {
        String hourTime = TimeUtils.getHourMinute(channelProgramBean.getGmtStart());
        String mdTime = TimeUtils.getMonthDayStr2(channelProgramBean.getRaceDataClientSimple().getGmtStart());
        String hmTime = TimeUtils.getHourMinute(channelProgramBean.getRaceDataClientSimple().getGmtStart());
        holder.setText(R.id.hour_txt, hourTime);
        String title = channelProgramBean.getAreaTitle().length() > 10 ? channelProgramBean.getAreaTitle().substring(0, 10) + "..." : channelProgramBean.getAreaTitle();
        holder.setText(R.id.title_txt, title);
        TextView statusTxt = holder.getView(R.id.status_txt);
        setProgramStatus(statusTxt, channelProgramBean.getStatus(), channelProgramBean.isUserOrderChannelArea(), getActivity(), true);
        holder.setText(R.id.match_name, channelProgramBean.getRaceDataClientSimple().getMatchName());
        holder.setText(R.id.host_nick_name, "主播:" + channelProgramBean.getMasterAnchorName());
        List<ProgramEntityBean.HeadPortraitBean> headPortraitList = channelProgramBean.getHeadPortraitBeanList();
        PileLayout pileLayout = holder.getView(R.id.pile_layout);
        setHeadPortraits(pileLayout, headPortraitList);
        if (headPortraitList != null && headPortraitList.size() > 0) {
            holder.setViewVisibility(R.id.head_portrait_txt, View.VISIBLE);
            holder.setViewVisibility(R.id.pile_layout, View.VISIBLE);
        } else {
            holder.setViewVisibility(R.id.head_portrait_txt, View.GONE);
            holder.setViewVisibility(R.id.pile_layout, View.GONE);
        }
        holder.setText(R.id.race_time_month_day, mdTime);
        holder.setText(R.id.race_time_hour_min, hmTime);
        holder.setText(R.id.host_name, channelProgramBean.getRaceDataClientSimple().getHomeTeamName());
        holder.setText(R.id.guest_name, channelProgramBean.getRaceDataClientSimple().getGuestTeamName());
        LinearLayout detailLay = holder.getView(R.id.detail_lay);

        statusTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("yjj ", "statusTxt click >>>");
                if ("INIT".equals(channelProgramBean.getStatus())) {
                    onProgramStatusClick(channelProgramBean.isUserOrderChannelArea(), channelProgramBean.getId(), position);
                } else {
                    LiveActivity.startLiveActivity(getActivity(), mDatas.get(position).getId());
                }
            }
        });
        pileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGuestPileLayoutClick(channelProgramBean.getId());
            }
        });
        detailLay.setVisibility(mIsDetailShow ? View.VISIBLE : View.GONE);
        TextView matchNameTxt = holder.getView(R.id.match_name);
        matchNameTxt.setVisibility(mIsDetailShow ? View.VISIBLE : View.GONE);
        if (!mIsDetailShow) {
            TextView hourTxt = holder.getView(R.id.hour_txt);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) hourTxt.getLayoutParams();
            hourTxt.setLayoutParams(params);
        }

        if (!mIsDetailShow || mProgramType == PROGRAM_TYPE_LIVE || mProgramType == PROGRAM_TYPE_RESERVED) {
            mLatestImg.setVisibility(View.GONE);
        } else {
            int firstPos = mRecycler.getFirstVisibleItemPosition();
            if (firstPos == mLatestPos - 1) {
                mLatestImg.setVisibility(View.GONE);
            } else {
                mLatestImg.setVisibility(View.VISIBLE);
            }
        }
        holder.setViewVisibility(R.id.divider_hor, position == mDatas.size() - 1 ? View.GONE : View.VISIBLE);

    }

    public void onProgramStatusClick(boolean isUserOrderChannelArea, long channelAreaId, int pos) {
        mItemPos = pos;
        if (isUserOrderChannelArea) {
            showConfirmDialog(channelAreaId);
        } else {
            reqOrder(channelAreaId);
        }
    }

    public void onGuestPileLayoutClick(long channelAreaId) {
        new InvitedGuestDialog.Builder(getActivity()).setTitle("特邀嘉宾").create().generateGuestData(channelAreaId).show();
    }

    private void setHeadPortraits(PileLayout pileLayout, List<ProgramEntityBean.HeadPortraitBean> headPortraitBeanList) {
        if (headPortraitBeanList != null && headPortraitBeanList.size() > 0) {
            if (headPortraitBeanList.size() > 3) {
                headPortraitBeanList = headPortraitBeanList.subList(0, 3);
            }
            pileLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            for (int i = 0; i < headPortraitBeanList.size(); i++) {
                String url = headPortraitBeanList.get(i).getUserLogoUrl();
                CircleImageView imageView = (CircleImageView) inflater.inflate(R.layout.item_praise, pileLayout, false);
                Picasso.with(getActivity()).load(url).error(R.drawable.user_default_logo).into(imageView);
                pileLayout.addView(imageView);
            }
        }
    }

    //和赛程的节目状态公用
    public static void setProgramStatus(TextView statusTxt, String status, boolean isUserOrderChannelArea, Context ctx, boolean isProgram) {
        String statusStr = "";
        int resBgId = -1;
        int resConpoundDrawable = -1;
        int resTxtColor = -1;
        switch (status) {
            case "INIT":
                statusStr = isUserOrderChannelArea ? "已预约" : "预约";
                int orderBgId = isProgram ? R.drawable.shape_blue_border_bg : R.drawable.shape_gray_solid_blue_border_bg;
                resBgId = isUserOrderChannelArea ? R.drawable.shape_light_gray_solid : orderBgId;
                resConpoundDrawable = isUserOrderChannelArea ? -1 : R.drawable.icon_btn_order;
                resTxtColor = isUserOrderChannelArea ? R.color.white : R.color.purple_blue;
                break;
            case "LIVE":
                statusStr = "直播中";
                resBgId = R.drawable.shape_red_solid_bg;
                resConpoundDrawable = R.drawable.icon_btn_under_way;
                resTxtColor = R.color.white;
                break;
            case "PAUSE":
                statusStr = "已结束";
                resBgId = isProgram ? R.drawable.shape_white_solid_gray_stroke_radius : R.drawable.shape_light_gray_solid_gray_stroke_radius;
                resConpoundDrawable = R.drawable.icon_btn_finish;
                resTxtColor = R.color.grey_550;
                break;
            case "FINISH":
                statusStr = "已结束";
                resBgId = isProgram ? R.drawable.shape_white_solid_gray_stroke_radius : R.drawable.shape_light_gray_solid_gray_stroke_radius;
                resConpoundDrawable = R.drawable.icon_btn_finish;
                resTxtColor = R.color.grey_550;
                break;
            case "CANCEL":
                statusStr = "已结束";
                resBgId = isProgram ? R.drawable.shape_white_solid_gray_stroke_radius : R.drawable.shape_light_gray_solid_gray_stroke_radius;
                resConpoundDrawable = R.drawable.icon_btn_finish;
                resTxtColor = R.color.grey_550;
                break;
            default:
                break;
        }
        statusTxt.setText(statusStr);
        statusTxt.setBackground(ctx.getResources().getDrawable(resBgId));
        statusTxt.setTextColor(ctx.getResources().getColor(resTxtColor));
        if (resConpoundDrawable > 0) {
            Drawable drawableLeft = ctx.getResources().getDrawable(resConpoundDrawable);
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            statusTxt.setCompoundDrawables(drawableLeft, null, null, null);
        } else {
            statusTxt.setCompoundDrawables(null, null, null, null);
        }

    }


    public void setProgramType(int type) {
        //每次切换Tab都会重置programType
        if (mLatestImg != null)
            mLatestImg.setVisibility(View.GONE);
        mDatas.clear();
        if (mRecycler != null) {
            mRecycler.refreshCompleted();
        }
        mProgramType = type;
        if (mProgramType == PROGRAM_TYPE_LIVE) {
            setRefreshEnable(false);
            setLoadMoreEnable(false);
            if (mRecycler != null) {
                mRecycler.setEnableLoadMore(false);
            }
        } else {
            setRefreshEnable(true);
            setLoadMoreEnable(true);
            if (mRecycler != null) {
                mRecycler.setEnableLoadMore(true);
            }
        }
        setDefaultPram();
    }


    private void setDefaultPram() {
        mIsPullDown = false;
        mPullUpCurPageIndex = 1;
        mPullDownCurPageIndex = 1;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {

        TitleItemDecoration itemDecor = new TitleItemDecoration(getActivity()
                , getResources().getColor(R.color.light_black)
                , getResources().getColor(R.color.light_gray_bg)
                , getResources().getColor(R.color.black)
                , TitleItemDecoration.GRAVITY_LEFT
                , new TitleItemDecoration.DecorationCallback() {
            @Override
            public String getGroupId(int position) {

                if (position < mDatas.size()
                        && StringUtils.isNotEmpty(mDatas.get(position).getGmtStart())) {
                    return mDatas.get(position).getSectionDate();
                }
                return null;

            }
        }, new TitleItemDecoration.TitleTextCallback() {
            @Override
            public String getGroupFirstLine(int position) {
                boolean isShowFloatTitle = getArguments().getBoolean("isShowFloatTitle", true);
                if (position < mDatas.size()
                        && StringUtils.isNotEmpty(mDatas.get(position).getGmtStart())) {
                    return isShowFloatTitle ? mDatas.get(position).getSectionDate() : "";
                }
                return "";
            }

            @Override
            public String getActiveGroup() {
                return "今日";
            }
        });
        return itemDecor;
    }


    private void showConfirmDialog(final long channelAreaId) {
        ShowMessageDialog dialog = new ShowMessageDialog(getActivity(), "确定", "取消",
                new OnMyClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqCancelOrder(channelAreaId);
                    }
                },
                new OnMyClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("", "on cancel click");
                    }
                }, "确定要取消预约该节目吗?");
    }


    public void reqOrder(long channelAreaId) {
        XjqRequestContainer map = new XjqRequestContainer(CHANNEL_AREA_USER_ORDER_CREATE, true);
        map.put("channelAreaId", channelAreaId);
        mHttpHelper.startRequest(map, true);
    }

    public void reqCancelOrder(long channelAreaId) {
        XjqRequestContainer map = new XjqRequestContainer(CHANNEL_AREA_USER_ORDER_CANCEL, true);
        map.put("channelAreaId", channelAreaId);
        mHttpHelper.startRequest(map, true);
    }

    public void requestProgramData() {
        XjqRequestContainer map = null;
        switch (mProgramType) {
            case PROGRAM_TYPE_LIVE: //直播
                mProgressBar.setVisibility(View.VISIBLE);
                map = new XjqRequestContainer(CHANNEL_AREA_QUERY_BY_LIVE, true);
                break;
            case PROGRAM_TYPE_ALL:  //全部第一次进入
                //一进来先加载3天的数据
                mProgressBar.setVisibility(View.VISIBLE);
                map = new XjqRequestContainer(GET_CHANNEL_AREA_QUERY, true);
                map.put("raceId", raceId);
                map.put("raceType", raceType);
                break;
            case PROGRAM_TYPE_RESERVED:
                //一进来默认显示第一页数据，currentPage=1,down为false；
                int currentPage = !mIsPullDown ? mPullUpCurPageIndex : mPullDownCurPageIndex;
                if (currentPage == 1 && !mIsPullDown) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                map = new XjqRequestContainer(CHANNEL_AREA_USER_ORDER_QUERY, true);
                map.put("down", mIsPullDown);
                map.put("currentPage", currentPage);
                break;
            case PROGRAM_TYPE_ALL_Next:  //全部上拉下拉
                String date = mIsPullDown ? mOldDate : mLatestDate;
                map = new XjqRequestContainer(CHANNEL_AREA_QUERY, true);
                map.put("down", mIsPullDown);
                map.put("raceId", raceId);
                map.put("raceType", raceType);
                map.put("date", date);
                break;
            default:
                break;
        }
        mHttpHelper.startRequest(map, false);
    }

    @Override
    public void onSuccess(RequestContainer request, ProgramEntityBean programEntityBean) {
        mProgressBar.setVisibility(View.GONE);
        XjqUrlEnum urlEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case CHANNEL_AREA_QUERY_BY_LIVE:    //直播
                //用于刷新全部节目传入参数
                refreshList(programEntityBean);
                break;
            case CHANNEL_AREA_USER_ORDER_QUERY: //已预约
                //refreshList(programEntityBean);
                if (mIsPullDown) {
                    mDatas.addAll(0, programEntityBean.getChannelAreaConfigClientSimpleList());
                } else {
                    mDatas.addAll(programEntityBean.getChannelAreaConfigClientSimpleList());
                }
                if (!mIsPullDown) {
                    mPullUpCurPageIndex++;
                } else {
                    mPullDownCurPageIndex++;
                }
                mRecycler.refreshCompleted();
                break;
            case GET_CHANNEL_AREA_QUERY:       //全部第一次
                mProgramType = PROGRAM_TYPE_ALL_Next;
                mLatestDate = programEntityBean.getLastestDate();
                mOldDate = programEntityBean.getOldestDate();
                refreshList(programEntityBean);
                if (mDatas.size() > 6) {
                    mLatestImg.setVisibility(View.VISIBLE);
                }
                findLatestProgram(programEntityBean.getNowDate());
                mLatestImg.setVisibility(View.GONE);
                scrollToLatestPos(true);
                break;
            case CHANNEL_AREA_QUERY:           //全部上下拉
                //refreshList(programEntityBean);
                if (mIsPullDown) {
                    mOldDate = programEntityBean.getDate();
                    mDatas.addAll(0, programEntityBean.getChannelAreaConfigClientSimpleList());
                } else {
                    mLatestDate = programEntityBean.getDate();
                    mDatas.addAll(programEntityBean.getChannelAreaConfigClientSimpleList());
                }
                mRecycler.refreshCompleted();
                if (mDatas.size() > 6) {
                    mLatestImg.setVisibility(View.VISIBLE);
                }
                findLatestProgram(programEntityBean.getNowDate());
                break;
            case CHANNEL_AREA_USER_ORDER_CANCEL:
                Toast.makeText(getActivity(), "预约已取消", Toast.LENGTH_SHORT).show();
                if (mProgramType == PROGRAM_TYPE_RESERVED) {
                    mDatas.remove(mItemPos);
                } else {
                    mDatas.get(mItemPos).setStatus("INIT");
                    mDatas.get(mItemPos).setUserOrderChannelArea(false);
                }
                mRecycler.refreshCompleted();
                break;
            case CHANNEL_AREA_USER_ORDER_CREATE:
                Toast.makeText(getActivity(), "预约成功", Toast.LENGTH_SHORT).show();
                mDatas.get(mItemPos).setStatus("INIT");
                mDatas.get(mItemPos).setUserOrderChannelArea(true);
                mRecycler.refreshCompleted();
                break;
            default:
                loadCompleted(programEntityBean == null ? null : programEntityBean.getChannelAreaConfigClientSimpleList());
                break;
        }

        if (mDatas.size() > 0) {
            mRecycler.showContentView();
        } else {
            mRecycler.showEmptyView(false, R.drawable.icon_no_content_about_program_live, "", "");
        }


    }

    private void findLatestProgram(String nowDate) {
        long ndiffer = -1;
        for (ProgramEntityBean.ChannelProgramBean channelProgramBean : mDatas) {
            long differ = Math.abs(TimeUtils.getDiffSecTwoDateStr(nowDate, channelProgramBean.getGmtStart()));
            if (ndiffer < 0) {
                mRecentChnlId = channelProgramBean.getId();
                ndiffer = differ;
            } else if (differ < ndiffer) {
                mRecentChnlId = channelProgramBean.getId();
                ndiffer = differ;
            }
        }


    }

    private void refreshList(ProgramEntityBean programEntityBean) {
        loadCompleted(programEntityBean == null ? null : programEntityBean.getChannelAreaConfigClientSimpleList());
    }


    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        mProgressBar.setVisibility(View.GONE);
        loadCompleted(null);
        ((BaseActivity) getActivity()).operateErrorResponseMessage(jsonObject);
    }

    @Override
    protected void initData() {

    }

}
