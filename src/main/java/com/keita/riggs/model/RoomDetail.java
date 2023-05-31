package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "detail")
public class RoomDetail {
    @Id
    @Column(nullable = false)
    private long id;
    @NotBlank(message = "Enter room view")
    private String view;
    @NotBlank(message = "Enter room bed type")
    private String bed;
    @NotBlank(message = "Is animal allow in the room")
    private String animal;
    @NotBlank(message = "Is smoking allow in the room")
    private String smoking;
    @NotBlank(message = "Bathroom desc")
     private String bathroom;
    @NotBlank(message = "Does room have tv")
    private String tv;
    @NotBlank(message = "Enter number of bed")
    private String numberOfBed;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "roomID")
    @JsonBackReference(value = "room")
    private Room room;
}
