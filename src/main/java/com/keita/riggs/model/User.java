package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
    @Column(nullable = false)
    private long userID;
    @NotBlank(message = "Enter first name")
    private String firstName;
    @NotBlank(message = "Enter last name")
    private String lastName;
    @Email
    @Column(updatable = false, unique = true)
    @NotBlank(message = "Enter email address")
    private String email;
    @NotBlank(message = "Enter phone number")
    private String phoneNum;

    @Valid
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "address")
    private Address address;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "booking")
    private Booking booking;

}
