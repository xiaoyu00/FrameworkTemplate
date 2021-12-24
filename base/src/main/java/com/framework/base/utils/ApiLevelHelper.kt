/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.framework.base.utils

import android.os.Build

/**
 * Encapsulates checking api levels.
 * 检测api等级
 */
object ApiLevelHelper {
    /**
     * Checks if the current api level is at least the provided value.
     *
     * @param apiLevel One of the values within [Build.VERSION_CODES].
     * @return `true` if the calling version is at least `apiLevel`.
     * Else `false` is returned.
     */
    fun isAtLeast(apiLevel: Int): Boolean {
        return Build.VERSION.SDK_INT >= apiLevel
    }

    /**
     * Checks if the current api level is at lower than the provided value.
     *
     * @param apiLevel One of the values within [Build.VERSION_CODES].
     * @return `true` if the calling version is lower than `apiLevel`.
     * Else `false` is returned.
     */
    fun isLowerThan(apiLevel: Int): Boolean {
        return Build.VERSION.SDK_INT < apiLevel
    }
}