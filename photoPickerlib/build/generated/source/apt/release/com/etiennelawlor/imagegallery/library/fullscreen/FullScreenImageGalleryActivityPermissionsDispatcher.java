// This file was generated by PermissionsDispatcher. Do not modify!
package com.etiennelawlor.imagegallery.library.fullscreen;

import android.support.v4.app.ActivityCompat;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

final class FullScreenImageGalleryActivityPermissionsDispatcher {
  private static final int REQUEST_SAVEIMAGE = 0;

  private static final String[] PERMISSION_SAVEIMAGE = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"};

  private FullScreenImageGalleryActivityPermissionsDispatcher() {
  }

  static void saveImageWithCheck(FullScreenImageGalleryActivity target) {
    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SAVEIMAGE)) {
      target.saveImage();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SAVEIMAGE)) {
        target.showRationaleForCamera(new SaveImagePermissionRequest(target));
      } else {
        ActivityCompat.requestPermissions(target, PERMISSION_SAVEIMAGE, REQUEST_SAVEIMAGE);
      }
    }
  }

  static void onRequestPermissionsResult(FullScreenImageGalleryActivity target, int requestCode, int[] grantResults) {
    switch (requestCode) {
      case REQUEST_SAVEIMAGE:
      if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_SAVEIMAGE)) {
        target.onCameraDenied();
        return;
      }
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.saveImage();
      } else {
        if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SAVEIMAGE)) {
          target.onCameraNeverAskAgain();
        } else {
          target.onCameraDenied();
        }
      }
      break;
      default:
      break;
    }
  }

  private static final class SaveImagePermissionRequest implements PermissionRequest {
    private final WeakReference<FullScreenImageGalleryActivity> weakTarget;

    private SaveImagePermissionRequest(FullScreenImageGalleryActivity target) {
      this.weakTarget = new WeakReference<>(target);
    }

    @Override
    public void proceed() {
      FullScreenImageGalleryActivity target = weakTarget.get();
      if (target == null) return;
      ActivityCompat.requestPermissions(target, PERMISSION_SAVEIMAGE, REQUEST_SAVEIMAGE);
    }

    @Override
    public void cancel() {
      FullScreenImageGalleryActivity target = weakTarget.get();
      if (target == null) return;
      target.onCameraDenied();
    }
  }
}
