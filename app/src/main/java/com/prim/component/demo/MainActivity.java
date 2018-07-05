package com.prim.component.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prim.router.primrouter_annotation.Router;

@Router(path = "/app/main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
