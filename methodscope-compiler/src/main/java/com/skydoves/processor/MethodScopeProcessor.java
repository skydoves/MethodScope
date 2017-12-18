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

import com.google.auto.service.AutoService;
import com.google.common.base.VerifyException;
import com.skydoves.methodscope.MethodScope;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import static javax.tools.Diagnostic.Kind.ERROR;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.skydoves.methodscope.MethodScope",
        "com.skydoves.methodscope.ScopeAnnotation"})
@AutoService(Processor.class)
public class MethodScopeProcessor extends AbstractProcessor {

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(annotations.isEmpty()) {
            return true;
        }

        roundEnv.getElementsAnnotatedWith(MethodScope.class).stream()
                .map(annotatedType -> (TypeElement) annotatedType)
                .forEach(annotatedType -> {
                    try {
                        checkValidScopeAnnotatedClass(annotatedType);
                        processMethodScope(annotatedType);
                    } catch (IllegalAccessException e) {
                        showErrorLog(e.getMessage(), annotatedType);
                    }
                });

        return true;
    }

    private void processMethodScope(TypeElement annotatedType) {
        try {
            MethodScopeAnnotatedClass annotatedClazz = new MethodScopeAnnotatedClass(annotatedType, processingEnv.getElementUtils());
            annotatedClazz.scopeList.forEach(scope -> {
                PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(annotatedClazz.annotatedElement);
                String packageName = packageElement.isUnnamed() ? null : packageElement.getQualifiedName().toString();
                generateProcessInitializeScopeAnnotation(annotatedClazz, packageName, scope);
                generateProcessScopeAnnotation(annotatedClazz, packageName, scope);
                generateProcessMethodScope(annotatedClazz, packageName, scope);
            });
        } catch (VerifyException e) {
            showErrorLog(e.getMessage(), annotatedType);
        }
    }

    protected void generateProcessInitializeScopeAnnotation(MethodScopeAnnotatedClass annotatedClazz, String packageName, String scope) {
        try {
            InitializeScopeAnnotationGenerator annotationsGenerator = new InitializeScopeAnnotationGenerator(annotatedClazz, packageName, scope);
            TypeSpec scopeAnnotation = annotationsGenerator.generate();
            JavaFile.builder(packageName, scopeAnnotation).build().writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            // ignore ;)
        }
    }

    private void generateProcessScopeAnnotation(MethodScopeAnnotatedClass annotatedClazz, String packageName, String scope) {
        try {
            ScopeAnnotationGenerator annotationsGenerator = new ScopeAnnotationGenerator(annotatedClazz, packageName, scope);
            TypeSpec scopeAnnotation = annotationsGenerator.generate();
            JavaFile.builder(packageName, scopeAnnotation).build().writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            // ignore :)
        }
    }

    private void generateProcessMethodScope(MethodScopeAnnotatedClass annotatedClazz, String packageName, String scope) {
        try {
            ScopeClassGenerator scopeClassGenerator = new ScopeClassGenerator(annotatedClazz, packageName, scope);
            TypeSpec scopeClazz = scopeClassGenerator.generate();
            JavaFile.builder(packageName, scopeClazz).build().writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            // ignore >.<
        }
    }

    private void checkValidScopeAnnotatedClass(TypeElement annotatedType) throws IllegalAccessException {
        if(!annotatedType.getKind().isClass()) {
            throw new IllegalAccessException("Only classes can be annotated with @KickbackBox");
        } else if(annotatedType.getModifiers().contains(Modifier.FINAL)) {
            throw new IllegalAccessException("class modifier can not be final");
        } else if(annotatedType.getModifiers().contains(Modifier.PRIVATE)) {
            throw new IllegalAccessException("class modifier can not be private");
        } else if(annotatedType.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new IllegalAccessException("class modifier can not be abstract");
        }
    }

    private void showErrorLog(String message, Element element) {
        messager.printMessage(ERROR, StringUtils.getErrorMessagePrefix() + message, element);
    }
}
