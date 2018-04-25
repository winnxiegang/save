package com.android.banana.groupchat.membermanage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.groupchat.bean.ChatRoomMemberBean;
import com.android.banana.groupchat.bean.SpeakSetTypeBean;
import com.android.banana.groupchat.bean.SpeakerSetData;
import com.android.banana.groupchat.chatenum.ChatMemberPermissionTypeEnum;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.view.MyListView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SpeakSettingActivity extends BaseActivity implements OnHttpResponseListener {
    public static int DELETE_STATE = 1;//完成
    public static int ADD_STATE = 0;//确定

    MyListView listView;
    RecyclerView recyclerListView;
    TextView finishTv;
    LinearLayout initAddLayout;
    ImageView addSpeakerIv;
    LinearLayout addSpeakLayout;
    private HttpRequestHelper requestHelper;

    private int operateType = ADD_STATE;

    private SpeakLimitSetAdapter speakLimitSetAdapter;

    private AddSpeakerAdapter addSpeakerAdapter;

    private SpeakSetTypeBean currentSelectType;

    private String webPermissionType;

    private boolean deleteWebData = false;

    private boolean originSpeakerIsNull = false;

    private String groupId;

    private int deletePosition ;

    private ChatRoomMemberBean.GroupMemberSimpleBean addSpeaker,deleteSpeaker;

    //点击加号的时候，放的是页面成员所有的userId，
    //点击确定的时候，放的是自己刚添加的userId,不包含从服务端获取的
    private ArrayList<String> speakerUserIdList = new ArrayList<>();

    private List<SpeakSetTypeBean> speakLimitSetList = new ArrayList<>();

    private ArrayList<ChatRoomMemberBean.GroupMemberSimpleBean> speakerList = new ArrayList<>();

    private List<ChatRoomMemberBean.GroupMemberSimpleBean> speakerAddList = new ArrayList<>();


    View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            back();
        }
    };

    private void back(){
        if(currentSelectType==null)
        {
            finish();

        } else if(!webPermissionType.equals(currentSelectType.getName())){

            showDialog();

        }else{

            if(currentSelectType.getName().equals(ChatMemberPermissionTypeEnum.ONLY_BY_USER_AND_GROUP_ADMIN.name())
                    &&checkAddSpeaker()){
                showDialog();
            }else{
                finish();
            }
        }
    }

    /**
     * 判断发言人有没有变化
     * 有变化为true，显示弹窗
     * @return
     */
    private boolean checkAddSpeaker(){
        if(originSpeakerIsNull){//服务端获取的没有已添加的发言人
            if(speakerList.size()>0){
                return true;
            }else{
                return false;
            }
        }else{//一进入页面有已添加的发言人
            if(deleteWebData){
                return true;
            }else{
                for (int i = 0; i <speakerList.size() ; i++) {
                    //排除加减号speakerList.get(i).getUserLogoUrl()!=null
                    if(!speakerList.get(i).isFromWeb()&&speakerList.get(i).getUserLogoUrl()!=null){
                        return true;
                    }
                }
            }
            return  false;
        }
    }

    private void showDialog(){
        new ShowMessageDialog(SpeakSettingActivity.this, new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, null, "确定要放弃修改吗?");
    }


    public void clickFinish(View v) {
        if (operateType == DELETE_STATE) {//可删除状态时，点完成的时候
            operateType = ADD_STATE;
            notifyView();
        }
        else if (operateType == ADD_STATE) {//点击确定的时候，如果选择了第三个，则需要请求接口
            String userIds = "";
            if (currentSelectType.getName().equals(ChatMemberPermissionTypeEnum.ONLY_BY_USER_AND_GROUP_ADMIN.name()) && speakerList.size() > 0) {

                speakerUserIdList.clear();//clear一下，然后存放传给服务端的自己新增的发言人的userIdList

                for (int i = 0; i < speakerList.size(); i++) {

                    if (!speakerList.get(i).isFromWeb() && speakerList.get(i).getUserId() != null) {

                        speakerUserIdList.add(speakerList.get(i).getUserId());
                    }
                }
                if (speakerUserIdList.size() > 0) {

                    userIds = speakerUserIdList.toString().replace("[", "").replace("]", "").trim();

                }

            }
            clickOkRequest(userIds);
        }
    }

    /**
     * 点击确认的时候请求
     */
    private void clickOkRequest(String userIds) {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.GROUP_MESSAGE_ALLOWED_SENDER_ADD, true);

        map.put("groupId", groupId);

        map.put("userIds", userIds);

        map.put("allowedSenderType", currentSelectType.getName());

        requestHelper.startRequest(map, true);
    }

    /**
     * 刷新界面
     * 点击减号，完成，添加发言人回来时，都需要刷新界面
     */
    private void notifyView(){
        if(operateType == ADD_STATE){
            finishTv.setText("确定");
        }else{
            finishTv.setText("完成");
        }
        addSpeakerAdapter.setType(operateType);

        addSpeakerAdapter.notifyDataSetChanged();
    }

