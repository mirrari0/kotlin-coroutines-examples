# kotlin-coroutines-android-example

Add the coroutines implementation to the build.gradle

```
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1"
```

Also ensure that the compile options are set to java 8 in the build.gradle
```
android {
    compileOptions {
        targetCompatibility = "8"
        sourceCompatibility = "8"
    }
}
```

The coroutine is spun off using the `Global.async{ }` 

The `Deferred<>` Object functions similar to a promise. 

To use the output of a Deferred object, add the `runBlocking{ }` function to execute the closure. This functions similar to an await function in other languages.
