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

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test cases for AddedOnceMember.
 *
 * @author ssidorov@the13haven.com
 */
class AddedOnceMemberTest {

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `value throws error if accessed before being set`() {
        val member = AddedOnceMember<String>()
        val exception = assertFailsWith<IllegalStateException> {
            member.value
        }
        assertTrue(exception.message?.contains("The value not added") == true)
    }

    @Test
    fun `set assigns value and allows access through value`() {
        val member = AddedOnceMember<String>()
        member.set("hello")
        assertEquals("hello", member.value)
    }

    @Test
    fun `set throws if called more than once`() {
        val member = AddedOnceMember<String>()
        member.set("first")
        val exception = assertFailsWith<IllegalStateException> {
            member.set("second")
        }
        assertTrue(exception.message?.contains("The value can only be added once") == true)
    }

    @Test
    fun `getOrDefault returns default if value not set`() {
        val member = AddedOnceMember<String>()
        val result = member.getOrDefault { "default" }
        assertEquals("default", result)
    }

    @Test
    fun `getOrDefault does not call default provider if value was set`() {
        val member = AddedOnceMember<String>()
        member.set("set")
        val defaultProvider = mockk<() -> String>()
        every { defaultProvider.invoke() } returns "default"

        val result = member.getOrDefault(defaultProvider)

        assertEquals("set", result)
        verify(exactly = 0) { defaultProvider.invoke() }
    }

    @Test
    fun `isAdded returns false when value not set`() {
        val member = AddedOnceMember<Int>()
        assertFalse(member.isAdded())
    }

    @Test
    fun `isAdded returns true after value is set`() {
        val member = AddedOnceMember<Int>()
        member.set(42)
        assertTrue(member.isAdded())
    }
}
