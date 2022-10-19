package com.mohn93.advanced.json2dart.ui

import com.mohn93.advanced.json2dart.ClassOptions
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.TabbedPaneImpl
import com.intellij.util.ui.JBEmptyBorder
import com.mohn93.advanced.json2dart.AnnotationOption
import java.awt.BorderLayout
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
    private lateinit var constructorAnnotationRadioButton: JRadioButton
    private lateinit var memberAnnotationRadioButton: JRadioButton
    private lateinit var jSerializerRadioButton: JRadioButton
    private lateinit var jsonSerializerRadioButton: JRadioButton
    private lateinit var radioGroup: ButtonGroup
    private lateinit var radioGroup2: ButtonGroup

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
        jsonSerializerRadioButton.isSelected = classOptions.annotationOption == AnnotationOption.JsonSerializer
        jSerializerRadioButton.isSelected = classOptions.annotationOption == AnnotationOption.JSerializer
        constructorAnnotationRadioButton.isSelected = classOptions.constructorAnnotation
        memberAnnotationRadioButton.isSelected = !classOptions.constructorAnnotation


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

        jSerializerRadioButton = createRadioButton("JSerializer");
        jsonSerializerRadioButton = createRadioButton("JsonSerializer");
        constructorAnnotationRadioButton = createRadioButton("At Constructor");
        memberAnnotationRadioButton = createRadioButton("At Members");

        radioGroup = ButtonGroup();

        radioGroup.add(jsonSerializerRadioButton);
        radioGroup.add(jSerializerRadioButton)

        val propertyContainer = createPanel();

        propertyContainer.add(finalCheckBox)
        propertyContainer.add(nullableCheckBox)
        propertyContainer.add(ignoreUnannotatedCheckBox)

        val generatePanel = createPanel()

        generatePanel.add(withCopyCheckBox)
        generatePanel.add(withEqualityCheckBox)

        val optionsPanel = createPanel()
        optionsPanel.add(nullSafetyCheckBox);

        val annotationPanel = createPanel();
        annotationPanel.add(jsonSerializerRadioButton);
        annotationPanel.add(jSerializerRadioButton);

        radioGroup2 = ButtonGroup();

        radioGroup2.add(constructorAnnotationRadioButton);
        radioGroup2.add(memberAnnotationRadioButton)
        val annotationPlacementPanel = createPanel();
        annotationPlacementPanel.add(constructorAnnotationRadioButton);
        annotationPlacementPanel.add(memberAnnotationRadioButton);


        taps.border = JBEmptyBorder(16, 16, 5, 16)
        taps.addTab("Property", propertyContainer)

        taps.addTab("Options", optionsPanel)

        taps.addTab("Annotation", annotationPanel)

        taps.addTab("Annotation Placement", annotationPlacementPanel)

        messagePanel.add(taps, BorderLayout.SOUTH)
        setViewValues()
        return messagePanel
    }

    private fun createCheckbox(title: String): JCheckBox {
        val checkbox = JCheckBox(title, false);

        checkbox.horizontalAlignment = SwingConstants.CENTER;
        return checkbox;
    }

    private fun createRadioButton(title: String): JRadioButton {
        val button = JRadioButton(title, false);
        button.horizontalAlignment = SwingConstants.CENTER;
        return button;
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
                        constructorAnnotation = constructorAnnotationRadioButton.isSelected,
                        annotationOption = if (jsonSerializerRadioButton.isSelected) AnnotationOption.JsonSerializer else AnnotationOption.JSerializer,
                )
        )
        super.doOKAction()

    }
}