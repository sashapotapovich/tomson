package com.common.model;


import com.common.annotation.CrearecNotSql;
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
@javax.persistence.Entity
@Table(name = "customer")
public class Customer extends Entity implements CustomerTO {
    @CrearecNotSql
    private static final long serialVersionUID = 1270898336029025561L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String ssn;
    @NonNull
    private String customerName;
    @NonNull
    private String address;
}
