package com.skydoves.methodscopedemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLink
import com.skydoves.methodscopedemo.scopes.TestScope

@TestScope(deeplink = DeepLink("https://www.naver.com"))
open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    fun initialize() {

    }

    fun TestScope_initialize() {

    }

    companion object {
        @TestAnnotation
        private val qdqwdwd: String? = null
    }
}
