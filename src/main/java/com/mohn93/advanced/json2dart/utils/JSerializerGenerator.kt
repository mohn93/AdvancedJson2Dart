package com.mohn93.advanced.json2dart.utils

import com.mohn93.advanced.json2dart.ClassOptions

class JSerializerGenerator(classOptions: ClassOptions, fileName: String) : DartClassGenerator(classOptions, fileName) {

    override fun importString(): String {
        return """import 'package:json_annotation/json_annotation.dart'; """
    }

    override fun classHead(fileName: String): String {
        return ""
    }

    override fun classAnnotation(classOptions: ClassOptions): String {
        return "@JSerializable()"
    }

    override fun fieldAnnotation(name: String): String {
        return "@JKey(name: '${name}')"
    }

    override fun afterFieldsString(className: String): String {
        return  ""
    }

    private fun partStr(fileName: String): String {
        return "part '$fileName.g.dart'; "
    }

}