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

/**
 * Empty line model.
 *
 * @author ssidorov@the13haven.com
 */
object EmptyLine : CommonMember

/**
 * Kotlin comment model.
 *
 * @author ssidorov@the13haven.com
 */
data class Comment(
    val kind: CommentKind,
    val lines: List<String>
) : CommonMember {

    /**
     * Kind of comments.
     */
    enum class CommentKind {
        /**
         * Represents simple one line comment.
         * ```
         * // simple line comment
         * ```
         */
        LINE,

        /**
         * Represents block line comment.
         * ```
         * /* block line comment */
         * ```
         */
        BLOCK_LINE,

        /**
         * Represents multi line block comment.
         * ```
         * /*
         *   block comment
         * */
         * ```
         */
        BLOCK,

        /**
         * Represents one line kdoc comment.
         * ```
         * /** KDoc comment. */
         * ```
         */
        KDOC_LINE,

        /**
         * Represents multi line kdoc comment.
         * ```
         * /**
         *  * Multi line kdoc comment.
         *  */
         * ```
         */
        KDOC_BLOCK
    }
}
