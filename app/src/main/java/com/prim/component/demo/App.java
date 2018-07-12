package com.prim.component.demo;

import android.app.Application;

import com.primrouter_core.core.PrimRouter;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2018/7/10 - 下午11:46
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PrimRouter.getInstance().initRouter(this);
    }
}
