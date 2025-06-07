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

import com.the13haven.kotfild.builder.internal.AddedOnceMember
import com.the13haven.kotfild.builder.internal.Builder
import com.the13haven.kotfild.builder.internal.MemberCollector
import com.the13haven.kotfild.builder.internal.TypedCollectionBuilder
import com.the13haven.kotfild.builder.internal.initBuilder
import com.the13haven.kotfild.builder.internal.initTypedCollection
import com.the13haven.kotfild.model.Annotation
import com.the13haven.kotfild.model.EmptyReceiverAcceptableType
import com.the13haven.kotfild.model.EmptyType
import com.the13haven.kotfild.model.Expression
import com.the13haven.kotfild.model.Expression.Companion.EMPTY_EXPRESSION
import com.the13haven.kotfild.model.FunctionBody
import com.the13haven.kotfild.model.FunctionBodyBlock
import com.the13haven.kotfild.model.FunctionBodyExpression
import com.the13haven.kotfild.model.FunctionDeclaration
import com.the13haven.kotfild.model.FunctionMember
import com.the13haven.kotfild.model.FunctionParameter
import com.the13haven.kotfild.model.FunctionParameters
import com.the13haven.kotfild.model.KotlinFunction
import com.the13haven.kotfild.model.ReceiverAcceptableType
import com.the13haven.kotfild.model.ReceiverType
import com.the13haven.kotfild.model.ReceiverType.Companion.EMPTY_RECEIVER_TYPE
import com.the13haven.kotfild.model.Type
import com.the13haven.kotfild.model.TypeConstraint
import com.the13haven.kotfild.model.TypeConstraints
import com.the13haven.kotfild.model.TypeParameter
import com.the13haven.kotfild.model.TypeParameters
import com.the13haven.kotfild.model.TypeReference
import com.the13haven.kotfild.model.modifiers.FunctionModifier
import com.the13haven.kotfild.model.modifiers.FunctionParameterModifier
import com.the13haven.kotfild.model.modifiers.TypeParameterModifier

abstract class BaseFunctionBuilder : ModelBuilder<KotlinFunction> {
    protected val declaration = AddedOnceMember<FunctionDeclaration>()
    protected var body = AddedOnceMember<FunctionBody>()

    fun declaration(name: String, init: FunctionDeclarationBuilder.() -> Unit) {
        declaration.set(initBuilder(FunctionDeclarationBuilder(name), init))
    }

    override fun build(): KotlinFunction =
        KotlinFunction(
            declaration.value,
            body.value
        )
}

class FunctionWithBodyBlockBuilder : BaseFunctionBuilder() {
    fun bodyBlock(init: FunctionBodyBlockBuilder.() -> Unit) =
        body.set(initBuilder(FunctionBodyBlockBuilder(), init))
}

class FunctionWithBodyExpressionBuilder : BaseFunctionBuilder() {
    fun bodyExpression(expression: () -> String) =
        body.set(FunctionBodyExpressionBuilder(expression.invoke()).build())
}

class FunctionDeclarationBuilder(private val name: String) : Builder<FunctionDeclaration> {
    private val annotations = mutableListOf<Annotation>()
    private val modifiers = mutableListOf<FunctionModifier>()
    private val typeParameters = mutableListOf<TypeParameter>()
    private val receiverType = AddedOnceMember<ReceiverType>()
    private val parameters = mutableListOf<FunctionParameter>()
    private val returnType = AddedOnceMember<Type>()
    private val typeConstraints = mutableListOf<TypeConstraint>()

    fun annotations(init: TypedCollectionBuilder<String>.() -> Unit) {
        annotations.addAll(initTypedCollection(init, ::Annotation))
    }

    fun modifiers(init: TypedCollectionBuilder<FunctionModifier>.() -> Unit) {
        modifiers.addAll(initTypedCollection(init))
    }

    fun typeParameters(init: TypeParametersBuilder.() -> Unit) {
        typeParameters.addAll(initBuilder(TypeParametersBuilder(), init))
    }

    fun receiverType(init: ReceiverTypeBuilder.() -> Unit) {
        receiverType.set(initBuilder(ReceiverTypeBuilder(), init))
    }

    fun parameters(init: FunctionParametersBuilder.() -> Unit) {
        parameters.addAll(initBuilder(FunctionParametersBuilder(), init))
    }

    fun returnType(type: () -> Type) {
        returnType.set(type())
    }

    fun typeConstraints(init: TypeConstraintsBuilder.() -> Unit) {
        typeConstraints.addAll(initBuilder(TypeConstraintsBuilder(), init))
    }

    override fun build(): FunctionDeclaration =
        FunctionDeclaration(
            annotations.toList(),
            modifiers.toList(),
            TypeParameters(typeParameters.toList()),
            receiverType.getOrDefault { EMPTY_RECEIVER_TYPE },
            name,
            FunctionParameters(parameters.toList()),
            returnType.getOrDefault { EmptyType },
            TypeConstraints(typeConstraints.toList())
        )

    class TypeParametersBuilder : TypedCollectionBuilder<TypeParameter>() {

        fun typeParameter(name: String, init: TypeParameterBuilder.() -> Unit) {
            +initBuilder(TypeParameterBuilder(name), init)
        }

