package com.android.banana.groupchat.groupchat.groupcreat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.CompressImageUtil;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.PermissionUtil;
import com.android.banana.commlib.utils.PhotoUtil;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.RequestContainer;
import com.etiennelawlor.imagegallery.library.entity.Photo;
import com.etiennelawlor.imagegallery.library.entity.StoreSelectBean;
import com.etiennelawlor.imagegallery.library.fullscreen.ImageGalleryActivity;
import com.google.gson.JsonElement;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;


/**
 * Created by qiaomu on 2017/10/25.
 * <p>
 * <p>
 * 家族创建----封面、头像、80字简介描述
 */

public class FamilyCreatActivity extends BaseActivity4Jczj implements View.OnClickListener, IHttpResponseListener<JsonElement> {
    private static final String TAG = "FamilyCreatActivity";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int SELECT_SINGLE_IMAGE = 101;
    private static final int REQUEST_CODE_CROP = 102;

    private LinearLayout groupShirtLayout;
    private TextView createGroupTipTv;
    private ImageView actionCamera, avartImage;
    private EditText family_description_edit, verify_edit_name, verify_shirt_name;

    private WrapperHttpHelper mHttpHelper;

    private String uploadFileName, command, logoUrl, mUploadFilePath, groupId, groupName, groupMemo;
    private boolean permissionStorageEnable, permissionCameraEnable, isGroupCreate = true;


    public static void startFamilyCreatActivity(Activity from, String command, int requestCode) {
        Intent intent = new Intent(from, FamilyCreatActivity.class);
        intent.putExtra("command", command);
        from.startActivityForResult(intent, requestCode);
    }

    public static void startFamilyCreatActivity(Activity from, String logoUrl, String groupId, String groupName, String groupMemo, int requestCode) {
        Intent intent = new Intent(from, FamilyCreatActivity.class);
        intent.putExtra("isGroupCreate", false);
        intent.putExtra("logoUrl", logoUrl);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupMemo", groupMemo);
        from.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void initEnv() {
        isGroupCreate = getIntent().getBooleanExtra("isGroupCreate", true);
        command = getIntent().getStringExtra("command");
        logoUrl = getIntent().getStringExtra("logoUrl");
        groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("groupName");
        groupMemo = getIntent().getStringExtra("groupMemo");


    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_family_description, R.string.creat_family_title, R.menu.menu_ok, MODE_BACK);
    }

