package com.hyy.forfun.aidl_client.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hyy.forfun.aidl_client.R;
import com.hyy.forfun.aidl_demo.IBookManager;
import com.hyy.forfun.aidl_demo.bean.Book;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {

    private static final String TAG = BookManagerActivity.class.getSimpleName();

    Button btnGetBookList, btnAddBook;

    private IBookManager mBookManager;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            mBookManager = IBookManager.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
            mBookManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        btnGetBookList = findViewById(R.id.btnGetBookList);
        btnAddBook = findViewById(R.id.btnAddBook);

        Intent service = new Intent("com.hyy.forfun.aidl_demo.BookManagerService");
        service.setPackage("com.hyy.forfun.aidl_demo");
        bindService(service, mConnection, BIND_AUTO_CREATE);

        btnGetBookList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mBookManager) {
                    try {
                        List<Book> bookList = mBookManager.getBookList();
                        Log.i(TAG, "$$query book list begin:");
                        bookList.forEach(book -> {
                            Log.i(TAG, book.toString());
                        });
                        Log.i(TAG, "$$query book list end");

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mBookManager) {
                    try {
                        Book book3 = new Book(3,"艺术探索");
                        mBookManager.addBook(book3);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);

    }
}