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

import com.the13haven.kotfild.model.Expression.Companion.EMPTY_EXPRESSION
import com.the13haven.kotfild.model.FunctionParameters.Companion.EMPTY_PARAMETERS
import com.the13haven.kotfild.model.ReceiverType.Companion.EMPTY_RECEIVER_TYPE
import com.the13haven.kotfild.model.TypeConstraints.Companion.EMPTY_TYPE_CONSTRAINTS
import com.the13haven.kotfild.model.TypeParameters.Companion.EMPTY_TYPE_PARAMETERS
import com.the13haven.kotfild.model.modifiers.FunctionModifier
import com.the13haven.kotfild.model.modifiers.FunctionParameterModifier

/**
 * Kotlin function model.
 *
 * @author ssidorov@the13haven.com
 */
data class KotlinFunction(
    val declaration: FunctionDeclaration,
    val body: FunctionBody
) : Model, FileMember, ClassMember

/**
 *
 */
data class FunctionDeclaration(
    val annotations: List<Annotation> = emptyList(),
    val modifiers: List<FunctionModifier> = emptyList(),
    val typeParameters: TypeParameters = EMPTY_TYPE_PARAMETERS,
    val receiverType: ReceiverType = EMPTY_RECEIVER_TYPE,
    val name: String = "",
    val parameters: FunctionParameters = EMPTY_PARAMETERS,
    val returnType: Type = EmptyType,
    val typeConstraints: TypeConstraints = EMPTY_TYPE_CONSTRAINTS
)

data class FunctionParameters(var parameters: List<FunctionParameter>) {
    companion object {
        val EMPTY_PARAMETERS = FunctionParameters(emptyList())
    }
}

data class FunctionParameter(
    val annotations: List<Annotation> = emptyList(),
    val modifiers: List<FunctionParameterModifier> = emptyList(),
    val name: String,
    val type: Type,
    val expression: Expression = EMPTY_EXPRESSION
)

data class TypeConstraints(
    val constraints: List<TypeConstraint>
) {
    companion object {
        @JvmStatic
        val EMPTY_TYPE_CONSTRAINTS = TypeConstraints(emptyList())
    }
}

data class TypeConstraint(
    val annotations: List<Annotation> = emptyList(),
    val name: String,
    val type: Type
)


sealed interface FunctionBody

data class FunctionBodyBlock(val members: List<FunctionMember>) : FunctionBody

data class FunctionBodyExpression(val expression: String) : FunctionBody

object FunctionBodyEmpty : FunctionBody
