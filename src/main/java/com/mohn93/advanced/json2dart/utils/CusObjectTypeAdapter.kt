package com.mohn93.advanced.json2dart.utils

import com.google.gson.TypeAdapter
import kotlin.Throws
import java.io.IOException
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.IllegalStateException
import java.util.ArrayList

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
class CusObjectTypeAdapter : TypeAdapter<Any?>() {
    @Throws(IOException::class)
    override fun read(reader: JsonReader): Any? {
        return when (reader.peek()) {
            JsonToken.BEGIN_ARRAY -> {
                val list: MutableList<Any?> = ArrayList()
                reader.beginArray()
                while (reader.hasNext()) {
                    list.add(read(reader))
                }
                reader.endArray()
                list
            }
            JsonToken.BEGIN_OBJECT -> {
                val map: MutableMap<String, Any?> =
                    LinkedTreeMap()
                reader.beginObject()
                while (reader.hasNext()) {
                    map[reader.nextName()] = read(reader)
                }
                reader.endObject()
                map
            }
            JsonToken.STRING -> reader.nextString()
            JsonToken.NUMBER -> {
                val numberStr = reader.nextString()
                if (numberStr.contains(".") || numberStr.contains("e")
                    || numberStr.contains("E")
                ) {
                    return numberStr.toDouble()
                }
                if (numberStr.toLong() <= Int.MAX_VALUE) {
                    numberStr.toInt()
                } else numberStr.toLong()
            }
            JsonToken.BOOLEAN -> reader.nextBoolean()
            JsonToken.NULL -> {
                reader.nextNull()
                null
            }
            else -> throw IllegalStateException()
        }
    }

    override fun write(out: JsonWriter, value: Any?) {}
}