package com.android.xjq.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.BaseDialogFragment;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.expandtv.NumLimitEditText;
import com.android.banana.groupchat.bean.GroupJoinListBean;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2018/3/1.
 */

public class ShareGroupListDialogFragment extends BaseDialogFragment implements View.OnClickListener, IHttpResponseListener<GroupJoinListBean> {
    private static final int SHARE_STATUS_START = 0;//发起分享ing

    private static final int SHARE_STATUS_COMPLETED = 1;//分享成功

    private static final int SHARE_STATUS_FAILED = 2;//分享失败

    private RecyclerView mRecyclerView;
    private TextView mLoadingView;
    private TextView mShareTitleTv;
    private ImageView mCloseIv;
    private NumLimitEditText mShareDesTv;
    private RadioButton mShareBtn;


    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    private JoinGroupAdapter mGroupAdapter;
    private List<GroupJoinListBean.GroupJoinBean> mDatas = new ArrayList<>();
    private String mGroupId, mObjectId, mObjectType, transDescription;
    private boolean mIsGroupShare;

    /**
     * 转发话题：objectId为话题id，objectType为SUBJECT
     * 转发PK：objectId为PK游戏局id，objectType为PK_GAME_BOARD
     * 转发极限手速：objectId为期次id，objectType为HAND_SPEED
     * <p>
     *
     * @param isGroupShare     true:转发到群聊  否则转到群空间
     * @param subjectId        话题对象的subjectId字段
     * @param objectType       话题类型 见上面
     * @param transDescription 话题是ARTICLE，PERSONAL_ARTICLE这两种的话那一块就显示标题，其他的都显示内容
     * @return
     */
    public static ShareGroupListDialogFragment newInstance(boolean isGroupShare, String subjectId, String objectType, String transDescription) {

        Bundle args = new Bundle();
        args.putBoolean("isGroupShare", isGroupShare);
        args.putString("objectId", subjectId);
        args.putString("objectType", objectType);
        args.putString("transDescription", transDescription);
        ShareGroupListDialogFragment fragment = new ShareGroupListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getDialogTheme() {
        return 0;//Theme.NORAML_THEME_DIMENABLE;
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.dialog_share_group_list_fragment;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public boolean fillScreenWidth() {
        return false;
    }

    @Override
    protected void onDialogCreate() {
        mIsGroupShare = getArguments().getBoolean("isGroupShare");
        mObjectId = getArguments().getString("objectId");
        mObjectType = getArguments().getString("objectType");
        transDescription = getArguments().getString("transDescription");

        getDialog().setCanceledOnTouchOutside(false);

        initView();

        doJoinGroupListRequestQuery();
    }

    private void doJoinGroupListRequestQuery() {
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.USER_JOINED_GROUP_INFO_QUERY, true);
        formBody.put("transmitCode", mIsGroupShare ? "GROUP" : "GROUP_TOPIC");
        mHttpHelper.startRequest(formBody);
    }

    private void doShareAction() {
        if (TextUtils.isEmpty(mGroupId)) {
            ToastUtil.showShort(getContext(), mIsGroupShare ? getString(R.string.select_group_first) : getString(R.string.select_group_zone_first));
            return;
        }
        String content = mShareDesTv.getText() == null ? "" : mShareDesTv.getText().toString();
        if (TextUtils.isEmpty(content))
            content = mShareDesTv.getHint() == null ? "" : mShareDesTv.getHint().toString();

        XjqUrlEnum shareUrl = mIsGroupShare ? XjqUrlEnum.TRANSMIT_TO_GROUP : XjqUrlEnum.TRANSMIT_TO_GROUP_TOPIC;
        RequestFormBody formBody = new RequestFormBody(shareUrl, true);
        formBody.put("content", content);
        if (mIsGroupShare) {
            formBody.put("groupId", mGroupId);
            formBody.put("objectId", mObjectId);
            formBody.put("objectType", mObjectType);
        } else {
            formBody.put("groupIds", mGroupId);
            formBody.put("subjectId", mObjectId);
        }
        mHttpHelper.startRequest(formBody);
        changShareBtnStatus(SHARE_STATUS_START);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mLoadingView = (TextView) rootView.findViewById(R.id.share_loading);
        mShareTitleTv = (TextView) rootView.findViewById(R.id.share_title);
        mCloseIv = (ImageView) rootView.findViewById(R.id.share_close);
        mShareDesTv = (NumLimitEditText) rootView.findViewById(R.id.share_des);
        mShareBtn = (RadioButton) rootView.findViewById(R.id.share_btn);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mGroupAdapter = new JoinGroupAdapter(getContext(), mDatas);
        mRecyclerView.setAdapter(mGroupAdapter);

        mCloseIv.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);
        mLoadingView.setOnClickListener(this);

        mShareTitleTv.setText(mIsGroupShare ? R.string.share_group_title : R.string.share_group_title_zone);

        mShareDesTv.setCountLocation(NumLimitEditText.LOCATION_RIGHT_CENTER);
        mShareDesTv.setMaxLength(15);
        if (!TextUtils.isEmpty(transDescription))
            mShareDesTv.setHint(transDescription);
        DetailsHtmlShowUtils.setHtmlText(mShareDesTv,transDescription);
        mShareDesTv.setEnabled(!TextUtils.equals(mObjectType, "SUBJECT"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_close:
                dismiss();
                break;
            case R.id.share_btn:
                doShareAction();
                break;
            case R.id.share_loading:
                mLoadingView.setText(R.string.share_group_loading);
                doJoinGroupListRequestQuery();
                break;
        }
    }


    private void changShareBtnStatus(int status) {
        switch (status) {
            case SHARE_STATUS_START:
                mShareBtn.setText(R.string.share_transform_ing);
                mShareBtn.setEnabled(false);
                mShareBtn.setAlpha(0.5f);
                break;
            case SHARE_STATUS_FAILED:
                mShareBtn.setText(R.string.str_share);
                mShareBtn.setEnabled(true);
                mShareBtn.setAlpha(1f);
                break;
            case SHARE_STATUS_COMPLETED:
                mShareBtn.setEnabled(true);
                mShareBtn.setAlpha(1f);
                ToastUtil.show(getContext(), getString(R.string.str_share_success), Toast.LENGTH_SHORT);
                mShareBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 1000);
                break;
        }
    }

