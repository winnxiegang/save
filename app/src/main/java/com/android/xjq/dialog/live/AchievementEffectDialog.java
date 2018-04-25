package com.android.xjq.dialog.live;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.dialog.BaseDialogFragment;
import com.android.banana.commlib.utils.AnimationListUtil;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.xjq.R;
import com.android.xjq.activity.ThirdWebActivity;

/**
 * Created by lingjiu on 2018/3/15.
 */

public class AchievementEffectDialog extends BaseDialogFragment implements View.OnClickListener {

    private ImageView achieveIv;
    private ImageView starIv;


    public static AchievementEffectDialog newInstance(String medalCode) {
        AchievementEffectDialog achievementEffectDialog = new AchievementEffectDialog();
        Bundle arguments = new Bundle();
        arguments.putString("medalCode", medalCode);
        achievementEffectDialog.setArguments(arguments);
        return achievementEffectDialog;
    }

    @Override
    protected int getDialogTheme() {
        return Theme.NORAML_THEME_DIMENABLE;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public boolean fillScreenWidth() {
        return false;
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.dialog_layout_achievement_effect;
    }

    @Override
    protected void onDialogCreate() {
        String medalCode = getArguments().getString("medalCode");
        achieveIv = ((ImageView) rootView.findViewById(R.id.achieveIv));
        starIv = ((ImageView) rootView.findViewById(R.id.starIv));
        rootView.findViewById(R.id.closeIv).setOnClickListener(this);
        rootView.findViewById(R.id.knowMedalTv).setOnClickListener(this);
        int medalResourceId = SubjectMedalEnum.getMedalResourceId(getContext(), medalCode, null);
        achieveIv.setImageResource(medalResourceId);
        AnimationListUtil.startAnimationList(R.drawable.anim_achieve_star, starIv, null, null, 10);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.knowMedalTv) {
            ThirdWebActivity.startThirdWebActivityByType(getActivity(), ThirdWebActivity.MEDAL_QUERY, LoginInfoHelper.getInstance().getUserId());
        } else if (id == R.id.closeIv) {
            dismiss();
        }
    }
}
