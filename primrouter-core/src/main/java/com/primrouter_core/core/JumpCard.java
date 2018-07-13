package com.primrouter_core.core;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;

import com.prim.router.primrouter_annotation.RouterMeta;
import com.primrouter_core.interfaces.IService;

import java.util.ArrayList;

/**
 * @author prim
 * @version 1.0.0
 * @desc 路由跳转的跳卡，跳卡根据路由地址来跳转,跳卡还可以设置跳转动画
 * 传递参数等。
 * @time 2018/7/10 - 下午11:05
 */
public class JumpCard extends RouterMeta {

    private Bundle mBundle;
    private int flags = -1;
    /**
     * 动画
     */
    //新版 md风格
    private Bundle optionsCompat;
    //老版
    private int enterAnim;
    private int exitAnim;

    private IService service;

    public JumpCard(String path, String group) {
        this(path, group, null);
    }

    public JumpCard(String path, String group, Bundle bundle) {
        setPath(path);
        setGroup(group);
        this.mBundle = bundle == null ? new Bundle() : bundle;
    }

    public IService getService() {
        return service;
    }

    public void setService(IService service) {
        this.service = service;
    }

    public Bundle getExtras() {
        return mBundle;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }

    public Bundle getOptionsBundle() {
        return optionsCompat;
    }

    /**
     * Intent.FLAG_ACTIVITY**
     *
     * @param flag
     * @return
     */
    public JumpCard withFlags(int flag) {
        this.flags = flag;
        return this;
    }


    public int getFlags() {
        return flags;
    }

    /**
     * 跳转动画
     *
     * @param enterAnim
     * @param exitAnim
     * @return
     */
    public JumpCard withTransition(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    /**
     * 转场动画
     *
     * @param compat
     * @return
     */
    public JumpCard withOptionsCompat(ActivityOptionsCompat compat) {
        if (null != compat) {
            this.optionsCompat = compat.toBundle();
        }
        return this;
    }

    public JumpCard withString(@Nullable String key, @Nullable String value) {
        mBundle.putString(key, value);
        return this;
    }


    public JumpCard withBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }


    public JumpCard withShort(@Nullable String key, short value) {
        mBundle.putShort(key, value);
        return this;
    }


    public JumpCard withInt(@Nullable String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }


    public JumpCard withLong(@Nullable String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }


    public JumpCard withDouble(@Nullable String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }


    public JumpCard withByte(@Nullable String key, byte value) {
        mBundle.putByte(key, value);
        return this;
    }


    public JumpCard withChar(@Nullable String key, char value) {
        mBundle.putChar(key, value);
        return this;
    }


    public JumpCard withFloat(@Nullable String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }


    public JumpCard withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }


    public JumpCard withStringArray(@Nullable String key, @Nullable String[] value) {
        mBundle.putStringArray(key, value);
        return this;
    }


    public JumpCard withBooleanArray(@Nullable String key, boolean[] value) {
        mBundle.putBooleanArray(key, value);
        return this;
    }


    public JumpCard withShortArray(@Nullable String key, short[] value) {
        mBundle.putShortArray(key, value);
        return this;
    }


    public JumpCard withIntArray(@Nullable String key, int[] value) {
        mBundle.putIntArray(key, value);
        return this;
    }


    public JumpCard withLongArray(@Nullable String key, long[] value) {
        mBundle.putLongArray(key, value);
        return this;
    }


    public JumpCard withDoubleArray(@Nullable String key, double[] value) {
        mBundle.putDoubleArray(key, value);
        return this;
    }


    public JumpCard withByteArray(@Nullable String key, byte[] value) {
        mBundle.putByteArray(key, value);
        return this;
    }


    public JumpCard withCharArray(@Nullable String key, char[] value) {
        mBundle.putCharArray(key, value);
        return this;
    }


    public JumpCard withFloatArray(@Nullable String key, float[] value) {
        mBundle.putFloatArray(key, value);
        return this;
    }


    public JumpCard withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        mBundle.putParcelableArray(key, value);
        return this;
    }

    public JumpCard withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends
            Parcelable> value) {
        mBundle.putParcelableArrayList(key, value);
        return this;
    }

    public JumpCard withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public JumpCard withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return this;
    }

    public Object navigation() {
        return PrimRouter.getInstance().navigation(null, this, -1, null);
    }

    public Object navigation(Context context) {
        return PrimRouter.getInstance().navigation(context, this, -1, null);
    }

    public Object navigation(Context context, int requestCode) {
        return PrimRouter.getInstance().navigation(context, this, requestCode, null);
    }
}
