//package com.hyy.forfun.aidl_demo.client;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.ComponentName;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.RemoteException;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import com.hyy.forfun.aidl_demo.BookManagerService;
//import com.hyy.forfun.aidl_demo.IBookManager;
//import com.hyy.forfun.aidl_demo.R;
//import com.hyy.forfun.aidl_demo.bean.Book;
//
//import java.util.List;
//
//public class BookManagerActivity extends AppCompatActivity {
//
//    private static final String TAG = BookManagerActivity.class.getSimpleName();
//
//    Button btnGetBookList, btnAddBook;
//
//    private IBookManager mBookManager;
//
//    private ServiceConnection mConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mBookManager = IBookManager.Stub.asInterface(service);
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mBookManager = null;
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_manager);
//        btnGetBookList = findViewById(R.id.btnGetBookList);
//        btnAddBook = findViewById(R.id.btnAddBook);
//
//        Intent intent = new Intent(this, BookManagerService.class);
//        bindService(intent, mConnection, BIND_AUTO_CREATE);
//
//        btnGetBookList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(null != mBookManager){
//                    try {
//                        List<Book> bookList = mBookManager.getBookList();
//                        Log.i(TAG,"$$query book list begin:");
//                        bookList.forEach(book -> {
//                            Log.i(TAG,book.toString());
//                        });
//                        Log.i(TAG,"$$query book list end");
//
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unbindService(mConnection);
//
//    }
//}