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

data class Annotation(val annotation: String)

data class TypeParameters(val typeParameters: List<TypeParameter>) {
    companion object {
        @JvmStatic
        val EMPTY_TYPE_PARAMETERS = TypeParameters(emptyList())
    }
}

data class TypeParameter(
    val annotations: List<Annotation> = emptyList(),
    val modifiers: List<TypeParameterModifier> = emptyList(),
    val identifier: String,
    val type: Type
)

sealed class TypeParameterModifier(override var name: String) : Modifier(name) {
    object REIFIED : TypeParameterModifier("reified")

    object IN : TypeParameterModifier("in")

    object OUT : TypeParameterModifier("out")
}

data class Expression(val expression: String) {
    companion object {
        @JvmStatic
        val EMPTY_EXPRESSION = Expression("")
    }
}

sealed class ReceiverTypeModifier(override var name: String) : Modifier(name) {
    /** Marks a function or lambda as suspending (usable as a coroutine). */
    object SUSPEND : ReceiverTypeModifier("suspend")
}
