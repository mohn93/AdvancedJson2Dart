package com.mohn93.advanced.json2dart.action

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.mohn93.advanced.json2dart.ClassOptions
import com.mohn93.advanced.json2dart.DartFileGenerator
import com.mohn93.advanced.json2dart.map2CustomClassDefinition
import com.mohn93.advanced.json2dart.parseInputJson
import com.mohn93.advanced.json2dart.repos.StorageRepo
import com.mohn93.advanced.json2dart.ui.JsonInputDialog
import com.mohn93.advanced.json2dart.utils.pascalCaseToSnakeCase
import com.mohn93.advanced.json2dart.utils.toLowerCaseFirstOne

class Json2DartAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val dataContext = event.dataContext
        val module = LangDataKeys.MODULE.getData(dataContext) ?: return
        //Get the folder selected by the right mouse button
        val directory = when (val navigationTable = LangDataKeys.NAVIGATABLE.getData(dataContext)) {
            is PsiDirectory -> navigationTable
            is PsiFile -> navigationTable.containingDirectory
            else -> {
                val root = ModuleRootManager.getInstance(module)
                root.sourceRoots
                    .asSequence()
                    .mapNotNull {
                        PsiManager.getInstance(project).findDirectory(it)
                    }.firstOrNull()
            }
        } ?: return

        JsonInputDialog(project) {
            //Remove the file suffix
            var fileName = it.inputClassName.split(".")[0]
            fileName = fileName.pascalCaseToSnakeCase().toLowerCaseFirstOne()
            if (containsFile(directory, fileName)) {
                Messages.showInfoMessage(project, "The $fileName.dart already exists", "Info")
                return@JsonInputDialog
            }
            val map = parseInputJson(it.inputJson)
            val classOptions = ClassOptions(
                isFinal = it.isFinal,
                jsNullable = it.nullable,
                jsIgnoreUnannotated = it.ignoreUnannotated,
                withEquality = it.withEquality,
                withCopy = it.withCopy,
                nullSafety = it.nullSafety
            )

            StorageRepo.saveOptions(options = classOptions)
            val dartClassDefinition = map2CustomClassDefinition(
                fileName,
                map,
                classOptions = classOptions
            )

            val generator = DartFileGenerator(project, directory,fileName)
            val codeContent = generator.generateCode(dartClassDefinition)
            generator.generateDarFile(codeContent)

        }.show()
    }


    private fun containsFile(directory: PsiDirectory, fileName: String): Boolean {
        return directory.files.filter { it.name.endsWith(".dart") }
            .firstOrNull { fileName.equals(it.name.split(".dart")[0], true) } != null
    }
}