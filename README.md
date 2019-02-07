# MethodScope
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![API](https://img.shields.io/badge/API-11%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=11)
[![Build Status](https://travis-ci.org/skydoves/MethodScope.svg?branch=master)](https://travis-ci.org/skydoves/MethodScope)<br>

Methodscope automatically generates classes that perform similar function tasks on a per-scope basis for a class.<br>
When similar and repetitive classes need to be created, the work what repetitive inheritance can be decreased.

## Including in your project
[![Download](https://api.bintray.com/packages/devmagician/maven/methodscope/images/download.svg) ](https://bintray.com/devmagician/maven/methodscope/_latestVersion)
### Gradle
And add below two dependencies code on your **module**'s `build.gradle` file.
```gradle
dependencies {
    implementation "com.github.skydoves:methodscope:1.0.1"
    annotationProcessor "com.github.skydoves:methodscope-processor:1.0.1"
}
```
    
## Usage

### @MethodScope
Create a custom scope annotation using `@MethodScope` annotation.
```java
@MethodScope
public @interface MyScope {
}
```

Here is the kotlin code example.
```kotlin
@MethodScope
annotation class MyScope
```

Attach the custom scope annotation on top of a class.<br>
All of the public methods are the base method for the scoped like the `init()` method.<br>
And designate scoping method using `@Scoped` annotation with the naming rule what startWith the base method's name.
```java
@MyScope
public class MyClass {
  private String scope;

  public void init() { // this is the base method of the scoped methods.
    this.scope = "initialized";
  }

  @Scoped(MyScope.class)
  public void initMyScope() { // this is a scoped method for @MyScope.
    this.scope = "initialized by @MyScope";
  }
}
```
After build the project, `MyClass_MyScope` class will be auto-generated.
```java
MyClass_MyScope myClass_myScope = new MyClass_MyScope();
myClass_myScope.init(); // calls init() and initMyScope() methods.
myClass_myScope.initMyScope(); // calls only initMyScope() method.
```

### Multiple Scoping
Here is how to create multiple scoped class.
```java
@MyScope
@HisScope
@YourScope
public class MyClass {
  private String scope;

  public void init() { // this is the base method of the scoped methods.
    this.scope = "initialized";
  }

  @Scoped(MyScope.class)
  public void initMyScope() { // this is a scoped method for @MyScope.
    this.scope = "initialized by @MyScope";
  }

  @Scoped(YourScope.class)
  public void initMineScopeNotYours() { // this is a scoped method for @YourScope.
    this.scope = "initialized by @YourScope";
  }
}
```
After build the project, `MyClass_MyScope`, `MyClass_HisScope`, `MyClass_YourScope` classes will be auto-generated.

### Scoping with a return type and parameters
Methods that have a return type and parameters can be scoped method.<br>
The base method's return type and parameters are must be the same as the scoped method's one.
```java
@MyScope
abstract public class MyClass {
  private String scope;

  public String init(String text) { // this is the base method of the scoped methods.
    this.scope += text;
    return this.scope;
  }

  @Scoped(MyScope.class)
  public String initMyScope(String text) { // this is a scoped method for @MyScope.
    this.scope += text;
    return this.scope;
  }
```

### Abstract class Scoping
MethodScope supports scopping for the abstract class. <br>
This is more clear because the base method of the scoped methods is being explicitly abstract.
```java
@MyScope
@YourScope
abstract public class MyClass {
  private String scope;

  abstract void init();

  @Scoped(MyScope.class)
  public void initMyScope() { // this is a scoped method for @MyScope.
    this.scope = "initialized by @MyScope";
  }

  @Scoped(YourScope.class)
  public void initMineScopeNotYours() { // this is a scoped method for @YourScope.
    this.scope = "initialized by @YourScope";
  }
```

## Android Project Usage
MethodScope is useful to the Android project for creating similar screens.<br>
```kotlin
@MyScope
@TestScope(deeplink = DeepLink("https://github.com/skydoves"))
abstract class MainActivity : AppCompatActivity() {

    private lateinit var hello: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init() // calls the base method. this will call each scoped method in the scoped class.
    }

    open fun init() {
        hello = "hello, "
    }

    @Scoped(MyScope::class)
    fun initMyScope() {
        setContentView(R.layout.activity_main_myscope) // setContentView for MyScope.
        text_message_myscope.text = hello + "MyScope" // changes text of the textView.
    }

    @Scoped(TestScope::class)
    fun initTestScope() {
        setContentView(R.layout.activity_main_testscope) // setContentView for TestScope.
        text_message_testscope.text = hello + "TestScope" // changes text of the textView.
    }
}
```
And on the `AndroidManifest.xml`, we should declare new generated activities.
```gradle
<activity android:name=".MainActivity_MyScope" />
<activity android:name=".MainActivity_TestScope" />
```
And then we can use the new activities.
```kotlin
val intent = Intent(this, MainActivity_MyScope::class.java)
startActivity(intent)
```

# License
```xml
Copyright 2019 skydoves

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
