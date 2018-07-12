package demo.prim.com.moudle1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.prim.router.primrouter_annotation.Router;
import com.primrouter_core.PrimRouter;

@Router(path = "/module1/test")
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button button = findViewById(R.id.button);
        TextView textview = findViewById(R.id.textview);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        textview.setText("我是module1 我的路由地址是：module1/test. 我是被地址：" + path + "调起来的");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrimRouter.getInstance().jump("/module2/test").withString("path", "/module1/test").navigation(Main2Activity.this);
            }
        });
    }
}
