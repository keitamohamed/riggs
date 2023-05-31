package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @Column(nullable = false)
    private long id;
    @NotBlank(message = "Enter street name")
    private String street;
    @NotBlank(message = "Enter city")
    private String city;
    @NotBlank(message = "Enter state")
    private String state;
    @Size(min = 5, max = 5, message = "Zip code must be a five digit number")
    @NotBlank(message = "Enter a zipcode")
    private String zipcode;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userID")
    @JsonBackReference(value = "address")
    private User user;

}
