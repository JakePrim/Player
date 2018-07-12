package com.prim.component.demo;

import android.content.Context;
import android.widget.Toast;

import com.base.TestService;
import com.prim.router.primrouter_annotation.Router;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2018/7/12 - 下午6:30
 */
@Router(path = "/app/service")
public class AppService implements TestService {
    @Override
    public void test(Context context, String s) {
        Toast.makeText(context, "我是app，我是被：" + s + "模块调用的，模块间通信测试成功", Toast.LENGTH_SHORT).show();
    }
}
