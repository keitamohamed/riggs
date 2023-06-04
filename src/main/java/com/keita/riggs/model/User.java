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

import java.util.List;

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
    @NotBlank(message = "Enter phone number")
    private String phoneNum;

    @Valid
    @OneToOne(mappedBy = "auth", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "auth")
    private Authenticate auth;

    @Valid
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "address")
    private Address address;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "roomBook")
    private List<Booking> roomBook;

    public void addNewBooking(Booking booking) {
        add(booking);
    }

    private void add(Booking booking) {
        roomBook.add(booking);
    }

}
