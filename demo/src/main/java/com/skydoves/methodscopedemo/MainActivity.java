package com.skydoves.methodscopedemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.skydoves.methodscope.MethodScope;
import com.skydoves.methodscope.ScopeInitializer;

@MethodScope(scopes = { "Test", "Power", "RealForce" })
@DeepLinkScopeAnnotation(
        scopes = { "Test", "Power" },
        values = { @DeepLink( value = "https://www.naver.com"), @DeepLink("https://google.com") })
public class MainActivity extends AppCompatActivity implements ScopeInitializer {

    @TestScope @TestAnnotation private String text;
    @PowerScope private String powerScopeString;
    @TestAnnotation private static String qdqwdwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeScopes();
    }

    @Override
    public void initializeScopes() {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

    }

    @InitTestScope
    public void initTestScope_Impl() {
        setContentView(R.layout.activity_test);
        Button button = findViewById(R.id.button);
        button.setText(text + "\nhere is TestScope");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realTest();
            }
        });
    }

    @InitPowerScope
    public void initPowerScope_Impl() {
        setContentView(R.layout.activity_power);
        TextView textView = findViewById(R.id.textView);
        textView.setText(text + "\nhere is PowerScope");
    }

    @TestScope
    public void realTest() {
        Toast.makeText(this, "realTest invoked!", Toast.LENGTH_SHORT).show();
    }
}
