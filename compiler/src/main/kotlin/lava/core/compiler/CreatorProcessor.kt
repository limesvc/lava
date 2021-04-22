package lava.core.compiler

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*

/**
 * Created by svc on 2021/4/13
 */

internal class CreatorProcessor : BaseProcessor() {
    companion object {
        const val CREATOR_CLASS = "lava.core.compiler.Creator"
        const val PARAMS_CLASS = "lava.core.compiler.Var"
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(CREATOR_CLASS)
        val ret = symbols.filter { it.validate() }
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

                val creatorPkg = packageName.plus(".creator")
                val fileSpec = FileSpec.builder(creatorPkg, builderName)
                    .addImport(ClassName(packageName, className), "")
                    .addImport(Cast, "")
                    .addType(buildInterfaceCreator(className, creatorPkg, optionalList))
                    .addFunction(buildConstructor(className, creatorPkg, requireList, optionalList))
                    .addFunction(buildActivityInject(className, requireList, optionalList))
                    .build()

                export(fileSpec, ksClass.containingFile!!, creatorPkg, builderName)
            }
        return ret
    }

    private fun buildInterfaceCreator(name: String, packageName: String, paramsList: List<KSPropertyDeclaration>): TypeSpec {
        val interfaceName = name.filter { it.isUpperCase() }.plus("Creator")
        val builder = TypeSpec.interfaceBuilder(interfaceName)
        paramsList.forEach {
            builder.addFunction(
                FunSpec.builder(it.name)
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameter(it.name, it.type.asClassName())
                    .returns(ClassName(packageName, interfaceName))
                    .build()
            )
        }
        builder.addFunction(
            FunSpec.builder("start")
                .addModifiers(KModifier.ABSTRACT)
                .addParameter("context", Context)
                .build()
        )
        return builder.build()
    }

    private fun buildCreatorImpl(name: String, packageName: String, paramsList: List<KSPropertyDeclaration>): TypeSpec {
        val interfaceName = name.filter { it.isUpperCase() }.plus("Creator")
        val builder = TypeSpec.anonymousClassBuilder().addSuperinterface(ClassName(packageName, interfaceName))
        paramsList.forEach {
            builder.addFunction(
                FunSpec.builder(it.name)
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter(it.name, it.type.asClassName())
                    .addStatement("intent.putExtra(%S, %N)", it.name, it.name)
                    .addStatement("return this")
                    .returns(ClassName(packageName, interfaceName))
                    .build()
            )
        }

        builder.addFunction(
            FunSpec.builder("start")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("context", Context)
                .addStatement("intent.setClass(context, %T::class.java)", ClassName(packageName, name))
                .beginControlFlow("if (context !is %T) {", Activity)
                .addStatement("intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)")
                .endControlFlow()
                .addStatement("context.startActivity(intent)")
                .build()
        )
        return builder.build()
    }

    private fun buildConstructor(
        className: String, packageName: String,
        requireList: List<KSPropertyDeclaration>,
        optionList: List<KSPropertyDeclaration>
    ): FunSpec {
        val interfaceName = className.filter { it.isUpperCase() }.plus("Creator")
        val construct = FunSpec.builder(className)
            .addAnnotation(
                AnnotationSpec.builder(Source::class)
                    .addMember("%T::class", ClassName("", className))
                    .build()
            )
            .returns(ClassName(packageName, interfaceName))
            .addStatement("val intent = %T()", Intent)

        requireList.forEach {
            construct.addParameter(it.name, it.type.asClassName())
            construct.addStatement("intent.putExtra(%S, %N)", it.name, it.name)
        }

        return construct.addStatement("val creator = %L", buildCreatorImpl(className, packageName, optionList))
            .addStatement("return creator")
            .build()
    }

    private fun buildActivityInject(
        className: String,
        requireList: List<KSPropertyDeclaration>,
        optionList: List<KSPropertyDeclaration>
    ): FunSpec {
        val activityClass = ClassName("", className)
        val builder = FunSpec.builder("inject")
        builder.addParameter("host", activityClass)
            .addStatement("val extras = host.intent.extras")
        buildInject(builder, requireList, optionList)
        return builder.build()
    }

    private fun buildFragInject(
        className: String,
        requireList: List<KSPropertyDeclaration>,
        optionList: List<KSPropertyDeclaration>
    ): FunSpec {
        val fragClass = ClassName("", className)
        val builder = FunSpec.builder("inject")
        builder.addParameter("host", fragClass)
            .addStatement("val extras = host.arguments")
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