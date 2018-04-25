package com.android.xjq.controller.schduledetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.adapter.schduledatail.PointLvAdapter;
import com.android.xjq.model.dynamic.JczqPointTypeEnum;
import com.android.xjq.bean.scheduledetail.JczqRankingBean;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.banana.commlib.http.AppParam.FT_API_S_URL;

/**
 * Created by laocao on 2016/5/27.
 */
public class JczqAnalysisPointController implements IHttpResponseListener<JczqRankingBean> {

    private int mCheckPoint = 0;

    private int mCheckCycle;

    private LayoutInflater inflater;
    private Context context;

    private String mInnerRaceId;

    private PointLvAdapter pointLvAdapter;

    private List<JczqRankingBean> list = new ArrayList<>();

    private JczqRankingBean jczqRankingBean;

    private WrapperHttpHelper httpHelper;

    private RadioButton totalPointsRbtn;

    private RadioGroup cycleLeagueRg;

    private RadioButton secondCycleRb;

    private RadioButton totalCycleRb;

    private View pointView;
    private ListView pointsLv;
    private RadioGroup pointsTableRg;
    private TextView noDataView;
    private JczqDataBean jczqDataBean;

    public JczqAnalysisPointController(Context context, JczqDataBean jczqDataBean) {

        this.context = context;
        this.jczqDataBean = jczqDataBean;

        this.inflater = LayoutInflater.from(context);

        httpHelper = new WrapperHttpHelper(this);

        getPointsView();
    }


    public View getPointsView() {
        if (pointView != null) {

            return pointView;

        }
        pointView = inflater.inflate(R.layout.activty_jczq_analysis_view_pager_points_item, null);

        noDataView = (TextView) pointView.findViewById(R.id.no_data);

        pointsTableRg = (RadioGroup) pointView.findViewById(R.id.PointsTableRg);

        pointsLv = (ListView) pointView.findViewById(R.id.listView);

        totalPointsRbtn = (RadioButton) pointView.findViewById(R.id.totalPointsRbtn);

        cycleLeagueRg = (RadioGroup) pointView.findViewById(R.id.cycleLeagueRg);

        secondCycleRb = ((RadioButton) pointView.findViewById(R.id.secondCycleRb));

        totalCycleRb = ((RadioButton) pointView.findViewById(R.id.totalCycleRb));

        pointsTableRg.setOnCheckedChangeListener(pointsCheckedListener);

        cycleLeagueRg.setOnCheckedChangeListener(pointsCheckedListener);

        pointLvAdapter = new PointLvAdapter(context, list);

        pointsLv.setAdapter(pointLvAdapter);

        return pointView;

    }


    //http://ftapi.huored.net/service.json?raceId=25308033&rankType=SR_AAR&service=TEAM_LEA_RANK_EXTRA_QUERY
    //http://ftapi.jczj123.com/service.json?raceId=4736112&rankType=SR_AAR&service=TEAM_LEA_RANK_EXTRA_QUERY
    public void getAnalysisPoints(String pointType, boolean showPg) {
        if ("0".equals(jczqDataBean.getInnerRaceId())) {
            return;
        }
        RequestFormBody  map = new RequestFormBody(XjqUrlEnum.TEAM_LEA_RANK_EXTRA_QUERY,false);
        map.setRequestUrl(AppParam.FT_API_DOMAIN + FT_API_S_URL);
        map.put("raceId", jczqDataBean.getInnerRaceId());
        map.put("rankType", pointType);
        httpHelper.startRequest(map);
    }

    private void responseSuccessPointsQuery(JczqRankingBean jczqRankingBean) {

        jczqRankingBean.operatorData(jczqDataBean.getInnerHomeTeamId(),jczqDataBean.getInnerGuestTeamId());

        list.clear();

        JczqRankingBean.RankTitleBean rankTitle = jczqRankingBean.getRankTitle();

        if (rankTitle.getGuestTitle() != null || rankTitle.getHomeTitle() != null || rankTitle.getSameTitle() != null) {

            list.add(jczqRankingBean);
        }
        if (jczqRankingBean.getMatchRuleText() != null) {
            pointLvAdapter.setMatchRuleText(jczqRankingBean.getMatchRuleText());
        }

        if (jczqRankingBean.isShowDiffGroupRank()) {

            cycleLeagueRg.setVisibility(View.VISIBLE);

            if (jczqRankingBean.getSecondRankMap() == null) {

                secondCycleRb.setVisibility(View.INVISIBLE);

                totalCycleRb.setVisibility(View.INVISIBLE);
            }

            handleDiffGroupList();

        } else {

            pointLvAdapter.notifyDataSetChanged();
        }

        noDataView.setVisibility(pointLvAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
    }


    public Boolean isRaceRankDataNull() {
        if (jczqRankingBean != null) {
            return false;
        } else {
            return true;
        }
    }

    public void getDefaultPoint(boolean showPg) {

        totalPointsRbtn.setChecked(true);

        getData(showPg);
    }

    public void notifyData(boolean clear) {
        pointsLv.setAdapter(clear ? null : pointLvAdapter);
        pointsTableRg.setVisibility(clear ? View.GONE : View.VISIBLE);
    }

    private void getData(boolean showPg) {
        switch (mCheckPoint) {
            case 0:
                getAnalysisPoints(JczqPointTypeEnum.SR_AAR.name(), showPg);
                break;
            case 1:
                getAnalysisPoints(JczqPointTypeEnum.SR_HAR.name(), showPg);
                break;
            case 2:
                getAnalysisPoints(JczqPointTypeEnum.SR_GAR.name(), showPg);
                break;
        }
    }

    private RadioGroup.OnCheckedChangeListener pointsCheckedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.totalPointsRbtn:
                    mCheckPoint = 0;
                    getData(true);
                    break;
                case R.id.homePointsRbtn:
                    mCheckPoint = 1;
                    getData(true);
                    break;
                case R.id.guestPointsRbtn:
                    mCheckPoint = 2;
                    getData(true);
                    break;
                case R.id.firstCycleRb:
                    mCheckCycle = 0;
                    handleDiffGroupList();
                    break;
                case R.id.secondCycleRb:
                    mCheckCycle = 1;
                    handleDiffGroupList();
                    break;
                case R.id.totalCycleRb:
                    mCheckCycle = 2;
                    handleDiffGroupList();
                    break;
            }

        }
    };

    /**
     * 日职联时点击轮次变化，设置不同的数据
     *
     * @param
     */
    private void handleDiffGroupList() {
        jczqRankingBean.operatorDiffGroupData(mCheckCycle);
        pointLvAdapter.notifyDataSetChanged();
    }



    @Override
    public void onSuccess(RequestContainer request, JczqRankingBean jczqRankingBean) {
        responseSuccessPointsQuery(jczqRankingBean);
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        noDataView.setVisibility(View.VISIBLE);
        ((BaseActivity) context).operateErrorResponseMessage(jsonObject);
    }
}
