package com.satish.service;

import com.satish.dto.AddressDto;
import com.satish.dto.StudentDto;
import com.satish.entity.Address;
import com.satish.entity.Student;
import com.satish.exception.EntityNotFoundException;
import com.satish.mapper.AddressMapper;
import com.satish.mapper.StudentMapper;
import com.satish.repository.AddressRepository;
import com.satish.repository.StudentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final AddressService addressService;

    public StudentService(StudentRepository studentRepository,
                          StudentMapper studentMapper,
                          AddressMapper addressMapper,
                          AddressRepository addressRepository,
                          AddressService addressService){
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
        this.addressService = addressService;
    }

    public StudentDto save(StudentDto studentDto){
        log.debug("Request to save Student : {}", studentDto);
        Student student = studentMapper.toEntity(studentDto);
        student.getAddresses().clear();
        addAddress(studentDto, student);
        student = studentRepository.save(student);
        return studentMapper.toDto(student);
    }

    /**
     * Add addresses to student from given studentDTO.
     *
     * @param studentDto StudentDTO to read addresses from
     * @param student    Student to add addresses to
     */
    private void addAddress(StudentDto studentDto, Student student){
        if(studentDto.getAddresses() != null && !studentDto.getAddresses().isEmpty()){
            for(AddressDto addressDto : studentDto.getAddresses()){
                student.addAddress(addressMapper.toEntity(addressDto));
            }
        }
    }

    public StudentDto updateStudent(StudentDto studentDto){
        log.debug("Request to update Student : {}", studentDto);

        Student student = studentRepository.findById(studentDto.getId()).get();
        Student entity = studentMapper.toEntity(studentDto);

        List<Address> existingAddresses = addressRepository.findByStudentId(student.getId());

        Set<Long> incomingAddressIds = studentDto.getAddresses().stream().map(AddressDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<Address> addressesToDelete = existingAddresses.stream()
                .filter(address -> !incomingAddressIds.contains(address.getId()))
                .toList();

        if(!addressesToDelete.isEmpty()){
            addressRepository.deleteAllInBatch(addressesToDelete);
        }

        entity.updateAddress();
        student = studentRepository.save(entity);
        return studentMapper.toDto(student);
    }

    @Transactional
    public void delete(Long id){
        log.debug("Request to delete Student : {}", id);
        studentRepository.findById(id).ifPresent(student -> {
            student.getAddresses().forEach(addressService::removeAddress);
            student.getAddresses().clear();
            studentRepository.delete(student);
        });
    }

    public StudentDto findOne(Long id){
        return studentRepository.findById(id).map(studentMapper::toDto).orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    public List<StudentDto> findAll() {
        log.debug("Request to find all Student");
        List<Student> students = studentRepository.findAll();
        if(students.isEmpty() || students == null){
            throw new EntityNotFoundException("Students are not found");
        }
        return students.stream().map(studentMapper::toDto).toList();
    }


}
