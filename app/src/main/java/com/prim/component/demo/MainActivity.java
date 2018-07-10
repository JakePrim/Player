package com.prim.component.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.prim.router.primrouter_annotation.Router;
import com.primrouter_core.PrimRouter;


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
