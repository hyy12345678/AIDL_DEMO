// IBookManager.aidl
package com.hyy.forfun.aidl_demo;

// Declare any non-default types here with import statements
import com.hyy.forfun.aidl_demo.bean.Book;
import com.hyy.forfun.aidl_demo.IOnNewBookArrivedListener;


interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unRegisterListener(IOnNewBookArrivedListener listener);

}