package com.android.banana.commlib.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.bean.JczjMedalBean;
import com.android.library.Utils.LibAppUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lingjiu on 2017/3/6 11:59.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {


    protected List<T> list;

    protected LayoutInflater layoutInflater;

    protected Context context;

    public MyBaseAdapter(Context context) {
        this(context, null);
    }

    public MyBaseAdapter(Context context, List<T> list) {

        this.context = context;

        this.list = list;

        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {


        return null;
    }

    /**
     * 设置黄马甲
     *
     * @param horseList
     * @return
     */
    public SpannableString getYellowClothes(List<String> horseList) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (horseList == null || horseList.size() == 0) return new SpannableString(ssb);
        if (horseList.contains("CTL_YELLOW_HORSE")) {
            addIcon(ssb, R.drawable.icon_control_clothes);
        }
        if (horseList.contains("HONOR_GREEN_HORSE")) {
            addIcon(ssb, R.drawable.icon_green_horse);
        }
        if (horseList.contains("HONOR_BLUE_HORSE")) {
            addIcon(ssb, R.drawable.icon_blue_horse);
        }

        return new SpannableString(ssb);
    }

    public void addIcon(SpannableStringBuilder ssb, int iconId) {
        SpannableString str1 = new SpannableString("黄");
        Drawable d = context.getResources().getDrawable(iconId);
        d.setBounds(0, 0, (int) (d.getIntrinsicWidth() * 1.2), (int) (d.getIntrinsicHeight() * 1.2));
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        str1.setSpan(span, 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(str1);
    }

    protected void setImageShow(SimpleDraweeView simpleDraweeView, String url, boolean enable) {

        if (url == null) return;

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setAutoPlayAnimations(enable)
                .build();
        simpleDraweeView.setController(draweeController);
    }

    protected void setImageShow(SimpleDraweeView simpleDraweeView, String url) {

        if (url == null) return;

        simpleDraweeView.setImageURI(Uri.parse(url));
    }

    protected void setImageShow(ImageView imageView, String url) {

        if (url == null) return;

        Picasso.with(context.getApplicationContext())
                .load(Uri.parse(url))
                .error(R.drawable.user_default_logo)
                .into(imageView);
    }

    protected void setIconShow(final ImageView iv, String url) {

        if (url == null) {
            return;
        }
        Picasso.with(context.getApplicationContext())
                .load(Uri.parse(url))
                .into(iv);
    }


    /**
     * 设置勋章图标
     *
     * @param userMedalBeanList
     * @param userInfoLayout
     */
    public void setUserMedalShow(List<String> userMedalBeanList, LinearLayout userInfoLayout, int location) {
        LinearLayout medalContainer = new LinearLayout(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        medalContainer.setLayoutParams(params);

        int childCount = userInfoLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {

            View child = userInfoLayout.getChildAt(i);

            if (child instanceof LinearLayout) {

                userInfoLayout.removeView(child);
            }
        }

        if (userMedalBeanList != null && userMedalBeanList.size() != 0) {
            for (String url : userMedalBeanList) {

                ImageView imageView = getUserMedalImageview(url);

                medalContainer.addView(imageView);

            }

            userInfoLayout.addView(medalContainer, location);
        }

    }


    public void setTouchListener(TextView tv) {
        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                CharSequence text = ((TextView) v).getText();
                Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
                TextView widget = (TextView) v;
                int action = event.getAction();

                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                    if (link.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            link[0].onClick(widget);
                        }
                        ret = true;
                    }
                }
                return ret;
            }
        });

    }

    //排序勋章根据orderValue
    private void sortMedalList(List<JczjMedalBean> userMedalBeanList) {
        Collections.sort(userMedalBeanList, new Comparator<JczjMedalBean>() {
            public int compare(JczjMedalBean o1, JczjMedalBean o2) {

                //按照orderValue进行升序排列
                if (o1.getOrderValue() > o2.getOrderValue()) {
                    return 1;
                }
                if (o1.getOrderValue() == o2.getOrderValue()) {
                    return 0;
                }
//                return  new Integer(o1.getOrderValue()).compareTo(o2.getOrderValue());
                return -1;
            }
        });
    }

    /**
     * 设置勋章图标
     *
     * @param userMedalBeanList
     * @param userInfoLayout
     */
    public void setJczjUserMedalShow(List<JczjMedalBean> userMedalBeanList, LinearLayout userInfoLayout, int location) {

        LinearLayout medalContainer = new LinearLayout(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        medalContainer.setLayoutParams(params);

        int childCount = userInfoLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {

            View child = userInfoLayout.getChildAt(i);

            if (child instanceof LinearLayout) {

                userInfoLayout.removeView(child);
            }
        }

        if (userMedalBeanList != null && userMedalBeanList.size() != 0) {

            sortMedalList(userMedalBeanList);

            for (JczjMedalBean medalBean : userMedalBeanList) {
                //如果有管家标识只显示管家标识
                if ("MANAGER_MANAGER".equals(medalBean.getImageName())) {

                    ImageView imageView = getUserMedalImageview(medalBean.getUserMedalUrl());

                    medalContainer.addView(imageView);

                    userInfoLayout.addView(medalContainer, location);

                    return;

                } else if ("CONTINUE_RED".equals(medalBean.getMedalCode())) {

                    View lianHongView = getLianHongView();

                    LinearLayout myLianHong = (LinearLayout) lianHongView.findViewById(R.id.myLianhong);

                    TextView lianHongNum = (TextView) lianHongView.findViewById(R.id.ruleDeclarationLianhongNum);

                    ImageView lianhong30 = (ImageView) lianHongView.findViewById(R.id.lianhong30);

                    if (Math.round(medalBean.getCalValue()) > 30) {

                        myLianHong.setVisibility(View.GONE);

                        lianhong30.setVisibility(View.VISIBLE);

                    } else {

                        myLianHong.setVisibility(View.VISIBLE);

                        lianhong30.setVisibility(View.GONE);

                        lianHongNum.setText(Math.round(medalBean.getCalValue()) + "");
                    }

                    medalContainer.addView(lianHongView);

                } else {

                    ImageView imageView = getUserMedalImageview(medalBean.getUserMedalUrl());

                    medalContainer.addView(imageView);
                }
            }

            userInfoLayout.addView(medalContainer, location);

        }

    }

    protected View getLianHongView() {

        LinearLayout view = (LinearLayout) View.inflate(context, R.layout.view_lianhong_medal, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.leftMargin = LibAppUtil.getDpToPxValue(context, 4);

        params.gravity = Gravity.CENTER;

        view.setLayoutParams(params);

        return view;
    }

    public void setIconView(ImageView iv, String url) {
        if (url == null) {
            iv.setImageResource(R.drawable.user_default_logo);
            return;
        }
        Picasso.with(context.getApplicationContext())
                .load(Uri.parse(url))
                .placeholder(R.drawable.user_default_logo)
                .error(R.drawable.user_default_logo)
                .into(iv);
    }

    public void setVip(View vip, boolean isVip) {
        if (isVip) {
            vip.setVisibility(View.VISIBLE);
        } else {
            vip.setVisibility(View.GONE);
        }
    }

    protected void setJczqTeamLogo(ImageView iv, String url, boolean home) {

        RequestCreator load = Picasso.with(context.getApplicationContext()).load(Uri.parse(url));

        if (home) {
            load.error(R.drawable.ft_ho_icon).placeholder(R.drawable.ft_ho_icon).into(iv);
        } else {
            load.error(R.drawable.ft_gs_icon).placeholder(R.drawable.ft_gs_icon).into(iv);
        }

    }


    protected ImageView getUserMedalImageview(String url) {

        ImageView imageView = new ImageView(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LibAppUtil.dip2px(context, 14));

        params.gravity = Gravity.CENTER_VERTICAL;

        params.leftMargin = LibAppUtil.getDpToPxValue(context, 2);

        params.rightMargin = LibAppUtil.getDpToPxValue(context, 0);

        imageView.setLayoutParams(params);

        //设定高度,使宽度自适应
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        imageView.setAdjustViewBounds(true);

        if (url == null) {
            imageView.setVisibility(View.GONE);
            return imageView;
        }

        setImageShow(imageView, url);

        return imageView;
    }



}
