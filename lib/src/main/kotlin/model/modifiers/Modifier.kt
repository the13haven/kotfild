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

import java.util.Locale

/**
 * Modifier.
 *
 *  @author ssidorov@the13haven.com
 */
sealed class Modifier(open val name: String) : Comparable<Modifier> {
    protected val order: Order = Order.valueOf(name.uppercase(Locale.getDefault()))

    /**
     * Compares this modifier with another for order.
     */
    override fun compareTo(other: Modifier): Int {
        return order.compareTo(other.order)
    }

    override fun toString(): String {
        return name
    }

    /**
     * Order constants.
     *
     * The order of this enum values is important!
     *
     * **Modifiers order**
     *
     * If a declaration has multiple modifiers, always put them in the following order:
     * * public / protected / private / internal
     * * expect / actual
     * * final / open / abstract / sealed / const
     * * override
     * * external
     * * lateinit
     * * tailrec
     * * vararg
     * * suspend
     * * inner
     * * enum / annotation / fun // as a modifier in `fun interface`
     * * companion
     * * inline/ noinline / crossinline / value
     * * infix
     * * operator
     * * data
     *
     * @author ssidorov@the13haven.com
     */
    enum class Order {

        /* public / protected / private / internal */
        DEFAULT, PUBLIC, PROTECTED, PRIVATE, INTERNAL,

        /* expect / actual */
        EXPECT, ACTUAL,

        /* final / open / abstract / override / sealed / const */
        FINAL, OPEN, ABSTRACT, OVERRIDE, SEALED, CONST,

        /* external */
        EXTERNAL,

        /* lateinit */
        LATEINIT,

        /* tailrec */
        TAILREC,

        /* vararg */
        VARARG,

        /* suspend */
        SUSPEND,

        /* inner */
        INNER,

        /* enum / annotation / fun // as a modifier in `fun interface` */
//        ENUM, ANNOTATION, FUN,

        /* companion */
        COMPANION,

        /* inline/ noinline / crossinline / value */
        INLINE, NOINLINE, CROSSINLINE, // VALUE,

        /* infix */
        INFIX,

        /* operator */
        OPERATOR,
    }
}
