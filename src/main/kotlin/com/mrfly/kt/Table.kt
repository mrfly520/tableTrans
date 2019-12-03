package com.mrfly.kt
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
class TableApp: App(MyView::class)
class TableView : View("My View") {

    val controller: CustomerController by inject()
    var tableViewEditModel: TableViewEditModel<Customer> by singleAssign()

    override val root =  borderpane {
        top = buttonbar {
            button("COMMIT").action {
                tableViewEditModel.commit()
            }
            button("ROLLBACK").setOnAction {
                tableViewEditModel.rollback()
            }
        }
        center = tableview<Customer> {

            items = controller.customers
            isEditable = true

            column("ID",Customer::idProperty)
            column("FIRST NAME", Customer::firstNameProperty).makeEditable()
            column("LAST NAME", Customer::lastNameProperty).makeEditable()

            enableCellEditing() //enables easier cell navigation/editing
            enableDirtyTracking() //flags cells that are dirty

            tableViewEditModel = editModel
        }
    }
}

class CustomerController : Controller() {
    val customers = listOf(
        Customer(1, "Marley", "John"),
        Customer(2, "Schmidt", "Ally"),
        Customer(3, "Johnson", "Eric")
    ).asObservable()
}

class Customer(id: Int, lastName: String, firstName: String) {
    val lastNameProperty = SimpleStringProperty(this, "lastName", lastName)
    var lastName by lastNameProperty
    val firstNameProperty = SimpleStringProperty(this, "firstName", firstName)
    var firstName by firstNameProperty
    val idProperty = SimpleIntegerProperty(this, "id", id)
    var id by idProperty
}