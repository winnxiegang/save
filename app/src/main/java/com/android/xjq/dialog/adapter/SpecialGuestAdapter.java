package com.android.xjq.dialog.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.view.MedalLayout;
import com.android.xjq.R;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.program.SpecialGuestBean;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ajiao on 2018\2\9 0009.
 */

public class SpecialGuestAdapter extends BaseAdapter {

    public static final int GUEST_TYPE = 0;
    public static final int PURCHASE_TYPE = 1;
    private int type;
    private Context mCtx;
    private List<SpecialGuestBean.GuestInfoListBean> mData;
    private onOperateFocusClick mListener;

    public void setType(int type) {
        this.type = type;
    }

    public SpecialGuestAdapter(Context ctx, List<SpecialGuestBean.GuestInfoListBean> list) {
        mCtx = ctx;
        mData = list;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.view_special_guest_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setData(holder, position);
        return convertView;
    }

    private void setData(ViewHolder holder, final int pos) {
        final SpecialGuestBean.GuestInfoListBean guestInfoBean = mData.get(pos);
        holder.guestName.setText(guestInfoBean.getLoginName());
        holder.attentionNumTxt.setText("关注 " + String.valueOf(guestInfoBean.getAttentionNum()));
        holder.fansNumTxt.setText("粉丝 " + String.valueOf(guestInfoBean.getFansNum()));
        Picasso.with(mCtx).load(guestInfoBean.getUserLogoUrl()).error(R.drawable.user_default_logo).into(holder.circleImageView);
        if (guestInfoBean.userMedalLevelList != null) {
            holder.medalLayout.removeAllViews();
            for (UserMedalLevelBean userMedalLevelBean : guestInfoBean.userMedalLevelList) {
                holder.medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(mCtx,
                        userMedalLevelBean.medalConfigCode, userMedalLevelBean.currentMedalLevelConfigCode));
            }
        }
        holder.takeAttentionTxt.setVisibility(TextUtils.equals(LoginInfoHelper.getInstance().getUserId(), guestInfoBean.getUserId()) ?
                View.GONE : View.VISIBLE);
        holder.takeAttentionTxt.setChecked(guestInfoBean.isFocus());
        holder.takeAttentionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onOperateFocus(guestInfoBean.isFocus(), guestInfoBean.getUserId(), pos);
                }
            }
        });

        if (type == GUEST_TYPE) {
            holder.send_gift_count_tv.setVisibility(View.GONE);
        } else {
            holder.send_gift_count_tv.setVisibility(View.VISIBLE);
            String giftCount = String.format(mCtx.getString(R.string.purchase_gift_num), String.valueOf(guestInfoBean.multiple));
            holder.send_gift_count_tv.setText(SpannableStringHelper.changeTextColor(giftCount, 3,
                    giftCount.length(), ContextCompat.getColor(mCtx, R.color.orange2)));
        }
    }

    public class ViewHolder {
        @BindView(R.id.portraitIv)
        CircleImageView circleImageView;
        @BindView(R.id.guest_name)
        TextView guestName;
        @BindView(R.id.attention_num_txt)
        TextView attentionNumTxt;
        @BindView(R.id.fans_num_txt)
        TextView fansNumTxt;
        @BindView(R.id.attentionTv)
        CheckedTextView takeAttentionTxt;
        @BindView(R.id.send_gift_count_tv)
        TextView send_gift_count_tv;
        @BindView(R.id.medalLayout)
        MedalLayout medalLayout;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface onOperateFocusClick {
        void onOperateFocus(boolean focusStatus, String userId, int pos);
    }

    public void setOnOperateFocusClickListener(onOperateFocusClick listener) {
        mListener = listener;
    }

}
