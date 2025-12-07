package com.intsoftdev.nrstations

import kotlin.reflect.KClass

// Stub implementations for Android Device Test
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
actual annotation class RunWith(
    actual val value: KClass<out Runner>
)

actual abstract class Runner

actual class AndroidJUnit4 : Runner()

actual annotation class Config(
    actual val sdk: IntArray = []
)
