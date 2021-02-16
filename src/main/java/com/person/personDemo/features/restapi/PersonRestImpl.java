package com.person.personDemo.features.restapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.cxf.feature.Features;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.person.personDemo.features.dataaccess.PersonRepository;
import com.person.personDemo.model.PersonModel;
import com.person.server.api.PersonApi;
import com.person.server.dto.PersonApiDto;
import com.person.server.dto.PersonResponseApiDto;

@Features(features = { "org.apache.cxf.jaxrs.validation.JAXRSBeanValidationFeature",
        "org.apache.cxf.ext.logging.LoggingFeature" })
@Component
public class PersonRestImpl implements PersonApi {

    @Autowired
    private PersonRepository personRepository;

    public PersonResponseApiDto addPerson(PersonApiDto person) {
        PersonModel personModel = new PersonModel();
        personModel.setAge(person.getAge());
        personModel.setFirstName(person.getFirstName());
        personModel.setLastName(person.getLastName());
        personModel.setFavouriteColour(person.getFavouriteColour());

        if (personRepository.existsByFirstNameAndLastName(person.getFirstName(), person.getLastName())) {
            PersonModel personInDB = personRepository.findByFirstNameAndLastName(person.getFirstName(),
                    person.getLastName());
            personInDB.setAge(person.getAge());
            personInDB.setFavouriteColour(person.getFavouriteColour());
            personInDB.setFirstName(person.getFirstName());
            personInDB.setLastName(person.getLastName());
            personRepository.saveAndFlush(personInDB);
        } else {
            personRepository.saveAndFlush(personModel);
        }

        return new PersonResponseApiDto().status("success").message("person has been added or updated")
                .errorCode("200");
    }

    public PersonResponseApiDto deletePerson(String personId) {
        Optional<PersonModel> personInDB = personRepository.findById(Long.parseLong(personId));
        if (personInDB.isPresent()) {
            personRepository.delete(personInDB.get());
            return new PersonResponseApiDto().status("success").message("person has been deleted")
                    .errorCode("200");
        }
        return null;
    }

    public PersonApiDto getPerson(String personId) {
        Optional<PersonModel> personInDB = personRepository.findById(Long.parseLong(personId));
        if (personInDB.isPresent()) {
            PersonApiDto person = new PersonApiDto();
            person.setAge(personInDB.get().getAge());
            person.setFirstName(personInDB.get().getFirstName());
            person.setLastName(personInDB.get().getLastName());
            person.setFavouriteColour(personInDB.get().getFavouriteColour());
            person.setId(personInDB.get().getId().toString());
            return person;
        }

        return null;
    }

    public List<PersonApiDto> getAllPerson() {
        List<PersonModel> allPersons = personRepository.findAll();
        List<PersonApiDto> allPersonsDto = new ArrayList<PersonApiDto>();
        for (PersonModel p : allPersons) {
            PersonApiDto person = new PersonApiDto();
            person.setAge(p.getAge());
            person.setFirstName(p.getFirstName());
            person.setLastName(p.getLastName());
            person.setFavouriteColour(p.getFavouriteColour());
            person.setId(p.getId().toString());
            allPersonsDto.add(person);
        }
        return allPersonsDto;
    }

}
