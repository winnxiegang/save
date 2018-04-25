package com.android.banana.groupchat.msglist.realm;


import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.groupchat.bean.MessageListBean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by zaozao on 2017/10/19.
 */

public class MyRealmHelper {

    private Realm mRealm;

    public static RealmConfiguration realmConfig;

    public static final String DB_NAME = "GroupChatMessageRealm.realm";

    private String userId = LoginInfoHelper.getInstance().getUserId();

    public MyRealmHelper() {

        if(realmConfig==null){

            mRealm = Realm.getDefaultInstance();
        }
    }



    /**
     * delete （删）
     */
    public void delete(String groupIdAndType) {
        initRealm();
        MessageListBean bean = mRealm.where(MessageListBean.class)
                .equalTo("groupIdAndType", groupIdAndType)
                .findFirst();

        if(bean!=null){
            mRealm.beginTransaction();
            //bean.deleteFromRealm();   //class MessageListBean extends RealmObject {
            RealmObject.deleteFromRealm(bean);//MessageListBean implements RealmModel {
            mRealm.commitTransaction();
        }

    }

    /**
     * update （改）
     * 先查询，在修改
     */
    public void update( MessageListBean bean) {
        initRealm();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(bean);
        mRealm.commitTransaction();
    }



    /**
     * query （查询所有）查询该角色对应的消息列表记录
     */
    public List<MessageListBean> queryAll() {
        RealmResults<MessageListBean> list = mRealm.where(MessageListBean.class).equalTo("userId", userId).findAll();
        return mRealm.copyFromRealm(list);
    }



    //异步保存：轮循回来的新增消息
    public void saveMessageAsync(final List<MessageListBean> showMessageList){
        initRealm();
        RealmAsyncTask transaction =  mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i <showMessageList.size() ; i++) {

                    MessageListBean messageListBean = showMessageList.get(i);
                    messageListBean.setUserId(userId);
                    realm.copyToRealmOrUpdate(messageListBean);
                }
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                //失败回调
            }
        });
    }

    /**
     * add （增），私聊回来的，推送来的
     */
    public void add(final MessageListBean bean) {
        bean.setUserId(userId);
        initRealm();
        RealmAsyncTask transaction =  mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(bean);
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                //失败回调
            }
        });
    }




    public void close(){
        if (mRealm!=null&&!mRealm.isClosed()){
            if(mRealm.isInTransaction()){
                mRealm.cancelTransaction();
            }
            mRealm.close();
        }
    }

    public void initRealm(){
        if(mRealm==null|| mRealm.isClosed()){

            mRealm = Realm.getDefaultInstance();
        }
    }
}
