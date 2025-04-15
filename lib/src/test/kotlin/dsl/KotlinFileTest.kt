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

import com.the13haven.kotfild.model.Comment
import com.the13haven.kotfild.model.EmptyLine
import com.the13haven.kotfild.model.FileAnnotation
import com.the13haven.kotfild.model.Import
import com.the13haven.kotfild.model.Package
import com.the13haven.kotfild.model.Shebang
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class KotlinFileTest {

    @Test
    fun testAddUniqueMemberMultipleTimes() {
        assertFailsWith<IllegalStateException>(
            "No check for duplicates of unique members",
            {
                kotlinFile("SomeFile") {
                    packageHeader { "com.the13haven" }
                    packageHeader { "org.the13th" }
                }
            }
        )
    }

    @Test
    fun testAll() {
        val file = kotlinFile("FirstClass") {
            shebangLine { "/usr/bin/env kotlin" }
            fileAnnotations {
                +"JvmName(\"Foo\")"
            }
            packageHeader { "com.me.project" }

            imports {
                +"com.the13haven.kotfild.model.FileAnnotation"
                +"com.the13haven.kotfild.model.Package"
            }

            emptyLine()
            emptyLine()

            commentLine { "line comment" }

            commentBlockLine { "block line comment" }

            commentBlock {
                +"multi"
                +"line"
                +"comment"
            }

            commentKDockBlockLine { "kdoc line" }

            commentKDockBlock {
                +"multi"
                +"line"
                +"kdoc"
            }
        }

        var emptyLinesCount = 0

        assertEquals("FirstClass", file.name)
        file.members
            .forEach {
                when (it) {
                    is Shebang -> assertEquals("/usr/bin/env kotlin", it.path)
                    is FileAnnotation -> assertEquals("JvmName(\"Foo\")", it.annotation)
                    is Package -> assertEquals("com.me.project", it.path)
                    is Import -> assertTrue(
                        it.import.equals("com.the13haven.kotfild.model.FileAnnotation")
                                || it.import.equals("com.the13haven.kotfild.model.Package")
                    )

                    is EmptyLine -> emptyLinesCount++
                    is Comment ->
                        when (it.kind) {
                            Comment.CommentKind.LINE -> assertEquals("line comment", it.lines.first())
                            Comment.CommentKind.BLOCK_LINE -> assertEquals("block line comment", it.lines.first())
                            Comment.CommentKind.BLOCK -> assertContentEquals(
                                listOf<String>("multi", "line", "comment"),
                                it.lines
                            )

                            Comment.CommentKind.KDOC_LINE -> assertEquals("kdoc line", it.lines.first())
                            Comment.CommentKind.KDOC_BLOCK -> assertContentEquals(
                                listOf<String>("multi", "line", "kdoc"),
                                it.lines
                            )
                        }
                }
            }

        assertEquals(2, emptyLinesCount)
    }
}