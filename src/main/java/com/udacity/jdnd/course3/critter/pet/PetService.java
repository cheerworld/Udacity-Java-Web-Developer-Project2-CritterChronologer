package com.udacity.jdnd.course3.critter.pet;


import com.udacity.jdnd.course3.critter.user.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerService customerService;

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet savePet(Pet pet){
        Pet newPet = petRepository.save(pet);
        customerService.addPetsToCustomer(newPet.getOwner().getId(), Collections.singletonList(newPet.getId()));
        return newPet;
    }

    public Pet getPetById(Long id) {
        return petRepository.findById(id).orElse(null);
    }

    public List<Pet> getPetsByOwnerId(Long ownerId) {
        System.out.println(petRepository.getPetsByOwnerId(ownerId).size());
        System.out.println(petRepository.getPetsByOwnerId(ownerId).get(0).getType());
        return petRepository.getPetsByOwnerId(ownerId);
    }

    public List<Pet> findAllPetsByIds (List<Long> petIds) {
        return petRepository.findAllById(petIds);
    }
}
