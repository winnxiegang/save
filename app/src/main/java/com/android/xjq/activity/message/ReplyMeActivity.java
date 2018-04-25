package com.android.xjq.activity.message;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.message.adapter.ReplyMeAdapter2;
import com.android.xjq.bean.message.ReplyMeBean;
import com.android.xjq.fragment.WriteMySubjectEmojiListFragment;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.XjqUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReplyMeActivity extends BaseActivity implements IHttpResponseListener {

    private WrapperHttpHelper httpHelper   = new WrapperHttpHelper(this);
    @BindView(R.id.fragmentLayout)
    LinearLayout fragmentLayout;

    private List<ReplyMeBean.MessagesBean> mList = new ArrayList<>();

    private ReplyMeAdapter2 mAdapter;

    String[] messageSubTypes = {"COMMENT_REPLY_TO_ME"};
    String[] messageTypes= {"SUBJECT_COMMENT"};

    private WriteMySubjectEmojiListFragment emojListFragment;

    private ListView mListView;

    public static void startReplyMeActivity(Activity activity) {

        Intent intent = new Intent();

        intent.setClass(activity, ReplyMeActivity.class);

        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reply_me);

        // setTitleBar(true, "回复我的");
        setTitle("回复我的");
        ButterKnife.bind(this);

        initView();

        getReplyMe();

    }

    private void initView() {

        setRefreshLayout(this);

        mAdapter = new ReplyMeAdapter2(this, mList);

        mListView.setAdapter(mAdapter);

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (fragmentLayout.getVisibility() == View.GONE) {

                    return false;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {

                    fragmentLayout.setVisibility(View.GONE);

                    emojListFragment.hideLayout();

                }
                return false;
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        emojListFragment = WriteMySubjectEmojiListFragment.newInstance();

        emojListFragment.setListener(new WriteMySubjectEmojiListFragment.OnFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(boolean sendResult) {
                if (sendResult) {
                    fragmentLayout.setVisibility(View.GONE);
                    LibAppUtil.hideSoftKeyboard(ReplyMeActivity.this);
                }

            }
        });

        fragmentTransaction.add(R.id.container, emojListFragment);

        fragmentTransaction.commit();

    }

    /**
     * 设置刷新布局
     */
    private void setRefreshLayout(Activity activity) {

        mRefreshEmptyViewHelper = new RefreshEmptyViewHelper(activity, new RefreshEmptyViewHelper.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mRequestType = REFRESH;

                // mPageSize = DEFAULT_PAGE_SIZE;

                currentPage = DEFAULT_PAGE;

                getReplyMe();
            }

            @Override
            public void onLoadMore() {

                mRequestType = LOAD_MORE;

                if (currentPage >= maxPages) {
                    ToastUtil.showLong(XjqApplication.getContext(),"已经到最后一页了!!");

                    mRefreshEmptyViewHelper.closeRefresh();

                } else {

                    currentPage++;

                    getReplyMe();
                }
            }

            @Override
            public void onEmptyRefresh() {

            }
        }, getResources().getDrawable(R.drawable.icon_no_content_about_chat_system_reply), "暂无消息");

        mListView = (ListView) findViewById(R.id.refreshListView);

        mListView.setDivider(new ColorDrawable(getResources().getColor(R.color.main_background)));

        mListView.setDividerHeight((int) getResources().getDimension(R.dimen.dp10));

    }


    private void getReplyMe() {

        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.COMMENT_REPLY_TO_ME_MESSAGE, true);
        container.put("currentPage",currentPage+"");
        container.putStringList("messageSubTypes", Arrays.asList(messageSubTypes));
        container.putStringList("messageTypes", Arrays.asList(messageTypes));
        container.setGenericClaz(ReplyMeBean.class);
        httpHelper.startRequest(container, false);

    }


    public void showReplyLayout(String commentId, String commentReplyId, String userName) {

        emojListFragment.setValue(commentId, commentReplyId, userName);

        fragmentLayout.setVisibility(View.VISIBLE);

    }
    @Override
    public void onSuccess(RequestContainer request, Object result) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        if (requestEnum == XjqUrlEnum.COMMENT_REPLY_TO_ME_MESSAGE && result != null) {
            responseSuccessGetReply((ReplyMeBean) result);
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        mRefreshEmptyViewHelper.closeRefresh();
    }
    private void responseSuccessGetReply(ReplyMeBean bean) {

        bean.operateData();

        for (int i = 0; i < bean.getMessages().size(); i++) {
            if(bean.getMessages().get(i).getComment().getSummary()!=null){
                bean.getMessages().get(i).getComment().setContent(XjqUtils.formatReplyString(bean.getMessages().get(i).getComment().getSummary()));
            }

            if (bean.getMessages().get(i).getCommentReply() != null) {
                bean.getMessages().get(i).getCommentReply().setContent(XjqUtils.formatReplyString(bean.getMessages().get(i).getCommentReply().getContent()));
            }
        }

        if (mRequestType == REFRESH) {

            mList.clear();
        }

        maxPages = bean.getPaginator().getPages();

        mList.addAll(bean.getMessages());

        mAdapter.notifyDataSetChanged();

        if (0 == mList.size()) {

            mRefreshEmptyViewHelper.closeRefresh();

        } else {

            mRefreshEmptyViewHelper.closeRefresh();

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (fragmentLayout.getVisibility() == View.VISIBLE) {
                fragmentLayout.setVisibility(View.GONE);
                return true;
            } else {
                finish();
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        //  ButterKnife.unbind(this);
        super.onDestroy();

    }
    void setTitle(String title){
        TextView tv = (TextView) findViewById(R.id.titleTv);
        tv.setText(title);
        findViewById(R.id.backIv).setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.backIv)
    void back(){
        finish();
    }


}
