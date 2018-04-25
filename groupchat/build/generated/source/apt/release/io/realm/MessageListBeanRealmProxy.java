package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.SharedRealm;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageListBeanRealmProxy extends com.android.banana.groupchat.bean.MessageListBean
    implements RealmObjectProxy, MessageListBeanRealmProxyInterface {

    static final class MessageListBeanColumnInfo extends ColumnInfo
        implements Cloneable {

        public long groupIdIndex;
        public long orderValueByUserIndex;
        public long unreadMessageCountIndex;
        public long sendUserLoginNameIndex;
        public long sendUserIdIndex;
        public long soleIdIndex;
        public long userIdIndex;
        public long groupIdAndTypeIndex;
        public long roomNameIndex;
        public long photoUrlIndex;
        public long showCouponIndex;
        public long gmtCreateIndex;
        public long originGmtCreateIndex;
        public long lastMessageCreateTimeIndex;
        public long lastMessageContentIndex;
        public long memberNumIndex;
        public long groupChatMessageTypeIndex;
        public long systemMessageIndex;

        MessageListBeanColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(18);
            this.groupIdIndex = getValidColumnIndex(path, table, "MessageListBean", "groupId");
            indicesMap.put("groupId", this.groupIdIndex);
            this.orderValueByUserIndex = getValidColumnIndex(path, table, "MessageListBean", "orderValueByUser");
            indicesMap.put("orderValueByUser", this.orderValueByUserIndex);
            this.unreadMessageCountIndex = getValidColumnIndex(path, table, "MessageListBean", "unreadMessageCount");
            indicesMap.put("unreadMessageCount", this.unreadMessageCountIndex);
            this.sendUserLoginNameIndex = getValidColumnIndex(path, table, "MessageListBean", "sendUserLoginName");
            indicesMap.put("sendUserLoginName", this.sendUserLoginNameIndex);
            this.sendUserIdIndex = getValidColumnIndex(path, table, "MessageListBean", "sendUserId");
            indicesMap.put("sendUserId", this.sendUserIdIndex);
            this.soleIdIndex = getValidColumnIndex(path, table, "MessageListBean", "soleId");
            indicesMap.put("soleId", this.soleIdIndex);
            this.userIdIndex = getValidColumnIndex(path, table, "MessageListBean", "userId");
            indicesMap.put("userId", this.userIdIndex);
            this.groupIdAndTypeIndex = getValidColumnIndex(path, table, "MessageListBean", "groupIdAndType");
            indicesMap.put("groupIdAndType", this.groupIdAndTypeIndex);
            this.roomNameIndex = getValidColumnIndex(path, table, "MessageListBean", "roomName");
            indicesMap.put("roomName", this.roomNameIndex);
            this.photoUrlIndex = getValidColumnIndex(path, table, "MessageListBean", "photoUrl");
            indicesMap.put("photoUrl", this.photoUrlIndex);
            this.showCouponIndex = getValidColumnIndex(path, table, "MessageListBean", "showCoupon");
            indicesMap.put("showCoupon", this.showCouponIndex);
            this.gmtCreateIndex = getValidColumnIndex(path, table, "MessageListBean", "gmtCreate");
            indicesMap.put("gmtCreate", this.gmtCreateIndex);
            this.originGmtCreateIndex = getValidColumnIndex(path, table, "MessageListBean", "originGmtCreate");
            indicesMap.put("originGmtCreate", this.originGmtCreateIndex);
            this.lastMessageCreateTimeIndex = getValidColumnIndex(path, table, "MessageListBean", "lastMessageCreateTime");
            indicesMap.put("lastMessageCreateTime", this.lastMessageCreateTimeIndex);
            this.lastMessageContentIndex = getValidColumnIndex(path, table, "MessageListBean", "lastMessageContent");
            indicesMap.put("lastMessageContent", this.lastMessageContentIndex);
            this.memberNumIndex = getValidColumnIndex(path, table, "MessageListBean", "memberNum");
            indicesMap.put("memberNum", this.memberNumIndex);
            this.groupChatMessageTypeIndex = getValidColumnIndex(path, table, "MessageListBean", "groupChatMessageType");
            indicesMap.put("groupChatMessageType", this.groupChatMessageTypeIndex);
            this.systemMessageIndex = getValidColumnIndex(path, table, "MessageListBean", "systemMessage");
            indicesMap.put("systemMessage", this.systemMessageIndex);

            setIndicesMap(indicesMap);
        }

        @Override
        public final void copyColumnInfoFrom(ColumnInfo other) {
            final MessageListBeanColumnInfo otherInfo = (MessageListBeanColumnInfo) other;
            this.groupIdIndex = otherInfo.groupIdIndex;
            this.orderValueByUserIndex = otherInfo.orderValueByUserIndex;
            this.unreadMessageCountIndex = otherInfo.unreadMessageCountIndex;
            this.sendUserLoginNameIndex = otherInfo.sendUserLoginNameIndex;
            this.sendUserIdIndex = otherInfo.sendUserIdIndex;
            this.soleIdIndex = otherInfo.soleIdIndex;
            this.userIdIndex = otherInfo.userIdIndex;
            this.groupIdAndTypeIndex = otherInfo.groupIdAndTypeIndex;
            this.roomNameIndex = otherInfo.roomNameIndex;
            this.photoUrlIndex = otherInfo.photoUrlIndex;
            this.showCouponIndex = otherInfo.showCouponIndex;
            this.gmtCreateIndex = otherInfo.gmtCreateIndex;
            this.originGmtCreateIndex = otherInfo.originGmtCreateIndex;
            this.lastMessageCreateTimeIndex = otherInfo.lastMessageCreateTimeIndex;
            this.lastMessageContentIndex = otherInfo.lastMessageContentIndex;
            this.memberNumIndex = otherInfo.memberNumIndex;
            this.groupChatMessageTypeIndex = otherInfo.groupChatMessageTypeIndex;
            this.systemMessageIndex = otherInfo.systemMessageIndex;

            setIndicesMap(otherInfo.getIndicesMap());
        }

        @Override
        public final MessageListBeanColumnInfo clone() {
            return (MessageListBeanColumnInfo) super.clone();
        }

    }
    private MessageListBeanColumnInfo columnInfo;
    private ProxyState proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("groupId");
        fieldNames.add("orderValueByUser");
        fieldNames.add("unreadMessageCount");
        fieldNames.add("sendUserLoginName");
        fieldNames.add("sendUserId");
        fieldNames.add("soleId");
        fieldNames.add("userId");
        fieldNames.add("groupIdAndType");
        fieldNames.add("roomName");
        fieldNames.add("photoUrl");
        fieldNames.add("showCoupon");
        fieldNames.add("gmtCreate");
        fieldNames.add("originGmtCreate");
        fieldNames.add("lastMessageCreateTime");
        fieldNames.add("lastMessageContent");
        fieldNames.add("memberNum");
        fieldNames.add("groupChatMessageType");
        fieldNames.add("systemMessage");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    MessageListBeanRealmProxy() {
        if (proxyState == null) {
            injectObjectContext();
        }
        proxyState.setConstructionFinished();
    }

    private void injectObjectContext() {
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (MessageListBeanColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState(com.android.banana.groupchat.bean.MessageListBean.class, this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @SuppressWarnings("cast")
    public String realmGet$groupId() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.groupIdIndex);
    }

    public void realmSet$groupId(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.groupIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.groupIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.groupIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.groupIdIndex, value);
    }

    @SuppressWarnings("cast")
    public long realmGet$orderValueByUser() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.orderValueByUserIndex);
    }

    public void realmSet$orderValueByUser(long value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.orderValueByUserIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.orderValueByUserIndex, value);
    }

    @SuppressWarnings("cast")
    public int realmGet$unreadMessageCount() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.unreadMessageCountIndex);
    }

    public void realmSet$unreadMessageCount(int value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.unreadMessageCountIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.unreadMessageCountIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$sendUserLoginName() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.sendUserLoginNameIndex);
    }

    public void realmSet$sendUserLoginName(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.sendUserLoginNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.sendUserLoginNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.sendUserLoginNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.sendUserLoginNameIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$sendUserId() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.sendUserIdIndex);
    }

    public void realmSet$sendUserId(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.sendUserIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.sendUserIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.sendUserIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.sendUserIdIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$soleId() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.soleIdIndex);
    }

    public void realmSet$soleId(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.soleIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.soleIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.soleIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.soleIdIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$userId() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.userIdIndex);
    }

    public void realmSet$userId(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.userIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.userIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.userIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.userIdIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$groupIdAndType() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.groupIdAndTypeIndex);
    }

    public void realmSet$groupIdAndType(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'groupIdAndType' cannot be changed after object was created.");
    }

    @SuppressWarnings("cast")
    public String realmGet$roomName() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.roomNameIndex);
    }

    public void realmSet$roomName(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.roomNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.roomNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.roomNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.roomNameIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$photoUrl() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.photoUrlIndex);
    }

    public void realmSet$photoUrl(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.photoUrlIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.photoUrlIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.photoUrlIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.photoUrlIndex, value);
    }

    @SuppressWarnings("cast")
    public boolean realmGet$showCoupon() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.showCouponIndex);
    }

    public void realmSet$showCoupon(boolean value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.showCouponIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.showCouponIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$gmtCreate() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.gmtCreateIndex);
    }

    public void realmSet$gmtCreate(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.gmtCreateIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.gmtCreateIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.gmtCreateIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.gmtCreateIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$originGmtCreate() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.originGmtCreateIndex);
    }

    public void realmSet$originGmtCreate(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.originGmtCreateIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.originGmtCreateIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.originGmtCreateIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.originGmtCreateIndex, value);
    }

    @SuppressWarnings("cast")
    public Date realmGet$lastMessageCreateTime() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNull(columnInfo.lastMessageCreateTimeIndex)) {
            return null;
        }
        return (java.util.Date) proxyState.getRow$realm().getDate(columnInfo.lastMessageCreateTimeIndex);
    }

    public void realmSet$lastMessageCreateTime(Date value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.lastMessageCreateTimeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setDate(columnInfo.lastMessageCreateTimeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.lastMessageCreateTimeIndex);
            return;
        }
        proxyState.getRow$realm().setDate(columnInfo.lastMessageCreateTimeIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$lastMessageContent() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.lastMessageContentIndex);
    }

    public void realmSet$lastMessageContent(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.lastMessageContentIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.lastMessageContentIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.lastMessageContentIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.lastMessageContentIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$memberNum() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.memberNumIndex);
    }

    public void realmSet$memberNum(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.memberNumIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.memberNumIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.memberNumIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.memberNumIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$groupChatMessageType() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.groupChatMessageTypeIndex);
    }

    public void realmSet$groupChatMessageType(String value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.groupChatMessageTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.groupChatMessageTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.groupChatMessageTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.groupChatMessageTypeIndex, value);
    }

    @SuppressWarnings("cast")
    public boolean realmGet$systemMessage() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.systemMessageIndex);
    }

    public void realmSet$systemMessage(boolean value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.systemMessageIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.systemMessageIndex, value);
    }

    public static RealmObjectSchema createRealmObjectSchema(RealmSchema realmSchema) {
        if (!realmSchema.contains("MessageListBean")) {
            RealmObjectSchema realmObjectSchema = realmSchema.create("MessageListBean");
            realmObjectSchema.add(new Property("groupId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("orderValueByUser", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED));
            realmObjectSchema.add(new Property("unreadMessageCount", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED));
            realmObjectSchema.add(new Property("sendUserLoginName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("sendUserId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("soleId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("userId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("groupIdAndType", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("roomName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("photoUrl", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("showCoupon", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED));
            realmObjectSchema.add(new Property("gmtCreate", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("originGmtCreate", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("lastMessageCreateTime", RealmFieldType.DATE, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("lastMessageContent", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("memberNum", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("groupChatMessageType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("systemMessage", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED));
            return realmObjectSchema;
        }
        return realmSchema.get("MessageListBean");
    }

    public static Table initTable(SharedRealm sharedRealm) {
        if (!sharedRealm.hasTable("class_MessageListBean")) {
            Table table = sharedRealm.getTable("class_MessageListBean");
            table.addColumn(RealmFieldType.STRING, "groupId", Table.NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "orderValueByUser", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "unreadMessageCount", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.STRING, "sendUserLoginName", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "sendUserId", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "soleId", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "userId", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "groupIdAndType", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "roomName", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "photoUrl", Table.NULLABLE);
            table.addColumn(RealmFieldType.BOOLEAN, "showCoupon", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.STRING, "gmtCreate", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "originGmtCreate", Table.NULLABLE);
            table.addColumn(RealmFieldType.DATE, "lastMessageCreateTime", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "lastMessageContent", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "memberNum", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "groupChatMessageType", Table.NULLABLE);
            table.addColumn(RealmFieldType.BOOLEAN, "systemMessage", Table.NOT_NULLABLE);
            table.addSearchIndex(table.getColumnIndex("groupIdAndType"));
            table.setPrimaryKey("groupIdAndType");
            return table;
        }
        return sharedRealm.getTable("class_MessageListBean");
    }

    public static MessageListBeanColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (sharedRealm.hasTable("class_MessageListBean")) {
            Table table = sharedRealm.getTable("class_MessageListBean");
            final long columnCount = table.getColumnCount();
            if (columnCount != 18) {
                if (columnCount < 18) {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 18 but was " + columnCount);
                }
                if (allowExtraColumns) {
                    RealmLog.debug("Field count is more than expected - expected 18 but was %1$d", columnCount);
                } else {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 18 but was " + columnCount);
                }
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < columnCount; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final MessageListBeanColumnInfo columnInfo = new MessageListBeanColumnInfo(sharedRealm.getPath(), table);

            if (!columnTypes.containsKey("groupId")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'groupId' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("groupId") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'groupId' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.groupIdIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'groupId' is required. Either set @Required to field 'groupId' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("orderValueByUser")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'orderValueByUser' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("orderValueByUser") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'long' for field 'orderValueByUser' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.orderValueByUserIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'orderValueByUser' does support null values in the existing Realm file. Use corresponding boxed type for field 'orderValueByUser' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("unreadMessageCount")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'unreadMessageCount' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("unreadMessageCount") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'int' for field 'unreadMessageCount' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.unreadMessageCountIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'unreadMessageCount' does support null values in the existing Realm file. Use corresponding boxed type for field 'unreadMessageCount' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("sendUserLoginName")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'sendUserLoginName' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("sendUserLoginName") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'sendUserLoginName' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.sendUserLoginNameIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'sendUserLoginName' is required. Either set @Required to field 'sendUserLoginName' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("sendUserId")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'sendUserId' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("sendUserId") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'sendUserId' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.sendUserIdIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'sendUserId' is required. Either set @Required to field 'sendUserId' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("soleId")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'soleId' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("soleId") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'soleId' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.soleIdIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'soleId' is required. Either set @Required to field 'soleId' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("userId")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'userId' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("userId") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'userId' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.userIdIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'userId' is required. Either set @Required to field 'userId' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("groupIdAndType")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'groupIdAndType' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("groupIdAndType") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'groupIdAndType' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.groupIdAndTypeIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(),"@PrimaryKey field 'groupIdAndType' does not support null values in the existing Realm file. Migrate using RealmObjectSchema.setNullable(), or mark the field as @Required.");
            }
            if (table.getPrimaryKey() != table.getColumnIndex("groupIdAndType")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Primary key not defined for field 'groupIdAndType' in existing Realm file. Add @PrimaryKey.");
            }
            if (!table.hasSearchIndex(table.getColumnIndex("groupIdAndType"))) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Index not defined for field 'groupIdAndType' in existing Realm file. Either set @Index or migrate using io.realm.internal.Table.removeSearchIndex().");
            }
            if (!columnTypes.containsKey("roomName")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'roomName' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("roomName") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'roomName' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.roomNameIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'roomName' is required. Either set @Required to field 'roomName' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("photoUrl")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'photoUrl' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("photoUrl") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'photoUrl' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.photoUrlIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'photoUrl' is required. Either set @Required to field 'photoUrl' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("showCoupon")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'showCoupon' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("showCoupon") != RealmFieldType.BOOLEAN) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'boolean' for field 'showCoupon' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.showCouponIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'showCoupon' does support null values in the existing Realm file. Use corresponding boxed type for field 'showCoupon' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("gmtCreate")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'gmtCreate' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("gmtCreate") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'gmtCreate' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.gmtCreateIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'gmtCreate' is required. Either set @Required to field 'gmtCreate' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("originGmtCreate")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'originGmtCreate' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("originGmtCreate") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'originGmtCreate' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.originGmtCreateIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'originGmtCreate' is required. Either set @Required to field 'originGmtCreate' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("lastMessageCreateTime")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'lastMessageCreateTime' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("lastMessageCreateTime") != RealmFieldType.DATE) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'Date' for field 'lastMessageCreateTime' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.lastMessageCreateTimeIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'lastMessageCreateTime' is required. Either set @Required to field 'lastMessageCreateTime' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("lastMessageContent")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'lastMessageContent' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("lastMessageContent") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'lastMessageContent' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.lastMessageContentIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'lastMessageContent' is required. Either set @Required to field 'lastMessageContent' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("memberNum")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'memberNum' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("memberNum") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'memberNum' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.memberNumIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'memberNum' is required. Either set @Required to field 'memberNum' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("groupChatMessageType")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'groupChatMessageType' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("groupChatMessageType") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'groupChatMessageType' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.groupChatMessageTypeIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'groupChatMessageType' is required. Either set @Required to field 'groupChatMessageType' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("systemMessage")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'systemMessage' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("systemMessage") != RealmFieldType.BOOLEAN) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'boolean' for field 'systemMessage' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.systemMessageIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'systemMessage' does support null values in the existing Realm file. Use corresponding boxed type for field 'systemMessage' or migrate using RealmObjectSchema.setNullable().");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'MessageListBean' class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_MessageListBean";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static com.android.banana.groupchat.bean.MessageListBean createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.android.banana.groupchat.bean.MessageListBean obj = null;
        if (update) {
            Table table = realm.getTable(com.android.banana.groupchat.bean.MessageListBean.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = TableOrView.NO_MATCH;
            if (json.isNull("groupIdAndType")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("groupIdAndType"));
            }
            if (rowIndex != TableOrView.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.schema.getColumnInfo(com.android.banana.groupchat.bean.MessageListBean.class), false, Collections.<String> emptyList());
                    obj = new io.realm.MessageListBeanRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("groupIdAndType")) {
                if (json.isNull("groupIdAndType")) {
                    obj = (io.realm.MessageListBeanRealmProxy) realm.createObjectInternal(com.android.banana.groupchat.bean.MessageListBean.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.MessageListBeanRealmProxy) realm.createObjectInternal(com.android.banana.groupchat.bean.MessageListBean.class, json.getString("groupIdAndType"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'groupIdAndType'.");
            }
        }
        if (json.has("groupId")) {
            if (json.isNull("groupId")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$groupId(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$groupId((String) json.getString("groupId"));
            }
        }
        if (json.has("orderValueByUser")) {
            if (json.isNull("orderValueByUser")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'orderValueByUser' to null.");
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$orderValueByUser((long) json.getLong("orderValueByUser"));
            }
        }
        if (json.has("unreadMessageCount")) {
            if (json.isNull("unreadMessageCount")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'unreadMessageCount' to null.");
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$unreadMessageCount((int) json.getInt("unreadMessageCount"));
            }
        }
        if (json.has("sendUserLoginName")) {
            if (json.isNull("sendUserLoginName")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$sendUserLoginName(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$sendUserLoginName((String) json.getString("sendUserLoginName"));
            }
        }
        if (json.has("sendUserId")) {
            if (json.isNull("sendUserId")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$sendUserId(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$sendUserId((String) json.getString("sendUserId"));
            }
        }
        if (json.has("soleId")) {
            if (json.isNull("soleId")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$soleId(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$soleId((String) json.getString("soleId"));
            }
        }
        if (json.has("userId")) {
            if (json.isNull("userId")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$userId(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$userId((String) json.getString("userId"));
            }
        }
        if (json.has("roomName")) {
            if (json.isNull("roomName")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$roomName(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$roomName((String) json.getString("roomName"));
            }
        }
        if (json.has("photoUrl")) {
            if (json.isNull("photoUrl")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$photoUrl(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$photoUrl((String) json.getString("photoUrl"));
            }
        }
        if (json.has("showCoupon")) {
            if (json.isNull("showCoupon")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'showCoupon' to null.");
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$showCoupon((boolean) json.getBoolean("showCoupon"));
            }
        }
        if (json.has("gmtCreate")) {
            if (json.isNull("gmtCreate")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$gmtCreate(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$gmtCreate((String) json.getString("gmtCreate"));
            }
        }
        if (json.has("originGmtCreate")) {
            if (json.isNull("originGmtCreate")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$originGmtCreate(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$originGmtCreate((String) json.getString("originGmtCreate"));
            }
        }
        if (json.has("lastMessageCreateTime")) {
            if (json.isNull("lastMessageCreateTime")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$lastMessageCreateTime(null);
            } else {
                Object timestamp = json.get("lastMessageCreateTime");
                if (timestamp instanceof String) {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$lastMessageCreateTime(JsonUtils.stringToDate((String) timestamp));
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$lastMessageCreateTime(new Date(json.getLong("lastMessageCreateTime")));
                }
            }
        }
        if (json.has("lastMessageContent")) {
            if (json.isNull("lastMessageContent")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$lastMessageContent(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$lastMessageContent((String) json.getString("lastMessageContent"));
            }
        }
        if (json.has("memberNum")) {
            if (json.isNull("memberNum")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$memberNum(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$memberNum((String) json.getString("memberNum"));
            }
        }
        if (json.has("groupChatMessageType")) {
            if (json.isNull("groupChatMessageType")) {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$groupChatMessageType(null);
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$groupChatMessageType((String) json.getString("groupChatMessageType"));
            }
        }
        if (json.has("systemMessage")) {
            if (json.isNull("systemMessage")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'systemMessage' to null.");
            } else {
                ((MessageListBeanRealmProxyInterface) obj).realmSet$systemMessage((boolean) json.getBoolean("systemMessage"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.android.banana.groupchat.bean.MessageListBean createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        com.android.banana.groupchat.bean.MessageListBean obj = new com.android.banana.groupchat.bean.MessageListBean();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("groupId")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$groupId(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$groupId((String) reader.nextString());
                }
            } else if (name.equals("orderValueByUser")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'orderValueByUser' to null.");
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$orderValueByUser((long) reader.nextLong());
                }
            } else if (name.equals("unreadMessageCount")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'unreadMessageCount' to null.");
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$unreadMessageCount((int) reader.nextInt());
                }
            } else if (name.equals("sendUserLoginName")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$sendUserLoginName(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$sendUserLoginName((String) reader.nextString());
                }
            } else if (name.equals("sendUserId")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$sendUserId(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$sendUserId((String) reader.nextString());
                }
            } else if (name.equals("soleId")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$soleId(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$soleId((String) reader.nextString());
                }
            } else if (name.equals("userId")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$userId(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$userId((String) reader.nextString());
                }
            } else if (name.equals("groupIdAndType")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$groupIdAndType(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$groupIdAndType((String) reader.nextString());
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("roomName")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$roomName(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$roomName((String) reader.nextString());
                }
            } else if (name.equals("photoUrl")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$photoUrl(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$photoUrl((String) reader.nextString());
                }
            } else if (name.equals("showCoupon")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'showCoupon' to null.");
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$showCoupon((boolean) reader.nextBoolean());
                }
            } else if (name.equals("gmtCreate")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$gmtCreate(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$gmtCreate((String) reader.nextString());
                }
            } else if (name.equals("originGmtCreate")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$originGmtCreate(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$originGmtCreate((String) reader.nextString());
                }
            } else if (name.equals("lastMessageCreateTime")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$lastMessageCreateTime(null);
                } else if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        ((MessageListBeanRealmProxyInterface) obj).realmSet$lastMessageCreateTime(new Date(timestamp));
                    }
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$lastMessageCreateTime(JsonUtils.stringToDate(reader.nextString()));
                }
            } else if (name.equals("lastMessageContent")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$lastMessageContent(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$lastMessageContent((String) reader.nextString());
                }
            } else if (name.equals("memberNum")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$memberNum(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$memberNum((String) reader.nextString());
                }
            } else if (name.equals("groupChatMessageType")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$groupChatMessageType(null);
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$groupChatMessageType((String) reader.nextString());
                }
            } else if (name.equals("systemMessage")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'systemMessage' to null.");
                } else {
                    ((MessageListBeanRealmProxyInterface) obj).realmSet$systemMessage((boolean) reader.nextBoolean());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'groupIdAndType'.");
        }
        obj = realm.copyToRealm(obj);
        return obj;
    }

    public static com.android.banana.groupchat.bean.MessageListBean copyOrUpdate(Realm realm, com.android.banana.groupchat.bean.MessageListBean object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.android.banana.groupchat.bean.MessageListBean) cachedRealmObject;
        } else {
            com.android.banana.groupchat.bean.MessageListBean realmObject = null;
            boolean canUpdate = update;
            if (canUpdate) {
                Table table = realm.getTable(com.android.banana.groupchat.bean.MessageListBean.class);
                long pkColumnIndex = table.getPrimaryKey();
                String value = ((MessageListBeanRealmProxyInterface) object).realmGet$groupIdAndType();
                long rowIndex = TableOrView.NO_MATCH;
                if (value == null) {
                    rowIndex = table.findFirstNull(pkColumnIndex);
                } else {
                    rowIndex = table.findFirstString(pkColumnIndex, value);
                }
                if (rowIndex != TableOrView.NO_MATCH) {
                    try {
                        objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.schema.getColumnInfo(com.android.banana.groupchat.bean.MessageListBean.class), false, Collections.<String> emptyList());
                        realmObject = new io.realm.MessageListBeanRealmProxy();
                        cache.put(object, (RealmObjectProxy) realmObject);
                    } finally {
                        objectContext.clear();
                    }
                } else {
                    canUpdate = false;
                }
            }

            if (canUpdate) {
                return update(realm, realmObject, object, cache);
            } else {
                return copy(realm, object, update, cache);
            }
        }
    }

    public static com.android.banana.groupchat.bean.MessageListBean copy(Realm realm, com.android.banana.groupchat.bean.MessageListBean newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.android.banana.groupchat.bean.MessageListBean) cachedRealmObject;
        } else {
            // rejecting default values to avoid creating unexpected objects from RealmModel/RealmList fields.
            com.android.banana.groupchat.bean.MessageListBean realmObject = realm.createObjectInternal(com.android.banana.groupchat.bean.MessageListBean.class, ((MessageListBeanRealmProxyInterface) newObject).realmGet$groupIdAndType(), false, Collections.<String>emptyList());
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$groupId(((MessageListBeanRealmProxyInterface) newObject).realmGet$groupId());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$orderValueByUser(((MessageListBeanRealmProxyInterface) newObject).realmGet$orderValueByUser());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$unreadMessageCount(((MessageListBeanRealmProxyInterface) newObject).realmGet$unreadMessageCount());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$sendUserLoginName(((MessageListBeanRealmProxyInterface) newObject).realmGet$sendUserLoginName());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$sendUserId(((MessageListBeanRealmProxyInterface) newObject).realmGet$sendUserId());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$soleId(((MessageListBeanRealmProxyInterface) newObject).realmGet$soleId());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$userId(((MessageListBeanRealmProxyInterface) newObject).realmGet$userId());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$roomName(((MessageListBeanRealmProxyInterface) newObject).realmGet$roomName());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$photoUrl(((MessageListBeanRealmProxyInterface) newObject).realmGet$photoUrl());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$showCoupon(((MessageListBeanRealmProxyInterface) newObject).realmGet$showCoupon());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$gmtCreate(((MessageListBeanRealmProxyInterface) newObject).realmGet$gmtCreate());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$originGmtCreate(((MessageListBeanRealmProxyInterface) newObject).realmGet$originGmtCreate());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$lastMessageCreateTime(((MessageListBeanRealmProxyInterface) newObject).realmGet$lastMessageCreateTime());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$lastMessageContent(((MessageListBeanRealmProxyInterface) newObject).realmGet$lastMessageContent());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$memberNum(((MessageListBeanRealmProxyInterface) newObject).realmGet$memberNum());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$groupChatMessageType(((MessageListBeanRealmProxyInterface) newObject).realmGet$groupChatMessageType());
            ((MessageListBeanRealmProxyInterface) realmObject).realmSet$systemMessage(((MessageListBeanRealmProxyInterface) newObject).realmGet$systemMessage());
            return realmObject;
        }
    }

    public static long insert(Realm realm, com.android.banana.groupchat.bean.MessageListBean object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.android.banana.groupchat.bean.MessageListBean.class);
        long tableNativePtr = table.getNativeTablePointer();
        MessageListBeanColumnInfo columnInfo = (MessageListBeanColumnInfo) realm.schema.getColumnInfo(com.android.banana.groupchat.bean.MessageListBean.class);
        long pkColumnIndex = table.getPrimaryKey();
        String primaryKeyValue = ((MessageListBeanRealmProxyInterface) object).realmGet$groupIdAndType();
        long rowIndex = TableOrView.NO_MATCH;
        if (primaryKeyValue == null) {
            rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
        } else {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
        }
        if (rowIndex == TableOrView.NO_MATCH) {
            rowIndex = table.addEmptyRowWithPrimaryKey(primaryKeyValue, false);
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$groupId = ((MessageListBeanRealmProxyInterface)object).realmGet$groupId();
        if (realmGet$groupId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.groupIdIndex, rowIndex, realmGet$groupId, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.orderValueByUserIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$orderValueByUser(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.unreadMessageCountIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$unreadMessageCount(), false);
        String realmGet$sendUserLoginName = ((MessageListBeanRealmProxyInterface)object).realmGet$sendUserLoginName();
        if (realmGet$sendUserLoginName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.sendUserLoginNameIndex, rowIndex, realmGet$sendUserLoginName, false);
        }
        String realmGet$sendUserId = ((MessageListBeanRealmProxyInterface)object).realmGet$sendUserId();
        if (realmGet$sendUserId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.sendUserIdIndex, rowIndex, realmGet$sendUserId, false);
        }
        String realmGet$soleId = ((MessageListBeanRealmProxyInterface)object).realmGet$soleId();
        if (realmGet$soleId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.soleIdIndex, rowIndex, realmGet$soleId, false);
        }
        String realmGet$userId = ((MessageListBeanRealmProxyInterface)object).realmGet$userId();
        if (realmGet$userId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.userIdIndex, rowIndex, realmGet$userId, false);
        }
        String realmGet$roomName = ((MessageListBeanRealmProxyInterface)object).realmGet$roomName();
        if (realmGet$roomName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.roomNameIndex, rowIndex, realmGet$roomName, false);
        }
        String realmGet$photoUrl = ((MessageListBeanRealmProxyInterface)object).realmGet$photoUrl();
        if (realmGet$photoUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.photoUrlIndex, rowIndex, realmGet$photoUrl, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.showCouponIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$showCoupon(), false);
        String realmGet$gmtCreate = ((MessageListBeanRealmProxyInterface)object).realmGet$gmtCreate();
        if (realmGet$gmtCreate != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.gmtCreateIndex, rowIndex, realmGet$gmtCreate, false);
        }
        String realmGet$originGmtCreate = ((MessageListBeanRealmProxyInterface)object).realmGet$originGmtCreate();
        if (realmGet$originGmtCreate != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.originGmtCreateIndex, rowIndex, realmGet$originGmtCreate, false);
        }
        java.util.Date realmGet$lastMessageCreateTime = ((MessageListBeanRealmProxyInterface)object).realmGet$lastMessageCreateTime();
        if (realmGet$lastMessageCreateTime != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.lastMessageCreateTimeIndex, rowIndex, realmGet$lastMessageCreateTime.getTime(), false);
        }
        String realmGet$lastMessageContent = ((MessageListBeanRealmProxyInterface)object).realmGet$lastMessageContent();
        if (realmGet$lastMessageContent != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lastMessageContentIndex, rowIndex, realmGet$lastMessageContent, false);
        }
        String realmGet$memberNum = ((MessageListBeanRealmProxyInterface)object).realmGet$memberNum();
        if (realmGet$memberNum != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.memberNumIndex, rowIndex, realmGet$memberNum, false);
        }
        String realmGet$groupChatMessageType = ((MessageListBeanRealmProxyInterface)object).realmGet$groupChatMessageType();
        if (realmGet$groupChatMessageType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.groupChatMessageTypeIndex, rowIndex, realmGet$groupChatMessageType, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.systemMessageIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$systemMessage(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.android.banana.groupchat.bean.MessageListBean.class);
        long tableNativePtr = table.getNativeTablePointer();
        MessageListBeanColumnInfo columnInfo = (MessageListBeanColumnInfo) realm.schema.getColumnInfo(com.android.banana.groupchat.bean.MessageListBean.class);
        long pkColumnIndex = table.getPrimaryKey();
        com.android.banana.groupchat.bean.MessageListBean object = null;
        while (objects.hasNext()) {
            object = (com.android.banana.groupchat.bean.MessageListBean) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                String primaryKeyValue = ((MessageListBeanRealmProxyInterface) object).realmGet$groupIdAndType();
                long rowIndex = TableOrView.NO_MATCH;
                if (primaryKeyValue == null) {
                    rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
                } else {
                    rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
                }
                if (rowIndex == TableOrView.NO_MATCH) {
                    rowIndex = table.addEmptyRowWithPrimaryKey(primaryKeyValue, false);
                } else {
                    Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
                }
                cache.put(object, rowIndex);
                String realmGet$groupId = ((MessageListBeanRealmProxyInterface)object).realmGet$groupId();
                if (realmGet$groupId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.groupIdIndex, rowIndex, realmGet$groupId, false);
                }
                Table.nativeSetLong(tableNativePtr, columnInfo.orderValueByUserIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$orderValueByUser(), false);
                Table.nativeSetLong(tableNativePtr, columnInfo.unreadMessageCountIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$unreadMessageCount(), false);
                String realmGet$sendUserLoginName = ((MessageListBeanRealmProxyInterface)object).realmGet$sendUserLoginName();
                if (realmGet$sendUserLoginName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.sendUserLoginNameIndex, rowIndex, realmGet$sendUserLoginName, false);
                }
                String realmGet$sendUserId = ((MessageListBeanRealmProxyInterface)object).realmGet$sendUserId();
                if (realmGet$sendUserId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.sendUserIdIndex, rowIndex, realmGet$sendUserId, false);
                }
                String realmGet$soleId = ((MessageListBeanRealmProxyInterface)object).realmGet$soleId();
                if (realmGet$soleId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.soleIdIndex, rowIndex, realmGet$soleId, false);
                }
                String realmGet$userId = ((MessageListBeanRealmProxyInterface)object).realmGet$userId();
                if (realmGet$userId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.userIdIndex, rowIndex, realmGet$userId, false);
                }
                String realmGet$roomName = ((MessageListBeanRealmProxyInterface)object).realmGet$roomName();
                if (realmGet$roomName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.roomNameIndex, rowIndex, realmGet$roomName, false);
                }
                String realmGet$photoUrl = ((MessageListBeanRealmProxyInterface)object).realmGet$photoUrl();
                if (realmGet$photoUrl != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.photoUrlIndex, rowIndex, realmGet$photoUrl, false);
                }
                Table.nativeSetBoolean(tableNativePtr, columnInfo.showCouponIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$showCoupon(), false);
                String realmGet$gmtCreate = ((MessageListBeanRealmProxyInterface)object).realmGet$gmtCreate();
                if (realmGet$gmtCreate != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.gmtCreateIndex, rowIndex, realmGet$gmtCreate, false);
                }
                String realmGet$originGmtCreate = ((MessageListBeanRealmProxyInterface)object).realmGet$originGmtCreate();
                if (realmGet$originGmtCreate != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.originGmtCreateIndex, rowIndex, realmGet$originGmtCreate, false);
                }
                java.util.Date realmGet$lastMessageCreateTime = ((MessageListBeanRealmProxyInterface)object).realmGet$lastMessageCreateTime();
                if (realmGet$lastMessageCreateTime != null) {
                    Table.nativeSetTimestamp(tableNativePtr, columnInfo.lastMessageCreateTimeIndex, rowIndex, realmGet$lastMessageCreateTime.getTime(), false);
                }
                String realmGet$lastMessageContent = ((MessageListBeanRealmProxyInterface)object).realmGet$lastMessageContent();
                if (realmGet$lastMessageContent != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.lastMessageContentIndex, rowIndex, realmGet$lastMessageContent, false);
                }
                String realmGet$memberNum = ((MessageListBeanRealmProxyInterface)object).realmGet$memberNum();
                if (realmGet$memberNum != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.memberNumIndex, rowIndex, realmGet$memberNum, false);
                }
                String realmGet$groupChatMessageType = ((MessageListBeanRealmProxyInterface)object).realmGet$groupChatMessageType();
                if (realmGet$groupChatMessageType != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.groupChatMessageTypeIndex, rowIndex, realmGet$groupChatMessageType, false);
                }
                Table.nativeSetBoolean(tableNativePtr, columnInfo.systemMessageIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$systemMessage(), false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.android.banana.groupchat.bean.MessageListBean object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.android.banana.groupchat.bean.MessageListBean.class);
        long tableNativePtr = table.getNativeTablePointer();
        MessageListBeanColumnInfo columnInfo = (MessageListBeanColumnInfo) realm.schema.getColumnInfo(com.android.banana.groupchat.bean.MessageListBean.class);
        long pkColumnIndex = table.getPrimaryKey();
        String primaryKeyValue = ((MessageListBeanRealmProxyInterface) object).realmGet$groupIdAndType();
        long rowIndex = TableOrView.NO_MATCH;
        if (primaryKeyValue == null) {
            rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
        } else {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
        }
        if (rowIndex == TableOrView.NO_MATCH) {
            rowIndex = table.addEmptyRowWithPrimaryKey(primaryKeyValue, false);
        }
        cache.put(object, rowIndex);
        String realmGet$groupId = ((MessageListBeanRealmProxyInterface)object).realmGet$groupId();
        if (realmGet$groupId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.groupIdIndex, rowIndex, realmGet$groupId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.groupIdIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.orderValueByUserIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$orderValueByUser(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.unreadMessageCountIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$unreadMessageCount(), false);
        String realmGet$sendUserLoginName = ((MessageListBeanRealmProxyInterface)object).realmGet$sendUserLoginName();
        if (realmGet$sendUserLoginName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.sendUserLoginNameIndex, rowIndex, realmGet$sendUserLoginName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.sendUserLoginNameIndex, rowIndex, false);
        }
        String realmGet$sendUserId = ((MessageListBeanRealmProxyInterface)object).realmGet$sendUserId();
        if (realmGet$sendUserId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.sendUserIdIndex, rowIndex, realmGet$sendUserId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.sendUserIdIndex, rowIndex, false);
        }
        String realmGet$soleId = ((MessageListBeanRealmProxyInterface)object).realmGet$soleId();
        if (realmGet$soleId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.soleIdIndex, rowIndex, realmGet$soleId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.soleIdIndex, rowIndex, false);
        }
        String realmGet$userId = ((MessageListBeanRealmProxyInterface)object).realmGet$userId();
        if (realmGet$userId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.userIdIndex, rowIndex, realmGet$userId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.userIdIndex, rowIndex, false);
        }
        String realmGet$roomName = ((MessageListBeanRealmProxyInterface)object).realmGet$roomName();
        if (realmGet$roomName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.roomNameIndex, rowIndex, realmGet$roomName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.roomNameIndex, rowIndex, false);
        }
        String realmGet$photoUrl = ((MessageListBeanRealmProxyInterface)object).realmGet$photoUrl();
        if (realmGet$photoUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.photoUrlIndex, rowIndex, realmGet$photoUrl, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.photoUrlIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.showCouponIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$showCoupon(), false);
        String realmGet$gmtCreate = ((MessageListBeanRealmProxyInterface)object).realmGet$gmtCreate();
        if (realmGet$gmtCreate != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.gmtCreateIndex, rowIndex, realmGet$gmtCreate, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.gmtCreateIndex, rowIndex, false);
        }
        String realmGet$originGmtCreate = ((MessageListBeanRealmProxyInterface)object).realmGet$originGmtCreate();
        if (realmGet$originGmtCreate != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.originGmtCreateIndex, rowIndex, realmGet$originGmtCreate, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.originGmtCreateIndex, rowIndex, false);
        }
        java.util.Date realmGet$lastMessageCreateTime = ((MessageListBeanRealmProxyInterface)object).realmGet$lastMessageCreateTime();
        if (realmGet$lastMessageCreateTime != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.lastMessageCreateTimeIndex, rowIndex, realmGet$lastMessageCreateTime.getTime(), false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.lastMessageCreateTimeIndex, rowIndex, false);
        }
        String realmGet$lastMessageContent = ((MessageListBeanRealmProxyInterface)object).realmGet$lastMessageContent();
        if (realmGet$lastMessageContent != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lastMessageContentIndex, rowIndex, realmGet$lastMessageContent, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.lastMessageContentIndex, rowIndex, false);
        }
        String realmGet$memberNum = ((MessageListBeanRealmProxyInterface)object).realmGet$memberNum();
        if (realmGet$memberNum != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.memberNumIndex, rowIndex, realmGet$memberNum, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.memberNumIndex, rowIndex, false);
        }
        String realmGet$groupChatMessageType = ((MessageListBeanRealmProxyInterface)object).realmGet$groupChatMessageType();
        if (realmGet$groupChatMessageType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.groupChatMessageTypeIndex, rowIndex, realmGet$groupChatMessageType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.groupChatMessageTypeIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.systemMessageIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$systemMessage(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.android.banana.groupchat.bean.MessageListBean.class);
        long tableNativePtr = table.getNativeTablePointer();
        MessageListBeanColumnInfo columnInfo = (MessageListBeanColumnInfo) realm.schema.getColumnInfo(com.android.banana.groupchat.bean.MessageListBean.class);
        long pkColumnIndex = table.getPrimaryKey();
        com.android.banana.groupchat.bean.MessageListBean object = null;
        while (objects.hasNext()) {
            object = (com.android.banana.groupchat.bean.MessageListBean) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                String primaryKeyValue = ((MessageListBeanRealmProxyInterface) object).realmGet$groupIdAndType();
                long rowIndex = TableOrView.NO_MATCH;
                if (primaryKeyValue == null) {
                    rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
                } else {
                    rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
                }
                if (rowIndex == TableOrView.NO_MATCH) {
                    rowIndex = table.addEmptyRowWithPrimaryKey(primaryKeyValue, false);
                }
                cache.put(object, rowIndex);
                String realmGet$groupId = ((MessageListBeanRealmProxyInterface)object).realmGet$groupId();
                if (realmGet$groupId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.groupIdIndex, rowIndex, realmGet$groupId, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.groupIdIndex, rowIndex, false);
                }
                Table.nativeSetLong(tableNativePtr, columnInfo.orderValueByUserIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$orderValueByUser(), false);
                Table.nativeSetLong(tableNativePtr, columnInfo.unreadMessageCountIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$unreadMessageCount(), false);
                String realmGet$sendUserLoginName = ((MessageListBeanRealmProxyInterface)object).realmGet$sendUserLoginName();
                if (realmGet$sendUserLoginName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.sendUserLoginNameIndex, rowIndex, realmGet$sendUserLoginName, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.sendUserLoginNameIndex, rowIndex, false);
                }
                String realmGet$sendUserId = ((MessageListBeanRealmProxyInterface)object).realmGet$sendUserId();
                if (realmGet$sendUserId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.sendUserIdIndex, rowIndex, realmGet$sendUserId, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.sendUserIdIndex, rowIndex, false);
                }
                String realmGet$soleId = ((MessageListBeanRealmProxyInterface)object).realmGet$soleId();
                if (realmGet$soleId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.soleIdIndex, rowIndex, realmGet$soleId, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.soleIdIndex, rowIndex, false);
                }
                String realmGet$userId = ((MessageListBeanRealmProxyInterface)object).realmGet$userId();
                if (realmGet$userId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.userIdIndex, rowIndex, realmGet$userId, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.userIdIndex, rowIndex, false);
                }
                String realmGet$roomName = ((MessageListBeanRealmProxyInterface)object).realmGet$roomName();
                if (realmGet$roomName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.roomNameIndex, rowIndex, realmGet$roomName, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.roomNameIndex, rowIndex, false);
                }
                String realmGet$photoUrl = ((MessageListBeanRealmProxyInterface)object).realmGet$photoUrl();
                if (realmGet$photoUrl != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.photoUrlIndex, rowIndex, realmGet$photoUrl, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.photoUrlIndex, rowIndex, false);
                }
                Table.nativeSetBoolean(tableNativePtr, columnInfo.showCouponIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$showCoupon(), false);
                String realmGet$gmtCreate = ((MessageListBeanRealmProxyInterface)object).realmGet$gmtCreate();
                if (realmGet$gmtCreate != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.gmtCreateIndex, rowIndex, realmGet$gmtCreate, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.gmtCreateIndex, rowIndex, false);
                }
                String realmGet$originGmtCreate = ((MessageListBeanRealmProxyInterface)object).realmGet$originGmtCreate();
                if (realmGet$originGmtCreate != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.originGmtCreateIndex, rowIndex, realmGet$originGmtCreate, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.originGmtCreateIndex, rowIndex, false);
                }
                java.util.Date realmGet$lastMessageCreateTime = ((MessageListBeanRealmProxyInterface)object).realmGet$lastMessageCreateTime();
                if (realmGet$lastMessageCreateTime != null) {
                    Table.nativeSetTimestamp(tableNativePtr, columnInfo.lastMessageCreateTimeIndex, rowIndex, realmGet$lastMessageCreateTime.getTime(), false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.lastMessageCreateTimeIndex, rowIndex, false);
                }
                String realmGet$lastMessageContent = ((MessageListBeanRealmProxyInterface)object).realmGet$lastMessageContent();
                if (realmGet$lastMessageContent != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.lastMessageContentIndex, rowIndex, realmGet$lastMessageContent, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.lastMessageContentIndex, rowIndex, false);
                }
                String realmGet$memberNum = ((MessageListBeanRealmProxyInterface)object).realmGet$memberNum();
                if (realmGet$memberNum != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.memberNumIndex, rowIndex, realmGet$memberNum, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.memberNumIndex, rowIndex, false);
                }
                String realmGet$groupChatMessageType = ((MessageListBeanRealmProxyInterface)object).realmGet$groupChatMessageType();
                if (realmGet$groupChatMessageType != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.groupChatMessageTypeIndex, rowIndex, realmGet$groupChatMessageType, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.groupChatMessageTypeIndex, rowIndex, false);
                }
                Table.nativeSetBoolean(tableNativePtr, columnInfo.systemMessageIndex, rowIndex, ((MessageListBeanRealmProxyInterface)object).realmGet$systemMessage(), false);
            }
        }
    }

    public static com.android.banana.groupchat.bean.MessageListBean createDetachedCopy(com.android.banana.groupchat.bean.MessageListBean realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.android.banana.groupchat.bean.MessageListBean unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.android.banana.groupchat.bean.MessageListBean)cachedObject.object;
            } else {
                unmanagedObject = (com.android.banana.groupchat.bean.MessageListBean)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new com.android.banana.groupchat.bean.MessageListBean();
            cache.put(realmObject, new RealmObjectProxy.CacheData(currentDepth, unmanagedObject));
        }
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$groupId(((MessageListBeanRealmProxyInterface) realmObject).realmGet$groupId());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$orderValueByUser(((MessageListBeanRealmProxyInterface) realmObject).realmGet$orderValueByUser());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$unreadMessageCount(((MessageListBeanRealmProxyInterface) realmObject).realmGet$unreadMessageCount());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$sendUserLoginName(((MessageListBeanRealmProxyInterface) realmObject).realmGet$sendUserLoginName());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$sendUserId(((MessageListBeanRealmProxyInterface) realmObject).realmGet$sendUserId());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$soleId(((MessageListBeanRealmProxyInterface) realmObject).realmGet$soleId());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$userId(((MessageListBeanRealmProxyInterface) realmObject).realmGet$userId());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$groupIdAndType(((MessageListBeanRealmProxyInterface) realmObject).realmGet$groupIdAndType());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$roomName(((MessageListBeanRealmProxyInterface) realmObject).realmGet$roomName());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$photoUrl(((MessageListBeanRealmProxyInterface) realmObject).realmGet$photoUrl());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$showCoupon(((MessageListBeanRealmProxyInterface) realmObject).realmGet$showCoupon());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$gmtCreate(((MessageListBeanRealmProxyInterface) realmObject).realmGet$gmtCreate());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$originGmtCreate(((MessageListBeanRealmProxyInterface) realmObject).realmGet$originGmtCreate());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$lastMessageCreateTime(((MessageListBeanRealmProxyInterface) realmObject).realmGet$lastMessageCreateTime());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$lastMessageContent(((MessageListBeanRealmProxyInterface) realmObject).realmGet$lastMessageContent());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$memberNum(((MessageListBeanRealmProxyInterface) realmObject).realmGet$memberNum());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$groupChatMessageType(((MessageListBeanRealmProxyInterface) realmObject).realmGet$groupChatMessageType());
        ((MessageListBeanRealmProxyInterface) unmanagedObject).realmSet$systemMessage(((MessageListBeanRealmProxyInterface) realmObject).realmGet$systemMessage());
        return unmanagedObject;
    }

    static com.android.banana.groupchat.bean.MessageListBean update(Realm realm, com.android.banana.groupchat.bean.MessageListBean realmObject, com.android.banana.groupchat.bean.MessageListBean newObject, Map<RealmModel, RealmObjectProxy> cache) {
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$groupId(((MessageListBeanRealmProxyInterface) newObject).realmGet$groupId());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$orderValueByUser(((MessageListBeanRealmProxyInterface) newObject).realmGet$orderValueByUser());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$unreadMessageCount(((MessageListBeanRealmProxyInterface) newObject).realmGet$unreadMessageCount());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$sendUserLoginName(((MessageListBeanRealmProxyInterface) newObject).realmGet$sendUserLoginName());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$sendUserId(((MessageListBeanRealmProxyInterface) newObject).realmGet$sendUserId());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$soleId(((MessageListBeanRealmProxyInterface) newObject).realmGet$soleId());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$userId(((MessageListBeanRealmProxyInterface) newObject).realmGet$userId());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$roomName(((MessageListBeanRealmProxyInterface) newObject).realmGet$roomName());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$photoUrl(((MessageListBeanRealmProxyInterface) newObject).realmGet$photoUrl());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$showCoupon(((MessageListBeanRealmProxyInterface) newObject).realmGet$showCoupon());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$gmtCreate(((MessageListBeanRealmProxyInterface) newObject).realmGet$gmtCreate());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$originGmtCreate(((MessageListBeanRealmProxyInterface) newObject).realmGet$originGmtCreate());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$lastMessageCreateTime(((MessageListBeanRealmProxyInterface) newObject).realmGet$lastMessageCreateTime());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$lastMessageContent(((MessageListBeanRealmProxyInterface) newObject).realmGet$lastMessageContent());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$memberNum(((MessageListBeanRealmProxyInterface) newObject).realmGet$memberNum());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$groupChatMessageType(((MessageListBeanRealmProxyInterface) newObject).realmGet$groupChatMessageType());
        ((MessageListBeanRealmProxyInterface) realmObject).realmSet$systemMessage(((MessageListBeanRealmProxyInterface) newObject).realmGet$systemMessage());
        return realmObject;
    }

    @Override
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("MessageListBean = [");
        stringBuilder.append("{groupId:");
        stringBuilder.append(realmGet$groupId() != null ? realmGet$groupId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{orderValueByUser:");
        stringBuilder.append(realmGet$orderValueByUser());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{unreadMessageCount:");
        stringBuilder.append(realmGet$unreadMessageCount());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{sendUserLoginName:");
        stringBuilder.append(realmGet$sendUserLoginName() != null ? realmGet$sendUserLoginName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{sendUserId:");
        stringBuilder.append(realmGet$sendUserId() != null ? realmGet$sendUserId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{soleId:");
        stringBuilder.append(realmGet$soleId() != null ? realmGet$soleId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{userId:");
        stringBuilder.append(realmGet$userId() != null ? realmGet$userId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{groupIdAndType:");
        stringBuilder.append(realmGet$groupIdAndType() != null ? realmGet$groupIdAndType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{roomName:");
        stringBuilder.append(realmGet$roomName() != null ? realmGet$roomName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{photoUrl:");
        stringBuilder.append(realmGet$photoUrl() != null ? realmGet$photoUrl() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{showCoupon:");
        stringBuilder.append(realmGet$showCoupon());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{gmtCreate:");
        stringBuilder.append(realmGet$gmtCreate() != null ? realmGet$gmtCreate() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{originGmtCreate:");
        stringBuilder.append(realmGet$originGmtCreate() != null ? realmGet$originGmtCreate() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lastMessageCreateTime:");
        stringBuilder.append(realmGet$lastMessageCreateTime() != null ? realmGet$lastMessageCreateTime() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lastMessageContent:");
        stringBuilder.append(realmGet$lastMessageContent() != null ? realmGet$lastMessageContent() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{memberNum:");
        stringBuilder.append(realmGet$memberNum() != null ? realmGet$memberNum() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{groupChatMessageType:");
        stringBuilder.append(realmGet$groupChatMessageType() != null ? realmGet$groupChatMessageType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{systemMessage:");
        stringBuilder.append(realmGet$systemMessage());
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageListBeanRealmProxy aMessageListBean = (MessageListBeanRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aMessageListBean.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aMessageListBean.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aMessageListBean.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
