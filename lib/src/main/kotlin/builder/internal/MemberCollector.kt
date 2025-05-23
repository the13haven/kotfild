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

package com.the13haven.kotfild.builder.internal

import com.the13haven.kotfild.model.Member
import com.the13haven.kotfild.model.UniqueMemberType
import kotlin.reflect.KClass

/**
 * Wrapper around the collection (list) that controls the number of members added.
 *
 * Uses autogenerated class [UniqueMemberType].
 *
 * @author ssidorov@the13haven.com
 */
class MemberCollector<T : Member> {
    private val addedUniqueMembers = mutableSetOf<KClass<*>>()
    private val members = mutableListOf<T>()

    fun addAll(members: Collection<T>) {
        members.forEach(::add)
    }

    fun add(member: T) {
        val type: KClass<*> = member::class

        if (UniqueMemberType.UNIQUE_MEMBER_TYPES.contains(type)) {
            if (addedUniqueMembers.contains(type)) {
                error("The ${type.simpleName} can only be set once")
            }

            addedUniqueMembers += type
        }

        members += member
    }

    fun getMembers(): List<T> = members.toList()
}
