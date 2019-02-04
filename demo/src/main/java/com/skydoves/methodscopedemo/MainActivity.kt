package com.skydoves.methodscopedemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLink
import com.skydoves.methodscopedemo.scopes.MyScope
import com.skydoves.methodscopedemo.scopes.TestScope

@MyScope
@TestScope(deeplink = DeepLink("https://www.naver.com"))
abstract class MainActivity : AppCompatActivity() {

    var hello = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    open fun initialize() {
        hello = "This is "
    }

    fun initializeTestScope() {
        hello += "TestScope"
        Toast.makeText(this, hello, Toast.LENGTH_LONG).show()
    }

    fun initializeMyScope() {
        hello += "MyScope"
        Toast.makeText(this, hello, Toast.LENGTH_LONG).show()
    }

    companion object {
        @TestAnnotation
        private val qdqwdwd: String? = null
    }
}
