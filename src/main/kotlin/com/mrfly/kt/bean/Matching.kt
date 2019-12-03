package com.mrfly.kt.bean

class Matching(fromColumn: ColumnInfo, toColumn: ColumnInfo) {
    var key: Boolean? = false
    var trans: Boolean? = false
    var fromColumn = fromColumn.name
    var toColumn = toColumn.name
}