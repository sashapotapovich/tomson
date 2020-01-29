package com.vaadin.ui.editor;

import com.vaadin.entity.Customer;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.repository.CustomerRepository;
import com.vaadin.ui.MenuView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UIScope
@SpringComponent
@Route(value = "createCustomer", layout = MenuView.class)
public class CustomerEditor extends Dialog implements RouterLayout {
    
    private final CustomerRepository repository;
    private Customer customer;
    
    public CustomerEditor(CustomerRepository repository) {
        this.repository = repository;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
