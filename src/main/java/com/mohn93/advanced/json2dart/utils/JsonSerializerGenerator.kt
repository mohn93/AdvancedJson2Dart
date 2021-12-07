package com.mohn93.advanced.json2dart.utils

import com.mohn93.advanced.json2dart.ClassOptions


class JsonSerializerGenerator(classOptions: ClassOptions, fileName: String) : DartClassGenerator(classOptions, fileName) {

    override fun importString(): String {
        return """import 'package:json_annotation/json_annotation.dart'; """
    }

    override fun classHead(fileName: String): String {
        return partStr(fileName)
    }

    override fun classAnnotation(classOptions: ClassOptions): String {
        return "@JsonSerializable(${if (!classOptions.nullSafety) "nullable: ${classOptions.jsNullable}, " else ""}ignoreUnannotated: ${classOptions.jsIgnoreUnannotated})"
    }

    override fun fieldAnnotation(name: String): String {
        return "@JsonKey(name: '${name}')"
    }

    override fun afterFieldsString(className: String): String {
        val sb = java.lang.StringBuilder()
        sb.append(" ${factoryConstructorStr(className)}")
        sb.append("\n")
        sb.append(" ${toJsonStr(className)}")
        return sb.toString()
    }

    private fun factoryConstructorStr(className: String): String {
        return "  factory $className.fromJson(Map<String, dynamic> json) => _$${className}FromJson(json);\n"
    }

    private fun toJsonStr(className: String): String {
        return "  Map<String, dynamic> toJson() => _$${className}ToJson(this);\n"
    }

    private fun partStr(fileName: String): String {
        return "part '$fileName.g.dart'; "
    }

}