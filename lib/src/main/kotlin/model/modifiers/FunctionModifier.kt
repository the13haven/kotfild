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

package com.the13haven.kotfild.model.modifiers


/**
 * Function modifiers.
 *
 * @author ssidorov@the13haven.com
 */
sealed class FunctionModifier(override var name: String) : Modifier(name) {
    /** Marks a declaration as visible anywhere. */
    data object PUBLIC : FunctionModifier("public")

    /** Marks a declaration as visible in the current class and its subclasses. */
    data object PROTECTED : FunctionModifier("protected")

    /** Marks a declaration as visible in the current class or file. */
    data object PRIVATE : FunctionModifier("private")

    /** Marks a declaration as visible in the current module. */
    data object INTERNAL : FunctionModifier("internal")

    /** Marks a declaration as platform-specific, expecting an implementation in platform modules. */
    data object EXPECT : FunctionModifier("expect")

    /** Denotes a platform-specific implementation in multiplatform projects. */
    data object ACTUAL : FunctionModifier("actual")

    /** Forbids overriding a member. */
    data object FINAL : FunctionModifier("final")

    /** Allows subclassing a class or overriding a member. */
    data object OPEN : FunctionModifier("open")

    /** Marks a class or member as abstract. */
    data object ABSTRACT : FunctionModifier("abstract")

    /** Marks a member as an override of a superclass member. */
    data object OVERRIDE : FunctionModifier("override")

    /** Marks a declaration as implemented outside of Kotlin (accessible through JNI or in JavaScript). */
    data object EXTERNAL : FunctionModifier("external")

    /** Marks a function or lambda as suspending (usable as a coroutine). */
    data object SUSPEND : FunctionModifier("suspend")

    /** Tells the compiler to inline a function and the lambdas passed to it at the call site. */
    data object INLINE : FunctionModifier("inline")

    /** Turns off inlining of a lambda passed to an inline function. */
    data object NOINLINE : FunctionModifier("noinline")

    /** Forbids non-local returns in a lambda passed to an inline function. */
    data object CROSSINLINE : FunctionModifier("crossinline")

    /** Marks a function as overloading an operator or implementing a convention. */
    data object OPERATOR : FunctionModifier("operator")

    /** Allows calling a function using infix notation. */
    data object INFIX : FunctionModifier("infix")

    /** Marks a function as tail-recursive (allowing the compiler to replace recursion with iteration). */
    data object TAILREC : FunctionModifier("tailrec")
}
