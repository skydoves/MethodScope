package com.skydoves.methodscopedemo;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.skydoves.methodscope.ScopeAnnotation;

/**
 * Created by skydoves on 2017. 12. 15.
 * Copyright (c) 2017 battleent rights reserved.
 */

@ScopeAnnotation
public @interface DeepLinkScopeAnnotation {
    Class[] scopes();
    DeepLink[] values();
}
