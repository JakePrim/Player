package demo.prim.com.moudle1;

import android.content.Intent;


/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2018/7/15 - 下午8:18
 */
public class TestExtra {

//    @Extra()
    public String test;

    public static void loadExtra(Main2Activity main2Activity) {
        Intent intent = main2Activity.getIntent();

        main2Activity.path = intent.getStringExtra("path");

    }
}
