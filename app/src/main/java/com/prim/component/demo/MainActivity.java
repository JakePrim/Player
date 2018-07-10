package com.prim.component.demo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.prim.router.generated.PrimRouter$$Group$$module;
import com.prim.router.generated.PrimRouter$$Root$$moudle1;
import com.prim.router.primrouter_annotation.Router;
import com.prim.router.primrouter_annotation.RouterMeta;
import com.primrouter_core.PrimRouter;
import com.primrouter_core.interfaces.IRouteGroup;

import java.util.HashMap;
import java.util.Map;

@Router(path = "/app/main")
public class MainActivity extends AppCompatActivity {

    private Button appToModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appToModule = findViewById(R.id.appToModule);
        appToModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                module1();
            }
        });
    }

    public void module1() {
        PrimRouter.getInstance().jump("/module/test2").navigation(this);
    }
}
