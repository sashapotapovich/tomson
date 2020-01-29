package com.vaadin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "UserInfo")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Email
    @Size(max = 255)
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 4, max = 255)
    private String passwordHash;

    @NotBlank
    @Size(max = 255)
    private String firstName;

    @NotBlank
    @Size(max = 255)
    private String lastName;

    @NotBlank
    @Size(max = 255)
    private String role;

    private Boolean locked;

    public User() {
        // An empty constructor is needed for all beans
    }

    public User(@NotEmpty @Email @Size(max = 255) String email, @NotNull @Size(min = 4, max = 255) String passwordHash, @NotBlank @Size(max = 255) String firstName, @NotBlank @Size(max = 255) String lastName, @NotBlank @Size(max = 255) String role, Boolean locked) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.locked = locked;
    }

    @PrePersist
    @PreUpdate
    private void prepareData() {
        this.email = email == null ? null : email.toLowerCase();
    }
}
