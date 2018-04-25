package com.android.xjq.activity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.adapter.UserTagAdapter;
import com.android.xjq.bean.interest.UserTagBean;
import com.android.xjq.utils.UiUtils;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.view.FlowLayout;
import com.android.xjq.view.XCFlowLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.button;
import static com.android.jjx.sdk.URLEnum.USER_ROLE_QUERY;
import static com.tencent.TIMConversation.getTag;

public class UserTagActivity extends BaseActivity implements OnHttpResponseListener {

    @BindView(R.id.iv_tag_close)
    ImageView mIvCancel;
    @BindView(R.id.iv_tag_complete)
    TextView mTvComplete;
    @BindView(R.id.tv_tag_choose)
    TextView mTvChoose;
    @BindView(R.id.flowlayout_tag)
    FlowLayout mFlowLayout;
    @BindView(R.id.rg_tag)
    RadioGroup mRadioGroup;
    @BindView(R.id.recyclerview_tag)
    RecyclerView mRecyclerView;

    private HttpRequestHelper httpRequestHelper;

    private UserTagAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tag);
        ButterKnife.bind(this);
        httpRequestHelper = new HttpRequestHelper(this, this);
        final GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAdapter.getItem(position).getType();
                return itemViewType == UserTagBean.Tag.NORMAL ? 1 : 3;
            }
        });
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new UserTagAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.USER_TAG_QUERY, false);
        container.put("groupBelongType", "USER_TAG");
        httpRequestHelper.startRequest(container, false);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton checkedButton = null;
                for (int i = 0, n = mRadioGroup.getChildCount(); i < n; i++) {
                    RadioButton button = (RadioButton) mRadioGroup.getChildAt(i);
                    if (button.getId() == checkedId) {
                        button.setTextColor(0xfff63f3f);
                        checkedButton = button;
                    } else {
                        button.setTextColor(0xff141414);
                    }
                }
                if (checkedButton != null) {
                    String id = checkedButton.getTag().toString();
                    for (int i = 0, n = mAdapter.getItemCount(); i < n; i++) {
                        UserTagBean.Tag tag = mAdapter.getItem(i);
                        if (TextUtils.equals(tag.getGroupId(), id)) {
                            int iii = mRecyclerView.getLayoutManager().findViewByPosition(i).getTop();
                            mRecyclerView.scrollBy(0, iii);
                            break;
                        }
                    }
                }
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstCompletelyVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
                int position = manager.findFirstVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    RadioButton button = (RadioButton) mRadioGroup.getChildAt(0);
                    button.setChecked(true);
                    return;
                }
                if (mAdapter.getItem(position).getType() == UserTagBean.Tag.DIVIDER) {
                    UserTagBean.Tag tag = mAdapter.getItem(position + 1);
                    for (int i = 0, n = mRadioGroup.getChildCount(); i < n; i++) {
                        RadioButton button = (RadioButton) mRadioGroup.getChildAt(i);
                        if (TextUtils.equals(tag.getGroupId(), button.getTag().toString())) {
                            button.setChecked(true);
                            break;
                        }
                    }
                }
            }
        });
    }

    @OnClick({R.id.iv_tag_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tag_complete:
                List<String> tagIds = mAdapter.getTagIds();
                StringBuilder builder = new StringBuilder();
                for (String s : tagIds) {
                    builder.append(s);
                    builder.append(",");
                }
                XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.USER_TAG_LINK_CREATE, false);
                container.put("tagIds", builder.substring(0, builder.length() - 1));
                httpRequestHelper.startRequest(container, false);
                break;
            default:
                break;
        }
    }

    public void addTag(UserTagBean.Tag tag) {
        TextView textView = createTag(tag);
        mFlowLayout.addView(textView);
        mTvChoose.setText(String.format(getString(R.string.selected_count), mFlowLayout.getChildCount()));
    }

    public void removeTag(UserTagBean.Tag tag) {
        for (int i = 0, n = mFlowLayout.getChildCount(); i < n; i++) {
            TextView textView = (TextView) mFlowLayout.getChildAt(i);
            if (TextUtils.equals(textView.getTag().toString(), tag.getId())) {
                mFlowLayout.removeView(textView);
                break;
            }
        }
        mTvChoose.setText(String.format(getString(R.string.selected_count), mFlowLayout.getChildCount()));
    }

    public TextView createTag(UserTagBean.Tag tag) {
        int paddingHorizontal = UiUtils.dp2px(this, 10);
        TextView textView = new TextView(this);
        textView.setText(tag.getTagName());
        textView.setTextColor(0xfff63f3f);
        textView.setBackgroundResource(R.drawable.red_stroke_corner);
        textView.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(paddingHorizontal, 0, paddingHorizontal, 0);
        textView.setTag(tag.getId());
        return textView;
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch ((XjqUrlEnum) requestContainer.getRequestEnum()) {
            case USER_TAG_QUERY:
                Gson gson = new GsonBuilder().create();
                UserTagBean bean = gson.fromJson(jsonObject.toString(), UserTagBean.class);
                //上面
                List<UserTagBean.Tag> defaultTags = bean.getDefaultTags();
                List<String> tagIds = new ArrayList<>();
                if (defaultTags != null && defaultTags.size() > 0) {
                    for (UserTagBean.Tag tag : defaultTags) {
                        TextView textView = createTag(tag);
                        mFlowLayout.addView(textView);
                        tagIds.add(tag.getId());
                    }
                }
                mTvChoose.setText(String.format(getString(R.string.selected_count), tagIds.size()));
                mAdapter.setTagIds(tagIds);
                //左边
                for (UserTagBean.GroupTag groupTag : bean.getUserTags()) {
                    RadioButton button = new RadioButton(UserTagActivity.this);
                    button.setText(groupTag.getGroupName());
                    button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    button.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
                    button.setGravity(Gravity.CENTER);
                    button.setBackgroundResource(R.drawable.selector_tag_rb);
                    button.setLayoutParams(new RadioGroup.LayoutParams(-1, UiUtils.dp2px(UserTagActivity.this, 41)));
                    button.setTag(groupTag.getGroupId());
                    mRadioGroup.addView(button);
                }
                ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
                //右边
                List<UserTagBean.Tag> list = new ArrayList<>();
                for (UserTagBean.GroupTag groupTag : bean.getUserTags()) {
                    for (UserTagBean.Tag tag : groupTag.getTagList()) {
                        tag.setType(UserTagBean.Tag.NORMAL);
                        list.add(tag);
                    }
                    UserTagBean.Tag divideTag = new UserTagBean.Tag();
                    divideTag.setType(UserTagBean.Tag.DIVIDER);
                    list.add(divideTag);
                }
                mAdapter.setData(list);
                break;
            case USER_TAG_LINK_CREATE:
                showToast("success");
                break;
            default:
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }
}
