package demo.prim.com.moudle1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.prim.router.primrouter_annotation.Router;
import com.primrouter_core.PrimRouter;

@Router(path = "/module/test2")
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrimRouter.getInstance().jump("/main/test/2").navigation(Main2Activity.this);
            }
        });
    }
}
