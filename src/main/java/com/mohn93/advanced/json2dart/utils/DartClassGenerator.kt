package com.mohn93.advanced.json2dart.utils

import com.mohn93.advanced.json2dart.ClassOptions
import com.mohn93.advanced.json2dart.CustomClassType
import com.mohn93.advanced.json2dart.ListClassType
import com.mohn93.advanced.json2dart.TypeDefinition

abstract class DartClassGenerator(private val classOptions: ClassOptions, private val fileName: String) {

    protected abstract fun importString(): String
    protected abstract fun classHead(fileName: String): String

    protected abstract fun classAnnotation(classOptions: ClassOptions): String
    protected abstract fun fieldAnnotation(name: String): String
    protected abstract fun afterFieldsString(className: String): String

    fun generateCode(dartClass: CustomClassType): String {
        val sb = StringBuilder()

        sb.append(importString())
        sb.append("\n")
        sb.append("\n")
        sb.append(classHead(fileName))
        sb.append("\n")
        sb.append("\n")
        sb.append(class2Code(dartClass))

        return sb.toString()
    }

    private fun dartClassStartStr(className: String): String {
        val sb = StringBuilder()
        sb.append(classAnnotation(classOptions))
        sb.append("\n")
        sb.append("class $className {")
        sb.append("\n")
        return sb.toString()
    }

    private fun fieldsStr(fields: List<TypeDefinition>): String {
        val sb = StringBuilder()
        fields.forEach {
            sb.append("  ${fieldAnnotation(it.name)}")
            sb.append("\n")
            if (classOptions.isFinal)
                sb.append("  final")
            sb.append("  ${it.typeName}${if (classOptions.jsNullable && classOptions.nullSafety) "?" else ""} ${it.name.snakeCaseToPascalCase()};")
            sb.append("\n")
        }
        return sb.toString()
    }


    private fun constructorStr(dartClass: CustomClassType): String {
        val sb = StringBuilder()
        sb.append("  ${dartClass.typeName}(")

        val constructorStr = StringBuilder()
        if (dartClass.fieldList.isNotEmpty()) {
            constructorStr.append("{")
            dartClass.fieldList.forEach {

                if (dartClass.classOptions.nullSafety && !dartClass.classOptions.jsNullable) {
                    constructorStr.append("required ")
                }

                constructorStr.append("this.${it.name.snakeCaseToPascalCase()}")
                constructorStr.append(", ")
            }
            constructorStr.setLength(constructorStr.length - 2)
            constructorStr.append("}")
        }
        sb.append(constructorStr)
        sb.append(")")
        sb.append(";")
        sb.append("\n")
        return sb.toString()
    }


    private fun dartClassEndStr(): String {
        return "}\n"
    }

    private fun class2Code(dartClass: CustomClassType): String {
        val needGenerateCode = mutableListOf<CustomClassType>()

        val sb = StringBuilder()
        sb.append(dartClassStartStr(dartClass.typeName))
        sb.append(fieldsStr(dartClass.fieldList))
        sb.append("\n")
        sb.append(constructorStr(dartClass))
        sb.append("\n")
        sb.append(afterFieldsString(dartClass.typeName))
        sb.append(dartClassEndStr())
        sb.append("\n")
        dartClass.fieldList.forEach {
            if (it is CustomClassType) {
                needGenerateCode.add(it)
            } else if (it is ListClassType && it.genericsType is CustomClassType) {
                needGenerateCode.add(it.genericsType)
            }
        }
        needGenerateCode.forEach {
            sb.append(class2Code(it))
        }
        return sb.toString();
    }

}
