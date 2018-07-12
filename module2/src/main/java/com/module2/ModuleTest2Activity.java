package com.module2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.prim.router.primrouter_annotation.Router;

@Router(path = "/module2/test")//注意不同的module 不能使用同一个 group 作为分组名 否则会生成相同的类导致报错
public class ModuleTest2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_test2);
        TextView module2_textView = findViewById(R.id.module2_textView);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        module2_textView.setText("我是module2，我的地址是：/module2/test. 我是被地址：" + path + " 调起的");

    }
}
