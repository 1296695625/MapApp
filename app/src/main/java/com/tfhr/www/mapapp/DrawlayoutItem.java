package com.tfhr.www.mapapp;

import android.os.Parcel;
import android.os.Parcelable;

public class DrawlayoutItem implements Parcelable{
    protected DrawlayoutItem(Parcel in) {
        name=in.readString();
        isCheck=in.readInt();
    }

    public static final Creator<DrawlayoutItem> CREATOR = new Creator<DrawlayoutItem>() {
        @Override
        public DrawlayoutItem createFromParcel(Parcel in) {
            return new DrawlayoutItem(in);
        }

        @Override
        public DrawlayoutItem[] newArray(int size) {
            return new DrawlayoutItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(isCheck);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public DrawlayoutItem(String name, int isCheck) {
        this.name = name;
        this.isCheck = isCheck;
    }

    private String name;
    private int isCheck;//0 false 1 true
}
