package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id")
    @SequenceGenerator(name = "seq_id", initialValue = 4651, allocationSize = 2)
    private long id;
//    @DecimalMin(value = "60", message = "Booking price must be $60 or higher")
//    @DecimalMax(value = "1000", message = "Booking price must be $1000.00 or less")
    private double price;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonBackReference(value = "prices")
    @JoinColumn(name = "bookingID")
    private Booking prices;
}
