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

import com.the13haven.kotfild.model.TypeParameters.Companion.EMPTY_TYPE_PARAMETERS
import com.the13haven.kotfild.model.modifiers.ClassModifier
import com.the13haven.kotfild.model.modifiers.InterfaceModifier


/**
 * Kotlin class model.
 *
 * @author ssidorov@the13haven.com
 */
data class KotlinClass(
    val name: String,
    ) : Model, FileMember, ClassMember

data class KotlinDataClass(
    val declaration: ClassDeclaration
) : Model, FileMember, ClassMember

data class KotlinInlineValueClass(
    val declaration: ClassDeclaration
)

data class KotlinAnnotationClass(
    val declaration: ClassDeclaration
) : Model, FileMember, ClassMember

data class KotlinInterface(
    val declaration: InterfaceDeclaration
) : Model, FileMember, ClassMember

data class KotlinFunInterface(
    val declaration: InterfaceDeclaration
) : Model, FileMember, ClassMember

data class KotlinObject(
    val declaration: ObjectDeclaration
) : Model, FileMember, ClassMember

data class ClassDeclaration(
    val annotations: List<Annotation> = emptyList(),
    val modifiers: List<ClassModifier> = emptyList(),
    val name: String,
    val typeParameters: TypeParameters = EMPTY_TYPE_PARAMETERS,

    )

data class ClassPrimaryConstructor(
    val n: String // temp
)

data class ObjectDeclaration(
    val annotations: List<Annotation> = emptyList(),
    val modifiers: List<ClassModifier> = emptyList(),
    val name: String
)

data class InterfaceDeclaration(
    val annotations: List<Annotation> = emptyList(),
    val modifiers: List<InterfaceModifier> = emptyList(),
    val name: String
)
