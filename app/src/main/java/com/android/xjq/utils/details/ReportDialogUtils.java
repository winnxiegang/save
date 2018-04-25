package com.android.xjq.utils.details;

import android.content.Context;
import android.view.View;

import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.xjq.XjqApplication;
import com.android.xjq.dialog.ShowReportDialog;

/**
 * Created by zaozao on 2017/12/5.
 * 举报工具类
 */

public class ReportDialogUtils {

    public void showReportDialog(Context context) {

        ShowReportDialog.Builder builder = new ShowReportDialog.Builder();

        builder.setNegativeMessage("取消");
        builder.setReportClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibAppUtil.showTip(XjqApplication.getContext(),"举报成功");
            }
        });


//        builder.setReportClickListener(
//                new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String reportMemoEnum = "";
//
//                switch (v.getTag().toString()) {
//
//                    case ShowReportDialog.SPAM:
//                        reportMemoEnum = "SPAM";
//                        blackReportManage(mUserId, userName, commentId, reportMemoEnum);
//                        break;
//
//                    case ShowReportDialog.SENSITIVE_MESSAGE:
//                        reportMemoEnum = "SENSITIVE_MESSAGE";
//                        blackReportManage(mUserId, userName, commentId, reportMemoEnum);
//                        break;
//
//                    case ShowReportDialog.PORNOGRAPHIC:
//                        reportMemoEnum = "PORNOGRAPHIC";
//                        blackReportManage(mUserId, userName, commentId, reportMemoEnum);
//                        break;
//
//                    case ShowReportDialog.FALSE_MESSAGE:
//                        reportMemoEnum = "FALSE_MESSAGE";
//                        blackReportManage(mUserId, userName, commentId, reportMemoEnum);
//                        break;
//
//                    case ShowReportDialog.FALSE_PRIZE:
//                        reportMemoEnum = "FALSE_PRIZE";
//                        blackReportManage(mUserId, userName, commentId, reportMemoEnum);
//                        break;
//
//                    case ShowReportDialog.PERSON_ATTACK:
//                        reportMemoEnum = "PERSON_ATTACK";
//                        blackReportManage(mUserId, userName, commentId, reportMemoEnum);
//                        break;
//
//                }
//            }
//        });

        ShowReportDialog dialog = new ShowReportDialog(context, builder);

    }




    /**
     * 举报
     * @param reportMemo
     */
    protected void blackReportManage(String mUserId, String userName, String commentId, String reportMemo) {

        RequestFormBody map = new RequestFormBody(null,true);

        map.put("reportUserId", mUserId);

        map.put("reportUserName", userName);

        map.put("reportItemId", commentId);

        map.put("reportItemType", "COMMENT");

        map.put("reportMemo", reportMemo);

//        httpOperate.blackReportManage(map, true);
    }

}
