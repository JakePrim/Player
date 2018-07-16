package com.primrouter_core.core;

import android.app.Activity;
import android.util.LruCache;

import com.prim.router.primrouter_annotation.Extra;
import com.primrouter_core.interfaces.IExtra;

import java.lang.reflect.InvocationTargetException;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2018/7/15 - 下午10:03
 */
public class ExtraLoader {
    public static ExtraLoader extraLoader;

    public static ExtraLoader getInstance() {
        if (extraLoader == null) {
            synchronized (ExtraLoader.class) {
                if (extraLoader == null) {
                    extraLoader = new ExtraLoader();
                }
            }
        }
        return extraLoader;
    }

    private LruCache<String, IExtra> extraLruCache;

    public ExtraLoader() {
        extraLruCache = new LruCache<>(88);
    }


    public void loadExtra(Activity activity) {
        String name = activity.getClass().getName();
        IExtra iExtra = extraLruCache.get(name);
        try {
            if (iExtra == null) {
                iExtra = (IExtra) Class.forName(activity.getClass().getName() + "$$Extra").getConstructor().newInstance();
            }
            iExtra.loadExtra(activity);
            extraLruCache.put(name, iExtra);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
