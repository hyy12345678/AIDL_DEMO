// IOnNewBookArrivedListener.aidl
package com.hyy.forfun.aidl_demo;

// Declare any non-default types here with import statements
import com.hyy.forfun.aidl_demo.bean.Book;


interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}