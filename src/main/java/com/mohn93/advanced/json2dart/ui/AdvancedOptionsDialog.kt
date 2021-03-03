package com.mohn93.advanced.json2dart.ui
import com.mohn93.advanced.json2dart.ClassOptions
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.JBEmptyBorder
import java.awt.BorderLayout
import javax.swing.*

/**
 * Json input Dialog
 */
open class AdvancedOptionsDialog(
    project: Project?,
    var classOptions: ClassOptions,
    canBeParent:Boolean,
    val doOKAction: (classOptions: ClassOptions) -> Unit
) : DialogWrapper(project, canBeParent) {

    private lateinit var finalCheckBox: JCheckBox
    private lateinit var nullableCheckBox: JCheckBox
    private lateinit var ignoreUnannotatedCheckBox: JCheckBox

    init {
        init()
        setOKButtonText("OK")
        title = "Advanced Options";
    }

    private fun setViewValues(){
        finalCheckBox.isSelected = classOptions.isFinal
        nullableCheckBox.isSelected = classOptions.jsNullable
        ignoreUnannotatedCheckBox.isSelected = classOptions.jsIgnoreUnannotated
    }

    override fun createCenterPanel(): JComponent? {
        val messagePanel = JPanel(BorderLayout())
        messagePanel.preferredSize
        finalCheckBox = JCheckBox("Final", false);
        nullableCheckBox = JCheckBox("Nullable", false);
        ignoreUnannotatedCheckBox = JCheckBox("Ignore Unannotated Properties", false);

        finalCheckBox.horizontalAlignment = SwingConstants.CENTER;
        nullableCheckBox.horizontalAlignment = SwingConstants.CENTER;
        ignoreUnannotatedCheckBox.horizontalAlignment = SwingConstants.CENTER;

        val settingContainer = JPanel()
        settingContainer.border = JBEmptyBorder(0, 0, 5, 5)

        val boxLayout = BoxLayout(settingContainer, BoxLayout.PAGE_AXIS)

        settingContainer.layout = boxLayout
        settingContainer.add(finalCheckBox)
        settingContainer.add(nullableCheckBox)
        settingContainer.add(ignoreUnannotatedCheckBox)
        settingContainer.add(Box.createHorizontalGlue())
        settingContainer.add(Box.createHorizontalStrut(16))

        messagePanel.add(settingContainer, BorderLayout.SOUTH)
        setViewValues()
        return messagePanel
    }

    override fun doOKAction() {
        doOKAction(
            ClassOptions(
                isFinal = finalCheckBox.isSelected,
                jsNullable = nullableCheckBox.isSelected,
                jsIgnoreUnannotated = ignoreUnannotatedCheckBox.isSelected,
            )
        )
        super.doOKAction()
    }
}