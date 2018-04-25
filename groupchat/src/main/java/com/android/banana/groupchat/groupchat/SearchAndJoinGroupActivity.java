package com.android.banana.groupchat.groupchat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.groupchat.bean.NewGroupBean;
import com.android.banana.groupchat.groupchat.chat.GroupChatActivity;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAndJoinGroupActivity extends BaseActivity implements OnHttpResponseListener {

    EditText searchGroupEt;

    ImageView backIv,emptyIv;

    CircleImageView groupPhotoIv;

    TextView groupNameTv;

    TextView waitCheckTV;

    ImageView applyJoinIv;

    TextView introduceTv;

    TextView managerNameTv;

    TextView userCountTv;

    LinearLayout resultLayout;

    TextView emptyTipTv;

    LinearLayout recordEmptyView;

    private HttpRequestHelper httpRequestHelper;

    private NewGroupBean.GroupChatSimpleBean bean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_join_group);
        initView();
        httpRequestHelper = new HttpRequestHelper(this, this);
    }

    private  void initView(){
        searchGroupEt = (EditText) findViewById(R.id.searchGroupEt);
        backIv = (ImageView) findViewById(R.id.backIv);
        emptyTipTv = (TextView) findViewById(R.id.emptyTipTv);
        emptyIv = (ImageView) findViewById(R.id.imageView);
        recordEmptyView = (LinearLayout) findViewById(R.id.recordEmptyView);
        groupPhotoIv= (CircleImageView) findViewById(R.id.groupPhotoIv);
        groupNameTv = (TextView) findViewById(R.id.groupNameTv);
        waitCheckTV= (TextView) findViewById(R.id.waitCheckTV);
        applyJoinIv= (ImageView) findViewById(R.id.applyJoinIv);
        introduceTv= (TextView) findViewById(R.id.introduceTv);
        managerNameTv= (TextView) findViewById(R.id.managerNameTv);
        userCountTv= (TextView) findViewById(R.id.userCountTv);
        resultLayout = (LinearLayout)findViewById(R.id.resultLayout);
    }

    //搜索
    public void search(View v) {
        if(!TextUtils.isEmpty(searchGroupEt.getText())){
//313080661
            String groupNo  = searchGroupEt.getText().toString();

            RequestFormBody requestFormBody = new RequestFormBody(JczjURLEnum.GROUP_SEARCH, true);

            requestFormBody.put("groupNo",groupNo);

            httpRequestHelper.startRequest(requestFormBody);

        }else{
            LibAppUtil.showTip(this,"请输入你要查找的群号码");
        }

    }

    public void back(View v) {
        finish();
    }

    //申请加入
    public void applyJoin(View v) {

        RequestFormBody requestFormBody = new RequestFormBody(JczjURLEnum.USER_GROUP_JOIN_APPLY, true);
//"2339202477004610980035563301"
        requestFormBody.put("groupChatId",bean.getId() );

        httpRequestHelper.startRequest(requestFormBody);

    }

    private void searchSuccess(JSONObject jsonObject){
        NewGroupBean newGroupBean = new Gson().fromJson(jsonObject.toString(), NewGroupBean.class);

        if (newGroupBean.getGroupChatSimple() != null) {

            resultLayout.setVisibility(View.VISIBLE);

            recordEmptyView.setVisibility(View.GONE);

            bean = newGroupBean.getGroupChatSimple();

            groupNameTv.setText(bean.getName());

            Picasso.with(this).
                    load(bean.getGroupLogoUrl()).
                    error(R.drawable.user_default_logo).
                    into(groupPhotoIv);

            managerNameTv.setText("群主:"+(bean.getCreatorName()!=null?bean.getCreatorName():""));

            introduceTv.setText("简介:"+(bean.getMemo()!=null?bean.getMemo():""));

            userCountTv.setText("成员:"+bean.getUserCount());

            applyJoinIv.setVisibility(View.GONE);

            waitCheckTV.setVisibility(View.GONE);

            if(!bean.isJoined()){
                if(bean.isApplied()){
                    waitCheckTV.setVisibility(View.VISIBLE);
                }else {
                    applyJoinIv.setVisibility(View.VISIBLE);
                }

            }else{
                resultLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GroupChatActivity.start2GroupChatActivity(
                                SearchAndJoinGroupActivity.this,
                                bean.getGroupId(),
                                bean.getName(),
                                bean.getGroupLogoUrl());

                    }
                });
            }

        } else {
            resultLayout.setVisibility(View.GONE);
            recordEmptyView.setVisibility(View.VISIBLE);
            emptyTipTv.setText("未找到该群");
        }
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {

        JczjURLEnum urlEnum = (JczjURLEnum) requestContainer.getRequestEnum();
        switch (urlEnum){
            case USER_GROUP_JOIN_APPLY:
                LibAppUtil.showTip(this,"申请成功");
                applyJoinIv.setVisibility(View.GONE);
                waitCheckTV.setVisibility(View.VISIBLE);
                break;
            case GROUP_SEARCH:
                searchSuccess(jsonObject);
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            ErrorBean bean = new ErrorBean(jsonObject);
            if (bean.getError() == null)
                return;
            if ("GROUP_NOT_EXITS".equals(bean.getError().getName())) {
                resultLayout.setVisibility(View.GONE);
                recordEmptyView.setVisibility(View.VISIBLE);
                emptyIv.setImageResource(R.drawable.icon_search_not_found);
                emptyTipTv.setText("未找到该群");
            }else{
                operateErrorResponseMessage(jsonObject);
            }

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
