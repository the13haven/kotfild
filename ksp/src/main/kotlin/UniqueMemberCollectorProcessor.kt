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

package com.the13haven.kotfild.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import java.io.OutputStream
import java.io.Writer
import java.time.LocalDate


/**
 * Collects models annotated with @UniqueMember and writes their classes to a UniqueMemberType object.
 *
 * @author ssidorov@the13haven.com
 */
class UniqueMemberCollectorProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    private var invoked = false

    companion object {
        private const val UNIQUE_MEMBER_ANNOTATION = "com.the13haven.kotfild.model.UniqueMember"
        private const val PACKAGE = "com.the13haven.kotfild.model"
        private const val CLASS_NAME = "UniqueMemberType"
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // Prevent multiple invocation during one build session
        if (invoked) return emptyList()
        // Mark as invoked to avoid processing multiple times in the same build session
        invoked = true

        environment.logger.info(">>> Start processing UniqueMembers...")

        val uniqueMemberSymbols = resolver.getSymbolsWithAnnotation(UNIQUE_MEMBER_ANNOTATION)
        val uniqueMemberAnnotatedClasses = uniqueMemberSymbols.filterIsInstance<KSClassDeclaration>()
            .toList()

        if (uniqueMemberAnnotatedClasses.isEmpty()) {
            environment.logger.info(">>> No UniqueMembers found")
            environment.logger.info(">>> Processing UniqueMembers finished")
            return emptyList()
        }

        environment.logger.info(
            ">>> Symbols marked as UniqueMembers:\n"
                    + uniqueMemberAnnotatedClasses.map { it.simpleName.asString() }
                .joinToString("\n") { "        # $it" }
        )

        // Collect all source files containing our annotated classes for dependencies tracking
        val uniqueMemberSourceFiles = uniqueMemberAnnotatedClasses.mapNotNull { it.containingFile }.toSet()

        // Create dependencies with aggregating=true to ensure proper regeneration
        val uniqueMemberDependencies = Dependencies(
            aggregating = true,
            *uniqueMemberSourceFiles.toTypedArray()
        )

        // Generate the object file with proper dependencies
        try {
            environment.codeGenerator
                .createNewFile(
                    uniqueMemberDependencies,
                    PACKAGE,
                    CLASS_NAME
                )
                .use { outputStream ->
                    writeUniqueMemberTypeObject(outputStream, uniqueMemberAnnotatedClasses)
                }

            environment.logger.info(">>> Processing UniqueMembers finished")

        } catch (e: Exception) {
            environment.logger.error("Error generating code: ${e.message}")
        }

        return emptyList()
    }

    private fun writeUniqueMemberTypeObject(outputStream: OutputStream, classes: List<KSClassDeclaration>) {
        environment.logger.info(">>> Writing $CLASS_NAME file...")

        outputStream.writer()
            .apply {
                // write common part
                writeCopyright(this)
                appendLine()
                appendLine()
                appendLine("package com.the13haven.kotfild.model")
                appendLine()
                writeClassKDoc(this)
                appendLine()
                appendLine("object $CLASS_NAME {")
                appendLine()
                writeMemberKDoc(this)
                appendLine()
                appendLine("    @JvmStatic")

                if (classes.isNotEmpty()) {
                    val uniqueMembers = classes.map { it.simpleName.asString() }
                        .map { "$it::class" }
                        .joinToString(",\n") { "        $it" }

                    appendLine("    val UNIQUE_MEMBER_TYPES = setOf(")
                    appendLine(uniqueMembers)
                    appendLine("    )")

                } else {
                    appendLine("    val UNIQUE_MEMBER_TYPES = setOf<kotlin.reflect.KClass<*>>()")
                }

                appendLine("}")
            }
            .flush()
    }

    private fun writeMemberKDoc(writer: Writer) {
        writer.write(
            """
            |    /**
            |     * Set of UniqueMember types.
            |     */
            """.trimMargin("|")
        )
    }

    private fun writeClassKDoc(writer: Writer) {
        writer.write(
            """
            /**
             * UniqueMemberType contains all Member classes marked with the [com.the13haven.kotfild.model.UniqueMember] annotation.
             *
             * Attention: This is autogenerated code, do not change it manually!
             * @see [com.the13haven.kotfild.ksp.UniqueMemberCollectorProcessor]
             *
             * @author ssidorov@the13haven.com
             */
        """.trimIndent()
        )
    }

    private fun writeCopyright(writer: Writer) {
        writer.write(
            """
            /*
             * Copyright ${LocalDate.now().year}
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
             """.trimIndent()
        )
    }
}
