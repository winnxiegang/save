package com.android.xjq.activity.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.dialog.ShowSimpleMessageDialog;
import com.android.banana.commlib.emoji.EmojBean;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.SoftKeyboardStateHelper;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.utils.IMInputFilter;
import com.android.banana.utils.fileupload.FileUploadManager;
import com.android.banana.utils.fileupload.SimpleUploadCallback;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.MatchSelectListActivity;
import com.android.xjq.activity.SpeechRuleDescriptionActivity;
import com.android.xjq.bean.ContactBean;
import com.android.xjq.bean.ForecastBean;
import com.android.xjq.bean.SubjectCreateConfig;
import com.android.xjq.bean.SubjectElement;
import com.android.xjq.bean.medal.UserMedalBean;
import com.android.xjq.fragment.input.EmojiFragment;
import com.android.xjq.fragment.input.InputCallback;
import com.android.xjq.model.StoreElementTypeEnum;
import com.android.xjq.model.SubjectTypeEnum;
import com.android.xjq.utils.XjqUtils;
import com.etiennelawlor.imagegallery.library.entity.Photo;
import com.etiennelawlor.imagegallery.library.entity.StoreSelectBean;
import com.etiennelawlor.imagegallery.library.fullscreen.FullScreenImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.fullscreen.ImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.fullscreen.MyPhotoAdapter;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.banana.http.JczjURLEnum.IMAGE_TEMP_UPLOAD;
import static com.android.xjq.activity.message.WriteMySubjectActivity.WriteTypeEnum.WRITE_SUBJECT;

/**
 * create by zhouyi
 */
public class WriteMySubjectActivity extends BaseActivity implements IHttpResponseListener, InputCallback {

    private WrapperHttpHelper httpOperate;
    private FileUploadManager uploadUtils;

    public enum WriteTypeEnum {
        //写说说
        WRITE_SPEAK,
        //写专栏
        WRITE_SUBJECT,

        //写评论
        WRITE_COMMENT,
        //写赛事评
        WRITE_MATCH_COMMENT
    }

    public static final int SUBJECT_TYPE = 0000;

    //插入最多图片的数量
    private final int INSERT_MAX_PHOTO_COUNT = 5;

    //评论最大输入字数
    private final int COMMENT_MAX_TEXT_COUNT = 2000;

    //话题最大输入字数
    private final int SUBJECT_MAX_INPUT_COUNT = 5000;

    //说说最大输入字数
    private final int SPEAK_MAX_INPUT_COUNT = 500;

    //评论至少输入字数
    private final int COMMENT_MIN_INPUT_COUNT = 5;

    //话题最少输入字数
    private final int SUBJECT_MIN_INPUT_COUNT = 300;

    //说说最少输入字数
    private final int SPEAK_MIN_INPUT_COUNT = 5;

    enum ShowType {
        SHOW_NONE,
        SHOW_KEYBOARD,
        SHOW_EMOJ_LAYOUT,
        SHOW_ADD_LAYOUT
    }

    @BindView(R.id.sendTv)
    TextView sendTv;

    @BindView(R.id.rootLayout)
    LinearLayout rootLayout;

    @BindView(R.id.contentTextNumTv)
    TextView contentTextNumTv;

    @BindView(R.id.titleEt)
    EditText titleEt;

    @BindView(R.id.contentEt)
    EditText contentEt;

    @BindView(R.id.addEmojIv)
    RadioButton addEmojIv;

    @BindView(R.id.addIv)
    RadioButton addIv;

    @BindView(R.id.addPhotoIv)
    RadioButton addPhotoIv;

    @BindView(R.id.addLayout)
    LinearLayout addLayout;

    @BindView(R.id.textParentLayout)
    LinearLayout textParentLayout;

    @BindView(R.id.syncHomePageLayout)
    LinearLayout syncHomePageLayout;

    @BindView(R.id.forecastLayout)
    RelativeLayout forecastLayout;

    @BindView(R.id.forecastIv)
    ImageView forecastIv;

    @BindView(R.id.analysisIv)
    ImageView analysisIv;

    @BindView(R.id.forecastGreenLayout)
    LinearLayout forecastGreenLayout;

    @BindView(R.id.forecastGreenTv)
    TextView forecastGreenTv;

    @BindView(R.id.analysisCloseIv)
    ImageView analysisCloseIv;

    @BindView(R.id.reAnalysisGreenLayout)
    LinearLayout reAnalysisGreenLayout;

    @BindView(R.id.lineView)
    View lineView;

    @BindView(R.id.gridView)
    GridView gridView;

    @BindView(R.id.emojLayout)
    LinearLayout emojLayout;

    @BindView(R.id.titleLinearLayout)
    LinearLayout titleLayout;

    @BindView(R.id.shareHomePageCb)
    CheckBox shareHomePageCb;

    @BindView(R.id.scrollContentLayout)
    ScrollView scrollContentLayout;

    @BindView(R.id.subjectTypeLayout)
    LinearLayout subjectTypeLayout;

    @BindView(R.id.subjectTypeTv)
    TextView subjectTypeTv;

    @BindView(R.id.subject_close)
    ImageView matchCloseIv;

    @BindView(R.id.agreeSpeechRuleLayout)
    LinearLayout agreeSpeechRuleLayout;

    private MyPhotoAdapter mPhotoAdapter;

    private List<Photo> mPhotoList;

    private WriteTypeEnum mWriteType = WRITE_SUBJECT;

    private String mObjectId;

    private String mCommentType;

    //是否通过用户输入@进入添加联系人界面
    private boolean mFromInputAt = false;

    private ShowType mShowType = ShowType.SHOW_NONE;

    /**
     * 存储插入的元素（图片，表情，方案，赛事，联系人）
     */
    private List<SubjectElement> mSubjectElements = new ArrayList<>();


    //获取删除前的字段
    private String mBeforeStr;

    //获取删除前的起始位置
    private int mBeforeStart;

    //获取删除几个字符
    private int mBeforeCount;

    private String mAfterStr;

    private int mMaxInputCount = SUBJECT_MAX_INPUT_COUNT;
    private int mMinInputCount = SUBJECT_MIN_INPUT_COUNT;
    private int mMinTitleCount = 5;
    private int mMaxTitleCount = 30;

    private SoftKeyboardStateHelper softKeyboardStateHelper;

    private boolean mRestoreFromSave = false;

    private String mInnerRaceId;

    private ForecastBean forecastBean;

    private String mTag = null;

    private String supportUserChooseOptions = null;

    private boolean mPredict = true;

    private String mPridictMessage = null;

