package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BookingPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id")
    @SequenceGenerator(name = "seq_id", initialValue = 4651, allocationSize = 2)
    private long id;
    @Digits(integer = 15, fraction = 0, message = "Enter room id")
    private long roomID;
    @DecimalMin(value = "60.0", message = "Booking price must be $60 or higher")
    @DecimalMax(value = "1000.00", message = "Booking price must be $1000.00 or less")
    private double bookingPrice;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonBackReference(value = "prices")
    @JoinColumn(name = "bookingID")
    private Booking prices;
}
