package org.example.services;

import org.example.models.Car;
import org.example.models.Person;
import org.example.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public List<Car> getPersonCars(int id) {
        Person person = peopleRepository.findById(id).orElse(null);
        if (person == null) {
            return Collections.emptyList();
        }
        for (Car car : person.getCars()) {
            car.setExpired(System.currentTimeMillis() - car.getRentalDate().getTime() > 7 * 24 * 60 * 60 * 100);
        }
        return person.getCars();
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> findPersonByFullName(String fullName) {
        return peopleRepository.findPersonByFullName(fullName);
    }
}
