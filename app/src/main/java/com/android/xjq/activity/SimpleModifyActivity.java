package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.view.ToggleEditTextView;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.view.LabelTextView;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qiaomu on 2018/2/6.
 * <p>
 * 简单信息修改界面,群名片修改
 */

public class SimpleModifyActivity extends BaseActivity4Jczj implements IHttpResponseListener {
    public static final int REQUEST_CODE = 1000;
    //常量字段
    //修改个人群名片
    public static final String HINT = "hint";
    public static final String MAX_LENGTH = "max_Length";
    public static final String POST_CARD_GROUPID = "post_card_group_id";
    public static final String POST_CARD_USERID = "post_card_user_id";

    public static final int MODIFY_GROUP_POST_CARD = 0;//修改群名片
    private int mCurOperate = MODIFY_GROUP_POST_CARD;

    /*------------------------------*/
    /*修改群信息名片的*/
    @BindView(R.id.editText)
    ToggleEditTextView mEditText;
    @BindView(R.id.counterTv)
    TextView mCounterTv;
    @BindView(R.id.group_card_layout)
    LinearLayout groupCardLayout;

    WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);

    //修改用户群名片调这个
    public static void startModifyPostCard4Result(Activity from, String hint, String groupId, String userId, int maxLength) {
        Intent intent = new Intent(from, SimpleModifyActivity.class);
        intent.putExtra(SimpleModifyActivity.HINT, hint);
        intent.putExtra(SimpleModifyActivity.MAX_LENGTH, maxLength);
        intent.putExtra(SimpleModifyActivity.POST_CARD_GROUPID, groupId);
        intent.putExtra(SimpleModifyActivity.POST_CARD_USERID, userId);
        intent.putExtra("operate", MODIFY_GROUP_POST_CARD);
        from.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_simple_modify);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpView() {
        mCurOperate = getIntent().getIntExtra("operate", MODIFY_GROUP_POST_CARD);
        setUpToolbar();

        switch (mCurOperate) {
            case MODIFY_GROUP_POST_CARD:
                setUpPostCardView();
                break;
            default:
                break;
        }
    }

    private void setUpToolbar() {
        String title = null;
        int menu = -1;
        int mode = MODE_BACK;
        int menuColor = Color.BLACK;
        int toolbarColor = Color.WHITE;
        String menuStr = getString(R.string.str_completed);
        switch (mCurOperate) {
            case MODIFY_GROUP_POST_CARD:
                title = getString(R.string.title_edit_group_postcard);
                menu = R.menu.menu_confirm;
                break;
            default:
                break;
        }
        setUpToolbar(title, menu, mode);
        LabelTextView menuLable = (LabelTextView) mToolbar.getMenu().getItem(0).getActionView();
        menuLable.setText(menuStr);
        menuLable.setTextColor(menuColor);
        menuLable.setPadding(0, 0, LibAppUtil.dip2px(this, 15), 0);
        mToolbar.setBackgroundColor(toolbarColor);
        mToolbarTitle.setTextColor(Color.BLACK);
        mToolbar.setNavigationIcon(R.drawable.ic_back_black);
    }

    private void setUpPostCardView() {
        groupCardLayout.setVisibility(View.VISIBLE);
        String mHint = getIntent().getStringExtra(HINT);
        final int mMaxLength = getIntent().getIntExtra(MAX_LENGTH, 10);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCounterTv.setText(s.length() + "/" + mMaxLength);
            }
        });
        mEditText.setText(mHint);
        if (!TextUtils.isEmpty(mHint))
            mEditText.setSelection(mEditText.getText().length());
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (mCurOperate) {
            case MODIFY_GROUP_POST_CARD:
                String input = mEditText.getText() == null ? "" : mEditText.getText().toString();
                doPostCardNickModifyRequest(input);
                break;
            default:
                break;
        }

        return false;
    }

    //发起修改名片的请求
    private void doPostCardNickModifyRequest(String input) {

        LibAppUtil.hideSoftKeyboard(this);
        showProgressDialog();

        String groupId = getIntent().getStringExtra(POST_CARD_GROUPID);
        String userId = getIntent().getStringExtra(POST_CARD_USERID);
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.GROUP_USER_INFO_MODIFY, true);
        formBody.put("groupId", groupId);
        formBody.put("userId", userId);
        if (!TextUtils.isEmpty(input))
            formBody.put("nickName", input);

        mHttpHelper.startRequest(formBody);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onSuccess(RequestContainer request, final Object o) {
        closeProgressDialog();
        XjqUrlEnum anEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (anEnum) {
            case GROUP_USER_INFO_MODIFY://修改群名片
                showToast(getString(R.string.modify_post_card_nick_success));
                Intent intent = new Intent();
                intent.putExtra("result", mEditText.getText().toString());
                setResult(RESULT_OK, intent);
                this.finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        closeProgressDialog();
        XjqUrlEnum anEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (anEnum) {
            case GROUP_USER_INFO_MODIFY:
                break;
            default:
                break;
        }
        operateErrorResponseMessage(jsonObject);
    }
}
