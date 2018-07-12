package demo.prim.com.moudle1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.base.TestService;
import com.prim.router.primrouter_annotation.Router;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2018/7/12 - 下午6:26
 */
@Router(path = "/module1/service")
public class Module1Service implements TestService {
    private static final String TAG = "Module1Service";

    @Override
    public void test(Context context, String s) {
        Toast.makeText(context, "我是Module1，我是被：" + s + "模块调用的，模块测试通信成功", Toast.LENGTH_SHORT).show();
    }
}
