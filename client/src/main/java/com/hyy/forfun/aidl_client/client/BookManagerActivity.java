package com.hyy.forfun.aidl_client.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hyy.forfun.aidl_client.R;
import com.hyy.forfun.aidl_demo.IBookManager;
import com.hyy.forfun.aidl_demo.IOnNewBookArrivedListener;
import com.hyy.forfun.aidl_demo.bean.Book;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {

    private static final String TAG = BookManagerActivity.class.getSimpleName();
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.e(TAG, "receive new book :" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    Button btnGetBookList, btnAddBook;

    private IBookManager mRemoteBookManager;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            mRemoteBookManager = IBookManager.Stub.asInterface(service);

            try {
                mRemoteBookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
            mRemoteBookManager = null;
        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
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
                if (null != mRemoteBookManager) {
                    try {
                        List<Book> bookList = mRemoteBookManager.getBookList();
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
                if (null != mRemoteBookManager) {
                    try {
                        Book book3 = new Book(3, "艺术探索");
                        mRemoteBookManager.addBook(book3);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        if(mRemoteBookManager !=null &&
            mRemoteBookManager.asBinder().isBinderAlive()){

            try {
                Log.e(TAG,"unregister listener:"+mOnNewBookArrivedListener);
                mRemoteBookManager.unRegisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(mConnection);
        super.onDestroy();
    }
}