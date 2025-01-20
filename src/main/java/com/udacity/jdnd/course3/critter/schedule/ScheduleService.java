package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Method to create a new schedule
    public Schedule createSchedule(Schedule schedule) {
        Schedule savedSchedule = scheduleRepository.save(schedule);
        schedule.setId(savedSchedule.getId());
        return schedule;
    }

    // Method to get all schedules
    public List<Schedule> getAllSchedules() {

        return scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesByEmployeeId(Long employeeId) {
        return scheduleRepository.findByEmployees_Id(employeeId);
    }

    public List<Schedule> getSchedulesByPetId(Long petId) {
        return scheduleRepository.findByPets_Id(petId);
    }

    public List<Schedule> getSchedulesByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        // Check if the customer exists and has pets
        if (customer != null && customer.getPets() != null && !customer.getPets().isEmpty()) {
            List<Pet> pets = customer.getPets();

            List<Schedule> allSchedules = new ArrayList<>();
            for (Pet pet : pets) {
                List<Schedule> schedulesForPet = getSchedulesByPetId(pet.getId());
                allSchedules.addAll(schedulesForPet); // Add the schedules to the allSchedules list
            }

            return allSchedules;
        }

        // Return an empty list if the customer does not exist or has no pets
        return Collections.emptyList();
    }
}
