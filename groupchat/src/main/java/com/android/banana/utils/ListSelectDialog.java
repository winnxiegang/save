package com.android.banana.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.dialog.BaseDialogFragment;
import com.android.banana.groupchat.bean.ParcelableMap;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.banana.pullrecycler.recyclerview.WrapRecyclerView;
import com.android.library.Utils.LibAppUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.OVER_SCROLL_NEVER;
import static com.android.banana.commlib.dialog.BaseDialogFragment.Theme.NORAML_THEME;
import static com.android.banana.commlib.dialog.BaseDialogFragment.Theme.SLIDE_FROM_TO_BOTTOM_THEME;

/**
 * Created by qiaomu on 2017/5/27.
 */

public class ListSelectDialog extends BaseDialogFragment {


    WrapRecyclerView mRecyclerView;
    TextView cancelTv;
    TextView headerView;
    LinearLayout wrapLayout;
    View decorationView;
    View shadowLayout;

    private Message mMessage;
    private HashMap<Integer, Integer> textSizeMap, textColorMap;
    private int defaultSzie, defaultColor;
    private static int theme = SLIDE_FROM_TO_BOTTOM_THEME;

    private final static String MESSAGE_LIST = "message";
    private final static String OVER_SCROLL = "overScroll";
    private final static String CANCEL_TEXT = "btnText";
    private final static String TITLE = "title";
    private final static String THEME = "theme";


    private OnClickListener clickListener;
    private ListAdapter listAdapter;


