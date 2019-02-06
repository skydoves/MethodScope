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

package com.skydoves.methodscopedemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLink
import com.skydoves.methodscope.Scoped
import com.skydoves.methodscopedemo.scopes.MyScope
import com.skydoves.methodscopedemo.scopes.TestScope
import kotlinx.android.synthetic.main.activity_main_myscope.*
import kotlinx.android.synthetic.main.activity_main_testscope.*

@MyScope
@TestScope(deeplink = DeepLink("https://github.com/skydoves"))
abstract class MainActivity : AppCompatActivity() {

    @Scoped(TestScope::class) private val flagTestScope = false

    private lateinit var hello: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init() // calls the base method. this will calls the scoped method in the scoped class.
        Toast.makeText(this, initWithArgument("Hello, this is"), Toast.LENGTH_SHORT).show()
    }

    /** the base method for the scoping. */
    open fun init() {
        hello = "hello, "
    }

    /** scoped method for the scoping [MyScope] by [init]. */
    @Scoped(MyScope::class)
    fun initMyScope() {
        // setContentView for MyScope
        setContentView(R.layout.activity_main_myscope)

        hello += "MyScope"
        text_message_myscope.text = hello
    }

    /** scoped method for the scoping [TestScope] by [init]. */
    @Scoped(TestScope::class)
    fun initTestScope() {
        // setContentView for TestScope
        setContentView(R.layout.activity_main_testscope)

        hello += "TestScope"
        text_message_text_message_testscope.text = hello
    }

    /** the base method for the scoping. */
    abstract fun initWithArgument(text: String): String

    /** scoped method for the scoping [MyScope] by [init]. */
    @Scoped(MyScope::class)
    fun initWithArgumentMyScope(text: String): String {
        hello = "$text MyScope2"
        return hello
    }

    /** scoped method for the scoping [TestScope] by [init]. */
    @Scoped(TestScope::class)
    fun initWithArgumentTestScope(text: String): String {
        hello = "$text TestScope2"
        return hello
    }
}
