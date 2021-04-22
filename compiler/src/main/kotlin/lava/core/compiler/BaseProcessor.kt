package lava.core.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSTypeReference
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec

/**
 * Created by svc on 2021/4/15
 * https://square.github.io/kotlinpoet/
 */
internal abstract class BaseProcessor : SymbolProcessor {
    protected lateinit var codeGenerator: CodeGenerator
    protected lateinit var logger: KSPLogger

    protected val KSDeclaration.name: String
        get() = simpleName.asString()

    protected val KSDeclaration.fullName: String
        get() = qualifiedName?.asString().orEmpty()

    protected val KSTypeReference.fullName: String
        get() = resolve().declaration.fullName

    protected fun KSTypeReference.asClassName(): ClassName {
        val declaration = this.resolve().declaration
        return ClassName(declaration.packageName.asString(), declaration.simpleName.asString())
    }


    override fun init(options: Map<String, String>, kotlinVersion: KotlinVersion, codeGenerator: CodeGenerator, logger: KSPLogger) {
        this.codeGenerator = codeGenerator
        this.logger = logger
    }

    protected fun log(log: String) {
        logger.warn(log)
    }

    protected fun export(fileSpec: FileSpec, ksFile: KSFile, creatorPkg: String, builderName: String) {
        val output = codeGenerator.createNewFile(Dependencies(true, ksFile), creatorPkg, builderName)
        output.write(fileSpec.toString().toByteArray())
    }
}