    private static ListSelectDialog newInstance(boolean overScroll, String btnText, Title title, Message listDialogMessage, int theme) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MESSAGE_LIST, listDialogMessage);
        bundle.putBoolean(OVER_SCROLL, overScroll);
        bundle.putString(CANCEL_TEXT, btnText);
        bundle.putParcelable(TITLE, title);
        bundle.putInt(THEME, theme);
        if (theme != 0) {
            ListSelectDialog.theme = theme;
        }
        ListSelectDialog dialog = new ListSelectDialog();
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    protected int getDialogTheme() {
        return theme;
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.base_list_dialog;
    }

    @Override
    protected void onDialogCreate() {
        getDialog().getWindow().setLayout(-1, -2);


        mRecyclerView = (WrapRecyclerView) rootView.findViewById(R.id.WrapRecyclerView);
        cancelTv = (TextView) rootView.findViewById(R.id.cancel);
        headerView = (TextView) rootView.findViewById(R.id.headerView);
        wrapLayout = (LinearLayout) rootView.findViewById(R.id.wrapLayout);
        decorationView = (View) rootView.findViewById(R.id.decorationView);
        shadowLayout = rootView.findViewById(R.id.shadowLayout);

        Bundle bundle = getArguments();
        mMessage = bundle.getParcelable(MESSAGE_LIST);

        defaultColor = mMessage.defaultColor;
        defaultSzie = mMessage.defaultSize;
        ParcelableMap parcelableTextSizeMap = mMessage.mTextSizeMap;
        ParcelableMap parcelableTextColorMap = mMessage.mColorMap;
        if (parcelableTextSizeMap != null) {
            textSizeMap = parcelableTextSizeMap.pracMap;
        }
        if (parcelableTextColorMap != null) {
            textColorMap = parcelableTextColorMap.pracMap;
        }

        Title mTitle = bundle.getParcelable(TITLE);
        boolean isEmpty = mTitle == null || TextUtils.isEmpty(mTitle.headerTxt);
        if (!isEmpty) {
            int color = ContextCompat.getColor(getContext(), defaultColor);
            headerView.setText(mTitle.headerTxt);
            headerView.setTextSize(mTitle.headerSize == 0 ? 12 : mTitle.headerSize);
            headerView.setTextColor(mTitle.headerColor == 0 ? headerView.getCurrentTextColor() : mTitle.headerColor);
            headerView.setBackgroundColor(mTitle.headerBgColor == 0 ? ContextCompat.getColor(getContext(), R.color.normal_bg) : mTitle.headerBgColor);
            headerView.setVisibility(View.VISIBLE);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listAdapter = new ListAdapter(getContext(), mMessage.messageList, 0, null);
        mRecyclerView.setAdapter(listAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.base_divider_list, 1, !isEmpty));
        mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);

        String btnText = bundle.getString(CANCEL_TEXT);
        if (!TextUtils.isEmpty(btnText)) {
            decorationView.setVisibility(View.VISIBLE);
            //WrapRecyclerView.addItemDecoration(new PositionDividerDecoration(getContext(), R.drawable.base_divider_list10dp, 1, listAdapter.getItemCount() - 1));
            cancelTv.setVisibility(View.VISIBLE);
            cancelTv.setText(btnText);
        }
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        boolean overScroll = bundle.getBoolean(OVER_SCROLL);
        if (overScroll && listAdapter.getItemCount() > 5) {
            mRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(-1, LibAppUtil.dip2px(getContext(), 200)));
        }

        if (NORAML_THEME == theme) {
            //不悬浮 背景不虚化,一般需要在底部加一个阴影使其看着更突出
            shadowLayout.setVisibility(View.VISIBLE);
        }

    }



   /* public void cancelOperate(View view) {
        ListSelectDialog.this.dismiss();
    }*/

    /*移除指定item*/
    public void removeItem(int itemPos) {
        mMessage.messageList.remove(itemPos);
        listAdapter.notifyDataSetChanged();
    }

    private class ListAdapter extends MultiTypeSupportAdapter<CharSequence> {

        public ListAdapter(Context context, ArrayList<CharSequence> list, int layoutRes, MultiTypeSupport typeSupport) {
            super(context, list, R.layout.base_list_dialog_item, null);
        }

        @Override
        public void onBindNormalHolder(ViewHolder holder, CharSequence item, final int position) {
            TextView itemView = (TextView) holder.itemView;
            itemView.setText(item);

            int textColor = ContextCompat.getColor(mContext, defaultColor);
            if (textColorMap != null && textColorMap.containsKey(position))
                textColor = ContextCompat.getColor(mContext, textColorMap.get(position));
            itemView.setTextColor(textColor);

            int textSize = defaultSzie;
            if (textSizeMap != null && textSizeMap.containsKey(position))
                textSize = textSizeMap.get(position);
            itemView.setTextSize(textSize);

        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            ListSelectDialog.this.dismiss();
            if (clickListener != null)
                clickListener.onItemClick(view, position);
        }
    }


    public interface OnClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class Builder {
        private ArrayList<CharSequence> messageList;
        private HashMap<Integer, Integer> textSizeMap;
        private HashMap<Integer, Integer> textColorMap;

        private int defaultColor;
        private int defaultSzie;
        private String btnText;
        private String header;
        private int headerColor;
        private int headerSize;
        private int headerBgColor;
        private Title mTitle;
        private boolean mOverScroll;
        private int theme;

        public Builder setTheme(int theme) {
            this.theme = theme;
            return this;
        }

        public Builder setItemList(CharSequence... messages) {
            if (messages != null) {
                if (messageList == null) {
                    messageList = new ArrayList<>();
                    messageList.clear();
                }
                for (CharSequence message : messages) {
                    if (!TextUtils.isEmpty(message)) {
                        messageList.add(message);
                    }
                }
            }
            return this;
        }

        public Builder setItemList(List<CharSequence> messages) {
            if (messageList == null) {
                messageList = new ArrayList<>();
                messageList.clear();
            }
            messageList.addAll(messages);
            return this;
        }

        public Builder setTextSize(int defaultSzie) {
            this.defaultSzie = defaultSzie;
            return this;
        }

        public Builder setTextSize(int pos, int size) {
            if (textSizeMap == null)
                textSizeMap = new HashMap<>();
            textSizeMap.put(pos, size);
            return this;
        }

        public Builder setTextColor(int pos, int color) {
            if (textColorMap == null)
                textColorMap = new HashMap<>();
            textColorMap.put(pos, color);
            return this;
        }

        public Builder setTextColor(int defaultColor) {
            this.defaultColor = defaultColor;
            return this;
        }

        public Builder setCancelBtnText(String btnText) {
            this.btnText = btnText;
            return this;
        }

        public Builder setOverScroll(boolean overScroll) {
            mOverScroll = overScroll;
            return this;
        }

        public Builder setTitle(Title title) {
            mTitle = title;
            return this;
        }

        public ListSelectDialog build() {
            return ListSelectDialog.newInstance(mOverScroll, btnText, mTitle, new Message(messageList, textSizeMap, textColorMap, defaultColor, defaultSzie), theme);
        }
    }


    private static class Message implements Parcelable {
        private ArrayList<CharSequence> messageList;
        private ParcelableMap mTextSizeMap;
        private ParcelableMap mColorMap;

        private int defaultColor;
        private int defaultSize;

        public Message(ArrayList<CharSequence> messageList, int defaultColor, int defaultSzie) {
            this(messageList, null, null, defaultColor, defaultSzie);
        }

        public Message(ArrayList<CharSequence> messageList, HashMap<Integer, Integer> textSizeMap, HashMap<Integer, Integer> textColorMap, int defaultColor, int defaultSzie) {
            if (textSizeMap != null) {
                this.mTextSizeMap = new ParcelableMap();
                mTextSizeMap.pracMap = textSizeMap;
            }

            if (textColorMap != null) {
                this.mColorMap = new ParcelableMap();
                mColorMap.pracMap = textSizeMap;
            }

            this.messageList = messageList;
            this.defaultColor = defaultColor;
            this.defaultSize = defaultSzie;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.messageList);
            dest.writeParcelable(this.mTextSizeMap, flags);
            dest.writeParcelable(this.mColorMap, flags);
            dest.writeInt(this.defaultColor);
            dest.writeInt(this.defaultSize);
        }

        protected Message(Parcel in) {
            this.messageList = new ArrayList<>();
            in.readList(this.messageList, CharSequence.class.getClassLoader());
            this.mTextSizeMap = in.readParcelable(ParcelableMap.class.getClassLoader());
            this.mColorMap = in.readParcelable(ParcelableMap.class.getClassLoader());
            this.defaultColor = in.readInt();
            this.defaultSize = in.readInt();
        }

        public static final Creator<Message> CREATOR = new Creator<Message>() {
            @Override
            public Message createFromParcel(Parcel source) {
                return new Message(source);
            }

            @Override
            public Message[] newArray(int size) {
                return new Message[size];
            }
        };
    }

    public static class Title implements Parcelable {
        private String headerTxt;
        private int headerSize;
        private int headerBgColor;
        private int headerColor;

        public Title(String headerTxt) {
            this(headerTxt, 0, 0, 0);
        }

        public Title(String headerTxt, int headerSize, int headerBgColor, int headerColor) {
            this.headerTxt = headerTxt;
            this.headerColor = headerColor;
            this.headerSize = headerSize;
            this.headerBgColor = headerBgColor;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.headerTxt);
            dest.writeInt(this.headerSize);
            dest.writeInt(this.headerBgColor);
            dest.writeInt(this.headerColor);
        }

        public Title() {
        }

        protected Title(Parcel in) {
            this.headerTxt = in.readString();
            this.headerSize = in.readInt();
            this.headerBgColor = in.readInt();
            this.headerColor = in.readInt();
        }

        public static final Creator<Title> CREATOR = new Creator<Title>() {
            @Override
            public Title createFromParcel(Parcel source) {
                return new Title(source);
            }

            @Override
            public Title[] newArray(int size) {
                return new Title[size];
            }
        };
    }
}