//    @OnClick(R.id.addSpeakerIv)
    public void toAddSpeaker(View v) {

        if (operateType == DELETE_STATE) {

            operateType = ADD_STATE;

            notifyView();
        }

        Intent intent = new Intent();

        intent.putExtra("operateType", GroupChatMembersManageActivity.ADD_MEMBER_STATE);

        intent.putExtra("groupId", groupId);

        intent.putExtra("isManager", "true");

        intent.putStringArrayListExtra("userIdList", speakerUserIdList);

        intent.setClass(this, GroupChatMembersManageActivity.class);

        startActivityForResult(intent, 1000);
    }

    public static void startSpeakSettingActivity(Activity activity, String groupId) {

        Intent intent = new Intent();

        intent.putExtra("groupId", groupId);

        intent.setClass(activity, SpeakSettingActivity.class);

        activity.startActivityForResult(intent,100);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speak_setting);

        listView = findViewOfId(R.id.listView) ;
        recyclerListView= findViewOfId(R.id.recyclerListView) ;
        finishTv= findViewOfId(R.id.finishTv) ;
        initAddLayout= findViewOfId(R.id.initAddLayout) ;
        addSpeakerIv= findViewOfId(R.id.addSpeakerIv) ;
        addSpeakLayout= findViewOfId(R.id.addSpeakLayout) ;

        setTitleBar(true, "发言设置", backClickListener);

        finishTv.setText("确定");

        initView();

        setListener();
    }

    private void initView() {

        requestHelper = new HttpRequestHelper(this, this);

        Intent intent = getIntent();

        groupId = intent.getStringExtra("groupId");

        listView.setDivider(null);

        speakLimitSetAdapter = new SpeakLimitSetAdapter(this, speakLimitSetList);

//        listView.setListViewHeightBasedOnChildren(listView);

        listView.setAdapter(speakLimitSetAdapter);

        recyclerListView.setLayoutManager(new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false));

        addSpeakerAdapter = new AddSpeakerAdapter(this, speakerList);

        recyclerListView.setAdapter(addSpeakerAdapter);

        addSpeaker = new ChatRoomMemberBean.GroupMemberSimpleBean();

        deleteSpeaker = new ChatRoomMemberBean.GroupMemberSimpleBean();

        getInitData();

    }

    private void setListener() {

        finishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFinish(v);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == speakLimitSetList.size() - 1) {

                    addSpeakLayout.setVisibility(View.VISIBLE);

                    setAddLayout();

                } else {
                    addSpeakLayout.setVisibility(View.GONE);
                }
                for (int i = 0; i < speakLimitSetList.size(); i++) {

                    speakLimitSetList.get(i).setSelect(false);
                }

                speakLimitSetList.get(position).setSelect(true);

                currentSelectType =  speakLimitSetList.get(position);

                speakLimitSetAdapter.notifyDataSetChanged();
            }
        });


        addSpeakerAdapter.setOnItemClickListener(new AddSpeakerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (speakerList.size() > 0) {
                    if (position == speakerList.size() - 1) {//最后一个减号

                        operateType = DELETE_STATE;

                        notifyView();
                    }

                    if (position == speakerList.size() - 2) {//倒数第二个加号

                        speakerUserIdList.clear();//使用之前clear一下，只是一个暂时的存储传给下一个界面的userIdList

                        for (int i = 0; i < speakerList.size(); i++) {

                            speakerUserIdList.add(speakerList.get(i).getUserId());
                        }

                        toAddSpeaker(view);
                    }
                }
            }

            @Override
            public void onDeleteClick(View view, final int position) {
                deletePosition = position;
                new ShowMessageDialog(SpeakSettingActivity.this, new OnMyClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (speakerList.get(position).isFromWeb()) {
                            //请求删除接口
                            deleteSpeakerRequest(speakerList.get(position).getUserId());

                            deleteWebData = true;
                        } else {
                            deleteSuccess();
                        }
                    }
                }, null, "确定删除该发言人吗?");
            }
        });
    }

    private void getInitData() {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.GROUP_USER_PERMISSION_QUERY, true);

        map.put("groupId", groupId);

        requestHelper.startRequest(map,true);
    }


    /**
     * 删除发言人
     */
    private void deleteSpeakerRequest(String deleteUserId) {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.GROUP_MESSAGE_ALLOWED_SENDER_REMOVE, true);

        map.put("groupId",groupId);

        map.put("userId", deleteUserId);

        requestHelper.startRequest(map,true);
    }

    private void setAddLayout() {

        if (speakerList.size() > 0) {

            initAddLayout.setVisibility(View.GONE);

        } else {

            initAddLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setAddDeleteBean() {

        addSpeaker.setResId(R.drawable.icon_add_mark);
        addSpeaker.setUserName("");
        addSpeaker.setGoneDelete(true);

        deleteSpeaker.setResId(R.drawable.icon_jianhao);
        deleteSpeaker.setUserName("");
        deleteSpeaker.setGoneDelete(true);

        speakerList.add(addSpeaker);
        speakerList.add(deleteSpeaker);
    }

    public void deleteSuccess() {

        speakerList.remove(deletePosition);

        if(speakerList.size()==2){//只剩下加号减号的时候
            speakerList.clear();
            speakerUserIdList.clear();
            initAddLayout.setVisibility(View.VISIBLE);//此时点击加号的时候，传到下一个界面的userId应该也清空
            recyclerListView.setVisibility(View.GONE);
        }
        addSpeakerAdapter.notifyDataSetChanged();
    }

    private void requestSuccessSpeakerSet(JSONObject jo) {
        finishTv.setVisibility(View.VISIBLE);

        SpeakerSetData speakerSetData = new Gson().fromJson(jo.toString(), SpeakerSetData.class);

        if(speakerSetData.getGroupMessgaePermissionTypeList()!=null){
            speakLimitSetList.addAll(speakerSetData.getGroupMessgaePermissionTypeList());

            webPermissionType = speakerSetData.getGroupMessagePermissionType().getName();

            for (int i = 0; i < speakLimitSetList.size(); i++) {
                if (webPermissionType.equals(speakLimitSetList.get(i).getName())) {

                    speakLimitSetList.get(i).setSelect(true);

                    currentSelectType = speakLimitSetList.get(i);
                }
            }
            speakLimitSetAdapter.notifyDataSetChanged();
        }

        //是否显示下面添加发言人布局
        if (webPermissionType.equals(ChatMemberPermissionTypeEnum.ONLY_BY_USER_AND_GROUP_ADMIN.name())) {
            addSpeakLayout.setVisibility(View.VISIBLE);
        } else {
            addSpeakLayout.setVisibility(View.GONE);
        }

        if (speakerSetData.getUserInfoList() != null && speakerSetData.getUserInfoList().size() > 0) {
            //从服务端获取的数据做个标记
            for (int j = 0; j < speakerSetData.getUserInfoList().size(); j++) {
                speakerSetData.getUserInfoList().get(j).setFromWeb(true);
            }
            speakerList.addAll(speakerSetData.getUserInfoList());

            setAddDeleteBean();//将加号和减号作为两个bean添加到list中

            initAddLayout.setVisibility(View.GONE);

            addSpeakerAdapter.notifyDataSetChanged();
        } else {
            originSpeakerIsNull = true;
            initAddLayout.setVisibility(View.VISIBLE);
        }
    }

    private void addSpeakerSuccess(){

        Intent intent = new Intent();

        intent.putExtra("currentSelectType",currentSelectType.getMessage());

        setResult(RESULT_OK, intent);

        finish();
    }


    @Override
    public void executorSuccess(RequestContainer request, JSONObject jo) {
        JczjURLEnum jczjURLEnum = (JczjURLEnum) request.getRequestEnum();
        switch (jczjURLEnum) {
            case GROUP_USER_PERMISSION_QUERY:
                requestSuccessSpeakerSet(jo);
                break;
            case GROUP_MESSAGE_ALLOWED_SENDER_REMOVE:
                deleteSuccess();
                break;
            case GROUP_MESSAGE_ALLOWED_SENDER_ADD:
                addSpeakerSuccess();
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer request, JSONObject jo) {
        try {
            operateErrorResponseMessage(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer request) {

    }

    @Override
    public void executorFinish() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (data != null && data.getParcelableArrayListExtra("memberList") != null) {
                //从本地添加的数据做个标记
                speakerAddList = data.getParcelableArrayListExtra("memberList");

                if (initAddLayout.getVisibility() == View.VISIBLE) {

                    initAddLayout.setVisibility(View.GONE);

                    recyclerListView.setVisibility(View.VISIBLE);

                    speakerList.addAll(speakerAddList);

                    setAddDeleteBean();

                } else {

                    speakerList.addAll(speakerList.size() - 2, speakerAddList);
                }
            }
            addSpeakerAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            back();
        }
        return false;
    }
}