        class TypeParameterBuilder(private val name: String) : Builder<TypeParameter> {
            private val annotations = mutableListOf<Annotation>()
            private val modifiers = mutableListOf<TypeParameterModifier>()
            private val type = AddedOnceMember<Type>()

            fun annotations(init: TypedCollectionBuilder<String>.() -> Unit) {
                annotations.addAll(initTypedCollection(init, ::Annotation))
            }

            fun modifiers(init: TypedCollectionBuilder<TypeParameterModifier>.() -> Unit) {
                modifiers.addAll(initTypedCollection(init))
            }

            fun type(type: () -> Type) {
                this.type.set(type())
            }

            override fun build(): TypeParameter =
                TypeParameter(
                    annotations.toList(),
                    modifiers.toList(),
                    name,
                    type.getOrDefault { TypeReference(Any::class) }
                )
        }
    }

    class ReceiverTypeBuilder : Builder<ReceiverType> {
        private val annotations = mutableListOf<Annotation>()
        private val suspendable = AddedOnceMember<Boolean>()
        private val receiverType = AddedOnceMember<ReceiverAcceptableType>()

        fun annotations(init: TypedCollectionBuilder<String>.() -> Unit) {
            annotations.addAll(initTypedCollection(init, ::Annotation))
        }

        fun suspendable(suspendable: () -> Boolean) {
            this.suspendable.set(suspendable())
        }

        fun receiverType(type: () -> ReceiverAcceptableType) {
            this.receiverType.set(type())
        }

        override fun build(): ReceiverType =
            ReceiverType(
                annotations.toList(),
                suspendable.getOrDefault { false },
                receiverType.getOrDefault { EmptyReceiverAcceptableType }
            )
    }

    class FunctionParametersBuilder : TypedCollectionBuilder<FunctionParameter>() {

        fun parameter(name: String, init: FunctionParameterBuilder.() -> Unit) {
            +initBuilder(FunctionParameterBuilder(name), init)
        }

        class FunctionParameterBuilder(private val name: String) : Builder<FunctionParameter> {
            private val annotations = mutableListOf<Annotation>()
            private val modifiers = mutableListOf<FunctionParameterModifier>()
            private val type = AddedOnceMember<Type>()
            private val expression = AddedOnceMember<Expression>()

            fun annotations(init: TypedCollectionBuilder<String>.() -> Unit) {
                annotations.addAll(initTypedCollection(init, ::Annotation))
            }

            fun modifiers(init: TypedCollectionBuilder<FunctionParameterModifier>.() -> Unit) {
                modifiers.addAll(initTypedCollection(init))
            }

            fun type(type: () -> Type) {
                this.type.set(type())
            }

            fun expression(expression: () -> String) {
                this.expression.set(Expression(expression()))
            }

            override fun build(): FunctionParameter =
                FunctionParameter(
                    annotations.toList(),
                    modifiers.toList(),
                    name,
                    type.getOrDefault { EmptyType },
                    expression.getOrDefault { EMPTY_EXPRESSION }
                )
        }
    }

    class TypeConstraintsBuilder : TypedCollectionBuilder<TypeConstraint>() {

        fun typeConstraint(name: String, init: TypeConstraintBuilder.() -> Unit) {
            +initBuilder(TypeConstraintBuilder(name), init)
        }

        class TypeConstraintBuilder(private val name: String) : Builder<TypeConstraint> {
            private val annotations = mutableListOf<Annotation>()
            private val type = AddedOnceMember<Type>()

            fun annotations(init: TypedCollectionBuilder<String>.() -> Unit) {
                annotations.addAll(initTypedCollection(init, ::Annotation))
            }

            fun type(type: () -> Type) {
                this.type.set(type())
            }

            override fun build(): TypeConstraint =
                TypeConstraint(
                    annotations.toList(),
                    name,
                    type.getOrDefault { EmptyType }
                )
        }
    }
}

sealed interface FunctionBodyBuilder : Builder<FunctionBody>

class FunctionBodyBlockBuilder(private val members: MemberCollector<FunctionMember> = MemberCollector()) :
    FunctionBodyBuilder,
    CommentsAwareBuilder by CommentsAwareBuilderDelegate(
        members::add,
    ) {

    override fun build(): FunctionBody = FunctionBodyBlock(members.getMembers())
}

class FunctionBodyExpressionBuilder(private val expression: String) : FunctionBodyBuilder {
    override fun build(): FunctionBody = FunctionBodyExpression(expression)
}

interface FunctionAwareBuilder {
    fun functionBlock(function: FunctionWithBodyBlockBuilder.() -> Unit)

    fun functionExpression(function: FunctionWithBodyExpressionBuilder.() -> Unit)
}

class FunctionBuilderDelegate(private val functionMemberAppender: (member: KotlinFunction) -> Unit) :
    FunctionAwareBuilder {

    override fun functionBlock(function: FunctionWithBodyBlockBuilder.() -> Unit) {
        functionMemberAppender.invoke(
            initBuilder(FunctionWithBodyBlockBuilder(), function)
        )
    }

    override fun functionExpression(function: FunctionWithBodyExpressionBuilder.() -> Unit) {
        functionMemberAppender.invoke(
            initBuilder(FunctionWithBodyExpressionBuilder(), function)
        )
    }
}
