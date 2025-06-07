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

import com.the13haven.kotfild.model.modifiers.TypeParameterModifier

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

data class Expression(val expression: String) {
    companion object {
        @JvmStatic
        val EMPTY_EXPRESSION = Expression("")
    }
}
