package org.example.repositories;

import org.example.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarsRepository extends JpaRepository<Car, Integer> {
    List<Car> findByBrandTitleStartingWith(String modelTitle);
}
