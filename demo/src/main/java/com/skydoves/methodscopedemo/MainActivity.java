package com.skydoves.methodscopedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.skydoves.methodscope.MethodScope;
import com.skydoves.methodscope.ScopeInitializer;

@MethodScope(scopes = {"Test", "Power"})
public class MainActivity extends AppCompatActivity implements ScopeInitializer {

    private String text = "hello, methodScope";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeScopes();
    }

    @Override
    public void initializeScopes() {
    }

    @InitTestScope
    public void initTestScope_Impl() {
        setContentView(R.layout.activity_test);
        Button button = findViewById(R.id.button);
        button.setText(text);
    }

    @InitPowerScope
    public void initPowerScope_Impl() {
        setContentView(R.layout.activity_power);
        TextView textView = findViewById(R.id.textView);
        textView.setText(text);
    }

    @TestScope
    public void realTest() {

    }
}
