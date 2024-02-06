package org.example.services;

import org.example.models.Car;
import org.example.models.Person;
import org.example.repositories.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CarsService {
    private final CarsRepository carsRepository;

    @Autowired
    public CarsService(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    public List<Car> findAll(boolean sortByYear) {
        if (sortByYear) {
            return carsRepository.findAll(Sort.by("yearOfRelease"));
        }
        return carsRepository.findAll();
    }

    public List<Car> searchByTitle(String query) {
        return carsRepository.findByBrandTitleStartingWith(query);
    }

    public List<Car> findAllWithPagination(Integer page, Integer carsPerPage, boolean sortByYear) {
        if (sortByYear) {
            return carsRepository.findAll(PageRequest.of(page, carsPerPage, Sort.by("yearOfRelease")))
                    .getContent();
        }
        return carsRepository.findAll(PageRequest.of(page, carsPerPage)).getContent();
    }

    public Car findById(int id) {
        return carsRepository.findById(id).orElse(null);
    }

    public Optional<Person> findOwner(int id) {
        return Optional.ofNullable(carsRepository.findById(id).orElse(new Car()).getOwner());
    }

    @Transactional
    public void save(Car car) {
        carsRepository.save(car);
    }

    @Transactional
    public void addOwner(int id, Person person) {
        carsRepository.findById(id).ifPresent(car -> {
            car.setOwner(person);
            car.setRentalDate(new Date());
        });
    }

    @Transactional
    public void deleteOwner(int id) {
        carsRepository.findById(id).ifPresent(car -> {
            car.setOwner(null);
            car.setRentalDate(null);
        });
    }

    @Transactional
    public void update(int id, Car car) {
        car.setId(id);
        carsRepository.save(car);
    }

    @Transactional
    public void delete(int id) {
        carsRepository.deleteById(id);
    }
}
