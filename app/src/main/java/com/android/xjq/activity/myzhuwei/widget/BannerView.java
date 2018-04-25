package com.android.xjq.activity.myzhuwei.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.bean.myzhuwei.BasketballRacesBean;
import com.android.xjq.bean.myzhuwei.FootballRacesBean;
import com.android.xjq.view.BaseCarouselViewGroup;

import java.util.List;

/**
 * Created by kokuma on 2017/11/6.
 */

public class BannerView extends BaseCarouselViewGroup {

    public View.OnClickListener onClickListener;

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void update(List mList) {
        if (mList == null || mList.size() == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
        super.update(mList);
    }

    @Override
    protected View createItemView(Object bean) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.item_banner_theme, null);
        ImageView btnDelete = (ImageView) root.findViewById(R.id.btnDelete);
        TextView changci_txt = (TextView) root.findViewById(R.id.changci_txt);
        TextView tvZhuName = (TextView) root.findViewById(R.id.tvZhuName);
        TextView tvKeName = (TextView) root.findViewById(R.id.tvKeName);

        setGroupViewMain(bean, changci_txt, tvZhuName, tvKeName, null);

        btnDelete.setTag(bean);
        btnDelete.setOnClickListener(onClickListener);
        return root;

    }


    @Override
    public void onClick(View view) {
        Object obj = view.getTag();
        // 删除数据
        //
    }

    public void setGroupViewMain(Object bean, TextView changci_txt, TextView tvZhuName, TextView tvKeName, @Nullable TextView ctr_name) {
        FootballRacesBean.FootballRaceClientSimpleListBean footBean = null;
        BasketballRacesBean.BasketballRaceClientSimpleListBean basketBean = null;
        String name1 = "";
        FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean gameBoardBean = null;
        if (bean instanceof FootballRacesBean.FootballRaceClientSimpleListBean) {
            footBean = (FootballRacesBean.FootballRaceClientSimpleListBean) bean;
            gameBoardBean = footBean.gameBoardBean;

            //名称
            tvZhuName.setText(footBean.getHomeTeamName());
            tvKeName.setText(footBean.getGuestTeamName());
            //赛事名、时间
            if (ctr_name != null) {
                ctr_name.setText(footBean.getMatchName());
            }

        } else if (bean instanceof BasketballRacesBean.BasketballRaceClientSimpleListBean) {
            basketBean = (BasketballRacesBean.BasketballRaceClientSimpleListBean) bean;
            gameBoardBean = basketBean.gameBoardBean;
            //名称
            tvZhuName.setText(basketBean.getHomeTeamName());
            tvKeName.setText(basketBean.getGuestTeamName());
            //赛事名、时间
            if (ctr_name != null) {
                ctr_name.setText(basketBean.getMatchName());
            }

        } else if (bean instanceof FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) {
            gameBoardBean = (FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) bean;
            //名称
            tvZhuName.setText(gameBoardBean.homeName);
            tvKeName.setText(gameBoardBean.guestName);
        } else if (bean instanceof GroupChatTopic1.RaceIdAndGameBoard) {
            GroupChatTopic1.RaceIdAndGameBoard gameBoard = (GroupChatTopic1.RaceIdAndGameBoard) bean;
            tvZhuName.setText(gameBoard.homeName);
            tvKeName.setText(gameBoard.guestName);

            if (gameBoard != null && gameBoard.raceStageType != null) {
                name1 = "[" + gameBoard.raceStageType.name + "]  ";
                if (gameBoard.raceType != null && gameBoard.raceType.equals("FOOTBALL")) {
                    name1 += "让球";
                } else {
                    name1 += "让分";
                }
                if (gameBoard.plate > 0) {
                    name1 += "+" + gameBoard.plate;
                } else {
                    name1 += gameBoard.plate;
                }
            }
            changci_txt.setText(name1);
            return;
        }
        try {
            if (gameBoardBean != null && gameBoardBean.getRaceStageType() != null) {
                name1 = "[" + gameBoardBean.getRaceStageType().getName() + "]  ";
                if (gameBoardBean.getRaceType() != null && gameBoardBean.getRaceType().equals("FOOTBALL")) {
                    name1 += "让球";
                } else {
                    name1 += "让分";
                }
                if (gameBoardBean.getPlate() > 0) {
                    name1 += "+" + gameBoardBean.getPlate();
                } else {
                    name1 += gameBoardBean.getPlate();
                }
                changci_txt.setText(name1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("xxl", "bug222");
        }

    }
}
