package com.skydoves.methodscopedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.skydoves.methodscope.MethodScope;
import com.skydoves.methodscope.ScopeInitializer;

@MethodScope(scopes = {"Test", "Power"})
public class MainActivity extends AppCompatActivity implements ScopeInitializer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeScopes();
        
    }

    @Override
    public void initializeScopes() {

    }
}
