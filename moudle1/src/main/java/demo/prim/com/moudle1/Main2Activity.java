package demo.prim.com.moudle1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prim.router.primrouter_annotation.Router;

@Router(path = "/module/test2")
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
