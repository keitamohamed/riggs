package com.keita.riggs.model;

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
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Enter connection type")
    private String internet;
    @NotBlank(message = "Room key type")
    private String roomKey;
    @NotBlank(message = "Enter room view")
    private String assistance;
    @NotBlank(message = "Does room have meeting room")
    private String meeting;
    @NotBlank(message = "Does room have pool")
    private String pool;

}
