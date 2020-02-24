package com.common.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "customer")
public class Customer implements CustomerTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String ssn;
    @NonNull
    private String customerName;
    @NonNull
    private String address;
    
    public void setErrorMessage(String message){
        System.err.println("setErrorMessage invoked for - " + message);
    }
}
