package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private long id;
    @NotBlank(message = "Enter first name")
    private String firstName;
    @NotBlank(message = "Enter last name")
    private String lastName;
    @Column(updatable = false, unique = true)
    @NotBlank(message = "Enter email address")
    private String email;
    @NotBlank(message = "Enter phone number")
    private String phoneNum;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user")
    private Address user;
}
