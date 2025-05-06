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

import com.the13haven.kotfild.builder.internal.Builder
import com.the13haven.kotfild.builder.internal.TypedCollectionBuilder
import com.the13haven.kotfild.builder.internal.initTypedCollection
import com.the13haven.kotfild.model.Comment
import com.the13haven.kotfild.model.CommonMember
import com.the13haven.kotfild.model.EmptyLine
import com.the13haven.kotfild.model.Model

/**
 * Base interface for Model Builder.
 *
 * @author ssidorov@the13haven.com
 */
interface ModelBuilder<out MODEL : Model> : Builder<MODEL>

internal class CommonModelBuilderDelegate<out MODEL : Model>(
    private val modelFactory: () -> MODEL
) : ModelBuilder<MODEL> {

    override fun build(): MODEL = modelFactory()
}

interface CommentsAwareBuilder {
    /**
     * Append empty line.
     */
    fun emptyLine()

    /**
     * Append single-line comment.
     *
     * Usage:
     * ```
     * commentLine { "comment" }
     * ```
     * This produces the following code:
     * ```
     * // comment
     * ```
     */
    fun commentLine(init: () -> String)

    /**
     * Append single-line-block comment.
     *
     * Usage:
     * ```
     * commentBlockLine { "comment" }
     * ```
     * This produces the following code:
     * ```
     * /* comment */
     * ```
     */
    fun commentBlockLine(init: () -> String)

    /**
     * Append multi-line comment.
     *
     * Usage:
     * ```
     * commentBlock {
     *     +"multi"
     *     +"line"
     *     +"comment"
     * }
     * ```
     * This produces the following code:
     * ```
     * /*
     *   multi
     *   line
     *   code
     * */
     * ```
     */
    fun commentBlock(init: TypedCollectionBuilder<String>.() -> Unit)

    /**
     * Single-line KDoc comment.
     *
     * Usage:
     * ```
     * commentKDockBlockLine { "kdoc comment" }
     * ```
     * This produces the following code:
     * ```
     * /** kdoc comment */
     * ```
     */
    fun commentKDockBlockLine(init: () -> String)

    /**
     * Multi-line KDoc comment.
     *
     * Usage:
     * ```
     * commentKDockBlock {
     *     +"multi"
     *     +"line"
     *     +"kdoc"
     * }
     * ```
     * This produces the following code:
     * ```
     * /**
     *  * multi
     *  * line
     *  * kdoc
     *  */
     * ```
     */
    fun commentKDockBlock(init: TypedCollectionBuilder<String>.() -> Unit)
}

/**
 * Default implementation for common builder function.
 *
 * @author ssidorov@the13haven.com
 */
internal class CommentsAwareBuilderDelegate(
    private val commonMemberAppender: (member: CommonMember) -> Unit,
) : CommentsAwareBuilder {

    override fun emptyLine() = commonMemberAppender.invoke(EmptyLine)

    override fun commentLine(init: () -> String) =
        commonMemberAppender.invoke(Comment(Comment.CommentKind.LINE, listOf(init())))

    override fun commentBlockLine(init: () -> String) =
        commonMemberAppender.invoke(Comment(Comment.CommentKind.BLOCK_LINE, listOf(init())))

    override fun commentBlock(init: TypedCollectionBuilder<String>.() -> Unit) =
        commonMemberAppender.invoke(
            Comment(
                Comment.CommentKind.BLOCK,
                initTypedCollection(init)
            )
        )

    override fun commentKDockBlockLine(init: () -> String) =
        commonMemberAppender.invoke(Comment(Comment.CommentKind.KDOC_LINE, listOf(init())))

    override fun commentKDockBlock(init: TypedCollectionBuilder<String>.() -> Unit) =
        commonMemberAppender.invoke(
            Comment(
                Comment.CommentKind.KDOC_BLOCK,
                initTypedCollection(init)
            )
        )
}
