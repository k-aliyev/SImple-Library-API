package org.layermark.lib.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique=true)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "user")
    List<Reservation> reservations;
}
