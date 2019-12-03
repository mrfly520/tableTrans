package com.mrfly.kt

import com.mrfly.java.DatabaseUtil
import com.mrfly.kt.bean.ColumnInfo
import com.mrfly.kt.bean.ConnInfo
import com.mrfly.kt.bean.Matching
import com.mrfly.kt.bean.TableInfo
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import tornadofx.*



class Person(name: String? = null, title: String? = null) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val titleProperty = SimpleStringProperty(this, "title", title)
    var title by titleProperty
}

/*class PersonModel(var person: Person) : ViewModel() {
    val name = bind { person.nameProperty }
    val title = bind { person.titleProperty }
}*/
/*
class Conn(name:String?=null,url:String?=null,user:String?=null,pwd:String?=null){
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty
    val urlProperty = SimpleStringProperty(this, "url", url)
    var url by urlProperty
    val userProperty = SimpleStringProperty(this, "user", user)
    var user by userProperty
    val pwdProperty = SimpleStringProperty(this, "pwd", pwd)
    var pwd by pwdProperty
}
*/


fun main(args: Array<String>) {
    //launch<MyApp>(args)
    launch<EditApp>(args)
}

class EditApp : App(PersonEditor::class)
class PersonModel : ItemViewModel<Person>() {
    val name = bind(Person::nameProperty)
    val title = bind(Person::titleProperty)
    /*val firstname = bind { item?.name?.toProperty() }
    val lastName = bind { item?.lastName?.toProperty() }*/
}

class ConnModel : ItemViewModel<ConnInfo>() {
    var url = bind { item?.url?.toProperty() }
    var name = bind { item?.name?.toProperty() }
    var user = bind { item?.user?.toProperty() }
    var pwd = bind { item?.pwd?.toProperty() }
    /*override fun onCommit(commits: List<Commit>) {
        super.onCommit(commits)
        for (commit in commits) {
            println("sdldfjsk")
        }
    }*/
}

class PersonEditor : View("Person Editor") {
    override val root = BorderPane()
    var fromTable: TableView<ColumnInfo> by singleAssign()
    var toTable: TableView<ColumnInfo> by singleAssign()
    var matchTable: TableView<Matching> by singleAssign()
    var fromTableName = ""
    var toTableName = ""
    var selectTable: TableView<TableInfo>? = null
    // Some fake data for our table
    val fromColumns = mutableListOf<ColumnInfo>().asObservable()
    val toColumns = mutableListOf<ColumnInfo>().asObservable()
    val model = PersonModel()
    val fromConnModel = ConnModel()
    val toConnModel = ConnModel()
    val fromConnInfo = ConnInfo(
        "carServDev",
        "jdbc:mysql://192.168.1.229:3306/zoan_admin_dev", "cardev", "Car_dev#68"
    )
    val toConnInfo = ConnInfo(
        "carServTest",
        "jdbc:mysql://192.168.1.240:3306/car_search", "cartest", "Car_test#68"
    );
    val fromDatabaseUtil = DatabaseUtil(fromConnInfo)
    val toDatabaseUtil = DatabaseUtil(toConnInfo)
    var fromTableNameList = mutableListOf<TableInfo>().asObservable()
    var toTableNameList = mutableListOf<TableInfo>().asObservable()
    var matchList = mutableListOf<Matching>().asObservable()

