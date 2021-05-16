package com.mohn93.advanced.json2dart


class CollectInfo(val inputClassName: String,
                  val inputJson: String,
                  val isFinal: Boolean,
                  val nullable: Boolean,
                  val ignoreUnannotated: Boolean,
                  val withCopy: Boolean,
                  val withEquality: Boolean,
                  val nullSafety: Boolean,
)