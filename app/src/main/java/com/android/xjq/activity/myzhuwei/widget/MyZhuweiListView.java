package com.android.xjq.activity.myzhuwei.widget;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.banana.groupchat.view.baselist.BaseListView;
import com.android.banana.groupchat.view.baselist.base.BaseViewHolder;
import com.android.xjq.R;
import com.android.xjq.activity.myzhuwei.ZhuweiDetailActivity;
import com.android.xjq.bean.myzhuwei.ZhuweiDetailBean;

/**
 * Created by kokuma on 2017/11/5.
 */

public class MyZhuweiListView extends BaseListView<ZhuweiDetailBean.PurchaseOrderBean> {

    public MyZhuweiListView(Context context) {
        super(context);
        initUI(R.layout.item_zhuwei);
    }

    public MyZhuweiListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(R.layout.item_zhuwei);
    }

    public MyZhuweiListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(R.layout.item_zhuwei);
    }

    @Override
    protected void setConvert(BaseViewHolder viewHolder, ZhuweiDetailBean.PurchaseOrderBean bean) {

        TextView changci_txt = viewHolder.getView(R.id.changci_txt);
        TextView tvTeams = viewHolder.getView(R.id.tvTeams);
        TextView tvFeng = viewHolder.getView(R.id.tvFeng);
        TextView tvMyTeam = viewHolder.getView(R.id.tvMyTeam);
        TextView tvHot = viewHolder.getView(R.id.tvHot);
        TextView tvStatus = viewHolder.getView(R.id.tvStatus);
        TextView ctr_time = viewHolder.getView(R.id.ctr_time);
        TextView tvRewardNum = viewHolder.getView(R.id.tvRewardNum);
        //
        if (bean.getGameBoardBean() != null) {
            ZhuweiDetailBean.GameBoardBean gameBoardBean = bean.getGameBoardBean();
            if (gameBoardBean.getRaceType() != null&&gameBoardBean.getHomeTeamName()!=null) {
                String txtFeng = gameBoardBean.getHomeTeamName(); //+ "让"
                if (gameBoardBean.getPlate() > 0) {
                    txtFeng += "<font color='#f7453d'>" + "+" + gameBoardBean.getPlate() + "</font>" ;
                } else {
                    txtFeng += "<font color='#009900'>" + gameBoardBean.getPlate()+ "</font>";
                }
                if (gameBoardBean.getRaceType() != null && gameBoardBean.getRaceType().equals("FOOTBALL")) {
                    txtFeng += "球";
                } else {
                    txtFeng += "分";
                }
                tvFeng.setText(Html.fromHtml(txtFeng+"，你支持哪支球队获胜？"));
                //名称、时间
                tvTeams.setText(gameBoardBean.getHomeTeamName()+" VS "+gameBoardBean.getGuestTeamName());
                if(!TextUtils.isEmpty(bean.getGmtCreate())){
                    String[] txts = bean.getGmtCreate().split(" ");
                    if(txts.length>1&&txts[0]!=null&&txts[1]!=null){
                        ctr_time.setText(txts[0]+"\n"+txts[1]+"发起");
                    }
                }
            }

            if (gameBoardBean.getRaceStageType() != null&&gameBoardBean.getRaceStageType().getName()!=null&&gameBoardBean.getHomeTeamName()!=null) {
                //让球让分
                String nameChang = "[" + gameBoardBean.getRaceStageType().getName() + "]比分";
                changci_txt.setText(nameChang);

                //赢平负
                if (bean.getEntryValue() != null  && bean.getEntryValue().equals("HOME_WIN")) {
                    tvMyTeam.setText("我助威："+gameBoardBean.getHomeTeamName());
                    tvHot.setText("助威值："+getIntNum("" + bean.getTotalFee()));
                } else if (bean.getEntryValue() != null  && bean.getEntryValue().equals("GUEST_WIN")) {
                    tvMyTeam.setText("我助威："+gameBoardBean.getGuestTeamName());
                    tvHot.setText("助威值："+getIntNum("" + bean.getTotalFee()));
                }
               // tvReward.setVisibility(VISIBLE);
                if (bean.getOrderStatus() != null) {
                    if ("WIN".equals(bean.getOrderStatus())) {
                        tvStatus.setText("成功");
                        tvRewardNum.setTextColor(getResources().getColor(R.color.main_red));
                        tvRewardNum.setText("+"+bean.getPrizeFee() + "");
                    } else if ("LOSE".equals(bean.getOrderStatus())) {
                        tvStatus.setText("惜败");
                        tvRewardNum.setTextColor(getResources().getColor(R.color.text_black));
                        String reword = bean.getPrizeFee()==0?"":bean.getPrizeFee() + "";
                        tvRewardNum.setText(reword);
                    } else if ("CANCEL_FINISH".equals(bean.getOrderStatus())) {
                        tvStatus.setText("流局");
                        tvRewardNum.setTextColor(getResources().getColor(R.color.text_black));
                        tvRewardNum.setText(""); // -
                    } else {
                        tvStatus.setText("");
                        tvRewardNum.setTextColor(getResources().getColor(R.color.text_black));
                        tvRewardNum.setText("");
                    }
                } else {
                   // tvReward.setVisibility(INVISIBLE);
                    tvRewardNum.setTextColor(getResources().getColor(R.color.text_black));
                    tvRewardNum.setText("");
                }

            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        super.onItemClick(adapterView, view, i, l);
        Object obj = adapterView.getItemAtPosition(i);
        if (obj != null) {
            ZhuweiDetailBean.PurchaseOrderBean bean = (ZhuweiDetailBean.PurchaseOrderBean) obj;
            Intent intent = new Intent(getContext(), ZhuweiDetailActivity.class);
            intent.putExtra("id", bean.getId());
            getContext().startActivity(intent);
        }
    }

    public static  String getIntNum(String txt){
        String intNum  = txt;
        if(!TextUtils.isEmpty(txt)){
            int index =  txt.indexOf(".");
            if (index>0) {
                intNum = txt.substring(0,index);
            }
        }
        return intNum;
    }
}
