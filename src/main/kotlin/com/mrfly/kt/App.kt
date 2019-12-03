package com.mrfly.kt

import com.mrfly.java.DatabaseUtil
import com.mrfly.kt.bean.ConnInfo
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.BorderPane
import tornadofx.*

fun main(args: Array<String>) {
    launch<MyApp>(args)
}
class MyApp: App(MyView::class)

//class Conn(var url:String,var user:String,var pwd:String)

class MyView: View() {
    var firstNameField: TextField by singleAssign()
    var lastNameField: TextField by singleAssign()
    var dialect  = "mysql"
    var connList = observableListOf<ConnInfo>()

    private val dialectGroup = ToggleGroup()
    init {

        connList.add(ConnInfo("","","",""))
        connList.add(ConnInfo("","","",""))
    }
    var xxx = borderpane{
        top{
            menubar {
                menu("File") {
                    menu("Connect") {
                        item("Facebook").action { println("Connecting Facebook!") }
                        item("Twitter").action { println("Connecting Twitter!") }
                    }
                    item("Save","Shortcut+S").action {
                        println("Saving!")
                    }
                    menu("Quit","Shortcut+Q").action {
                        println("Quitting!")
                    }
                }
                menu("Edit") {
                    item("Copy","Shortcut+C").action {
                        println("Copying!")
                    }
                    item("Paste","Shortcut+V").action {
                        println("Pasting!")
                    }
                }
            }
        }
        center{

        }
    }
    override val root = vbox {
        hbox {
            label("请选择数据库")
        }
        hbox{
            radiobutton("mysql", dialectGroup){
                isSelected = true
                action {
                    dialect = "mysql"
                }
            }
            radiobutton("oracle", dialectGroup){
                action {
                    dialect = "oracle"
                }
            }
            radiobutton("postgreSQL", dialectGroup){
                action {
                    dialect = "postgreSQL"

                }
            }
        }
        hbox {
            label("源数据库:")
        }
        hbox {
            var conn =  connList.get(0)
            label("url")
            textfield(conn.url)
            label("user")
            textfield(conn.user)
            label("pwd")
            textfield(conn.pwd)
        }
        hbox {
            label("目标数据库:")
        }
        hbox {
            var conn =  connList.get(1)
            label("url")
            textfield(conn.url)
            label("user")
            textfield(conn.user)
            label("pwd")
            textfield(conn.pwd)
        }
/*        button("LOGIN") {
            useMaxWidth = true
            action {
                println("Logging in as ${firstNameField.text} ${lastNameField.text}")
            }
        }*/
    }
}
