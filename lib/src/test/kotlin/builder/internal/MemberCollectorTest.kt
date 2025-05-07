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

import com.the13haven.kotfild.model.EmptyLine
import com.the13haven.kotfild.model.Member
import com.the13haven.kotfild.model.Package
import com.the13haven.kotfild.model.Shebang
import io.mockk.MockKAnnotations
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Test cases for MemberCollector.
 *
 * @author ssidorov@the13haven.com
 */
class MemberCollectorTest {


    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `add stores non-unique members multiple times`() {
        val collector = MemberCollector<Member>()
        val member1 = EmptyLine
        val member2 = EmptyLine

        collector.add(member1)
        collector.add(member2)

        val result = collector.getMembers()
        assertEquals(2, result.size)
        assertEquals(listOf(member1, member2), result)
    }

    @Test
    fun `add stores unique member once`() {
        val collector = MemberCollector<Member>()
        val unique = Package("")

        collector.add(unique)
        val result = collector.getMembers()

        assertEquals(1, result.size)
        assertEquals(unique, result[0])
    }

    @Test
    fun `add throws when same unique member type is added twice`() {
        val collector = MemberCollector<Member>()
        collector.add(Package(""))

        val exception = assertFailsWith<IllegalStateException> {
            collector.add(Package(""))
        }
        assertTrue(exception.message?.contains("can only be set once") == true)
    }

    @Test
    fun `addAll adds multiple members including non-unique`() {
        val collector = MemberCollector<Member>()
        val members = listOf<Member>(EmptyLine, Package(""), Shebang(Shebang.DEFAULT_SHEBANG_LINE))

        collector.addAll(members)

        assertEquals(3, collector.getMembers().size)
        assertEquals(members, collector.getMembers())
    }

    @Test
    fun `addAll throws when duplicate unique member types are present`() {
        val collector = MemberCollector<Member>()
        val members = listOf(Package(""), Package(""))

        val exception = assertFailsWith<IllegalStateException> {
            collector.addAll(members)
        }

        assertTrue(exception.message?.contains("can only be set once") == true)
    }

    @Test
    fun `getMembers returns empty list when nothing is added`() {
        val collector = MemberCollector<Member>()
        assertTrue(collector.getMembers().isEmpty())
    }

    @Test
    fun `getMembers returns defensive copy`() {
        val collector = MemberCollector<Member>()
        val member = EmptyLine
        collector.add(member)

        val list = collector.getMembers().toMutableList()
        list.clear()

        assertEquals(1, collector.getMembers().size, "Original list should not be affected by external modification")
    }
}
