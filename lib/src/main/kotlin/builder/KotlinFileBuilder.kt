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

package com.the13haven.kotfild.builder

import com.the13haven.kotfild.builder.internal.MemberCollector
import com.the13haven.kotfild.builder.internal.TypedCollectionBuilder
import com.the13haven.kotfild.builder.internal.initTypedCollection
import com.the13haven.kotfild.model.FileAnnotation
import com.the13haven.kotfild.model.FileMember
import com.the13haven.kotfild.model.Import
import com.the13haven.kotfild.model.KotlinFile
import com.the13haven.kotfild.model.Package
import com.the13haven.kotfild.model.Shebang

/**
 * Builder for KotlinFile DSL.
 *
 * @author ssidorov@the13haven.com
 */
@DslBuilder
class KotlinFileBuilder(
    private val fileName: String,
    private val members: MemberCollector<FileMember> = MemberCollector()
) :
    ModelBuilder<KotlinFile> by CommonModelBuilderDelegate(
        { KotlinFile(fileName, members.getMembers()) }
    ),
    CommentsAwareBuilder by CommentsAwareBuilderDelegate(
        members::add
    ),
    FunctionAwareBuilder by FunctionBuilderDelegate(
        members::add
    ) {

    /**
     * Shebang line.
     *
     * Usage:
     * ```
     * shebangLine { "/usr/bin/env kotlin" }
     * ```
     * This produces the following code:
     * ```
     * #!/usr/bin/env kotlin
     * ```
     *
     * @param path the path to kotlin runtime
     */
    fun shebangLine(path: () -> String) {
        members.add(Shebang(path()))
    }

    /**
     * Default shebang.
     *
     * ```
     * #!/usr/bin/env kotlin
     * ```
     */
    fun shebangLineDefault() {
        shebangLine { Shebang.DEFAULT_SHEBANG_LINE }
    }

    /**
     * File annotation.
     *
     * Usage:
     * ```
     * fileAnnotations {
     *     +"JvmName(\"Foo\")"
     *
     * ```
     * This produces the following code:
     * ```
     * @file:@JvmName("Foo")
     * ```
     */
    fun fileAnnotations(init: TypedCollectionBuilder<String>.() -> Unit) {
        members.addAll(initTypedCollection(init, ::FileAnnotation))
    }

    /**
     * Package header.
     *
     * Usage:
     * ```
     * packageHeader { "com.the13haven.kotfild" }
     * ```
     * This produces the following code:
     * ```
     * package com.the13haven.kotfild
     * ```
     */
    fun packageHeader(path: () -> String) {
        members.add(Package(path()))
    }

    /**
     * Imports block.
     *
     * Usage:
     * ```
     * imports {
     *     +"com.the13haven.kotfild.model.FileAnnotation"
     *     +"com.the13haven.kotfild.model.Package"
     * }
     * ```
     * This produces the following code:
     * ```
     * import com.the13haven.kotfild.model.FileAnnotation
     * import com.the13haven.kotfild.model.Package
     * ```
     */
    fun imports(init: TypedCollectionBuilder<String>.() -> Unit) {
        members.addAll(initTypedCollection(init, ::Import))
    }
}
