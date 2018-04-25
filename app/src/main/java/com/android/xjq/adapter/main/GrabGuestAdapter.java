package com.android.xjq.adapter.main;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.guest.SeatApplyInfoBean;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.xjq.utils.live.SpannableStringHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/7/31.
 */

public class GrabGuestAdapter extends MyBaseAdapter<SeatApplyInfoBean.SeatIssuesBean> {

    private int maxApplyCount;

    public void setMaxApplyCount(int maxApplyCount) {
        this.maxApplyCount = maxApplyCount;
    }

    public GrabGuestAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_grab_guest_listview, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemData(holder, position);

        return convertView;
    }

    private void setItemData(ViewHolder holder, int position) {
        SeatApplyInfoBean.SeatIssuesBean bean = list.get(position);
        holder.stateIv.setVisibility(View.GONE);
        if (bean.isSelected()) {
            holder.stateIv.setVisibility(View.VISIBLE);
            holder.hasApplyCountTv.setBackgroundResource(R.drawable.icon_guest_selected_bottom);
            holder.stageTv.setBackgroundResource(R.drawable.icon_guest_selected_top);
            holder.stateIv.setImageResource(R.drawable.icon_guest_selected);
        } else {
            holder.hasApplyCountTv.setBackgroundResource(R.drawable.icon_guest_unselected_bottom);
            holder.stageTv.setBackgroundResource(R.drawable.icon_guest_unselected_top);
        }
        holder.hasApplyCountTv.setText(bean.getApplyCount() + "/" + maxApplyCount+"人");

        SpannableStringBuilder ssb = new SpannableStringBuilder("第" + bean.getIssueNo() + "期\n");
        String enableTime = "(" + subTimeString(bean.getGmtStart()) + "至" + subTimeString(bean.getGmtEnd()) + ")";
        SpannableString ss = SpannableStringHelper.changeTextColor(enableTime, Color.parseColor("#FFF5D8"));
        ss.setSpan(new AbsoluteSizeSpan(HhsUtils.spToPx(context,12)), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);
        holder.stageTv.setText(ssb);
        if (bean.isAppliedIssue()) {
            holder.stateIv.setVisibility(View.VISIBLE);
            holder.stageTv.setBackgroundResource(R.drawable.icon_guest_full_top);
            holder.hasApplyCountTv.setBackgroundResource(R.drawable.icon_guest_full_bottom);
            holder.stateIv.setImageResource(R.drawable.icon_guest_has_apply);
        } else if (bean.issueFull()) {
            holder.stateIv.setVisibility(View.VISIBLE);
            holder.stageTv.setBackgroundResource(R.drawable.icon_guest_full_top);
            holder.hasApplyCountTv.setBackgroundResource(R.drawable.icon_guest_full_bottom);
            holder.stateIv.setImageResource(R.drawable.icon_guest_has_full);
        }

    }

    private String subTimeString(String time) {
        if (time == null) return null;
        int startIndex = time.indexOf("-");
        int endIndex = time.indexOf(" ");
        time = time.substring(startIndex + 1, endIndex);
        return time;
    }

    static class ViewHolder {
        @BindView(R.id.stageTv)
        TextView stageTv;
        @BindView(R.id.hasApplyCountTv)
        TextView hasApplyCountTv;
        @BindView(R.id.stateIv)
        ImageView stateIv;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }

        public static ViewHolder getTag(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();

            if (holder == null) {
                holder = new ViewHolder(convertView);
            }
            return holder;
        }
    }
}
