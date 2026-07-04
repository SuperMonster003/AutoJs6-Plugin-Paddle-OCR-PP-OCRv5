// Copyright (c) 2026 PaddlePaddle Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.paddle.ocr.util

import android.content.Context
import android.util.Log
import java.io.File

object OpenCVUtils {

    private const val TAG = "OpenCVUtils"

    @Volatile
    private var initialized = false

    @Volatile
    var lastError: String? = null
        private set

    fun init(context: Context): Boolean {
        if (initialized) return true
        synchronized(this) {
            if (initialized) return true
            val failures = mutableListOf<String>()
            if (tryLoadWithSystemLoader(failures) || tryLoadFromNativeLibraryDir(context, failures)) {
                initialized = true
                lastError = null
                return true
            }
            lastError = failures.joinToString(separator = " | ")
            Log.e(TAG, "Failed to initialize OpenCV: $lastError")
            return false
        }
    }

    private fun tryLoadWithSystemLoader(failures: MutableList<String>): Boolean {
        return try {
            loadLibraryIfPresent("c++_shared")
            System.loadLibrary("opencv_java4")
            true
        } catch (e: UnsatisfiedLinkError) {
            failures += "System.loadLibrary(opencv_java4): ${e.message}"
            false
        }
    }

    private fun tryLoadFromNativeLibraryDir(context: Context, failures: MutableList<String>): Boolean {
        val nativeLibraryDir = context.applicationInfo.nativeLibraryDir
        if (nativeLibraryDir.isNullOrBlank()) {
            failures += "nativeLibraryDir is blank"
            return false
        }
        return try {
            loadFileIfExists(File(nativeLibraryDir, "libc++_shared.so"))
            System.load(File(nativeLibraryDir, "libopencv_java4.so").absolutePath)
            true
        } catch (e: UnsatisfiedLinkError) {
            failures += "System.load(nativeLibraryDir/libopencv_java4.so): ${e.message}"
            false
        }
    }

    private fun loadLibraryIfPresent(name: String) {
        try {
            System.loadLibrary(name)
        } catch (_: UnsatisfiedLinkError) {
            // OpenCV may still load if the dependency is resolved by the platform linker.
        }
    }

    private fun loadFileIfExists(file: File) {
        if (file.isFile) {
            System.load(file.absolutePath)
        }
    }
}
