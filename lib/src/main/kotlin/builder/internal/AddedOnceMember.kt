/*
 * Copyright 2025
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.the13haven.kotfild.builder.internal

/**
 * Wrapper for members that must be added only once.
 *
 * @author ssidorov@the13haven.com
 */
class AddedOnceMember<T> {

    private var _value: T? = null
    private var isAdded = false

    val value: T
        get() = _value ?: error("The value not added")

    fun set(value: T) {
        check(!isAdded) { "The value can only be added once" }

        _value = value
        isAdded = true
    }

    fun getOrDefault(defaultValueProvider: () -> T): T =
        if (isAdded) {
            value
        } else {
            defaultValueProvider.invoke()
        }

    fun isAdded(): Boolean = isAdded
}
