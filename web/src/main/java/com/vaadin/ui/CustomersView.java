package com.vaadin.ui;

import com.vaadin.entity.Customer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.repository.CustomerRepository;
import com.vaadin.ui.editor.CustomerEditor;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;

@Slf4j
@UIScope
@SpringComponent
@Route(value = "customers", layout = MenuView.class)
@Secured("ROLE_ADMIN")
public class CustomersView extends VerticalLayout implements RouterLayout {
    public static final String ID = "customers";
    private static TextField groupFilter = new TextField("Name Filter", "");
    private CustomerRepository customerRepository;
    private CustomerEditor customerEditor;
    private Button clearFilters = new Button("Show All Customers");
    private HorizontalLayout filters = new HorizontalLayout(clearFilters);
    private Grid<Customer> grid;

    public CustomersView(CustomerRepository repository, CustomerEditor customerEditor) {
        this.customerRepository = repository;
        this.customerEditor = customerEditor;
    }

    @PostConstruct
    public void init() {
        groupFilter.addValueChangeListener(listener -> {
            if (!groupFilter.getValue().isEmpty()) {
                clearFilters.setVisible(true);
            }
            List<Customer> customers = customerRepository.findAllByFirstNameContains(groupFilter.getValue());
            grid.setItems(customers);
        });
        filters.setAlignItems(Alignment.BASELINE);
        clearFilters.setVisible(false);
        clearFilters.addClickListener(action -> {
            groupFilter.setValue("");
            grid.setItems(customerRepository.findAll());
            clearFilters.setVisible(false);
        });
        grid = new Grid<>(Customer.class, false);
        grid.addColumn(Customer::getId).setHeader("ID").setFlexGrow(0).setSortable(true);
        grid.addColumn(Customer::getFirstName).setHeader("First Name").setSortable(true);
        grid.addColumn(Customer::getAddress).setHeader("Address").setSortable(true);
        add(filters, grid);
        grid.setItems(customerRepository.findAll());
        grid.addItemClickListener(action -> {
            customerEditor.setCustomer(action.getItem());
            customerEditor.open();
        });
    }

    public static void setFilter(String filter) {
        groupFilter.setValue(filter);
    }

}
