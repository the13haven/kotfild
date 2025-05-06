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

package com.the13haven.kotfild.model

import kotlin.reflect.KClass

sealed interface Type

sealed interface NullableAcceptableType : Type
sealed interface DefinitelyNonNullableAcceptableType : Type
sealed interface ReceiverAcceptableType : Type

object EmptyType : Type
object EmptyReceiverAcceptableType : ReceiverAcceptableType

data class TypeReference(val name: String) :
    Type, NullableAcceptableType, DefinitelyNonNullableAcceptableType, ReceiverAcceptableType {

    constructor(clazz: KClass<*>) : this(clazz.simpleName ?: "")
    constructor(clazz: Class<*>) : this(clazz.simpleName)
}

data class ParenthesizedType(val inner: Type) :
    Type, NullableAcceptableType, DefinitelyNonNullableAcceptableType, ReceiverAcceptableType

data class FunctionType(
    val receiver: ReceiverAcceptableType?,
    val parameters: List<Type>,
    val returnType: Type
) : Type

data class NullableType(val inner: NullableAcceptableType) : Type

data class DefinitelyNonNullableType(val inner: DefinitelyNonNullableAcceptableType) : Type


data class ReceiverType(
    val annotations: List<Annotation> = emptyList(),
    val isSuspendable: Boolean = false,
    val type: ReceiverAcceptableType,
) {
    companion object {
        @JvmStatic
        val EMPTY_RECEIVER_TYPE = ReceiverType(type = EmptyReceiverAcceptableType)
    }
}
