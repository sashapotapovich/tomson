package com.vaadin.ui;

import com.vaadin.entity.Customer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.repository.CustomerRepository;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UIScope
@SpringComponent
@Route(value = "details", layout = MenuView.class)
public class CustomerDetails extends VerticalLayout implements RouterLayout {
    public static final String ID = "details";
    public final CustomerRepository customerRepository;
    private Customer customer;
    
    public CustomerDetails(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @PostConstruct
    public void init() {
        TextField name = new TextField("Name", "");
        NumberField id = new NumberField("Id", "");
        TextField address = new TextField("Address", "");
        HorizontalLayout getUpdateActions = new HorizontalLayout();
        HorizontalLayout addDeleteActions = new HorizontalLayout();
        Button getCustomer = new Button("Get Customer");
        getCustomer.addClickListener(action -> {
            if (id.getValue() != null) {
                Optional<Customer> byId = customerRepository.findById(id.getValue().longValue());
                byId.ifPresent(customer1 -> {
                    name.setValue(customer1.getFirstName());
                    id.setValue(customer1.getId().doubleValue());
                    address.setValue(customer1.getAddress());
                    customer = customer1;
                });
            }
        });
        Button updateCustomer = new Button("Update Customer");
        updateCustomer.addClickListener(action -> {
            customer.setFirstName(name.getValue());
            customer.setAddress(address.getValue());
            customerRepository.save(customer);
        });
        Button addCustomer = new Button("Add Customer");
        addCustomer.addClickListener(click -> {
            Customer customer = new Customer(name.getValue(), address.getValue());
            customerRepository.save(customer);
        });
        Button deleteCustomer = new Button("DeleteCustomer");
        deleteCustomer.addClickListener(action -> {
            Optional<Customer> byId = customerRepository.findById(id.getValue().longValue());
            byId.ifPresent(customerRepository::delete);

        });
        getUpdateActions.add(getCustomer, updateCustomer);
        addDeleteActions.add(addCustomer, deleteCustomer);
        add(name, id, address, getUpdateActions, addDeleteActions);
    }
    
    
    
}
