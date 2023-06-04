package com.keita.riggs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authenticate")
public class Authenticate {

    @Id
    private Long authID;
    @Email
    @Column(updatable = false, unique = true)
    @NotBlank(message = "Enter email address")
    private String email;
    @Column(columnDefinition = "LONGBLOB")
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Enter user role")
    private String role;
    private boolean isAccountNonExpired;
    private boolean isAccountNotLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userID")
    @JsonBackReference(value = "auth")
    private User auth;
}
