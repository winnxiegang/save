package com.android.banana.commlib.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.banana.commlib.R;
import com.android.library.Utils.LogUtils;

/**
 * Created by qiaomu on 2018/3/9.
 * <p>
 * <p>
 * 动态勋章
 */

public enum SubjectMedalEnum {

    SUBSTANCE_MEDAL("内容勋章"),  //领域标签

    CONSUME_MEDAL("消费勋章"),

    GAME_MEDAL("游戏勋章"),

    SHEN_HUA(R.drawable.ic_medal_achieve_shen_hua, "神话"),//成就标签

    SHEN_JI(R.drawable.ic_medal_achieve_ji_qi, "神迹"),

    WEN_GOLDEN(R.drawable.ic_medal_achieve_wen_jin, "稳（金）"),

    WEN_RED(R.drawable.ic_medal_achieve_wen_hong, "稳（红）"),

    NIU_GOLDEN(R.drawable.ic_medal_achieve_niu_jin, "牛（金）"),

    NIU_RED(R.drawable.ic_medal_achieve_niu_hong, "牛（红）"),

    HEI_YAO_SHI(R.drawable.ic_medal_achieve_hei_yao_shi, "黑曜石"),

    HEI_MEI_QIU(R.drawable.ic_medal_achieve_hei_meiu_qiu, "黑煤球"),

    BAO_GOLDEN(R.drawable.ic_medal_achieve_bao_jin, "爆（金）"),

    BAO_RED(R.drawable.ic_medal_achieve_bao_hong, "爆（红）"),

    GLOBAL_GRADE_MEDAL("平台等级勋章"),

    UNKNOW("未知");

    private int mIcon = -1;
    private String mMedalType;

    SubjectMedalEnum(String medalType) {
        mMedalType = medalType;
    }

    SubjectMedalEnum(int icon, String medalType) {
        mIcon = icon;
        mMedalType = medalType;
    }

    public static int getMedalResourceId(Context context, String medalCode, String medalLevel) {
        SubjectMedalEnum[] enums = values();
        int level = 0;
        if (!TextUtils.isEmpty(medalLevel)) {
            try {
                level = Integer.parseInt(medalLevel.substring(2, medalLevel.length()));
            } catch (Exception e) {
                LogUtils.e("SubjectMedalEnum", "勋章等级格式化出错了");
            }
        }

        if (SUBSTANCE_MEDAL.name().equalsIgnoreCase(medalCode)) {

            return getSubTranceMedal(level);

        } else if (CONSUME_MEDAL.name().equalsIgnoreCase(medalCode)) {

            return getConsumeMedal(level);

        } else if (GAME_MEDAL.name().equalsIgnoreCase(medalCode)) {

            return getGameMedal(level);

        } else if (GLOBAL_GRADE_MEDAL.name().equalsIgnoreCase(medalCode)) {

            return getGlobalGradeMedal(context, level);
        }
        for (SubjectMedalEnum anEnum : enums) {
            if (anEnum.name().equalsIgnoreCase(medalCode)) {
                if (anEnum.mIcon != -1) {
                    return anEnum.mIcon;
                }
            }
        }
        return 0;
    }

    private static int getGlobalGradeMedal(Context context, int level) {
        if (level <= 40) {
            return context.getResources().getIdentifier("ic_medal_grade_lv" + level, "drawable", context.getPackageName());

        } else {
            return R.drawable.icon_blue_horse;
        }

    }

    private static int getGameMedal(int level) {
        if (level >= 0 && level < 10) {
            return R.drawable.ic_medal_filed_game_lv1;
        } else if (level >= 10 && level < 20) {
            return R.drawable.ic_medal_filed_game_lv10;
        } else if (level >= 20 && level < 25) {
            return R.drawable.ic_medal_filed_game_lv20;
        } else if (level >= 25 && level < 30) {
            return R.drawable.ic_medal_filed_game_lv25;
        } else if (level >= 30 && level < 35) {
            return R.drawable.ic_medal_filed_game_lv30;
        } else if (level >= 35 && level < 40) {
            return R.drawable.ic_medal_filed_game_lv35;
        } else if (level >= 40) {
            return R.drawable.ic_medal_filed_game_lv40;
        }
        return R.drawable.ic_medal_filed_game_lv1;
    }

    private static int getConsumeMedal(int level) {
        if (level >= 0 && level < 10) {
            return R.drawable.ic_medal_field_consume_lv1;
        } else if (level >= 10 && level < 20) {
            return R.drawable.ic_medal_field_consume_lv10;
        } else if (level >= 20 && level < 25) {
            return R.drawable.ic_medal_field_consume_lv20;
        } else if (level >= 25 && level < 30) {
            return R.drawable.ic_medal_field_consume_lv25;
        } else if (level >= 30 && level < 35) {
            return R.drawable.ic_medal_field_consume_lv30;
        } else if (level >= 35 && level < 40) {
            return R.drawable.ic_medal_field_consume_lv35;
        } else if (level >= 40) {
            return R.drawable.ic_medal_field_consume_lv40;
        }
        return R.drawable.ic_medal_field_consume_lv1;
    }

    @Nullable
    private static int getSubTranceMedal(int level) {
        if (level >= 0 && level < 10) {
            return R.drawable.ic_medal_filed_content_lv1;
        } else if (level >= 10 && level < 20) {
            return R.drawable.ic_medal_filed_content_lv10;
        } else if (level >= 20 && level < 25) {
            return R.drawable.ic_medal_filed_content_lv20;
        } else if (level >= 25 && level < 30) {
            return R.drawable.ic_medal_filed_content_lv25;
        } else if (level >= 30 && level < 35) {
            return R.drawable.ic_medal_filed_content_lv30;
        } else if (level >= 35 && level < 40) {
            return R.drawable.ic_medal_filed_content_lv35;
        } else if (level >= 40) {
            return R.drawable.ic_medal_filed_content_lv40;
        }
        return R.drawable.ic_medal_filed_content_lv1;
    }
}
