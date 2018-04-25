package com.etiennelawlor.imagegallery.library.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by donglua on 15/6/30.
 */
public class Photo implements Parcelable {

    private int id;

    private String path;

    private boolean selected;

    private String uploadSuccessFileName;

    private boolean uploadSuccess;

    private String thumbnailsPath;

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
    }
    public Photo(int id, String path,String thumbnailsPath) {
        this.id = id;
        this.path = path;
        this.thumbnailsPath = thumbnailsPath;
    }

    public Photo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        return id == photo.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getUploadSuccessFileName() {
        return uploadSuccessFileName;
    }

    public void setUploadSuccessFileName(String uploadSuccessFileName) {
        this.uploadSuccessFileName = uploadSuccessFileName;
    }

    public boolean isUploadSuccess() {
        return uploadSuccess;
    }

    public void setUploadSuccess(boolean uploadSuccess) {
        this.uploadSuccess = uploadSuccess;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.path);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
        dest.writeString(this.uploadSuccessFileName);
        dest.writeByte(uploadSuccess ? (byte) 1 : (byte) 0);
        dest.writeString(this.thumbnailsPath);
    }

    protected Photo(Parcel in) {
        this.id = in.readInt();
        this.path = in.readString();
        this.selected = in.readByte() != 0;
        this.uploadSuccessFileName = in.readString();
        this.uploadSuccess = in.readByte() != 0;
        this.thumbnailsPath = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
