package com.example.demo_java;

import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.example.demo_java.scopes.MyScope;
import com.example.demo_java.scopes.TestScope;
import com.skydoves.methodscope.Scoped;

import androidx.appcompat.app.AppCompatActivity;

@MyScope
@TestScope(deeplink = @DeepLink("https://github.com/skydoves"))
abstract class MainActivity extends AppCompatActivity {

    private String hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(); // calls the base method. this will calls the scoped method in the scoped class.
    }

    /** the base method for the scoping. */
    public void init() {
        hello = "hello, ";
    }

    /** scoped method for the scoping [MyScope] by [init]. */
    @Scoped(MyScope.class)
    public void initMyScope() {
        // setContentView for MyScope
        setContentView(R.layout.activity_main_myscope);

        hello += "MyScope";
        TextView textView = findViewById(R.id.text_message_myscope);
        textView.setText(hello);
    }

    /** scoped method for the scoping [TestScope] by [init]. */
    @Scoped(TestScope.class)
    public void initTestScope() {
        // setContentView for TestScope
        setContentView(R.layout.activity_main_testscope);

        hello += "TestScope";
        TextView textView = findViewById(R.id.text_message_text_message_testscope);
        textView.setText(hello);
    }
}