    /**
     * 当前写话题的类型
     * 默认是普通话题类型
     */
    private String subjectType = SubjectTypeEnum.NORMAL_SUBJECT.name();

    /**
     * 是否显示等级提示
     */
    private boolean isShowLevelCommon;

    //香蕉球二期的赛事选择
    private JczqDataBean selectDataBean;
    private ArrayList<JczqDataBean> selectedList;

    public static void startWriteMySubjectActivity(Context from, WriteTypeEnum writeType, String objectId, String commentType) {

        Intent intent = new Intent();

        intent.setClass(from, WriteMySubjectActivity.class);

        intent.putExtra("writeType", writeType.name());

        intent.putExtra("objectId", objectId);

        intent.putExtra("commentType", commentType);

        from.startActivity(intent);

    }

    //图片删除监听事件
    private View.OnClickListener imageDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int pos = (int) v.getTag();

            mPhotoList.remove(pos);

            mPhotoAdapter.notifyDataSetChanged();

            operatePhotoAddOrDeleteTextCount(-1, false);

        }
    };

    /**
     * @param addSize 增加图片的数量
     * @param isAdd   是否是增加图片
     */
    private void operatePhotoAddOrDeleteTextCount(int addSize, boolean isAdd) {
        if (!StringUtils.isBlank(contentTextNumTv.getText().toString())) {
            String count = contentTextNumTv.getText().toString();
            int i = Integer.parseInt(count);
            int allCount = 0;
            if (isAdd) {
                allCount = i - addSize * 2;
            } else {
                allCount = i + 2;
            }
            contentTextNumTv.setText(String.valueOf((allCount)));
        } else {
            contentTextNumTv.setText(String.valueOf((mPhotoList.size() * 2)));
        }
    }

    //TODO
    private void addTextNumCount(int textCount) {
        //如果全是空格的话
        if(contentTextNumTv.getText().toString().trim().length()==0){
            contentTextNumTv.setText("0");
        }
        else if (!StringUtils.isBlank(contentTextNumTv.getText().toString())) {

            String count = contentTextNumTv.getText().toString();
            int i = Integer.parseInt(count);
            i -= textCount;
            if (i < 0) {
                contentTextNumTv.setTextColor(getResources().getColor(R.color.main_red));
            } else {
                contentTextNumTv.setTextColor(Color.parseColor("#757576"));
            }
            contentTextNumTv.setText(String.valueOf(i));
        }
    }


    @OnClick(R.id.sendTv)
    public void send() {
        final String comment = contentEt.getText().toString();

        //如果是发话题必须要有赛事
        if (mWriteType == WRITE_SUBJECT && selectDataBean == null) {
            LibAppUtil.showTip(this, "请选择一场比赛");
            return;
        }

        //获取已输入字符
        int textCount = mMaxInputCount - Integer.parseInt(contentTextNumTv.getText().toString());

        if (textCount < mMinInputCount) {//内容最小值判断
            ToastUtil.showLong(XjqApplication.getContext(), String.format(getString(R.string.write_subject_content_limit_min), mMinInputCount));
            return;
        }

        if (textCount > mMaxInputCount) {//内容最大值判断
            ToastUtil.showLong(XjqApplication.getContext(), String.format(getString(R.string.write_subject_content_limit_max), mMaxInputCount));
            return;
        }
        //--------------------------------标题最大最小值判断
        if (mWriteType == WRITE_SUBJECT &&( TextUtils.isEmpty(titleEt.getText()) || titleEt.getText().length() <= mMinTitleCount || titleEt.getText().length() > mMaxTitleCount)) {
            showToast(String.format(getString(R.string.subject_title_limit), mMinTitleCount, mMaxTitleCount));
            return;
        }

        //没插入图片就先执行发送文本，否则先发送图片
        if (mPhotoList.size() == 0 && !TextUtils.isEmpty(comment)) {
            sendContent();
            return;
        }

        //------------------------没插入图标就不往下执行了
        if (mPhotoList.size() == 0)
            return;

        showProgressDialog();
        FileUploadManager.upload(mPhotoList, null, JczjURLEnum.IMAGE_TEMP_UPLOAD, new SimpleUploadCallback() {
            @Override
            public void onUploaded(int posInAdapter, int curPosition) {
                if (curPosition == mPhotoList.size() - 1)
                    sendContent();
            }

            @Override
            public void onUploadError(JSONObject jsonObject, int posInAdapter, int curPosition) {
                if (curPosition == mPhotoList.size() - 1)
                    sendContent();
            }
        });
    }

    @OnClick(R.id.agreeSpeechRuleLayout)
    public void agreeSpeechRule() {
        SpeechRuleDescriptionActivity.startSpeechRuleDescriptionActivity(this);
    }

    @OnClick(R.id.addEmojIv)
    public void addEmoj() {

        if (emojLayout.getVisibility() == View.VISIBLE) {
            LibAppUtil.showSoftKeyboard(this, contentEt);
        } else {
            if (addLayout.getVisibility() == View.VISIBLE) {
                addLayout.setVisibility(View.GONE);
                emojLayout.setVisibility(View.VISIBLE);
                addIv.setButtonDrawable(R.drawable.icon_add_content_normal);
                addEmojIv.setButtonDrawable(R.drawable.ic_subject_emoji_normal);
                return;
            } else {
                if (mShowType == ShowType.SHOW_NONE) {
                    mShowType = ShowType.SHOW_EMOJ_LAYOUT;
                    emojLayout.setVisibility(View.VISIBLE);
                    addIv.setButtonDrawable(R.drawable.icon_add_content_normal);
                    addEmojIv.setButtonDrawable(R.drawable.ic_subject_emoji_pressed);
                } else {
                    mShowType = ShowType.SHOW_EMOJ_LAYOUT;
                    LibAppUtil.hideSoftKeyboard(this);
                }
            }
        }
    }

    @OnClick(R.id.addIv)
    public void showAddLayout() {

        if (addLayout.getVisibility() == View.VISIBLE) {
            LibAppUtil.showSoftKeyboard(this, contentEt);
        } else {
            if (emojLayout.getVisibility() == View.VISIBLE) {
                emojLayout.setVisibility(View.GONE);
                addLayout.setVisibility(View.VISIBLE);
                addIv.setButtonDrawable(R.drawable.icon_add_content_selected);
                addEmojIv.setButtonDrawable(R.drawable.ic_subject_emoji_normal);
                return;
            } else {
                if (mShowType == ShowType.SHOW_NONE) {
                    mShowType = ShowType.SHOW_ADD_LAYOUT;
                    addLayout.setVisibility(View.VISIBLE);
                    addIv.setButtonDrawable(R.drawable.icon_add_content_selected);
                    addEmojIv.setButtonDrawable(R.drawable.ic_subject_emoji_normal);
                } else {
                    mShowType = ShowType.SHOW_ADD_LAYOUT;
                    LibAppUtil.hideSoftKeyboard(this);
                }
            }
        }
    }

    @OnClick(R.id.subjectTypeTv)
    public void selectMatchClick() {
        MatchSelectListActivity.startMatchSelectListActivity(this, 1, selectedList);
    }

    @OnClick(R.id.subject_close)
    public void onSubjectCloseClick() {
        subjectTypeTv.setGravity(Gravity.CENTER_VERTICAL);
        subjectTypeTv.setPadding(LibAppUtil.dip2px(this, 16), 0, 0, 0);
        subjectTypeTv.setTextColor(getResources().getColor(R.color.hintColor));
        subjectTypeTv.setText(R.string.select_match_tip);

        subjectTypeLayout.setBackgroundColor(Color.WHITE);

        if (selectedList != null)
            selectedList.clear();

        selectDataBean = null;
        matchCloseIv.setVisibility(View.GONE);
    }

    @OnClick(R.id.addPhotoIv)
    public void addPhoto() {

        if (mPhotoList == null) {
            ImageGalleryActivity.startImageGalleryActivity(this, INSERT_MAX_PHOTO_COUNT, ImageGalleryActivity.INSERT_IMAGE);
        } else {
            ImageGalleryActivity.startImageGalleryActivity(this, INSERT_MAX_PHOTO_COUNT - mPhotoList.size(), ImageGalleryActivity.INSERT_IMAGE);
        }
    }


    /**
     * 插入方案
     */
    @OnClick(R.id.orderLayout)
    public void showOrderActivity() {

        //SelectOrderActivity.startSelectOrderActivity(this, getCurrentInsertCount(StoreElementTypeEnum.PURCHASE_ORDER.name()));

    }

    /**
     * 插入赛事
     */
    @OnClick(R.id.matchLayout)
    public void showMatchActivity() {

        // SelectMatchActivity.startSelectMatchActivity(this, getCurrentInsertCount(StoreElementTypeEnum.MATCH.name()));

    }

    /**
     * 插入联系人
     */
    @OnClick(R.id.atLayout)
    public void showContactActivity() {

        mFromInputAt = false;

        //InsertContactActivity.startInsertContactActivity(this, getCurrentInsertCount(StoreElementTypeEnum.AT.name()));

    }

    @OnClick(R.id.subjectTypeLayout)
    public void subjectTypeSelect() {

        //SubjectTypeSelectActivity.startSubjectTypeSelectActivity(this, subjectType);
    }

    /**
     * 根据类型名获取当前插入相应类型的个数(联系人，赛事，方案)
     *
     * @param type
     * @return
     */
    private int getCurrentInsertCount(String type) {
        String content = contentEt.getText().toString();
        int count = 0;
        for (int i = 0; i < mSubjectElements.size(); i++) {
            if (mSubjectElements.get(i).getType().equals(type)) {
                String model = mSubjectElements.get(i).getKey();
                int lastIndex = 0;
                for (int j = 0; j < content.length(); j++) {
                    int index = content.indexOf(model, lastIndex);
                    if (index != -1) {
                        lastIndex = index + model.length();
                        j = lastIndex;
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * 根据文本，获取插入的元素的所有文字数量
     *
     * @param content
     * @return
     */
    //TODO
    private int getInsertElementTextCount(String content) {

        int textCount = 0;
        int hideTextCount = 0;
        for (int i = 0; i < mSubjectElements.size(); i++) {
            String model = mSubjectElements.get(i).getKey();
            int lastIndex = 0;
            for (int j = 0; j < content.length(); j++) {
                int index = content.indexOf(model, lastIndex);
                if (index != -1) {
                    lastIndex = index + model.length();
                    j = lastIndex;
                    hideTextCount += model.length();
                    if (model.contains(StoreElementTypeEnum.EMOTION.name())) {
                        textCount += mSubjectElements.get(i).getEmojMessage().length();
                    } else {
                        textCount += mSubjectElements.get(i).getShowMessage().length();
                    }
                }
            }
        }

        int contentTextCount = content.length() - hideTextCount + textCount;

        return contentTextCount;

    }

    public static void startWriteMySubjectActivity(Context from, @NonNull WriteTypeEnum writeType) {
        Intent intent = new Intent(from, WriteMySubjectActivity.class);
        intent.putExtra("writeType", writeType.name());
        from.startActivity(intent);
    }

    /**
     * 插入表情、方案、联系人、赛事设置内容输入框获取焦点
     */
    private void setContentEditTextFocus() {
        contentTextNumTv.setVisibility(View.VISIBLE);
        contentEt.requestFocus();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_my_subject);

        ButterKnife.bind(this);

        initIntentData();

        initView();

        agreeSpeechRuleLayout.setVisibility(View.VISIBLE);

        if (mWriteType == WRITE_SUBJECT) {

            subjectTypeLayout.setVisibility(View.VISIBLE);
            mMaxInputCount = SUBJECT_MAX_INPUT_COUNT;

        } else if (mWriteType == WriteTypeEnum.WRITE_COMMENT) {

            contentTextNumTv.setVisibility(View.VISIBLE);
            subjectTypeLayout.setVisibility(View.GONE);
            mMaxInputCount = COMMENT_MAX_TEXT_COUNT;

        } else if (mWriteType == WriteTypeEnum.WRITE_MATCH_COMMENT) {

            contentTextNumTv.setVisibility(View.VISIBLE);
            subjectTypeLayout.setVisibility(View.GONE);
            mMaxInputCount = COMMENT_MAX_TEXT_COUNT;

        }
        contentTextNumTv.setText(String.valueOf(mMaxInputCount));

        querySubjectCreateConfig();
    }

    private void initIntentData() {

        mObjectId = getIntent().getStringExtra("objectId");

        mCommentType = getIntent().getStringExtra("commentType");

        mWriteType = WriteTypeEnum.valueOf(getIntent().getStringExtra("writeType"));

        switch (mWriteType) {
            case WRITE_SUBJECT:
                mMaxInputCount = SUBJECT_MAX_INPUT_COUNT;
                mMinInputCount = SUBJECT_MIN_INPUT_COUNT;
                subjectType = "PERSONAL_ARTICLE";
                break;
            case WRITE_SPEAK:
                mMaxInputCount = SPEAK_MAX_INPUT_COUNT;
                mMinInputCount = SPEAK_MIN_INPUT_COUNT;
                subjectType = "NORMAL";
                break;
            case WRITE_COMMENT:
            case WRITE_MATCH_COMMENT:
                mMaxInputCount = COMMENT_MAX_TEXT_COUNT;
                mMinInputCount = COMMENT_MIN_INPUT_COUNT;
                break;
        }

        mInnerRaceId = mObjectId;
        titleEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        contentEt.setSingleLine(false);
        contentEt.setFilters(new InputFilter[]{new IMInputFilter(), new InputFilter.LengthFilter(mMaxInputCount)});
    }

    private void reInitAfterIntentData(HashMap<String, SubjectCreateConfig.Config> subjectConfigMap) {
        if (subjectConfigMap == null || subjectConfigMap.size() <= 0)
            return;
        SubjectCreateConfig.Config config = null;
        switch (mWriteType) {
            case WRITE_SUBJECT:
                config = subjectConfigMap.get("PERSONAL_ARTICLE_SUBJECT_LENGTH_CONFIG");
                break;
            case WRITE_SPEAK:
                config = subjectConfigMap.get("NORMAL_SUBJECT_LENGTH_CONFIG");
                break;
        }

        if (config == null)
            return;

        mMaxInputCount = config.maxContentLength;
        mMinInputCount = config.minContentLength;
        mMinTitleCount = config.minTitleLength;
        mMaxTitleCount = config.maxTitleLength;

        titleEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxTitleCount)});
        contentEt.setFilters(new InputFilter[]{new IMInputFilter(), new InputFilter.LengthFilter(mMaxInputCount)});
    }


    private void initView() {
        EmojiFragment emojiFragment = (EmojiFragment) getSupportFragmentManager().findFragmentById(R.id.emojiFragment);
        emojiFragment.setCallback(this);
        httpOperate = new WrapperHttpHelper(this);
        sendTv.setVisibility(View.VISIBLE);

        if (mWriteType == WRITE_SUBJECT) {

            setTitleBar(true, getString(R.string.write_subject_coloum_title), backClickListener);

            shareHomePageCb.setVisibility(View.GONE);


        } else if (mWriteType == WriteTypeEnum.WRITE_COMMENT) {

            subjectTypeLayout.setVisibility(View.GONE);

            titleLayout.setVisibility(View.GONE);

            setTitleBar(true, getString(R.string.write_subject_reply), backClickListener);

            contentEt.setHint(R.string.write_subject_hint);

        } else if (mWriteType == WriteTypeEnum.WRITE_SPEAK) {

            subjectTypeLayout.setVisibility(View.GONE);

            titleLayout.setVisibility(View.GONE);

            setTitleBar(true, getString(R.string.write_subject_speak_title), backClickListener);

            contentEt.setHint(getString(R.string.write_subject_hint));

        }

        mPhotoList = new ArrayList<>();

        mPhotoAdapter = new MyPhotoAdapter(this, mPhotoList, imageDeleteListener);

        gridView.setAdapter(mPhotoAdapter);

        //插入的图片点击预览功能
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == mPhotoList.size() && mPhotoList.size() != 5) {
                    if (mPhotoList == null) {
                        ImageGalleryActivity.startImageGalleryActivity(WriteMySubjectActivity.this, 5, ImageGalleryActivity.INSERT_IMAGE);
                    } else {
                        ImageGalleryActivity.startImageGalleryActivity(WriteMySubjectActivity.this, 5 - mPhotoList.size(), ImageGalleryActivity.INSERT_IMAGE);
                    }
                    return;
                }

                Intent intent = new Intent(WriteMySubjectActivity.this, FullScreenImageGalleryActivity.class);

                intent.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) mPhotoList);

                intent.putExtra("position", position);

                intent.putExtra("type", FullScreenImageGalleryActivity.PREVIEW);

                startActivity(intent);
            }
        });

        contentEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                mBeforeStr = String.valueOf(s);

                mBeforeStart = start;

                mBeforeCount = count;

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mAfterStr = String.valueOf(s);
                //检测用户是否输入@，如果用户输入@就弹出插入联系人列表
                if (before == 0 && count == 1) {
                    CharSequence input = s.subSequence(start, start + 1);
                    String str = String.valueOf(input);
                    if ("@".equals(str)) {
                        mFromInputAt = true;
                        //InsertContactActivity.startInsertContactActivity(WriteMySubjectActivity.this, getCurrentInsertCount(StoreElementTypeEnum.AT.name()));
                    }
                    addTextNumCount(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                int insertElementTextCount = getAllImageSpanTextCount((SpannableStringBuilder) s);
                int count = mMaxInputCount - insertElementTextCount - mPhotoList.size() * 2;

                contentTextNumTv.setText(String.valueOf(count));

                if (contentEt.getLineCount() < 3) {

                    textParentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp90)));

                } else if (contentEt.getLineCount() >= 3) {

                    textParentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                }
            }
        });

        softKeyboardStateHelper = new SoftKeyboardStateHelper(this, rootLayout);

        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {

                mShowType = ShowType.SHOW_KEYBOARD;

                addEmojIv.setButtonDrawable(R.drawable.ic_subject_emoji_normal);

                addIv.setButtonDrawable(R.drawable.icon_add_content_normal);

                addLayout.setVisibility(View.GONE);

                emojLayout.setVisibility(View.GONE);

            }

            @Override
            public void onSoftKeyboardClosed() {
                switch (mShowType) {
                    case SHOW_ADD_LAYOUT:
                        addLayout.setVisibility(View.VISIBLE);
                        addIv.setButtonDrawable(R.drawable.icon_add_content_selected);
                        break;
                    case SHOW_EMOJ_LAYOUT:
                        emojLayout.setVisibility(View.VISIBLE);
                        addEmojIv.setButtonDrawable(R.drawable.ic_subject_emoji_pressed);
                        break;
                    case SHOW_KEYBOARD:
                        mShowType = ShowType.SHOW_NONE;
                        break;
                }
            }
        });

        titleEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                contentTextNumTv.setVisibility(View.GONE);
                return false;
            }
        });

        contentEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                contentTextNumTv.setVisibility(View.VISIBLE);
                return false;
            }
        });

    }

    private void forecastShowRequest() {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.APP_FUNCTION_DISPLAY_CONFIG_QUERY, true);
        httpOperate.startRequest(formBody, true);
    }

    private void forecastRequest() {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.JCZQ_DATA_PREDICT, true);

        map.put("raceId", mInnerRaceId);

        httpOperate.startRequest(map, true);

    }

    private void levelCommonRequest() {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.TAG_OR_LEVEL_COMMON_QUERY, true);

        httpOperate.startRequest(map, false);
    }


    private int getAllImageSpanTextCount(SpannableStringBuilder ssb) {

        ImageSpan[] spans = ssb.getSpans(0, ssb.length(), ImageSpan.class);

        int count = 0;

        int allCount = ssb.toString().length();

        for (ImageSpan span : spans) {
            count += Integer.parseInt(span.getSource());
        }

        if (Math.abs(count) > allCount) {

            return Math.abs(count);
        } else {

            return allCount + count;
        }

    }


    /**
     * 获取隐藏元素，显示的文字字数
     *
     * @param hideString
     */
    private int getElementTextCount(String hideString) {
        for (int i = 0; i < mSubjectElements.size(); i++) {
            if (mSubjectElements.get(i).getKey().equals(hideString.trim())) {
                return mSubjectElements.get(i).getShowMessage().length();
            }
        }
        return 0;
    }

    //TDdo 评论类别
    private void sendContent() {

        switch (mWriteType) {
            case WRITE_SUBJECT:

                sendProject(true);
                break;
            case WRITE_SPEAK:
                sendProject(false);
                break;
            case WRITE_COMMENT:
            case WRITE_MATCH_COMMENT:
                sendComment();
                break;
        }

        showProgressDialog();
    }

    //查询 发话题 发说说的字数配置
    private void querySubjectCreateConfig() {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.SUBJECT_CREATE_BEFORE_QUERY, true);
        formBody.setGenericClaz(SubjectCreateConfig.class);
        httpOperate.startRequest(formBody, true);
    }

    //判断当前是否符合分析发表的接口
    private void isSendJudge() {
        showProgressDialog();
        RequestFormBody map = new RequestFormBody(JczjURLEnum.JCZQ_PREDICT_BEFORE_CREATE, true);
        map.put("raceId", mInnerRaceId);
        map.put("ischooes", "true");
        httpOperate.startRequest(map, true);

    }

    //发送话题
    private void sendProject(boolean isSubject) {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.SUBJECT_CREATE, true);

        map.put("content", getWrapContent());

        map.put("raceContent", getWrapTagMatch());

        map.put("title", titleEt.getText().toString());

        map.put("objectType", subjectType);

        if (isSubject && selectDataBean != null) {
            //  raceId=4000334507018207890980016647&* raceType=FOOTBALL
            map.put("raceId", selectDataBean.id);
            map.put("raceType", selectDataBean.isBasketballRace() ? "BASKETBALL" : "FOOTBALL");
        }

        httpOperate.startRequest(map, true);

    }

    //发送评论
    private void sendComment() {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.COMMENT_CREATE, true);

        map.put("content", getWrapContent());

        //   map.put("createTopic", "1");

        map.put("objectId", mObjectId);

        map.put("objectType", mCommentType);
        //    map.put("createTopic", shareHomePageCb.isChecked() ? "1" : "0");

        if (mPredict && mWriteType == WriteTypeEnum.WRITE_MATCH_COMMENT && supportUserChooseOptions != null) {

            map.put("userChoosedPredictOptions", supportUserChooseOptions);

            map.put("predictModeTypeEnum", "SPF_AND_RQSPF");
        }

        httpOperate.startRequest(map, true);

    }

    /**
     * 获得处理过数据
     *
     * @return
     */
    private String getWrapContent() {

        StringBuilder sb = new StringBuilder();

        String content = contentEt.getText().toString();

        //替换特殊字符
        HashMap<String, String> specialChar = XjqUtils.getSpecialChar();

        Set<String> keys = specialChar.keySet();

        Iterator<String> iterator = keys.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            if (content.contains(key)) {
                content = content.replace(key, specialChar.get(key));
            }
        }

        for (int i = 0; i < mSubjectElements.size(); i++) {
            if (content.contains(mSubjectElements.get(i).getKey())) {
                content = content.replace(mSubjectElements.get(i).getKey(), mSubjectElements.get(i).getHideMessage());
            }
        }

        sb.append(content);

        sb.append(wrapImage());

        return sb.toString();
    }

    private String getWrapTagMatch() {
        if (selectDataBean == null)
            return "";
        //<bet_article_race race_type="FOOTBALL" race_id="4000677455718507890970025453">[球会友谊 TT河内U19 VS 大南城主U19]</bet_article_race>

        String format = "<bet_article_race race_type=\"%1$s\" race_id=\"%2$s\">[%3$s]</bet_article_race>\n";
        String raceType = selectDataBean.isBasketballRace() ? "BASKETBALL" : "FOOTBALL";
        String homeName = selectDataBean.getHomeTeamName();
        String guestName = selectDataBean.getGuestTeamName();
        String matchName = selectDataBean.getMatchName() + "  " + (selectDataBean.isBasketballRace() ? guestName + " VS " + homeName : homeName + " VS " + guestName);
        return String.format(format, raceType, selectDataBean.getId(), matchName);
    }

    private String wrapImage() {

        if (mPhotoList.size() == 0) {
            return "";
        }

        String uploadImage = "<upload_image file_name=\"FILE_NAME\">[图片]</upload_image>";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < mPhotoList.size(); i++) {
            String target = mPhotoList.get(i).getUploadSuccessFileName();
            sb.append(uploadImage.replace("FILE_NAME", target == null ? "" : target));
        }

        return sb.toString();
    }

    private String wrapMatch(JczqDataBean bean, String raceType) {

        String lotteryRace = "<lottery_race race_type=\"RACE_TYPE\" biz_id=\"BIZ_ID\">MATCH_NAME</lottery_race>";

        String str = "";
        //TODO
        if ("JCLQ".equals(raceType)) {
            str = "[" + bean.getGameNo() + " " + bean.getMatchName() + " " + bean.getGuestTeamName() + "VS" + bean.getHomeTeamName() + "]";
        } else if ("JCZQ".equals(raceType)) {
            str = "[" + bean.getGameNo() + " " + bean.getMatchName() + " " + bean.getHomeTeamName() + "VS" + bean.getGuestTeamName() + "]";
        }

        String replace = lotteryRace.replace("RACE_TYPE", raceType).replace("BIZ_ID", bean.getId()).replace("MATCH_NAME", str);

        return replace;

    }

    private String wrapPurchaseOrder(String purchaseNo) {

        String lottery_project = "<lottery_project purchase_no=\"PURCHASE_NO\">[方案：PURCHASE_NO]</lottery_project>";

        String purchase_no = lottery_project.replace("PURCHASE_NO", purchaseNo);

        return purchase_no;

    }

    private String showOrderMessage(String purchaseNo) {

        String str = "[方案:" + purchaseNo + "]";

        return str;
    }

    private String showMatchMessage(JczqDataBean bean, String raceType) {

        String str = "";

        if ("JCLQ".equals(raceType)) {
            str = "[" + bean.getGameNo() + " " + bean.getMatchName() + " " + bean.getGuestTeamName() + "VS" + bean.getHomeTeamName() + "]";
        } else if ("JCZQ".equals(raceType)) {
            str = "[" + bean.getGameNo() + " " + bean.getMatchName() + " " + bean.getHomeTeamName() + "VS" + bean.getGuestTeamName() + "]";
        }

        return str;

    }

    /**
     * 获取选择的联系人
     *
     * @param list
     */
    public void onEvent(List<ContactBean> list) {
        //如果用户是输入@进入联系人选择界面，然后用户选择了联系人，就将之前输入的@删除掉。
        if (list.size() > 0 && mFromInputAt) {
            deleteChar();
        }

        setContentEditTextFocus();

        for (int i = 0; i < list.size(); i++) {

            StringBuilder sb = new StringBuilder();

            sb.append("@");

            sb.append(list.get(i).getUserName());

            int index = 0;

            Editable editable = null;

            index = contentEt.getSelectionStart();

            editable = contentEt.getText();

            SubjectElement element = addElement(list.get(i).getUserName(), sb.toString(), sb.toString(), StoreElementTypeEnum.AT.name());

            editable.insert(index, getTextSpannableString(element.getKey(), sb.toString(), Color.parseColor("#559ce8"), Color.parseColor("#ffffff")));

            editable.insert(contentEt.getSelectionStart(), " ");

            addTextNumCount(element.getShowMessage().length());

        }

    }

    @Subscribe
    public void onEvent(StoreSelectBean bean) {

        switch (bean.getStoreType()) {

            case StoreSelectBean.STORE_IMAGE: {

                List<Photo> list = (List<Photo>) bean.getList();

                mPhotoList.addAll(list);

                mPhotoAdapter.notifyDataSetChanged();

                operatePhotoAddOrDeleteTextCount(list.size(), true);
            }
            break;

            case StoreSelectBean.STORE_MATCH: {

                StoreSelectBean.StoreSubType storeSubType = bean.getStoreSubType();

                setContentEditTextFocus();

                List<JczqDataBean> list = (List<JczqDataBean>) bean.getList();

                String wrapType = null;

                int spanBackgroundColor = Color.parseColor("#33a243");

                switch (storeSubType) {
                    case JCLQ_MATCH:
                        wrapType = "JCLQ";
                        spanBackgroundColor = Color.parseColor("#FE922E");
                        break;
                    case JCZQ_MACTH:
                        wrapType = "JCZQ";
                        spanBackgroundColor = Color.parseColor("#33a243");
                        break;
                }

                for (int i = 0; i < list.size(); i++) {

                    Editable editable = contentEt.getText();

                    int index = contentEt.getSelectionStart();

                    SubjectElement element = addElement(list.get(i).getId(), showMatchMessage(list.get(i), wrapType), wrapMatch(list.get(i), wrapType), StoreElementTypeEnum.MATCH.name());

                    editable.insert(index, getTextSpannableString(element.getKey(), element.getShowMessage(), Color.parseColor("#ffffff"), spanBackgroundColor));

                    editable.insert(contentEt.getSelectionStart(), " ");

                    addTextNumCount(element.getShowMessage().length());

                }

            }
            break;

            case StoreSelectBean.STORE_PURCHASE: {

                setContentEditTextFocus();

                List<String> list = (List<String>) bean.getList();

                for (int i = 0; i < list.size(); i++) {

                    String purchaseNo = list.get(i);

                    Editable editable = contentEt.getText();

                    int index = contentEt.getSelectionStart();

                    SubjectElement element = addElement(purchaseNo, showOrderMessage(purchaseNo), wrapPurchaseOrder(purchaseNo), StoreElementTypeEnum.PURCHASE_ORDER.name());

                    editable.insert(index, getTextSpannableString(element.getKey(), element.getShowMessage(), Color.parseColor("#ffffff"), Color.parseColor("#fb4747")));

                    editable.insert(contentEt.getSelectionStart(), " ");

                    addTextNumCount(element.getShowMessage().length());
                }
            }
            break;
        }
    }

    private SubjectElement addElement(String id, String showMessage, String hideMessage, String type) {

        SubjectElement element = new SubjectElement();

        element.setKey("[" + type + id + "]");

        element.setHideMessage(hideMessage);

        element.setShowMessage(showMessage);

        element.setType(type);

        if (StoreElementTypeEnum.EMOTION.name().equals(type)) {
            element.setEmojMessage(id);
        }

        if (mSubjectElements.contains(element)) {
            return element;
        }

        mSubjectElements.add(element);

        return element;


    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor, int bgColor) {

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.SANS_SERIF);

        float baseline = -paint.ascent(); // ascent() is negative

        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(bgColor);
        canvas.drawText(text, 0, baseline, paint);
        return image;

    }

    /**
     * @param hideString 覆盖显示的文字
     * @param showString 显示给用户的文字
     * @return
     */
    private SpannableString getTextSpannableString(String hideString, String showString, int textColor, int bgColor) {

        SpannableString spanString = new SpannableString(hideString);

        ImageSpan span = getTextImageSpan(showString, textColor, bgColor, hideString);

        spanString.setSpan(span, 0, hideString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;

    }

    private SpannableString getEmojSpannableString(String hideString, int resId, String message) {

        SpannableString spanString = new SpannableString(hideString);

        String count = String.valueOf(-hideString.length() + message.length());

        ImageSpan span = getEmojImageSpan(count, resId);

        spanString.setSpan(span, 0, hideString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;

    }

    private ImageSpan getTextImageSpan(String showString, int textColor, int bgColor, String hideString) {

        Drawable d = new BitmapDrawable(getResources(), textAsBitmap(showString, contentEt.getTextSize(), textColor, bgColor));

        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

        String count = String.valueOf((-hideString.length() + showString.length()));

        ImageSpan span = new ImageSpan(d, count, ImageSpan.ALIGN_BOTTOM);

        return span;
    }

    private ImageSpan getEmojImageSpan(String message, int resId) {

        Drawable d = getResources().getDrawable(resId);

        d.setBounds(0, 0, (int) contentEt.getTextSize(), (int) contentEt.getTextSize());

        ImageSpan span = new ImageSpan(d, String.valueOf(message.length()), ImageSpan.ALIGN_BOTTOM);

        return span;

    }


    private void responseSuccessLevelCommon(JSONObject jo) throws JSONException {

//        if (jo.has("grade")) {
//
//            isShowLevelCommon = true;
//
//            //levelCommonLayout.setVisibility(View.VISIBLE);
//
//            String grade = jo.getString("grade");
//
//            SpannableStringBuilder ssb = new SpannableStringBuilder();
//
//            ssb.append("未达到");
//
//            SpannableString ss = new SpannableString("LV" + grade);
//
//            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ef630b")), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            ssb.append(ss);
//
//            ssb.append("等级发表话题不会显示在公共发现栏目");
//
//            levelCommonTv.setText(ssb);
//        }
    }

    private void responseSuccessPredictBeforeCreate() {

        sendPredict();

    }

    //发表预测接口请求
    private void sendPredict() {

        int textCount = Integer.parseInt(contentTextNumTv.getText().toString());

        if (textCount < 0) {
            ToastUtil.showLong(XjqApplication.getContext(), "评论最多只能输入2000个字");
            return;
        } else if (textCount > COMMENT_MAX_TEXT_COUNT - COMMENT_MIN_INPUT_COUNT) {
            ToastUtil.showLong(XjqApplication.getContext(), "内容不能为空，随便说点什么吧~");
            return;
        }

        sendComment();

    }

    private void responseSuccessForecastShow(JSONObject jo) throws JSONException {

        boolean predictFunctionShowConfig = Boolean.parseBoolean(jo.getString("predictFunctionShowConfig"));

        if (predictFunctionShowConfig) {

            forecastRequest();
        }

    }

    //赛事评论的数据解析
    private void responseSuccessDataPredict(JSONObject jo) throws JSONException {

        forecastBean = new Gson().fromJson(jo.toString(), ForecastBean.class);

        //如果没有预测
        if (!(forecastBean.isPredict())) {
            //我要预测布局
            forecastLayout.setVisibility(View.VISIBLE);

            forecastIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //ShowJczqPredictDialog showJczqPredictDialog = new ShowJczqPredictDialog(context, forecastBean, mTag, supportUserChooseOptions, mPridictMessage);
                }
            });

            //如果已经预测
        } else {

            String predictStatusEntity = forecastBean.getUserRacePredict().getPredictStatus().getName();

            ForecastBean.UserRacePredictEntity.PropertiesEntity sp = forecastBean.getUserRacePredict().getProperties();

            int plate = forecastBean.getUserRacePredict().getRqPlate();

            forecastGreenLayout.setVisibility(View.VISIBLE);

            //待计算状态
            if ("READY_CALCULATE".equals(predictStatusEntity)) {

                forecastGreenTv.setBackgroundResource(R.drawable.shape_green_radius_analysis);

                //命中状态
            } else if ("CORRECT".equals(predictStatusEntity)) {

                analysisIv.setBackgroundResource(R.drawable.analysis_forecast_success);

                forecastGreenTv.setBackgroundResource(R.drawable.shape_red_radius_analysis);

                //未命中状态
            } else if ("NOT_CORRECT".equals(predictStatusEntity)) {

                analysisIv.setBackgroundResource(R.drawable.analysis_forecast_fail);

                forecastGreenTv.setBackgroundResource(R.drawable.shape_gray_radius_analysis);
            }

            switch (forecastBean.getUserRacePredict().getOptions().get(0).getName()) {

                case "SPF_WIN"://胜

                    forecastGreenTv.setText("胜" + "(" + sp.getSp_SPF_WIN() + ")");

                    break;
                case "SPF_DRAW"://平

                    forecastGreenTv.setText("平" + "(" + sp.getSp_SPF_DRAW() + ")");

                    break;
                case "SPF_LOST"://负

                    forecastGreenTv.setText("负" + "(" + sp.getSp_SPF_LOST() + ")");
                    break;
                case "RQSPF_WIN"://让胜

                    if (plate > 0) {

                        forecastGreenTv.setText("[让+" + plate + "]" + "胜" + "(" + sp.getSp_RQSPF_WIN() + ")");

                    } else {

                        forecastGreenTv.setText("[让" + plate + "]" + "胜" + "(" + sp.getSp_RQSPF_WIN() + ")");

                    }
                    break;
                case "RQSPF_DRAW"://让平

                    if (plate > 0) {

                        forecastGreenTv.setText("[让+" + plate + "]" + "平" + "(" + sp.getSp_RQSPF_DRAW() + ")");

                    } else {

                        forecastGreenTv.setText("[让" + plate + "]" + "平" + "(" + sp.getSp_RQSPF_DRAW() + ")");

                    }
                    break;
                case "RQSPF_LOST"://让负


                    if (plate > 0) {

                        forecastGreenTv.setText("[让+" + plate + "]" + "负" + "(" + sp.getSp_RQSPF_LOST() + ")");

                    } else {

                        forecastGreenTv.setText("[让" + plate + "]" + "负" + "(" + sp.getSp_RQSPF_LOST() + ")");

                    }

                    break;
            }

        }
    }


    private void responseSuccessImageUpload(final JSONObject jo) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendContent();
            }
        });
    }


    private void ShowGagDialog(JSONObject jo, String content) {

        try {
            String gmtExpired = null;
            String forbiddenReason = null;
            if (jo.has("gmtExpired")) {
                gmtExpired = jo.getString("gmtExpired");
            }
            if (jo.has("forbiddenReason")) {
                forbiddenReason = jo.getString("forbiddenReason");
            }
            ShowSimpleMessageDialog simpleMessageDialog = new ShowSimpleMessageDialog(WriteMySubjectActivity.this, content + "\n" + "禁言原因:" + forbiddenReason + "\n" + "解封时间:" + gmtExpired);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showMessageDialogBeforeComment(JSONObject jo) throws JSONException {

        ErrorBean bean = new ErrorBean(jo);
        String tipMessage = "";
        if ("USER_PREDICT_ALREADY_PREDICTED".equals(bean.getError().getName())) {
            tipMessage = getString(R.string.user_predict_already_tip);
        } else if ("RACE_STOP_SELL_CAN_NOT_PREDICT".equals(bean.getError().getName())) {
            tipMessage = getString(R.string.user_predict_race_stop_sell);
        }
        ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();
        builder.setPositiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
        builder.setMessage(tipMessage);
        builder.setNegativeMessage("取消");
        builder.setPositiveMessage("确定");
        builder.setShowTitle(false);
        ShowMessageDialog dialog = new ShowMessageDialog(this, builder);
    }


    public void insertEmoj(EmojBean bean) {

        Editable editable = contentEt.getText();

        int index = contentEt.getSelectionStart();

        SubjectElement element = addElement(bean.getMessage(), String.valueOf(bean.getResId()), bean.getUploadMessage(), StoreElementTypeEnum.EMOTION.name());

        editable.insert(index, getEmojSpannableString(element.getKey(), bean.getResId(), bean.getMessage()));

        addTextNumCount(bean.getMessage().length());

    }

    public void deleteChar() {

        contentEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));

    }


    @Override
    public void onBackPressed() {
        if (mShowType != ShowType.SHOW_NONE) {
            emojLayout.setVisibility(View.GONE);
            addLayout.setVisibility(View.GONE);
            mShowType = ShowType.SHOW_NONE;
            addEmojIv.setButtonDrawable(R.drawable.ic_subject_emoji_normal);
            addIv.setButtonDrawable(R.drawable.icon_add_content_normal);
        } else {
            if (mWriteType == WRITE_SUBJECT || mWriteType == WriteTypeEnum.WRITE_SPEAK) {
                showMessageDialog();
            } else {
                super.onBackPressed();
            }
        }
    }

    public void showMessageDialog() {
        ShowMessageDialog dialog = new ShowMessageDialog(this, new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                WriteMySubjectActivity.this.finish();
            }
        }, null, getString(R.string.leave_tip));
    }

    public void setData(String tag, String chooseOptions, String predictMessage) {

        mTag = tag;

        supportUserChooseOptions = chooseOptions;

        mPridictMessage = predictMessage;

        if (!forecastBean.isPredict() && mTag != null && supportUserChooseOptions != null) {

            forecastIv.setVisibility(View.GONE);

            forecastGreenLayout.setVisibility(View.VISIBLE);

            lineView.setVisibility(View.VISIBLE);

            analysisCloseIv.setVisibility(View.VISIBLE);

            forecastGreenTv.setText(mPridictMessage);

            reAnalysisGreenLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // ShowJczqPredictDialog showForecateDialog = new ShowJczqPredictDialog(context, forecastBean, mTag, supportUserChooseOptions, mPridictMessage);

                }
            });

            //关闭预测，并初始化预测
            analysisCloseIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mTag = null;

                    supportUserChooseOptions = null;

                    mPridictMessage = null;

                    forecastGreenLayout.setVisibility(View.GONE);

                    forecastIv.setVisibility(View.VISIBLE);

                    lineView.setVisibility(View.GONE);

                    analysisCloseIv.setVisibility(View.GONE);
                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MatchSelectListActivity.SELECTED_REQUEST_CODE && data != null & resultCode == RESULT_OK) {
            this.selectedList = data.getParcelableArrayListExtra("result_list");
            if (selectedList == null || selectedList.size() <= 0)
                return;
            selectDataBean = selectedList.get(0);
            String stringDate = TimeUtils.formatStringDate(selectDataBean.getGmtStart(), TimeUtils.MATCH_FORMAT);
            NormalObject raceStatus = selectDataBean.raceStatus;
            String insertStr = " VS ";
            if ("FINISH".equals(raceStatus.getName())) {
                insertStr = selectDataBean.getFullHomeScore() + ":" + selectDataBean.getFullGuestScore();
            }

            String matchTitle = selectDataBean.getMatchName() + stringDate + selectDataBean.getHomeTeamName() + insertStr + selectDataBean.getGuestTeamName();

            subjectTypeTv.setText(matchTitle);
            subjectTypeTv.setTextColor(ContextCompat.getColor(this, R.color.txt_middle));
            subjectTypeTv.setGravity(Gravity.CENTER);
            subjectTypeTv.setPadding(0, 0, 0, 0);
            subjectTypeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.light_blue_50));

            matchCloseIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(RequestContainer request, Object object) {
        closeProgressDialog();
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        switch (requestEnum) {
            case IMAGE_TEMP_UPLOAD: {
                responseSuccessImageUpload(((JSONObject) object));
            }
            break;
            case SUBJECT_CREATE: {
                com.android.banana.commlib.utils.LibAppUtil.showTip(this.getApplicationContext(), "发表成功", R.drawable.icon_subscribe_success);
                try {
                    String subjectId = ((JSONObject) object).getString("subjectId");
                    //SubjectDetailActivity.startSubjectDetailActivity(this, subjectId);
                    //clearSaveContent();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            case COMMENT_CREATE: {

                com.android.banana.commlib.utils.LibAppUtil.showTip(this, "发表成功", R.drawable.icon_subscribe_success);

                finish();

            }
            break;

            case JCZQ_DATA_PREDICT:
                try {
                    responseSuccessDataPredict((JSONObject) object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case APP_FUNCTION_DISPLAY_CONFIG_QUERY:
                try {
                    responseSuccessForecastShow((JSONObject) object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            //评论是否允许的请求
            case JCZQ_PREDICT_BEFORE_CREATE:

                responseSuccessPredictBeforeCreate();

                break;

            case TAG_OR_LEVEL_COMMON_QUERY:
                try {
                    responseSuccessLevelCommon((JSONObject) object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case SUBJECT_CREATE_BEFORE_QUERY:
                reInitAfterIntentData(((SubjectCreateConfig) object).subjectConfigMap);
                break;

        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        closeProgressDialog();
        JczjURLEnum anEnum = (JczjURLEnum) request.getRequestEnum();
        if (anEnum == IMAGE_TEMP_UPLOAD) {
            ToastUtil.showLong(XjqApplication.getContext(), "网络超时或服务器繁忙");
        }

        try {
            ErrorBean bean = new ErrorBean(jsonObject);

            if (anEnum == IMAGE_TEMP_UPLOAD) {
                operateErrorResponseMessage(jsonObject);
            } else if (anEnum == JczjURLEnum.JCZQ_PREDICT_BEFORE_CREATE) {

                //TODO
                mPredict = false;

                showMessageDialogBeforeComment(jsonObject);

            } else if ("POST_FORBIDDEN".equals(bean.getError().getName())) {

                ShowGagDialog(jsonObject, "您已被禁言");
            } else if ("PUSH_ANALYSE_FORBIDDEN".equals(bean.getError().getName())) {

                ShowGagDialog(jsonObject, "您已被禁止发分析");
            } else if ("PUSH_ANALYSE_WITHOUT_AUTHORITY".equals(bean.getError().getName())) {

                // PushAnalyseNoPermissionDialog dialog = new PushAnalyseNoPermissionDialog(this);
            } else {
                operateErrorResponseMessage(jsonObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WriteMySubjectActivity.this.finish();
        }
    };

    @Override
    public void onEmojAdd(EmojBean bean) {
        setContentEditTextFocus();
        if (bean.getMessage() == null) {
            deleteChar();
        } else {
            insertEmoj(bean);
        }
    }

    @Override
    public void onEmojDelete() {
        deleteChar();
    }

    @Override
    public void onBubbleSelected(String fontColor) {

    }

    @Override
    public void onFansMedalSelected(UserMedalBean userMedalBean) {

    }
}
