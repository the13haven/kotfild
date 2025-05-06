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

package com.the13haven.kotfild.dsl

import com.the13haven.kotfild.builder.KotlinFileBuilder
import com.the13haven.kotfild.model.KotlinFile
import com.the13haven.kotfild.model.KotlinFunction


/**
 * KotlinFile DSL.
 *
 * @author ssidorov@the13haven.com
 */
fun kotlinFile(fileName: String, init: KotlinFileBuilder.() -> Unit): KotlinFile =
    KotlinFileBuilder(fileName)
        .apply(init)
        .build()

/**
 * KotlinScript DSL.
 *
 * @author ssidorov@the13haven.com
 */
fun kotlinScript() {
    TODO()
}

fun function(): KotlinFunction {
    TODO()
}
