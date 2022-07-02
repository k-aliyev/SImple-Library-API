package org.layermark.lib.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String edition;

    private String publisher;

    private boolean isAvailable;

    @JoinColumn(name = "author_id")
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Author author;

    @OneToMany(mappedBy = "book")
    List<Reservation> reservations;
}
