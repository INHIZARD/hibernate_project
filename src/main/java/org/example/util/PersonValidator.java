package org.example.util;

import org.example.models.Person;
import org.example.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleRepository) {
        this.peopleService = peopleRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleService.findPersonByFullName(person.getFullName()).isPresent()) {
            errors.rejectValue("fullName", "", "Данное имя уже используется");
        }
    }

    public void validate(int id, Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleService.findPersonByFullName(person.getFullName()).isPresent()
                && peopleService.findPersonByFullName(person.getFullName()).get().getId() != id) {
            errors.rejectValue("fullName", "", "Данное имя уже используется");
        }
    }
}
