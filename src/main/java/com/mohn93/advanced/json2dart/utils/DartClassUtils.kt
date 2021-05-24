package com.mohn93.advanced.json2dart.utils

import com.mohn93.advanced.json2dart.ClassOptions
import com.mohn93.advanced.json2dart.CustomClassType
import com.mohn93.advanced.json2dart.TypeDefinition


object DartClassUtils {
    private const val IMPORT_CONSTANT = """import 'package:json_annotation/json_annotation.dart'; """

    fun insertClassHead(fileName: String, originalContent: String): String {
        val sb = StringBuilder()
        sb.append(IMPORT_CONSTANT)
        sb.append("\n")
        sb.append("\n")
        sb.append(partStr(fileName))
        sb.append("\n")
        sb.append("\n")
        sb.append(originalContent)
        return sb.toString()
    }

    private fun partStr(fileName: String): String {
        return "part '$fileName.g.dart'; "
    }

    fun dartClassStartStr(className: String, classOptions: ClassOptions): String {
        val sb = StringBuilder()
        sb.append("@JsonSerializable(${if (!classOptions.nullSafety) "nullable: ${classOptions.jsNullable}, " else ""}ignoreUnannotated: ${classOptions.jsIgnoreUnannotated})")
        sb.append("\n")
        sb.append("class $className {")
        sb.append("\n")
        return sb.toString()
    }

    fun fieldsStr(fields: List<TypeDefinition>, classOptions: ClassOptions): String {
        val sb = StringBuilder()
        fields.forEach {
            sb.append("  @JsonKey(name: '${it.name}')")
            sb.append("\n")
            if (classOptions.isFinal)
                sb.append("  final")
            sb.append("  ${it.typeName}${if (classOptions.jsNullable && classOptions.nullSafety) "?" else ""} ${it.name.snakeCaseToPascalCase()};")
            sb.append("\n")
        }
        return sb.toString()
    }


    fun constructorStr(dartClass: CustomClassType): String {
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


    fun copyWith(dartClass: CustomClassType): String {
        val sb = StringBuilder()
        sb.append("  ${dartClass.typeName}(")

        val constructorStr = StringBuilder()
        if (dartClass.fieldList.isNotEmpty()) {
            constructorStr.append("{")
            dartClass.fieldList.forEach {
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

    fun factoryConstructorStr(className: String): String {
        return "  factory $className.fromJson(Map<String, dynamic> json) => _$${className}FromJson(json);\n"
    }

    fun toJsonStr(className: String): String {
        return "  Map<String, dynamic> toJson() => _$${className}ToJson(this);\n"
    }

    fun dartClassEndStr(): String {
        return "}\n"
    }

}