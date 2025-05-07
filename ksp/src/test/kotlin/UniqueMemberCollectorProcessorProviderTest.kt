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

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Test cases for UniqueMemberCollectorProcessorProvider.
 *
 * @author ssidorov@the13haven.com
 */
class UniqueMemberCollectorProcessorProviderTest {

    private lateinit var provider: UniqueMemberCollectorProcessorProvider
    private lateinit var mockEnvironment: SymbolProcessorEnvironment

    @BeforeTest
    fun setup() {
        provider = UniqueMemberCollectorProcessorProvider()
        mockEnvironment = mockk(relaxed = true)
    }

    @Test
    fun `create returns a non-null SymbolProcessor`() {
        // When
        val processor = provider.create(mockEnvironment)

        // Then
        assertNotNull(processor, "Processor should not be null")
    }

    @Test
    fun `create returns an instance of UniqueMemberCollectorProcessor`() {
        // When
        val processor = provider.create(mockEnvironment)

        // Then
        assertTrue(
            processor is UniqueMemberCollectorProcessor,
            "Processor should be an instance of UniqueMemberCollectorProcessor"
        )
    }

    @Test
    fun `create uses environment logger and codeGenerator`() {
        // Given
        val mockLogger = mockk<KSPLogger>()
        val mockCodeGenerator = mockk<CodeGenerator>()

        every { mockEnvironment.logger } returns mockLogger
        every { mockEnvironment.codeGenerator } returns mockCodeGenerator

        // When
        provider.create(mockEnvironment)

        // Then
        verify(exactly = 1) { mockEnvironment.logger }
        verify(exactly = 1) { mockEnvironment.codeGenerator }
    }
}