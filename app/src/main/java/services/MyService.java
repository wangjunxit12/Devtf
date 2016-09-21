package services;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.annotation.Nullable;

import com.example.devtf.devtf.IMyAidlInterface;
import com.example.devtf.devtf.Message;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyService extends Service{
    IMyAidlInterface.Stub mBinder=new IMyAidlInterface.Stub() {
        @Override
        public Message sayhello() throws RemoteException {
            return new Message("msg from service at thread"+Thread.currentThread().toString()
            +"\n"+"tid is"+Thread.currentThread().getId()
                    +"main id is"+getMainLooper().getThread().getId(), android.os.Process.myPid());
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
