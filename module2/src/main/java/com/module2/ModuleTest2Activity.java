package com.module2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prim.router.primrouter_annotation.Router;

@Router(path = "/main/test/2")
public class ModuleTest2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_test2);
    }
}
