package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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
public class Room {
    @Id
    @Column(nullable = false)
    private long roomID;
    @NotBlank(message = "Enter room name")
    private String roomName;
    @NotBlank(message = "Enter room description")
    private String description;
    @NotBlank(message = "Enter room size")
    private String size;

    @Valid
    @OneToOne(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "room")
    private RoomDetail room;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "booking")
    private List<Booking> bookings;

    public void addNewBooking(Booking booking) {
        add(booking);
    }

    private void add(Booking booking) {
        bookings.add(booking);
    }

}
