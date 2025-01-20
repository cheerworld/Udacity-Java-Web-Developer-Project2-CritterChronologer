package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;
/*
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertPetDTOToPet(petDTO);
        Long petId = petService.savePet(pet);
        petDTO.setId(petId);

        return petDTO;
    }
*/
@PostMapping
public PetDTO savePet(@RequestBody PetDTO petDTO) {

    Pet pet = new Pet();

    BeanUtils.copyProperties(petDTO, pet);

    Long ownerId = petDTO.getOwnerId();

    if (ownerId > 0) {
        Customer owner = customerService.getCustomerById(ownerId);
        if (owner != null) {
            pet.setOwner(owner);
        }
    }

    Pet newPet = petService.savePet(pet);

    return convertPetToPetDTO(newPet);
}

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetToPetDTO(petService.getPetById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets() {
        return petService.getAllPets().stream().map(this::convertPetToPetDTO).toList();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return petService.getPetsByOwnerId(ownerId).stream().map(this::convertPetToPetDTO).toList();
    }


    private PetDTO convertPetToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getOwner() != null) {
            petDTO.setOwnerId(pet.getOwner().getId()); // Manually set ownerId
        }
        return petDTO;
    }


    private Pet convertPetDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);

        Long ownerId = petDTO.getOwnerId();
        System.out.println("ownerId: "+ownerId);
        // Check if ownerId exists and is greater than zero
        if (ownerId > 0) {
            Customer owner = customerService.getCustomerById(ownerId);
            if (owner != null) { // Ensure the owner is found
                System.out.println("owner found");
                pet.setOwner(owner);
            }
        }
        return pet;
    }
}
