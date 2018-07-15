package com.skydoves.methodscopedemo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Developed by skydoves on 2018-07-15.
 * Copyright (c) 2018 skydoves rights reserved.
 */

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface TestAnnotation {
}
