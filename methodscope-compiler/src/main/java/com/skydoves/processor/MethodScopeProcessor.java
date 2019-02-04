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

package com.skydoves.processor;

import static javax.tools.Diagnostic.Kind.ERROR;

import com.google.auto.service.AutoService;
import com.google.common.base.VerifyException;
import com.skydoves.methodscope.MethodScope;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

@SuppressWarnings("unused")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class MethodScopeProcessor extends AbstractProcessor {

  private HashSet<TypeElement> candidates = new HashSet<>();
  private Messager messager;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.messager = processingEnv.getMessager();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(MethodScope.class.getCanonicalName());
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (annotations.isEmpty()) {
      return true;
    }

    HashSet<TypeElement> methodScopedAnnotations = new HashSet<>();
    roundEnv
        .getElementsAnnotatedWith(MethodScope.class)
        .stream()
        .map(annotatedType -> (TypeElement) annotatedType)
        .forEach(
            annotatedType -> {
              try {
                checkValidScopeAnnotations(annotatedType, methodScopedAnnotations);
              } catch (IllegalArgumentException e) {
                handleExceptions(e.getMessage(), annotatedType);
              }
            });

    methodScopedAnnotations.forEach(
        element ->
            roundEnv
                .getElementsAnnotatedWith(element)
                .stream()
                .map(annotatedType -> (TypeElement) annotatedType)
                .forEach(
                    annotatedType -> {
                      try {
                        checkValidScopeAnnotatedClasses(annotatedType, candidates);
                      } catch (IllegalArgumentException e) {
                        handleExceptions(e.getMessage(), annotatedType);
                      }
                    }));

    candidates.forEach(
        candidate -> {
          try {
            processMethodScope(candidate);
          } catch (IllegalArgumentException e) {
            handleExceptions(e.getMessage(), candidate);
          }
        });

    return true;
  }

  private void processMethodScope(TypeElement annotatedType) {
    try {
      MethodScopeAnnotatedClass annotatedClazz =
          new MethodScopeAnnotatedClass(annotatedType, processingEnv.getElementUtils());
      annotatedClazz.scopeAnnotationList.forEach(
          scopeAnnotation -> {
            PackageElement packageElement =
                processingEnv.getElementUtils().getPackageOf(annotatedClazz.annotatedElement);
            String packageName =
                packageElement.isUnnamed() ? null : packageElement.getQualifiedName().toString();
            generateProcessMethodScope(packageName, annotatedClazz, scopeAnnotation);
          });
    } catch (VerifyException e) {
      handleExceptions(e.getMessage(), annotatedType);
    }
  }

  private void generateProcessMethodScope(
      String packageName,
      MethodScopeAnnotatedClass annotatedClazz,
      AnnotationMirror scopeAnnotation) {
    try {
      ScopeClassGenerator scopeClassGenerator =
          new ScopeClassGenerator(packageName, annotatedClazz, scopeAnnotation);
      TypeSpec scopeClazz = scopeClassGenerator.generate();
      JavaFile.builder(packageName, scopeClazz).build().writeTo(processingEnv.getFiler());
    } catch (IOException e) {
      // ignore >.<
    }
  }

  private void checkValidScopeAnnotations(
      TypeElement annotatedType, HashSet<TypeElement> methodScopedAnnotations)
      throws IllegalArgumentException {
    if (!annotatedType.getKind().isInterface()) {
      throw new IllegalArgumentException("Only interfaces can be annotated with @MethodScope");
    } else if (annotatedType.getModifiers().contains(Modifier.FINAL)) {
      throw new IllegalArgumentException("class modifier can not be final");
    } else if (annotatedType.getModifiers().contains(Modifier.PRIVATE)) {
      throw new IllegalArgumentException("class modifier can not be private");
    } else {
      methodScopedAnnotations.add(annotatedType);
    }
  }

  private void checkValidScopeAnnotatedClasses(
      TypeElement annotatedType, HashSet<TypeElement> candidates) throws IllegalArgumentException {
    if (!annotatedType.getKind().isClass()) {
      throw new IllegalArgumentException("Only classes can be annotated with scope annotation.");
    } else if (annotatedType.getModifiers().contains(Modifier.FINAL)) {
      throw new IllegalArgumentException("class modifier can not be final");
    } else if (annotatedType.getModifiers().contains(Modifier.PRIVATE)) {
      throw new IllegalArgumentException("class modifier can not be private");
    } else {
      candidates.add(annotatedType);
    }
  }

  private void handleExceptions(String message, Element element) {
    messager.printMessage(ERROR, StringUtils.getErrorMessagePrefix() + message, element);
  }
}