    class JoinGroupAdapter extends RecyclerView.Adapter<JoinGroupAdapter.ViewHolder> {
        private Context mContext;
        private List<GroupJoinListBean.GroupJoinBean> mList;
        private int curSelectPos = -1;

        public JoinGroupAdapter(Context context, List<GroupJoinListBean.GroupJoinBean> list) {
            mContext = context;
            mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.dialog_share_group_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final GroupJoinListBean.GroupJoinBean groupJoinBean = mList.get(position);

            PicUtils.load(mContext, holder.groupIv, groupJoinBean.groupLogoUrl, R.drawable.user_default_logo);
            holder.groupNameTv.setText(groupJoinBean.name);
            holder.selectIv.setVisibility(curSelectPos == position ? View.VISIBLE : View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.selectIv.setVisibility(View.VISIBLE);
                    curSelectPos = position;
                    mGroupId = groupJoinBean.id;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView groupIv;
            public TextView groupNameTv;
            public ImageView selectIv;

            public ViewHolder(View itemView) {
                super(itemView);
                groupIv = (ImageView) itemView.findViewById(R.id.group_image);
                groupNameTv = (TextView) itemView.findViewById(R.id.group_name);
                selectIv = (ImageView) itemView.findViewById(R.id.group_select_iv);
            }
        }
    }


    @Override
    public void onSuccess(RequestContainer request, GroupJoinListBean groupJoinListBean) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case USER_JOINED_GROUP_INFO_QUERY:
                if (groupJoinListBean == null || groupJoinListBean.groupChatList == null || groupJoinListBean.groupChatList.size() <= 0) {
                    if(mIsGroupShare){//分享到群
                        mLoadingView.setText(R.string.share_group_no_data_tip);
                    }else{//到群空间
                        mLoadingView.setText("该操作权限仅限群主和管理员");
                    }
                    mShareBtn.setEnabled(false);
                    return;
                }
                mLoadingView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                mDatas.clear();
                mDatas.addAll(groupJoinListBean.groupChatList);
                if (mDatas.size() >= 3) {
                    mRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(-1, LibAppUtil.dip2px(getContext(), 200)));
                }

                mGroupAdapter.notifyDataSetChanged();

                break;
            case TRANSMIT_TO_GROUP:
            case TRANSMIT_TO_GROUP_TOPIC:
                changShareBtnStatus(SHARE_STATUS_COMPLETED);
                break;

        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case USER_JOINED_GROUP_INFO_QUERY:
                mLoadingView.setText(R.string.share_group_no_data_retry);
                break;
            case TRANSMIT_TO_GROUP:
            case TRANSMIT_TO_GROUP_TOPIC:
                changShareBtnStatus(SHARE_STATUS_FAILED);
                ((BaseActivity) getActivity()).operateErrorResponseMessage(jsonObject);
                //ToastUtil.showShort(getContext(), getString(R.string.str_share_failed));
                break;
        }
    }
}
