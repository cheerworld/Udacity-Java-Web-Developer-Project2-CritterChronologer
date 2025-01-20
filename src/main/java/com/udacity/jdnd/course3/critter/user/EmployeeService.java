package com.udacity.jdnd.course3.critter.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Long saveEmployee(Employee employee) {
        return employeeRepository.save(employee).getId();
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public void setEmployeeAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if (employee != null) {
            employee.setDaysAvailable(daysAvailable);

            employeeRepository.save(employee);

            return;
        }

        throw new EntityNotFoundException("Employee not found with ID: " + employeeId);
    }

    public List<Employee> findEmployeesForService(EmployeeRequestDTO employeeRequestDTO) {
        Set<EmployeeSkill> requiredSkills = employeeRequestDTO.getSkills();
        LocalDate availableDays = employeeRequestDTO.getDate();

        Set<DayOfWeek> dayOfWeek = Set.of(availableDays.getDayOfWeek());

        List<Employee> allEmployees = employeeRepository.findAll();

        // Filter employees based on required skills and availability
        return allEmployees.stream()
                .filter(employee -> employee.getSkills().containsAll(requiredSkills) &&
                        employee.getDaysAvailable().containsAll(dayOfWeek))
                .collect(Collectors.toList());
    }

    public List<Employee> findEmployeesByIds (List<Long> employeeIds) {
        return employeeRepository.findAllById(employeeIds);
    }
}
