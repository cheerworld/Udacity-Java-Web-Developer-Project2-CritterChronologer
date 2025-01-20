package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Transactional
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Long saveCustomer(Customer customer) {
        return customerRepository.save(customer).getId();
    }


    public Customer getCustomerByPet(Long petId) {
        // Fetch the pet by its ID
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + petId));

        // Return the owner (customer) of the pet
        System.out.println("pet.getOwner():"+pet.getOwner().getId());
        System.out.println("pet.getOwner():"+pet.getOwner().getPets());
        return pet.getOwner();
    }


    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }


    public void addPetsToCustomer(Long customerId, List<Long> petIds) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        List<Pet> pets = petRepository.findAllById(petIds);

        if (customer.getPets() == null) {
            customer.setPets(pets);
        } else {
            customer.getPets().addAll(pets);
        }

        customerRepository.save(customer);
    }
}
