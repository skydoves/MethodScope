/*
 * Copyright (C) 2017 skydoves
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

package com.skydoves.processor;

import com.google.common.base.VerifyException;
import com.skydoves.methodscope.MethodScope;
import com.skydoves.methodscope.ScopeAnnotation;
import com.skydoves.methodscope.ScopeInitializer;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class MethodScopeAnnotatedClass {

    public final TypeElement annotatedElement;
    public final String packageName;
    public final String clazzName;
    public final List<String> scopeList;
    public final List<AnnotationMirror> scopeAnnotationList;
    private boolean checkScopeInitializer = false;

    public MethodScopeAnnotatedClass(TypeElement annotatedElement, Elements elementUtils) throws VerifyException {
        MethodScope methodScope = annotatedElement.getAnnotation(MethodScope.class);
        PackageElement packageElement = elementUtils.getPackageOf(annotatedElement);
        this.packageName = packageElement.isUnnamed() ? null : packageElement.getQualifiedName().toString();
        this.annotatedElement = annotatedElement;
        this.clazzName = annotatedElement.getSimpleName().toString();
        this.scopeList = new ArrayList<>();
        this.scopeAnnotationList = new ArrayList<>();

        Arrays.stream(methodScope.scopes()).forEach(scope -> {
            if(!scopeList.contains(scope)) {
                scopeList.add(StringUtils.toUpperCamel(scope));
            } else
                throw new VerifyException(String.format("scope %s is already exist.", scope));
        });

        this.annotatedElement.getInterfaces().stream()
                .filter(impl -> TypeName.get(impl).equals(TypeName.get(ScopeInitializer.class)))
                .forEach(impl -> checkScopeInitializer = true);

        checkScopeInitializerImplemented();

        annotatedElement.getAnnotationMirrors().forEach(annotationMirror -> {
            Element element = annotationMirror.getAnnotationType().asElement();
            element.getAnnotationMirrors().forEach(annotation -> {
                if(annotation.toString().equals("@" + ScopeAnnotation.class.getName())) {
                    annotationMirror.getElementValues().forEach((method, value) ->
                            this.scopeAnnotationList.add(annotationMirror));
                }
            });
        });
    }

    private void checkScopeInitializerImplemented() {
        if(!checkScopeInitializer) {
            throw new VerifyException("MethodScope class must implements ScopeInitializer interface.");
        }
    }
}
