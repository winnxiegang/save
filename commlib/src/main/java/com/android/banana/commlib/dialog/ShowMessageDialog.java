package com.android.banana.commlib.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.R;


/**
 * Created by zhouyi on 2015/4/23.
 */
public class ShowMessageDialog extends DialogBase {

    private boolean shouldDismiss = true;

    public ShowMessageDialog(Context context, final OnMyClickListener ok, final OnMyClickListener cancel, String message) {

        super(context, R.layout.dialog_layout);

        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);

        messageTv.setText(message);

        final TextView okBtn = (TextView) window.findViewById(R.id.okBtn);

        TextView cancelBtn = (TextView) window.findViewById(R.id.cancelBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ok != null) {
                    ok.onClick(v);
                }
                if (shouldDismiss) {
                    dialog.dismiss();
                }

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancel != null) {
                    cancel.onClick(v);
                }
                dialog.dismiss();
            }
        });

    }

    public ShowMessageDialog(Context context, final OnMyClickListener ok, final OnMyClickListener cancel, SpannableStringBuilder message) {
        super(context, R.layout.dialog_layout);
        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);
        messageTv.setText(message);
        TextView okBtn = (TextView) window.findViewById(R.id.okBtn);
        TextView cancelBtn = (TextView) window.findViewById(R.id.cancelBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.onClick(v);
                if (shouldDismiss) {
                    dialog.dismiss();
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancel != null) {
                    cancel.onClick(v);
                }
                dialog.dismiss();
            }
        });
    }

    public ShowMessageDialog(Context context, String positiveMessage, String negativeMessage, final OnMyClickListener ok, final OnMyClickListener cancel, String message) {
        super(context, R.layout.dialog_layout);
        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);
        messageTv.setText(message);
        TextView okBtn = (TextView) window.findViewById(R.id.okBtn);
        TextView cancelBtn = (TextView) window.findViewById(R.id.cancelBtn);
        if (positiveMessage != null) {
            okBtn.setText(positiveMessage);
        }
        if (negativeMessage != null) {
            cancelBtn.setText(negativeMessage);
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.onClick(v);
                if (shouldDismiss) {
                    dialog.dismiss();
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancel != null) {
                    cancel.onClick(v);
                }
                dialog.dismiss();
            }
        });
    }

    public ShowMessageDialog(Context context, int resId, String positiveMessage, String negativeMessage, final OnMyClickListener ok, final OnMyClickListener cancel, String message) {
        super(context, R.layout.dialog_layout_whith_image, R.style.MyDialog);
        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);
        messageTv.setText(message);
        TextView okBtn = (TextView) window.findViewById(R.id.okBtn);
        TextView cancelBtn = (TextView) window.findViewById(R.id.cancelBtn);
        ImageView headIv = (ImageView) window.findViewById(R.id.headIv);
        if (resId != 0) {
            headIv.setBackgroundResource(resId);
        }
        if (positiveMessage != null) {
            okBtn.setText(positiveMessage);
        }
        if (negativeMessage != null) {
            cancelBtn.setText(negativeMessage);
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.onClick(v);
                if (shouldDismiss) {
                    dialog.dismiss();
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancel != null) {
                    cancel.onClick(v);
                }
                dialog.dismiss();
            }
        });
    }

    public ShowMessageDialog(Context context, final Builder builder) {

        super(context, R.layout.dialog_layout);

        final TextView okBtn = (TextView) window.findViewById(R.id.okBtn);

        TextView cancelBtn = (TextView) window.findViewById(R.id.cancelBtn);

        TextView messageTv = (TextView) window.findViewById(R.id.messageTv);

        TextView titleTv = (TextView) window.findViewById(R.id.titleTv);

        if (!builder.showTitle) {
            titleTv.setVisibility(View.GONE);
        }

        if (builder.message != null) {
            messageTv.setText(builder.message);
            if (builder.showMessageMiddle) {
                messageTv.setGravity(Gravity.CENTER);
            }
        }

        if (builder.title != null) {
            titleTv.setText(builder.title);
        }

        if (builder.negativeMessage != null) {
            cancelBtn.setText(builder.negativeMessage);
        }

        if (builder.positiveMessage != null) {
            okBtn.setText(builder.positiveMessage);
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.positiveClickListener != null) {
                    builder.positiveClickListener.onClick(v);
                }
                if (builder.dismiss) {
                    dialog.dismiss();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.cancelListener != null) {
                    builder.cancelListener.onClick(v);
                }
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (builder.dismissListener != null) {
                    builder.dismissListener.onClick(okBtn);
                }
            }
        });
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void shouldDismiss(boolean showDismiss) {
        this.shouldDismiss = showDismiss;
    }


    public static class Builder {

        boolean dismiss = true;
        CharSequence title;
        CharSequence message;
        CharSequence positiveMessage;
        View.OnClickListener positiveClickListener;
        CharSequence negativeMessage;
        View.OnClickListener cancelListener;
        View.OnClickListener dismissListener;
        boolean showTitle = true;
        boolean showMessageMiddle = false;

        public void setTitle(CharSequence title) {
            this.title = title;
        }

        public void setMessage(CharSequence message) {
            this.message = message;
        }

        public void setPositiveMessage(CharSequence positiveMessage) {
            this.positiveMessage = positiveMessage;
        }

        public void setPositiveClickListener(View.OnClickListener positiveClickListener) {
            this.positiveClickListener = positiveClickListener;
        }

        public void setDismissListener(View.OnClickListener dismissListener) {
            this.dismissListener = dismissListener;
        }

        public void setNegativeMessage(CharSequence negativeMessage) {
            this.negativeMessage = negativeMessage;
        }

        public void setCancelListener(View.OnClickListener cancelListener) {
            this.cancelListener = cancelListener;
        }

        public void setDismiss(boolean dismiss) {
            this.dismiss = dismiss;
        }

        public void setShowTitle(boolean showTitle) {
            this.showTitle = showTitle;
        }

        public void setShowMessageMiddle(boolean showMessageMiddle) {
            this.showMessageMiddle = showMessageMiddle;
        }
    }

}