    @Override
    protected void setUpView() {

        if (isGroupCreate) {
            MenuItem item = mToolbar.getMenu().getItem(0);
            item.setTitle(R.string.family_creat);
        } else {
            setUpTitle(getString(R.string.modify_group_info));
        }

        createGroupTipTv = findViewOfId(R.id.createGroupTipTv);
        groupShirtLayout = findViewOfId(R.id.group_shirt_layout);
        actionCamera = findViewOfId(R.id.family_action_camera);
        avartImage = findViewOfId(R.id.avart_image);
        family_description_edit = findViewOfId(R.id.family_description_edit);
        verify_edit_name = findViewOfId(R.id.verify_edit_name);
        verify_shirt_name = findViewOfId(R.id.verify_shirt_name);

        createGroupTipTv.setVisibility(isGroupCreate ? View.VISIBLE : View.GONE);
        groupShirtLayout.setVisibility(isGroupCreate ? View.VISIBLE : View.GONE);
        ((View) findViewOfId(R.id.line)).setVisibility(isGroupCreate ? View.VISIBLE : View.GONE);

        verify_edit_name.setText(groupName);
        family_description_edit.setText(groupMemo);

        family_description_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    creatGroup();
                    return true;
                }
                return false;
            }
        });

        actionCamera.setOnClickListener(this);
        avartImage.setOnClickListener(this);
        if (!isGroupCreate && !TextUtils.isEmpty(logoUrl)) {
            PicUtils.load(this, avartImage, logoUrl, R.drawable.family_avart);
        }

        requestPermission();

    }

    private void requestPermission() {
        permissionStorageEnable = PermissionUtil.isPermissionEnable(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionCameraEnable = PermissionUtil.isPermissionEnable(this, Manifest.permission.CAMERA);

        PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionUtil.PermissionChecker() {
            @Override
            public void onGrant(String grantPermission, int index) {
                if (grantPermission == Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    permissionStorageEnable = true;
                if (grantPermission == Manifest.permission.CAMERA)
                    permissionCameraEnable = true;
            }

            @Override
            public void onDenied(String deniedPermission, int index) {
                if (deniedPermission == Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    showToast(getString(R.string.storage_permission_denied));
                if (deniedPermission == Manifest.permission.CAMERA)
                    showToast(getString(R.string.camera_permission_denied));
            }
        });
    }

    @Override
    protected void setUpData() {
        mHttpHelper = new WrapperHttpHelper(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (creatGroup())
            return false;

        return super.onMenuItemClick(item);
    }

    private boolean creatGroup() {
        if (isGroupCreate
                && (TextUtils.isEmpty(mUploadFilePath)
                || !new File(mUploadFilePath).exists())
                || !isGroupCreate
                && TextUtils.isEmpty(logoUrl)) {
            showToast(getString(R.string.group_avart_must));
            return true;
        }

        String name = verify_edit_name.getText().toString();
        String description = family_description_edit.getText().toString();
        String shirtName = verify_shirt_name.getText().toString();

        if (TextUtils.isEmpty(name)) {
            showToast(getString(R.string.pelease_input_group_name_within_10wrods));
            return true;
        }

        if (TextUtils.isEmpty(description)) {
            showToast(getString(R.string.pelease_input_group_des_within_80wrods));
            return true;
        }

        if (TextUtils.isEmpty(shirtName) && isGroupCreate) {
            showToast(getString(R.string.pelease_input_group_shirt_name));
            return true;
        }

        showProgressDialog();
        if (!TextUtils.isEmpty(mUploadFilePath) && new File(mUploadFilePath).exists()) {
            File cropFile = new File(mUploadFilePath);
            RequestFormBody formBody = new RequestFormBody(JczjURLEnum.IMAGE_TEMP_UPLOAD, true, true);
            formBody.putFormDataPart("file", cropFile.getName(), cropFile);
            mHttpHelper.startRequest(formBody);

        } else if (!isGroupCreate) {
            updateGroupInfo();
        }

        return false;
    }

    private void uploadGroupInfo() {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.GROUP_CREATE, true);
        formBody.put("activationCode", command);
        formBody.put("groupName", verify_edit_name.getText().toString().toString());
        formBody.put("memo", family_description_edit.getText().toString().toString());
        formBody.put("logoFileName", uploadFileName);
        formBody.put("labelContent", verify_shirt_name.getText().toString().toString());
        mHttpHelper.startRequest(formBody);

    }

    private void updateGroupInfo() {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.GROUP_INFO_MODIFY, true);
        formBody.put("groupId", groupId);
        formBody.put("groupName", verify_edit_name.getText().toString().toString());
        formBody.put("memo", family_description_edit.getText().toString().toString());
        formBody.put("logoFileName", uploadFileName);
        mHttpHelper.startRequest(formBody);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.family_action_camera) {
            if (!permissionStorageEnable) {
                requestPermission();
                return;
            }
            mUploadFilePath = getExternalCacheDir().getAbsolutePath() + "/temp_capture.png";
            PhotoUtil.capture(this, mUploadFilePath);
        } else if (id == R.id.avart_image) {
            if (!permissionCameraEnable) {
                requestPermission();
                return;
            }
            startImageGalleryActivity();
        }
    }


    public void startImageGalleryActivity() {
        Intent intent = new Intent(this, ImageGalleryActivity.class);
        intent.putExtra("maxInsert", 1);
        intent.putExtra("operation", 1);
        startActivity(intent);
    }

    //图片选择完成后，发送图片文件消息,这个消息是从ImageGalleryActivity  post过来的
    @Subscribe(priority = 100)
    public void onImageSelectResult(StoreSelectBean bean) {
        List<Photo> list = (List<Photo>) bean.getList();
        if (list == null || list.size() == 0)
            return;
        mUploadFilePath = list.get(0).getPath();
        int dstSize = LibAppUtil.dip2px(this, 80);
        Bitmap smallBitmap = CompressImageUtil.getScaleBitmap(mUploadFilePath, dstSize, dstSize);
        avartImage.setImageBitmap(smallBitmap);
        bean.setList(null);

        EventBus.getDefault().cancelEventDelivery(bean) ;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("path", mUploadFilePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("path"))
            mUploadFilePath = savedInstanceState.getString("path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (!new File(mUploadFilePath).exists()) {
            showToast(R.string.file_not_found);
            return;
        }
        int dstSize = LibAppUtil.dip2px(this, 80);
        Bitmap smallBitmap = CompressImageUtil.getScaleBitmap(mUploadFilePath, dstSize, dstSize);
        avartImage.setImageBitmap(smallBitmap);
    }


    @Override
    public void onSuccess(RequestContainer request, JsonElement jsonElement) {
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        if (requestEnum == JczjURLEnum.IMAGE_TEMP_UPLOAD) {
            try {
                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                uploadFileName = jsonObject.getString("fileName");
                if (isGroupCreate) {
                    uploadGroupInfo();
                } else {
                    updateGroupInfo();
                }
            } catch (JSONException e) {
                closeProgressDialog();
                showToast(getString(R.string.data_error));
                e.printStackTrace();
            }
        } else {
            closeProgressDialog();
            showToast(getString(isGroupCreate ? R.string.group_creat_success : R.string.group_update_success));
            setResult(RESULT_OK, new Intent());
            finish();
        }
    }


    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        closeProgressDialog();
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        if (requestEnum == JczjURLEnum.IMAGE_TEMP_UPLOAD) {
            showToast(getString(R.string.file_upload_failed));
        }
        operateErrorResponseMessage(jsonObject);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
