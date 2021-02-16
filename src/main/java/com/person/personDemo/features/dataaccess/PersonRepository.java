package com.person.personDemo.features.dataaccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.person.personDemo.model.PersonModel;

@Repository
public interface PersonRepository extends JpaRepository<PersonModel, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    PersonModel findByFirstNameAndLastName(String firstName, String lastName);

    Optional<PersonModel> findById(Long id);

}
