package com.satish.mapper;

import com.satish.dto.AddressDto;
import com.satish.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDto, Address>{
}
