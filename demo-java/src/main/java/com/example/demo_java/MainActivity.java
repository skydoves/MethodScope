/*
 * Copyright (C) 2019 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demo_java;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.deeplinkdispatch.DeepLink;
import com.example.demo_java.scopes.MyScope;
import com.example.demo_java.scopes.TestScope;
import com.skydoves.methodscope.Scoped;

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
