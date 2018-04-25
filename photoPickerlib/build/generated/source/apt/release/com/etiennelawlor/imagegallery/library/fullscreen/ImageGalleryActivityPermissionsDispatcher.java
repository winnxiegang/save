// This file was generated by PermissionsDispatcher. Do not modify!
package com.etiennelawlor.imagegallery.library.fullscreen;

import android.support.v4.app.ActivityCompat;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.GrantableRequest;
import permissions.dispatcher.PermissionUtils;

final class ImageGalleryActivityPermissionsDispatcher {
  private static final int REQUEST_GETALLIMAGE = 1;

  private static final String[] PERMISSION_GETALLIMAGE = new String[] {"android.permission.READ_EXTERNAL_STORAGE"};

  private static final int REQUEST_DISPATCHTAKEPICTUREINTENT = 2;

  private static final String[] PERMISSION_DISPATCHTAKEPICTUREINTENT = new String[] {"android.permission.CAMERA"};

  private static GrantableRequest PENDING_DISPATCHTAKEPICTUREINTENT;

  private ImageGalleryActivityPermissionsDispatcher() {
  }

  static void getAllImageWithCheck(ImageGalleryActivity target) {
    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_GETALLIMAGE)) {
      target.getAllImage();
    } else {
      ActivityCompat.requestPermissions(target, PERMISSION_GETALLIMAGE, REQUEST_GETALLIMAGE);
    }
  }

  static void dispatchTakePictureIntentWithCheck(ImageGalleryActivity target, int actionCode) {
    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_DISPATCHTAKEPICTUREINTENT)) {
      target.dispatchTakePictureIntent(actionCode);
    } else {
      PENDING_DISPATCHTAKEPICTUREINTENT = new DispatchTakePictureIntentPermissionRequest(target, actionCode);
      ActivityCompat.requestPermissions(target, PERMISSION_DISPATCHTAKEPICTUREINTENT, REQUEST_DISPATCHTAKEPICTUREINTENT);
    }
  }

  static void onRequestPermissionsResult(ImageGalleryActivity target, int requestCode, int[] grantResults) {
    switch (requestCode) {
      case REQUEST_GETALLIMAGE:
      if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_GETALLIMAGE)) {
        return;
      }
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.getAllImage();
      }
      break;
      case REQUEST_DISPATCHTAKEPICTUREINTENT:
      if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_DISPATCHTAKEPICTUREINTENT)) {
        return;
      }
      if (PermissionUtils.verifyPermissions(grantResults)) {
        if (PENDING_DISPATCHTAKEPICTUREINTENT != null) {
          PENDING_DISPATCHTAKEPICTUREINTENT.grant();
        }
      }
      PENDING_DISPATCHTAKEPICTUREINTENT = null;
      break;
      default:
      break;
    }
  }

  private static final class DispatchTakePictureIntentPermissionRequest implements GrantableRequest {
    private final WeakReference<ImageGalleryActivity> weakTarget;

    private final int actionCode;

    private DispatchTakePictureIntentPermissionRequest(ImageGalleryActivity target, int actionCode) {
      this.weakTarget = new WeakReference<>(target);
      this.actionCode = actionCode;
    }

    @Override
    public void proceed() {
      ImageGalleryActivity target = weakTarget.get();
      if (target == null) return;
      ActivityCompat.requestPermissions(target, PERMISSION_DISPATCHTAKEPICTUREINTENT, REQUEST_DISPATCHTAKEPICTUREINTENT);
    }

    @Override
    public void cancel() {
    }

    @Override
    public void grant() {
      ImageGalleryActivity target = weakTarget.get();
      if (target == null) return;
      target.dispatchTakePictureIntent(actionCode);
    }
  }
}