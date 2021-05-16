package com.mohn93.advanced.json2dart.ui

import com.mohn93.advanced.json2dart.ClassOptions
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.TabbedPaneImpl
import com.intellij.util.ui.JBEmptyBorder
import java.awt.BorderLayout
import java.awt.Checkbox
import javax.swing.*

/**
 * Json input Dialog
 */
open class AdvancedOptionsDialog(
    project: Project?,
    var classOptions: ClassOptions,
    canBeParent: Boolean,
    val doOKAction: (classOptions: ClassOptions) -> Unit
) : DialogWrapper(project, canBeParent) {

    private lateinit var finalCheckBox: JCheckBox
    private lateinit var nullableCheckBox: JCheckBox
    private lateinit var ignoreUnannotatedCheckBox: JCheckBox
    private lateinit var withCopyCheckBox: JCheckBox
    private lateinit var withEqualityCheckBox: JCheckBox
    private lateinit var nullSafetyCheckBox: JCheckBox

    init {
        init()
        setOKButtonText("OK")
        title = "Advanced Options";
    }

    private fun setViewValues() {
        finalCheckBox.isSelected = classOptions.isFinal
        nullableCheckBox.isSelected = classOptions.jsNullable
        ignoreUnannotatedCheckBox.isSelected = classOptions.jsIgnoreUnannotated
        withEqualityCheckBox.isSelected = classOptions.withEquality
        withCopyCheckBox.isSelected = classOptions.withCopy
        nullSafetyCheckBox.isSelected = classOptions.nullSafety
    }

    override fun createCenterPanel(): JComponent? {
        val messagePanel = JPanel(BorderLayout())
        messagePanel.preferredSize
        val taps = TabbedPaneImpl(SwingConstants.TOP);

        finalCheckBox = createCheckbox("Final");
        nullableCheckBox = createCheckbox("Nullable");
        ignoreUnannotatedCheckBox = createCheckbox("Ignore Unannotated Properties");
        withCopyCheckBox = createCheckbox("With Copy");
        withEqualityCheckBox = createCheckbox("With Equality");
        nullSafetyCheckBox = createCheckbox("Support null safety");

        val propertyContainer = createPanel();

        propertyContainer.add(finalCheckBox)
        propertyContainer.add(nullableCheckBox)
        propertyContainer.add(ignoreUnannotatedCheckBox)

        val generatePanel = createPanel()

        generatePanel.add(withCopyCheckBox)
        generatePanel.add(withEqualityCheckBox)

        val optionsPanel = createPanel()
        optionsPanel.add(nullSafetyCheckBox);

        taps.border = JBEmptyBorder(16, 16, 5, 16)
        taps.addTab("Property", propertyContainer)
//        taps.addTab("Generate", generatePanel)
        taps.addTab("Options", optionsPanel)

        messagePanel.add(taps, BorderLayout.SOUTH)
        setViewValues()
        return messagePanel
    }

    private fun createCheckbox(title: String): JCheckBox {
        val checkbox = JCheckBox(title, false);

        checkbox.horizontalAlignment = SwingConstants.CENTER;
        return checkbox;
    }

    private fun createPanel(): JPanel {
        val panel = JPanel()
        panel.border = JBEmptyBorder(16, 0, 0, 0)
        val boxLayout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.layout = boxLayout

        return panel
    }

    override fun doOKAction() {
        doOKAction(
            ClassOptions(
                isFinal = finalCheckBox.isSelected,
                jsNullable = nullableCheckBox.isSelected,
                jsIgnoreUnannotated = ignoreUnannotatedCheckBox.isSelected,
                withCopy = withCopyCheckBox.isSelected,
                withEquality = withEqualityCheckBox.isSelected,
                nullSafety = nullSafetyCheckBox.isSelected,
            )
        )
        super.doOKAction()
    }
}