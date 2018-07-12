package demo.prim.com.moudle1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.TestService;
import com.prim.router.primrouter_annotation.Router;
import com.primrouter_core.core.PrimRouter;

@Router(path = "/module1/test")
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button button = findViewById(R.id.button);
        Button module1_button = findViewById(R.id.module1_button);
        TextView textview = findViewById(R.id.textview);
        Intent intent = getIntent();
        final String path = intent.getStringExtra("path");
        textview.setText("我是module1 我的路由地址是：module1/test. 我是被地址：" + path + "调起来的");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrimRouter.getInstance().jump("/module2/test").withString("path", "/module1/test").navigation(Main2Activity.this);
            }
        });

        module1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrimRouter.getInstance().jump("/app/main").navigation(Main2Activity.this);
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

    private void appToService2() {
        TestService testService = (TestService) PrimRouter.getInstance().jump("/module2/service").navigation();
        testService.test(this, "module1");
    }

    private void appToService1() {
        TestService testService = (TestService) PrimRouter.getInstance().jump("/app/service").navigation();
        testService.test(this, "module1");
    }
}
