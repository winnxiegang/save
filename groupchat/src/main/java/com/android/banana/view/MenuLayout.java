package com.android.banana.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.banana.R;
import com.android.banana.commlib.emoji.EmojAdapter;
import com.android.banana.commlib.emoji.EmojBean;
import com.android.banana.commlib.emoji.EmojUtils;
import com.android.banana.groupchat.ilistener.EmojClickListener;
import com.android.library.Utils.LibAppUtil;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrs on 2017/4/10.
 */

public class MenuLayout extends FrameLayout {
    /**
     * 以下是 菜单展示所需要的
     */
    private RecyclerView menuRecyler;
    private ArrayList<MenuItem> gridList = new ArrayList<>();
    private onMenuItemClickListener listener;
    private MenuMoreAdapter menuAdapter;
    /**
     * 以下是表情布局
     */
    private ViewPager viewPager;
    private CirclePageIndicator circles;
    private LinearLayout emojiPanel;
    private EmojClickListener emojClickListener;


    private int height;
    private ValueAnimator animator;


    public MenuLayout(@NonNull Context context) {
        this(context, null);
    }

    public MenuLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        height = LibAppUtil.dip2px(context, 200);

        View view = LayoutInflater.from(context).inflate(R.layout.im_menu_layout, this, true);
        menuRecyler = (RecyclerView) view.findViewById(R.id.menuRecyler);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        circles = (CirclePageIndicator) view.findViewById(R.id.circles);
        emojiPanel = (LinearLayout) view.findViewById(R.id.emojLayout);

    }

    /**
     * 显示网格 比如 图片 红包 ....
     */
    public void showMenuMore(boolean active) {
        if (menuRecyler.getAdapter() == null) {
            menuRecyler.setHasFixedSize(true);
            menuRecyler.setLayoutManager(new GridLayoutManager(getContext(), 5));
            menuAdapter = new MenuMoreAdapter(getContext(), gridList);
            menuRecyler.setAdapter(menuAdapter);
        }
        showContentView(active, menuRecyler);
    }

    /**
     * 显示emoji表情
     */
    public void showEmojiPanel(boolean active) {
        if (viewPager.getAdapter() == null)
            initEmojLayout();
        showContentView(active, emojiPanel);
    }

    private void initEmojLayout() {
        ArrayList<View> viewList = new ArrayList<>();
        List<List<EmojBean>> list = EmojUtils.getEmojList(getContext());
        for (int i = 0; i < list.size(); i++) {
            GridView gridView = createEmojiGridView((ArrayList<EmojBean>) list.get(i));
            viewList.add(gridView);
        }
        EmojiPagerAdapter adapter = new EmojiPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
        circles.setViewPager(viewPager);
    }

    private GridView createEmojiGridView(final ArrayList<EmojBean> list) {
        GridView gridView = new GridView(getContext());
        gridView.setLayoutParams(new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)));
        gridView.setNumColumns(7);
        EmojAdapter mAdapter = new EmojAdapter(getContext(), list);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (emojClickListener != null)
                    emojClickListener.onEmojiClick(position >= list.size() ? new EmojBean() : list.get(position));
            }
        });
        return gridView;
    }


    class EmojiPagerAdapter extends PagerAdapter {
        private ArrayList<View> list;

        public EmojiPagerAdapter(ArrayList<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }


    private void showContentView(boolean active, View showView) {

        if (getVisibility() != VISIBLE && !active)
            startVisibilityAnimation(VISIBLE);
        else {
            setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            setVisibility(VISIBLE);
        }

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == showView)
                child.setVisibility(VISIBLE);
            else child.setVisibility(GONE);
        }
    }


    class MenuMoreAdapter extends RecyclerView.Adapter<MenuMoreAdapter.MenuHolder> {
        private Context context;
        private ArrayList<MenuItem> gridList;

        public MenuMoreAdapter(Context context, ArrayList<MenuItem> hashMap) {
            this.context = context;
            this.gridList = hashMap;
        }

        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new MenuHolder(LayoutInflater.from(context).inflate(R.layout.item_bottom_grid_item, parent, false));
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, final int position) {
            holder.img.setBackgroundDrawable(ContextCompat.getDrawable(context, gridList.get(position).imgRes));
            holder.txt.setText(gridList.get(position).txt);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onMenuMoreItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return gridList == null ? 0 : gridList.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder {
            public ImageView img;
            public TextView txt;

            public MenuHolder(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.grid_img);
                txt = (TextView) itemView.findViewById(R.id.grid_txt);
            }
        }
    }

    /*------------------------------菜单子项-----------------------------------------*/
    public ArrayList<MenuItem> getMenuGrid() {
        return gridList;
    }

    /*添加+号展开后的 item项*/
    public MenuLayout addMenuItem(String txt, @DrawableRes int resIcon) {
        if (gridList == null)
            gridList = new ArrayList<>();
        for (MenuItem menuItem : gridList) {
            if (TextUtils.equals(menuItem.txt, txt))
                return MenuLayout.this;
        }
        gridList.add(new MenuItem(txt, resIcon));
        if (menuAdapter != null)
            menuAdapter.notifyDataSetChanged();
        return this;
    }

    /*根据权限可能要移除一项或某几项*/
    public void removeItem(String itemStr) {
        if (gridList == null || gridList.size() == 0)
            return;
        for (int i = 0; i < gridList.size(); i++) {
            if (TextUtils.equals(gridList.get(i).txt, itemStr)) {
                gridList.remove(i);
                if (menuAdapter != null)
                    menuAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    class MenuItem {
        public String txt;
        public int imgRes;

        public MenuItem(String txt, int imgRes) {
            this.txt = txt;
            this.imgRes = imgRes;
        }
    }

    /*------------------------------点击监听-----------------------------------------*/
    /*展开菜单点击监听*/
    public interface onMenuItemClickListener {
        void onMenuMoreItemClick(int position);
    }

    public void setOnMenuItemClickListener(onMenuItemClickListener listener1) {
        this.listener = listener1;
    }

    public onMenuItemClickListener getMenuItemListener() {
        return listener;
    }

    /*表情点击监听*/
    public EmojClickListener getEmojClickListener() {
        return emojClickListener;
    }

    public void setEmojClickListener(EmojClickListener emojClickListener) {

        this.emojClickListener = emojClickListener;
    }

    /*判断当前显示的时表情还是展开菜单*/
    public boolean isShowEmoji() {
        return emojiPanel == null ? false : (getVisibility() == VISIBLE && emojiPanel.getVisibility() == View.VISIBLE);
    }

    /*判断当前显示的时表情还是展开菜单*/
    public boolean isShowMenu() {
        return menuRecyler == null ? false : (getVisibility() == VISIBLE && menuRecyler.getVisibility() == View.VISIBLE);
    }


    @Override

    public void setVisibility(int visibility) {
        if (menuRecyler != null && visibility == View.GONE)
            menuRecyler.setVisibility(visibility);
        if (emojiPanel != null && visibility == View.GONE)
            emojiPanel.setVisibility(visibility);

        super.setVisibility(visibility);
    }

    public void startVisibilityAnimation(final int visibility) {
        if (getVisibility() == visibility)
            return;
        int start = visibility == View.VISIBLE ? 0 : height;
        int end = visibility == View.VISIBLE ? height : 0;

        animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = MenuLayout.this.getLayoutParams();
                params.height = value;
                MenuLayout.this.setLayoutParams(params);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                setVisibility(visibility);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(visibility);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            animator = null;
        }
    }
}
