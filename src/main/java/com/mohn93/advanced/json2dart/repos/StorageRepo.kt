package com.mohn93.advanced.json2dart.repos

import com.intellij.ide.util.PropertiesComponent
import com.mohn93.advanced.json2dart.ClassOptions

class StorageRepo {
    companion object {
        fun getOptions(): ClassOptions {
            return ClassOptions(
                isFinal = getBoolean("isFinal"),
                withCopy = getBoolean("withCopy"),
                withEquality = getBoolean("withEquality"),
                jsNullable = getBoolean("jsNullable"),
                jsIgnoreUnannotated = getBoolean("jsIgnoreUnannotated"),
            )
        }

        fun saveOptions(options: ClassOptions) {
            setBoolean("isFinal", options.isFinal);
            setBoolean("withCopy", options.withCopy);
            setBoolean("withEquality", options.withEquality);
            setBoolean("jsNullable", options.jsNullable);
            setBoolean("jsIgnoreUnannotated", options.jsIgnoreUnannotated);

        }

        private fun getBoolean(name: String): Boolean {
            return PropertiesComponent.getInstance().getBoolean(name, false)
        }

        private fun setBoolean(name: String, value: Boolean) {
            return PropertiesComponent.getInstance().setValue(name, value)
        }
    }

}