package com.mohn93.advanced.json2dart

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.jetbrains.lang.dart.DartFileType

class DartFileGenerator(
    private val project: Project,
    private val directory: PsiDirectory,
    private val fileName: String
) {

    fun generateDarFile(classCodeContent: String) {
        val psiFileFactory = PsiFileFactory.getInstance(project)
        CommandProcessor.getInstance().executeCommand(directory.project, {
            ApplicationManager.getApplication().runWriteAction {
                val file =
                    psiFileFactory.createFileFromText("$fileName.dart", DartFileType.INSTANCE, classCodeContent)
                directory.add(file)
            }
        }, "JSON to Dart Class", "JSON to Dart Class")
    }


}