package com.hyy.forfun.aidl_demo;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.hyy.forfun.aidl_demo.bean.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList =
            new CopyOnWriteArrayList<>();

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
            if(!mListenerList.contains(listener)){
                mListenerList.add(listener);
            }else{
                Log.e(TAG,"already exists");
            }
            Log.e(TAG,"registerListener, size:"+mListenerList.size());
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if(mListenerList.contains(listener)){
                mListenerList.remove(listener);
                Log.e(TAG,"unregister listener succeed ");

            }else{
                Log.e(TAG,"not found, can not unregister");
            }
            Log.e(TAG,"unRegisterListener, size:"+mListenerList.size());
        }


        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            //权限验证
            int check = checkCallingPermission("com.hyy.forfun.aidl.permission.TEST");
            if(check == PackageManager.PERMISSION_DENIED){
                Log.e(TAG, "onTransact AIDL permission denied!");
                return false;
            }

            //包名验证
            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if(packages != null && packages.length > 0){
                packageName = packages[0];
            }
            assert packageName != null;
            if(!packageName.startsWith("com.hyy.forfun.aidl_client")){
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
        mBookList.add(new Book(1,"ios"));
        mBookList.add(new Book(2,"Android"));
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

    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            //doing background process
            while(!mIsServiceDestoryed.get()){

            }
        }
    }

}