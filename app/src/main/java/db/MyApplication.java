package db;

import android.app.Application;

/**
 * Created by Administrator on 2016/9/21.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.init(this);
    }
}
