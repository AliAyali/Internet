package com.aliayali.internet.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for initializing Hilt dependency injection.
 *
 * Annotating this class with [HiltAndroidApp] triggers Hilt's code generation
 * and sets up the dependency injection container for the entire application.
 */
@HiltAndroidApp
class MyApplication : Application()