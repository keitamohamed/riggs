package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

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
    @NotNull(message = "Enter arrival date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date arrDate;
    @NotNull(message = "Enter departure date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date depDate;
    @Min(value = 1, message = "Number of room must be equal or greater than 18")
    private int numRoom;
    @Min(value = 1, message = "Number of adult must be equal or greater than 18")
    private int numAdult;
    private int numChildren;


    @JsonBackReference(value = "book")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userID")
    private User user;


    @JsonManagedReference(value = "rooms")
    @OneToMany(mappedBy = "rooms", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Room> rooms;

    public void addRoom(Room room) {
        this.rooms.add(room);
    }
}
