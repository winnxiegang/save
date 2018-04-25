package com.android.xjq.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.R;
import com.android.xjq.adapter.main.SearchAdapter;
import com.android.xjq.bean.live.main.SearchResultBean;

import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.banana.commlib.utils.SharePreferenceUtils;
import com.android.xjq.view.XCFlowLayout;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Admin on 2017/3/6.
 */

public class SearchActivity extends BaseActivity implements OnHttpResponseListener {
    @BindView(R.id.searchLayout)
    LinearLayout searchLayout;
    @BindView(R.id.searchEt)
    EditText searchEt;
    @BindView(R.id.deleteTextIv)
    ImageView deleteTextIv;
    @BindView(R.id.searchTv)
    TextView searchTv;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.searchResultLayout)
    LinearLayout searchResultLayout;
    @BindView(R.id.deleteIv)
    ImageView deleteIv;
    @BindView(R.id.historySearchFl)
    XCFlowLayout historySearchFl;
    @BindView(R.id.historySearchLayout)
    LinearLayout historySearchLayout;
    @BindView(R.id.searchPromptLayout)
    LinearLayout searchPromptLayout;
    @BindView(R.id.emptyTv)
    TextView emptyTv;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;

    private HttpRequestHelper httpRequestHelper;

    private List<String> historySearchList = new ArrayList<>();

    private List<SearchResultBean.ChannelInfoSimpleBean> channelInfoList = new ArrayList<>();

    private SearchAdapter searchAdapter;

    @OnClick(R.id.searchTv)
    public void search() {

        searchResult();
    }

    private void searchResult() {
        LibAppUtil.hideSoftKeyboard(this);

        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.SEARCH, false);

        container.put("searchText", searchEt.getText().toString());

        httpRequestHelper.startRequest(container, true);
    }

    @OnClick(R.id.deleteTextIv)
    public void deleteText() {
        searchEt.setText("");
    }

    public static void startSearchActivity(Activity activity) {

        activity.startActivity(new Intent(activity, SearchActivity.class));
    }

    @OnClick(R.id.deleteIv)
    public void deleteHistoryRecord() {
        ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();
        builder.setMessage("是否删除全部历史记录？");
        builder.setPositiveMessage("确定");
        builder.setNegativeMessage("取消");
        builder.setShowTitle(false);
        builder.setShowMessageMiddle(true);
        builder.setPositiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPromptLayout.setVisibility(View.GONE);

                historySearchList.clear();

                SharePreferenceUtils.remove(SharePreferenceUtils.HISTORY_SEARCH_RECORD);
            }
        });
        ShowMessageDialog dialog = new ShowMessageDialog(this, builder);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.at_search);

        ButterKnife.bind(this);

        httpRequestHelper = new HttpRequestHelper(this, this);

        initial();

        getPromptData();

        searchAdapter = new SearchAdapter(this, channelInfoList);

        listView.setAdapter(searchAdapter);

        setEmptyView();

        compat(searchLayout);
    }

    private void getPromptData() {

        String data = (String) SharePreferenceUtils.getData(SharePreferenceUtils.HISTORY_SEARCH_RECORD, "");

//        if (!StringUtils.isEmpty(data)) {
//            historySearchList = new Gson().fromJson(data, new TypeToken<List<String>>() {
//            }.getType());
//
//            historySearchLayout.setVisibility(View.VISIBLE);
//
//            searchPromptLayout.setVisibility(View.VISIBLE);
//            initFlowChildViews(historySearchFl, historySearchList);
//        } else {
//
//            searchPromptLayout.setVisibility(View.GONE);
//        }


        historySearchLayout.setVisibility(View.VISIBLE);
        searchPromptLayout.setVisibility(View.VISIBLE);

        historySearchList.add("jkljfkl");
        historySearchList.add("jkljfkl");
        historySearchList.add("jkljfkl");
        historySearchList.add("jkljfkl");
        historySearchList.add("jkljfkl");
        initFlowChildViews(historySearchFl, historySearchList);
    }

    private void initFlowChildViews(XCFlowLayout flowLayout, final List<String> list) {

        if (list == null || list.size() == 0) {
            return;
        }
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 28;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for (int i = 0; i < list.size(); i++) {
            TextView view = new TextView(this);
            final String historyResult = list.get(i);
            view.setText(historyResult);
            view.setTextColor(getResources().getColor(R.color.light_gray_text_color));
            view.setPadding(25, 12, 25, 12);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_all_radius_gray_solid));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //搜索该用户
                    searchEt.setText(historyResult);
                    searchEt.setSelection(historyResult.length());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
                    search();
                }
            });
            flowLayout.addView(view, lp);
        }
    }

    private void setEmptyView() {

        listView.setEmptyView(emptyLayout);
    }

    private void initial() {

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    searchResultLayout.setVisibility(View.INVISIBLE);

                    deleteTextIv.setVisibility(View.GONE);

                    if (historySearchList != null && historySearchList.size() > 0) {
                        historySearchFl.removeAllViews();

                        historySearchLayout.setVisibility(View.VISIBLE);

                        searchPromptLayout.setVisibility(View.VISIBLE);

                        initFlowChildViews(historySearchFl, historySearchList);
                    }
                } else {
                    deleteTextIv.setVisibility(View.VISIBLE);
                }
            }
        });

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchResult();
                    return true;
                }
                return false;
            }
        });

    }

    private void responseSuccessSearch(JSONObject jsonObject) {
        searchResultLayout.setVisibility(View.VISIBLE);

        searchPromptLayout.setVisibility(View.GONE);

        SearchResultBean searchResultBean = new Gson().fromJson(jsonObject.toString(), SearchResultBean.class);

        searchResultBean.operatorData();

        List<SearchResultBean.ChannelInfoSimpleBean> searchResultList = searchResultBean.getSearchResultList();

        channelInfoList.clear();

        if (searchResultList != null && searchResultList.size() > 0) {

            channelInfoList.addAll(searchResultList);

            saveHistorySearchList(searchResultBean.getSearchText());
        } else {
            emptyTv.setText("\""+searchResultBean.getSearchText()+"\"");
        }

        searchAdapter.notifyDataSetChanged();
    }

    private void saveHistorySearchList(String searchText) {
        if (historySearchList != null && historySearchList.size() > 0) {
            for (String str : historySearchList) {
                if (str.equals(searchText)) {
                    historySearchList.remove(str);
                    break;
                }
            }
        } else {
            historySearchList = new ArrayList<>();
        }
        historySearchList.add(0, searchText);

        if (historySearchList.size() > 3) {
            historySearchList = historySearchList.subList(0, 3);
        }

        SharePreferenceUtils.putData(SharePreferenceUtils.HISTORY_SEARCH_RECORD, historySearchList);
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {

        responseSuccessSearch(jsonObject);
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
