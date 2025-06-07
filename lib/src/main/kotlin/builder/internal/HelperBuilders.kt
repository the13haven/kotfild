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
 * Base class for collection builders.
 *
 * @author ssidorov@the13haven.com
 */
open class TypedCollectionBuilder<T> : Builder<List<T>> {
    private val items = mutableListOf<T>()

    operator fun T.unaryPlus() {
        items.add(this)
    }

    override fun build(): List<T> {
        return items.toList()
    }
}

/**
 * Helper function for initialize builder.
 *
 * @author ssidorov@the13haven.com
 */
fun <B : Builder<R>, R> initBuilder(instance: B, init: B.() -> Unit): R =
    instance.apply { init() }
        .build()

/**
 * Helper function for initialize collection builder.
 *
 * @author ssidorov@the13haven.com
 */
fun <T> initTypedCollection(init: TypedCollectionBuilder<T>.() -> Unit): List<T> =
    initBuilder(TypedCollectionBuilder(), init)


/**
 * Helper function for initialize collection with converter.
 *
 * @author ssidorov@the13haven.com
 */
fun <T> initTypedCollection(
    init: TypedCollectionBuilder<String>.() -> Unit,
    converter: (item: String) -> T
): List<T> =
    initTypedCollection(init)
        .map { converter.invoke(it) }
        .toList()