    fun dataInit(){
        fromConnModel.item = fromConnInfo
        toConnModel.item = toConnInfo
        for (tableName in fromDatabaseUtil.getTableNames()) {
            fromTableNameList.add(TableInfo(tableName, ""))
        }
        for (tableName in toDatabaseUtil.getTableNames()) {
            toTableNameList.add(TableInfo(tableName, ""))
        }
    }
    init {
        if(fromConnInfo!=null&&toConnInfo!=null){
            dataInit()
        }
        with(root) {
            top {
                menubar {
                    menu("File") {
                        menu("Connect") {
                            item("Facebook").action { println("Connecting Facebook!") }
                            item("Twitter").action { println("Connecting Twitter!") }
                        }
                        item("Save").action {
                            println("Saving!")
                        }
                        item("更新数据").action {
                            println("Saving!")
                        }
                        menu("Quit").action {
                            println("Quitting!")
                        }

                    }
                    menu("Edit") {
                        item("源数据库").action {
                            dialog("源数据库配置") {
                                /*val model = ViewModel()
                                val note = model.bind { SimpleStringProperty() }*/
                                field("name") {
                                    textfield(fromConnModel.name)
                                }
                                field("url") {
                                    textfield(fromConnModel.url)
                                }
                                field("user") {
                                    textfield(fromConnModel.user)
                                }
                                field("pwd") {
                                    textfield(fromConnModel.pwd)
                                }
                                buttonbar {
                                    button("Save") {
                                        enableWhen(fromConnModel.dirty)
                                        action {
                                            fromConnModel.commit {
                                                fromConnModel.item.name = fromConnModel.name.value
                                                fromConnModel.item.url = fromConnModel.url.value
                                                fromConnModel.item.pwd = fromConnModel.pwd.value
                                                fromConnModel.item.user = fromConnModel.user.value

                                                if(!fromTableNameList.isEmpty()){
                                                    fromTableNameList.clear()
                                                }
                                                for (tableName in fromDatabaseUtil.getTableNames()) {
                                                    fromTableNameList.add(TableInfo(tableName, ""))
                                                }
                                            }
                                        }
                                    }
                                    button("Cancel") {
                                        action {
                                            this@dialog.close()
                                        }
                                    }
                                    button("Reset").action {
                                        fromConnModel.rollback()
                                    }
                                }
                            }
                        }
                        item("目标数据库").action {
                            dialog("目标数据库配置") {
                                field("name") {
                                    textfield(toConnModel.name)
                                }
                                field("url") {
                                    textfield(toConnModel.url)
                                }
                                field("user") {
                                    textfield(toConnModel.user)
                                }
                                field("pwd") {
                                    textfield(toConnModel.pwd)
                                }
                                buttonbar {
                                    button("Save") {
                                        action {
                                            toConnModel.commit {
                                                this@dialog.close()
                                                toConnModel.item.name = toConnModel.name.value
                                                toConnModel.item.url = toConnModel.url.value
                                                toConnModel.item.pwd = toConnModel.pwd.value
                                                toConnModel.item.user = toConnModel.user.value
                                                if(!toTableNameList.isEmpty()){
                                                    toTableNameList.clear()
                                                }
                                                for (tableName in toDatabaseUtil.getTableNames()) {
                                                    toTableNameList.add(TableInfo(tableName, ""))
                                                }
                                            }
                                        }
                                    }
                                    button("Reset").action {
                                        fromConnModel.rollback()
                                    }
                                    button("Cancel") {
                                        action {
                                            toConnModel.rollback()
                                            this@dialog.close()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            left{
                vbox {
                    hbox {
                        label(fromTableName)
                        button("选择表").action {
                            dialog("源数据库: "+fromConnInfo.name) {
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
                            }
                        }
                    }
                    tableview(fromColumns) {
                        fromTable = this
                        column("选择",ColumnInfo::selected){
                            useCheckbox(true)
                        }
                        column("Name", ColumnInfo::name){
                            style{

                            }
                        }
                        column("Type", ColumnInfo::type)
                    }
                }
            }
            // TableView showing a list of people
            center {
                vbox {
                    hbox {
                        label("匹配方式")
                        button("匹配"){
                            action {
                                if(!matchList.isEmpty()){
                                    matchList.clear()
                                }
                                for (fromColumn in fromColumns) {
                                    for (toColumn in toColumns) {
                                        if(fromColumn.name.equals(toColumn.name)){
                                            matchList.add(Matching(fromColumn,toColumn))
                                        }
                                    }
                                }
                            }
                        }
                        button("更新添加"){
                            action{

                            }
                        }
                    }
                    tableview(matchList) {
                        matchTable = this
                        column("关键字段",Matching::key){
                            useCheckbox(true)
                        }
                        column("源字段", Matching::fromColumn)
                        column("目的字段", Matching::toColumn)
                        column("传输字段",Matching::key){
                            useCheckbox(true)
                        }
                    }
                }

            }
            right {
                vbox {
                    hbox {
                        label(toTableName)
                        button("选择表").action {
                            dialog("源数据库: "+toConnInfo.name) {
                                tableview(toTableNameList) {
                                    selectTable = this
                                    column("Name", TableInfo::name)
                                }
                                buttonbar {
                                    button("确定") {
                                        action {
                                            val selectedItem = selectTable?.selectedItem
                                            if (selectedItem != null) {
                                                toTableName = selectedItem.name
                                                if(!toColumns.isEmpty()){
                                                    toColumns.clear()
                                                }
                                                toColumns.addAll(toDatabaseUtil.getColumnInfoList(toTableName))
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
                            }
                        }
                    }
                    tableview(toColumns) {
                        toTable = this
                        column("选择",ColumnInfo::selected){
                            useCheckbox(true)
                        }
                        column("Name", ColumnInfo::name)
                        column("Type", ColumnInfo::type)
                    }
                }

            }
        }
    }

    private fun save() {
        // Extract the selected person from the tableView
        model.commit()
        val person = model.item
        // A real application would persist the person here
        println("Saving ${person.name} / ${person.title}")
    }
    /*form {
                    fieldset("Edit person") {
                        field("Name") {
                            textfield(model.name).required()
                        }
                        field("Title") {
                            textfield(model.title)
                        }
                        hbox {
                            button("Save") {
                                enableWhen(model.dirty)
                                action {
                                    save()
                                }
                            }
                            button("Reset").action {
                                model.rollback()
                            }
                            button("dialog").action {
                                dialog("Add note") {
                                    val model = ViewModel()
                                    val note = model.bind { SimpleStringProperty() }

                                    field("Note") {
                                        textarea(note) {
                                            required()
                                            whenDocked { requestFocus() }
                                        }
                                    }
                                    buttonbar {
                                        button("Save note") {
                                            action {
                                                model.commit { print("dkslfj") }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }
                }*/
}

