package com.android.xjq.activity.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.message.NoticeMessageBean;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity implements IHttpResponseListener {

    @BindView(R.id.replyMeCount)
    TextView replyMeCount;

    @BindView(R.id.atMeCount)
    TextView atMeCount;

    @BindView(R.id.notifyCount)
    TextView notifyCount;

    private WrapperHttpHelper httpHelper   = new WrapperHttpHelper(this);

    public static void startMessageActivity(Activity activity) {

        Intent intent = new Intent();

        intent.setClass(activity, MessageActivity.class);

        activity.startActivity(intent);
    }

    @OnClick(R.id.toReplyMeLayout)
    public void toReplyMe() {

        replyMeCount.setVisibility(View.GONE);

        ReplyMeActivity.startReplyMeActivity(this);

    }

//    @OnClick(R.id.toAtMeLayout)
//    public void toAtMe() {
//
//        atMeCount.setVisibility(View.GONE);
//
//        AtMeActivity.startAtMeActivity(this);
//
//    }

    @OnClick(R.id.toNotifyLayout)
    public void toNotify() {

        notifyCount.setVisibility(View.GONE);

        MessageNotifyActivity.startMessageNotifyActivity(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);

        //setTitleBar(true, "我的消息", ContextCompat.getColor(this, com.android.banana.commlib.R.color.main_red));
        setTitle("我的消息");
        getData();
       // httpOperate.messageNoticeQuery(null, true);
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

    void getData(){
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.MESSAGE_NOTICE_QUERY, true);
        container.setGenericClaz(NoticeMessageBean.class);
        httpHelper.startRequest(container, false);
    }



    public void executorSuccess( NoticeMessageBean bean) {
    //    NoticeMessageBean bean = new Gson().fromJson(jo.toString(), NoticeMessageBean.class);
        setRedPoint(replyMeCount, bean.getAccessCodeAndNewCount().getREPLY());

        setRedPoint(atMeCount, bean.getAccessCodeAndNewCount().getMENTION());

        setRedPoint(notifyCount, bean.getAccessCodeAndNewCount().getUSER_MESSAGE_NOTICE());

    }


    public void executorFalse(JSONObject jo) {
        try {
            operateErrorResponseMessage(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public  void setRedPoint(TextView tv, int count) {
        if (count == 0) {
            tv.setVisibility(View.INVISIBLE);
            return;
        }
        tv.setVisibility(View.VISIBLE);

        if (count <= 9) {
            tv.setText(String.valueOf(count));
            tv.requestLayout();
            tv.setBackgroundResource(R.drawable.shape_message_point_oval);
        } else {
            if (count > 99) {
                tv.setText("99+");
            } else {
                tv.setText(String.valueOf(count));
            }
            tv.requestLayout();
            tv.setBackgroundResource(R.drawable.shape_message_point_radius_rect);
        }
    }


    @Override
    public void onSuccess(RequestContainer request, Object result) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        if (requestEnum == XjqUrlEnum.MESSAGE_NOTICE_QUERY && result != null) {
            NoticeMessageBean bean = (NoticeMessageBean) result;
            if (bean != null && bean.getAccessCodeAndNewCount()!=null) {
                executorSuccess(bean);
            }
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        executorFalse(jsonObject);
    }
}
