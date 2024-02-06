package org.example.controllers;

import jakarta.validation.Valid;
import org.example.models.Car;
import org.example.models.Person;
import org.example.services.CarsService;
import org.example.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cars")
public class CarsController {
    private final CarsService carsService;
    private final PeopleService peopleService;

    @Autowired
    public CarsController(CarsService carsService, PeopleService peopleService) {
        this.carsService = carsService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "cars_per_page", required = false) Integer carsPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
        if (page == null || carsPerPage == null) {
            model.addAttribute("cars", carsService.findAll(sortByYear));
        } else {
            model.addAttribute("cars", carsService.findAllWithPagination(page, carsPerPage, sortByYear));
        }
        return "cars/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("car", carsService.findById(id));
        Optional<Person> owner = carsService.findOwner(id);
        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
        } else {
            model.addAttribute("people", peopleService.findAll());
        }
        return "cars/show";
    }

    @GetMapping("/new")
    public String newCar(@ModelAttribute("car") Car car) {
        return "cars/new";
    }

    @PostMapping
    public String create(@ModelAttribute("car") Car car) {
        carsService.save(car);
        return "redirect:/cars";
    }

    @PatchMapping("/{id}/add")
    public String addPerson(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        carsService.addOwner(id, person);
        return "redirect:/cars/" + id;
    }

    @PatchMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id") int id) {
        carsService.deleteOwner(id);
        return "redirect:/cars/" + id;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("car", carsService.findById(id));
        return "cars/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("car") @Valid Car car, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/cars/edit";
        }
        carsService.update(id, car);
        return "redirect:/cars/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        carsService.delete(id);
        return "redirect:/cars";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "cars/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("cars", carsService.searchByTitle(query));
        return "cars/search";
    }
}
