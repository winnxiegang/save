package com.android.banana.groupchat.msglist.realm;


import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by zaozao on 2017/10/18.
 */

public class MyDatabaseMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {


        RealmSchema schema = realm.getSchema();

        if (oldVersion !=newVersion) {
            schema.get("MessageListBean")
                    .addField("userId", String.class);//在MessageListBean表中添加属性userId
            oldVersion ++;
        }

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MyDatabaseMigration;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}