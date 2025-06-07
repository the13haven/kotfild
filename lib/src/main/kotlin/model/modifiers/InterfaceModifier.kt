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
 * Interface modifiers.
 *
 * @author ssidorov@the13haven.com
 */
sealed class InterfaceModifier(override var name: String) : Modifier(name) {

    /** Marks a declaration as visible anywhere. */
    data object PUBLIC : ClassModifier("public")

    /** Marks a declaration as visible in the current module. */
    data object INTERNAL : ClassModifier("internal")

    /** Marks a declaration as visible in the current class or file. */
    data object PRIVATE : ClassModifier("private")

    /** Declares a sealed class (a class with restricted subclassing). */
    data object SEALED : ClassModifier("sealed")
}