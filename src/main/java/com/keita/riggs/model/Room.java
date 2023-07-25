package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @OneToOne(mappedBy = "detail", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "detail")
    private RoomDetail detail;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonBackReference(value = "rooms")
    @JoinColumn(name = "bookingID")
    private Booking rooms;

}
