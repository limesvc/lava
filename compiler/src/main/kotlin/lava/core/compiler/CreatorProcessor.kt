package lava.core.compiler

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*

/**
 * Created by svc on 2021/4/13
 */
internal class CreatorProcessor(environment: SymbolProcessorEnvironment) : BaseProcessor(environment) {
    companion object {
        const val CREATOR_CLASS = "lava.core.compiler.Creator"
        const val PARAMS_CLASS = "lava.core.compiler.Var"
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(CREATOR_CLASS)
        symbols
            .filterIsInstance<KSClassDeclaration>()
            .forEach { ksClass ->
                val requireList = mutableListOf<KSPropertyDeclaration>()
                val optionalList = mutableListOf<KSPropertyDeclaration>()
                ksClass.getAllProperties().forEach { property ->
                    property.annotations.firstOrNull {
                        val name = it.annotationType.resolve().declaration.qualifiedName?.asString()
                        name == PARAMS_CLASS
                    }?.also {
                        if (it.arguments[0].value == false) {
                            optionalList.add(property)
                        } else {
                            requireList.add(property)
                        }
                    }
                }

                val packageName = ksClass.packageName.asString()
                val className = ksClass.simpleName.asString()
                val builderName = className.plus("Creator")

                val fileSpec = FileSpec.builder(packageName, builderName)
                    .addImport(ClassName(packageName, className), "")
                    .addImport(ClassName("lava.core", "appContext"), "")
                    .addImport(Cast, "")
                    .addFunction(buildConstructor(className, packageName, requireList, optionalList))
                    .addFunction(buildActivityInject(ClassName(packageName, className), requireList, optionalList))
                    .build()

                export(fileSpec, ksClass.containingFile!!, packageName, builderName)
            }
        return emptyList()
    }

    private fun buildConstructor(
        className: String, packageName: String,
        requireList: List<KSPropertyDeclaration>,
        optionList: List<KSPropertyDeclaration>
    ): FunSpec {
        val funName = className[0].toLowerCase().plus(className.substring(1, className.length))
        val construct = FunSpec.builder(funName)
            .addAnnotation(
                AnnotationSpec.builder(Source::class)
                    .addMember("%T::class", ClassName(packageName, className))
                    .build()
            )
            .returns(Intent)
            .addStatement("val intent = %T()", Intent)
            .addStatement("intent.setClassName(appContext.packageName, %S)", packageName.plus(".").plus(className))

        requireList.forEach {
            construct.addParameter(it.name, it.type.asClassName())
            construct.addStatement("intent.putExtra(%S, %N)", it.name, it.name)
        }

        optionList.forEach {
            construct.addParameter(ParameterSpec.builder(it.name, it.type.asClassName().copy(true))
                .defaultValue("null")
                .build())
            construct.beginControlFlow("if (%N != null)", it.name)
                .addStatement("intent.putExtra(%S, %N)", it.name, it.name)
                .endControlFlow()
        }

        return construct.addStatement("return intent").build()
    }

    private fun buildActivityInject(
        className: ClassName,
        requireList: List<KSPropertyDeclaration>,
        optionList: List<KSPropertyDeclaration>
    ): FunSpec {
        val builder = FunSpec.builder("inject")
        builder.addParameter("host", className)
            .addStatement("val extras = host.intent.extras")
        buildInject(builder, requireList, optionList)
        return builder.build()
    }

    private fun buildInject(
        builder: FunSpec.Builder,
        requireList: List<KSPropertyDeclaration>,
        optionList: List<KSPropertyDeclaration>
    ) {
        requireList.forEach {
            builder.addStatement("extras?.get(%S).am<%T> { host.%N = it }", it.name, it.type.asClassName(), it.name)
        }
        optionList.forEach {
            builder.addStatement("extras?.get(%S).am<%T> { host.%N = it }", it.name, it.type.asClassName(), it.name)
        }
    }
}