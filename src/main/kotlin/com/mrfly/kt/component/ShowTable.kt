package com.mrfly.kt.component

import com.mrfly.java.DatabaseUtil
import com.mrfly.kt.bean.ColumnInfo
import com.mrfly.kt.bean.ConnInfo
import com.mrfly.kt.bean.TableInfo
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import tornadofx.*

class ShowTable: BorderPane() {
    var fromTable: TableView<ColumnInfo> by singleAssign()
    var fromTableName = ""
    var selectTable: TableView<TableInfo>? = null
    // Some fake data for our table
    val fromColumns = mutableListOf<ColumnInfo>().asObservable()
    val fromConnInfo = ConnInfo(
        "carServDev",
        "jdbc:mysql://192.168.1.229:3306/zoan_admin_dev", "cardev", "Car_dev#68"
    )
    val fromDatabaseUtil = DatabaseUtil(fromConnInfo)
    var fromTableNameList = mutableListOf<TableInfo>().asObservable()
    init {
        vbox {
            hbox {
                label(fromTableName)
                button("选择表").action {
                    /*dialog("源数据库: "+fromConnInfo.name) {
                        tableview(fromTableNameList) {
                            selectTable = this
                            column("Name", TableInfo::name)
                        }
                        buttonbar {
                            button("确定") {
                                action {
                                    val selectedItem = selectTable?.selectedItem
                                    if (selectedItem != null) {
                                        fromTableName = selectedItem.name
                                        if(!fromColumns.isEmpty()){
                                            fromColumns.clear()
                                        }
                                        fromColumns.addAll(fromDatabaseUtil.getColumnInfoList(fromTableName))
                                        this@dialog.close()
                                    }
                                }
                            }
                            button("取消") {
                                action {
                                    this@dialog.close()
                                }
                            }
                        }
                    }*/
                }
            }
            tableview(fromColumns) {
                fromTable = this
                column("选择", ColumnInfo::selected){
                    useCheckbox(true)
                }
                column("Name", ColumnInfo::name){
                    style{

                    }
                }
                column("Type", ColumnInfo::type)

                // Update the person inside the view model on selection change
                /*model.rebindOnChange(this) { selectedPerson ->
                    person = selectedPerson ?: Person()
                }*/
            }
        }
    }
}