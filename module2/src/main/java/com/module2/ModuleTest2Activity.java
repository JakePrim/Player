package com.module2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.TestService;
import com.prim.router.primrouter_annotation.Extra;
import com.prim.router.primrouter_annotation.Router;
import com.primrouter_core.core.PrimRouter;

@Router(path = "/module2/test")//注意不同的module 不能使用同一个 group 作为分组名 否则会生成相同的类导致报错
public class ModuleTest2Activity extends AppCompatActivity {

    @Extra(name = "path")
    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_test2);
        PrimRouter.getInstance().inject(this);
        TextView module2_textView = findViewById(R.id.module2_textView);
        Button module2_button = findViewById(R.id.module2_button);
        Button app_button = findViewById(R.id.app_button);
//        Intent intent = getIntent();
//        final String path = intent.getStringExtra("path");
        module2_textView.setText("我是module2，我的地址是：/module2/test. 我是被地址：" + url + " 调起的");
        module2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrimRouter.getInstance().jump("/module1/test").withString("path", "/module2/test").navigation(ModuleTest2Activity.this);
            }
        });

        app_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app_button();
            }
        });

        Button appToService1 = findViewById(R.id.appToService1);
        Button appToService2 = findViewById(R.id.appToService2);

        appToService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appToService1();
            }
        });

        appToService2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appToService2();

            }
        });
    }

    private void app_button() {
        PrimRouter.getInstance().jump("/app/main").navigation(this);
    }

    private void appToService2() {
        TestService testService = (TestService) PrimRouter.getInstance().jump("/module1/service").navigation();
        testService.test(this, "module2");
    }

    private void appToService1() {
        TestService testService = (TestService) PrimRouter.getInstance().jump("/app/service").navigation();
        testService.test(this, "module2");
    }
}
