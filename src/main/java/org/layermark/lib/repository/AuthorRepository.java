package org.layermark.lib.repository;

import org.layermark.lib.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByFirstNameAndLastNameLike(String firstName, String lastName);

}
