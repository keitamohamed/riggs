package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @Column(nullable = false)
    private long bookingID;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date bookDate;
    @NotNull(message = "Enter move in date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fromDate;
    @NotNull(message = "Enter move out date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date toDate;


    @JsonBackReference(value = "book")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userID")
    private User user;

    @JsonBackReference(value = "booking")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "roomID")
    private Room room;
}
