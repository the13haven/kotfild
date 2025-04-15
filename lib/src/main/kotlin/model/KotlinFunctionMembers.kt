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

sealed class FunctionModifier(override var name: String) : Modifier(name) {
    /** Marks a declaration as visible anywhere. */
    object PUBLIC : FunctionModifier("public")

    /** Marks a declaration as visible in the current class and its subclasses. */
    object PROTECTED : FunctionModifier("protected")

    /** Marks a declaration as visible in the current class or file. */
    object PRIVATE : FunctionModifier("private")

    /** Marks a declaration as visible in the current module. */
    object INTERNAL : FunctionModifier("internal")

    /** Marks a declaration as platform-specific, expecting an implementation in platform modules. */
    object EXPECT : FunctionModifier("expect")

    /** Denotes a platform-specific implementation in multiplatform projects. */
    object ACTUAL : FunctionModifier("actual")

    /** Forbids overriding a member. */
    object FINAL : FunctionModifier("final")

    /** Allows subclassing a class or overriding a member. */
    object OPEN : FunctionModifier("open")

    /** Marks a class or member as abstract. */
    object ABSTRACT : FunctionModifier("abstract")

    /** Marks a member as an override of a superclass member. */
    object OVERRIDE : FunctionModifier("override")

    /** Marks a declaration as implemented outside of Kotlin (accessible through JNI or in JavaScript). */
    object EXTERNAL : FunctionModifier("external")

    /** Marks a function or lambda as suspending (usable as a coroutine). */
    object SUSPEND : FunctionModifier("suspend")

    /** Tells the compiler to inline a function and the lambdas passed to it at the call site. */
    object INLINE : FunctionModifier("inline")

    /** Turns off inlining of a lambda passed to an inline function. */
    object NOINLINE : FunctionModifier("noinline")

    /** Forbids non-local returns in a lambda passed to an inline function. */
    object CROSSINLINE : FunctionModifier("crossinline")

    /** Marks a function as overloading an operator or implementing a convention. */
    object OPERATOR : FunctionModifier("operator")

    /** Allows calling a function using infix notation. */
    object INFIX : FunctionModifier("infix")

    /** Marks a function as tail-recursive (allowing the compiler to replace recursion with iteration). */
    object TAILREC : FunctionModifier("tailrec")
}

sealed class FunctionParameterModifier(override var name: String) : Modifier(name) {
    /** Allows passing a variable number of arguments for a parameter. */
    object VARARG : FunctionParameterModifier("vararg")

    /** Turns off inlining of a lambda passed to an inline function. */
    object NOINLINE : FunctionParameterModifier("noinline")

    /** Forbids non-local returns in a lambda passed to an inline function. */
    object CROSSINLINE : Modifier("crossinline")
}
