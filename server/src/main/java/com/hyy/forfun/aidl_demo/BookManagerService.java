package com.hyy.forfun.aidl_demo;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.hyy.forfun.aidl_demo.bean.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

//    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList =
//            new CopyOnWriteArrayList<>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList =
            new RemoteCallbackList<>();

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (!mListenerList.contains(listener)) {
//                mListenerList.add(listener);
//            } else {
//                Log.e(TAG, "already exists");
//            }
//            Log.e(TAG, "registerListener, size:" + mListenerList.size());

            mListenerList.register(listener);
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (mListenerList.contains(listener)) {
//                mListenerList.remove(listener);
//                Log.e(TAG, "unregister listener succeed ");
//
//            } else {
//                Log.e(TAG, "not found, can not unregister");
//            }
//            Log.e(TAG, "unRegisterListener, size:" + mListenerList.size());

            mListenerList.unregister(listener);
        }


        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            //权限验证
            int check = checkCallingPermission("com.hyy.forfun.aidl.permission.TEST");
            if (check == PackageManager.PERMISSION_DENIED) {
                Log.e(TAG, "onTransact AIDL permission denied!");
                return false;
            }

            //包名验证
            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            assert packageName != null;
            if (!packageName.startsWith("com.hyy.forfun.aidl_client")) {
                Log.e(TAG, "onTransact AIDL package denied!");
                return false;
            }


            Log.e(TAG, "onTransact AIDL permission passed!");
            return super.onTransact(code, data, reply, flags);
        }
    };


    public BookManagerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "ios"));
        mBookList.add(new Book(2, "Android"));

        new Thread(new ServiceWorker()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");

////        int check = checkCallingPermission("com.flyscale.permission.TEST1");
//        int check = checkCallingPermission("com.flyscale.permission.TEST");
//        if (check == PackageManager.PERMISSION_DENIED){
//            Log.e(TAG, "AIDL permission denied!");
//            return null;
//        }
//
//        Log.e(TAG, "AIDL permission passed!");

        //PS：测试的时候总是权限验证失败，还没找到问题原因

        return mBinder;
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
//        Log.e(TAG, "onNewBookArrived, notify listeners:" + mListenerList.size());
//        for (IOnNewBookArrivedListener listener : mListenerList) {
//            Log.e(TAG,"onNewBookArrived, notify listener:"+listener);
//            listener.onNewBookArrived(book);
//        }

        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener l = mListenerList.getBroadcastItem(i);
            if(l != null){
                l.onNewBookArrived(book);
            }
        }
        mListenerList.finishBroadcast();

    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            //doing background process
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);

                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}