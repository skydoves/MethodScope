package com.example.demo_java.scopes;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.skydoves.methodscope.MethodScope;

@MethodScope
public @interface TestScope {
    DeepLink deeplink();
}
