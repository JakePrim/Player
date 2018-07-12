package com.base;

import android.content.Context;

import com.primrouter_core.interfaces.IService;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2018/7/12 - 下午6:26
 */
public interface TestService extends IService {
    void test(Context context,String s);

}
