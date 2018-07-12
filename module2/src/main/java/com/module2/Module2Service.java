package com.module2;

import android.content.Context;
import android.widget.Toast;

import com.base.TestService;
import com.prim.router.primrouter_annotation.Router;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2018/7/12 - 下午6:40
 */
@Router(path = "/module2/service")
public class Module2Service implements TestService {
    @Override
    public void test(Context context, String s) {
        Toast.makeText(context, "我是Module2，我是被：" + s + "模块调用的，模块测试通信成功", Toast.LENGTH_SHORT).show();

    }
}
