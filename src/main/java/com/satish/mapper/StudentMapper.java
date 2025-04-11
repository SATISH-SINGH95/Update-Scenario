package com.satish.mapper;

import com.satish.dto.AddressDto;
import com.satish.dto.StudentDto;
import com.satish.entity.Address;
import com.satish.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface StudentMapper extends EntityMapper<StudentDto, Student>{

    @Mapping(source = "addresses", target = "addresses", qualifiedByName = "addressToAddressDto")
    StudentDto toDto(Student entity);

    @Named("addressToAddressDto")
    @Mapping(target = "id", source = "id")
    default AddressDto addressToAddressDto(Address address){
        if(address == null){
            return null;
        }
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setCity(address.getCity());
        return dto;
    }

}
