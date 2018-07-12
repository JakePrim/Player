package com.prim.component.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.base.TestService;
import com.prim.router.primrouter_annotation.Router;
import com.primrouter_core.core.PrimRouter;


@Router(path = "/app/main")
public class MainActivity extends AppCompatActivity {

    private Button appToModule, appToService1, appToService2, appToModule2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appToModule = findViewById(R.id.appToModule);
        appToService1 = findViewById(R.id.appToService1);
        appToService2 = findViewById(R.id.appToService2);
        appToModule2 = findViewById(R.id.appToModule2);
        appToModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                module1();
            }
        });

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

        appToModule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appToModule2();
            }
        });
    }

    private void appToModule2() {
        PrimRouter.getInstance().jump("/module2/test").withString("path", "/app/main").navigation(this);
    }

    private void appToService2() {
        TestService testService = (TestService) PrimRouter.getInstance().jump("/module2/service").navigation();
        testService.test(this, "app");
    }

    private void appToService1() {
        TestService testService = (TestService) PrimRouter.getInstance().jump("/module1/service").navigation();
        testService.test(this, "app");
    }

    public void module1() {
        PrimRouter.getInstance().jump("/module1/test").withString("path", "/app/main").navigation(this);
    }
}
