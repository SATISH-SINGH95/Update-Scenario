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

    public void delete(Long id){
        addressRepository.deleteById(id);
    }

    public void removeAddress(Address address){
        delete(address.getId());
    }
}
