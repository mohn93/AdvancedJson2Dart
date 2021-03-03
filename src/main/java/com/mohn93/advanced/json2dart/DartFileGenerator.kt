package com.mohn93.advanced.json2dart

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.mohn93.advanced.json2dart.utils.DartClassUtils
import com.jetbrains.lang.dart.DartFileType

class DartFileGenerator(private val project: Project, private val directory: PsiDirectory, private val fileName: String) {

    fun generateDarFile( classCodeContent: String) {
        val psiFileFactory = PsiFileFactory.getInstance(project)
        CommandProcessor.getInstance().executeCommand(directory.project, {
            ApplicationManager.getApplication().runWriteAction {
                val file =
                        psiFileFactory.createFileFromText("$fileName.dart", DartFileType.INSTANCE, classCodeContent)
                directory.add(file)
            }
        }, "JSON to Dart Class", "JSON to Dart Class")
    }

    fun class2Code(dartClass: CustomClassType): String {
        val needGenerateCode = mutableListOf<CustomClassType>()

        val sb = StringBuilder()
        sb.append(DartClassUtils.dartClassStartStr(dartClass.typeName,dartClass.classOptions))
        sb.append(DartClassUtils.fieldsStr(dartClass.fieldList, dartClass.classOptions.isFinal))
        sb.append("\n")
        sb.append(DartClassUtils.constructorStr(dartClass))
        sb.append("\n")
        sb.append(DartClassUtils.factoryConstructorStr(dartClass.typeName))
        sb.append("\n")
        sb.append(DartClassUtils.toJsonStr(dartClass.typeName))
        sb.append(DartClassUtils.dartClassEndStr())
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



        return DartClassUtils.insertClassHead(fileName, sb.toString())
    }

}