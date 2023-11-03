package com.hyy.forfun.aidl_demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private int index;
    private String name;

    public Book(int index,String name){
        this.index = index;
        this.name =  name;
    }

    protected Book(Parcel in) {
        index = in.readInt();
        name = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return "Book{" +
                "index=" + index +
                ", name='" + name + '\'' +
                '}';
    }
}
