package com.satish.service;

import com.satish.entity.Address;
import com.satish.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    /**
     * Deletes the address with the given id.
     *
     * @param id The id of the address to delete
     */
    public void delete(Long id){
        addressRepository.deleteById(id);
    }

    /**
     * Removes the given address from the database.
     *
     * @param address The address to be removed
     */
    public void removeAddress(Address address){
        delete(address.getId());
    }
}
