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
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Test cases for UniqueMemberCollectorProcessor.
 *
 * @author ssidorov@the13haven.com
 */
class UniqueMemberCollectorProcessorTest {

    private lateinit var logger: KSPLogger
    private lateinit var codeGenerator: CodeGenerator
    private lateinit var processor: UniqueMemberCollectorProcessor
    private lateinit var resolver: Resolver

    @BeforeTest
    fun setup() {
        logger = mockk(relaxed = true)
        codeGenerator = mockk()
        processor = UniqueMemberCollectorProcessor(logger, codeGenerator)
        resolver = mockk()
    }

    @Test
    fun `already invoked processor must return empty list`() {
        // First call
        every { resolver.getSymbolsWithAnnotation(any()) } returns emptySequence()
        processor.process(resolver)

        // Second call
        val result = processor.process(resolver)

        assertTrue(result.isEmpty())
        verify(exactly = 1) { logger.info(">>> Start processing UniqueMembers...") }
    }

    @Test
    fun `processor must return empty list and log info when no annotated symbols found`() {
        every { resolver.getSymbolsWithAnnotation(any()) } returns emptySequence()

        val result = processor.process(resolver)

        assertTrue(result.isEmpty())
        verify { logger.info(">>> No UniqueMembers found") }
        verify { logger.info(">>> Processing UniqueMembers finished") }
    }

    @Test
    fun `processor must generate UniqueMemberType file for annotated classes`() {
        val classDecl = mockk<KSClassDeclaration>()
        val ksFile = mockk<KSFile>()
        every { classDecl.simpleName.asString() } returns "MyClass"
        every { classDecl.containingFile } returns ksFile

        every { resolver.getSymbolsWithAnnotation(any()) } returns sequenceOf(classDecl)

        val outputStream = mockk<java.io.OutputStream>(relaxed = true)
        every {
            codeGenerator.createNewFile(
                any(), "com.the13haven.kotfild.model", "UniqueMemberType"
            )
        } returns outputStream

        val result = processor.process(resolver)

        assertTrue(result.isEmpty())
        verify { logger.info(">>> Writing UniqueMemberType file...") }
    }

    @Test
    fun `processor must log error if file generation fails`() {
        val classDeclaration = mockk<KSClassDeclaration>()
        val ksFile = mockk<KSFile>()
        every { classDeclaration.simpleName.asString() } returns "FailingClass"
        every { classDeclaration.containingFile } returns ksFile

        every { resolver.getSymbolsWithAnnotation(any()) } returns sequenceOf(classDeclaration)

        every {
            codeGenerator.createNewFile(
                any(),
                "com.the13haven.kotfild.model",
                "UniqueMemberType"
            )
        } throws RuntimeException("boom")

        processor.process(resolver)

        verify { logger.error(match { "boom" in it }) }
    }
}