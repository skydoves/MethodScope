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

@MyScope
@TestScope(deeplink = DeepLink("https://www.naver.com"))
abstract class MainActivity : AppCompatActivity() {

    @Scoped(TestScope::class) private val tt = true

    var hello = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize(1, "123")
    }

    open fun initialize(aa: Int, bb: String): String {
        hello = "This is "
        return hello
    }

    @Scoped(TestScope::class)
    fun initializeTestScope333(aa: Int, bb: String): String {
        hello += "TestScope"
        Toast.makeText(this, hello, Toast.LENGTH_LONG).show()
        return hello
    }

    fun initializeMyScope(aa: Int, bb: String): String {
        hello += "MyScope"
        Toast.makeText(this, hello, Toast.LENGTH_LONG).show()
        return hello
    }

    abstract fun test()

    @Scoped(TestScope::class)
    fun testTestScopeqwdqwd() {
        Toast.makeText(this, hello, Toast.LENGTH_LONG).show()
    }

    fun testMyScope() {
    }

    companion object {
        @TestAnnotation
        private val qdqwdwd: String? = null
    }
}
