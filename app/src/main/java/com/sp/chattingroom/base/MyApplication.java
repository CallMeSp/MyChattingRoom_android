package com.sp.chattingroom.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.antfortune.freeline.FreelineCore;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by my on 2016/12/22.
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    @Override
    public void onLowMemory() {
        LogUtil.log(TAG,"lowmemory");
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        LogUtil.log(TAG,"terminate");
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        LogUtil.log(TAG,"trimmemery");
        super.onTrimMemory(level);
    }
    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication ) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        LogUtil.log(TAG,"oncreate");
        super.onCreate();



        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtil.log(TAG,activity.getLocalClassName()+"  create");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtil.log(TAG,activity.getLocalClassName()+" start");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtil.log(TAG,activity.getLocalClassName()+" resume");
            }
            @Override
            public void onActivityPaused(Activity activity) {
                LogUtil.log(TAG,activity.getLocalClassName()+" paused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtil.log(TAG,activity.getLocalClassName()+" stopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtil.log(TAG,activity.getLocalClassName()+" saveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtil.log(TAG,activity.getLocalClassName()+" Destroy");
            }
        });
        FreelineCore.init(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher=LeakCanary.install(this);
    }
}
