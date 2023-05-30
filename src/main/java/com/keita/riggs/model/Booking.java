package com.keita.riggs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "Booking")
public class Booking {
    @Id
    private long id;
    @Column(updatable = false, unique = true)
    @NotBlank(message = "Room id is missing")
    private long roomId;
    @Column(updatable = false, unique = true)
    @NotBlank(message = "User id is missing")
    private long userID;
}
