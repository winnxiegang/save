package com.android.xjq.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.banana.commlib.dialog.DialogBase;
import com.android.xjq.R;


/**
 * Created by zhouyi on 2015/4/23.
 */

public class ShowReportDialog extends DialogBase {

    public static final String SPAM = "垃圾广告";

    public static final String SENSITIVE_MESSAGE = "敏感信息";

    public static final String PORNOGRAPHIC = "虚假中奖";

    public static final String FALSE_MESSAGE = "淫秽情色";

    public static final String FALSE_PRIZE = "不实信息";

    public static final String PERSON_ATTACK = "人身攻击";

    private boolean shouldDismiss = true;

    public ShowReportDialog(Context context, final Builder builder) {

        super(context, R.layout.report_dialog_layout);

        TextView cancelTv = (TextView) window.findViewById(R.id.cancelTv);

        TextView spamTV = (TextView) window.findViewById(R.id.spamTV);

        TextView sensitiveMessageTV = (TextView) window.findViewById(R.id.sensitiveMessageTV);

        TextView falsePrizeTv = (TextView) window.findViewById(R.id.falsePrizeTv);

        TextView pornographicTv = (TextView) window.findViewById(R.id.pornographicTv);

        TextView falseMessageTv = (TextView) window.findViewById(R.id.falseMessageTv);

        TextView personAttackTv = (TextView) window.findViewById(R.id.personAttackTv);

        spamTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.reportClickListener != null) {
                    v.setTag(SPAM);
                    builder.reportClickListener.onClick(v);
                }
                dialog.dismiss();


            }
        });

        sensitiveMessageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.reportClickListener != null) {

                    v.setTag(SENSITIVE_MESSAGE);

                    builder.reportClickListener.onClick(v);

                }
                dialog.dismiss();
            }
        });

        falsePrizeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.reportClickListener != null) {

                    v.setTag(PORNOGRAPHIC);

                    builder.reportClickListener.onClick(v);

                }
                dialog.dismiss();

            }
        });

        pornographicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.reportClickListener != null) {

                    v.setTag(PORNOGRAPHIC);

                    builder.reportClickListener.onClick(v);

                }
                dialog.dismiss();
            }
        });

        falseMessageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.reportClickListener != null) {

                    v.setTag(FALSE_MESSAGE);

                    builder.reportClickListener.onClick(v);

                }
                dialog.dismiss();
            }
        });

        personAttackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.reportClickListener != null) {

                    v.setTag(PERSON_ATTACK);

                    builder.reportClickListener.onClick(v);

                }
                dialog.dismiss();
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.cancelListener != null) {
                    builder.cancelListener.onClick(v);
                }
                dialog.dismiss();
            }
        });


    }

    public void dismiss() {
        dialog.dismiss();
    }

    public static class Builder {

        CharSequence spamMessage;
        CharSequence sensitiveMessage;
        CharSequence falsePrizeMessage;
        CharSequence pornographicMessage;
        CharSequence falseMessage;
        CharSequence personAttackMessage;
        CharSequence negativeMessage;

        View.OnClickListener cancelListener;

        View.OnClickListener reportClickListener;

        public Builder() {

            spamMessage = SPAM;

            sensitiveMessage = SENSITIVE_MESSAGE;

            falsePrizeMessage = PORNOGRAPHIC;

            pornographicMessage = FALSE_MESSAGE;

            falseMessage = FALSE_PRIZE;

            personAttackMessage = PERSON_ATTACK;
        }

        public void setSpamMessage(CharSequence spamMessage) {
            this.spamMessage = spamMessage;
        }

        public void setSensitiveMessage(CharSequence sensitiveMessage) {
            this.sensitiveMessage = sensitiveMessage;
        }

        public void setFalsePrizeMessage(CharSequence falsePrizeMessage) {
            this.falsePrizeMessage = falsePrizeMessage;
        }

        public void setPornographicMessage(CharSequence pornographicMessage) {
            this.pornographicMessage = pornographicMessage;
        }

        public void setFalseMessage(CharSequence falseMessage) {
            this.falseMessage = falseMessage;
        }

        public void setPersonAttackMessage(CharSequence personAttackMessage) {
            this.personAttackMessage = personAttackMessage;
        }

        public void setReportClickListener(View.OnClickListener reportClickListener) {
            this.reportClickListener = reportClickListener;
        }

        public void setNegativeMessage(CharSequence negativeMessage) {
            this.negativeMessage = negativeMessage;
        }

        public void setCancelListener(View.OnClickListener cancelListener) {
            this.cancelListener = cancelListener;
        }

    }


}
