package com.testcontainers.service;

import com.testcontainers.entity.Employee;
import com.testcontainers.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    public Employee addEmp(Employee employee){
        log.info("EmployeeService - adding new employee");
        return employeeRepository.save(employee);
    }

    public List<Employee> findAll(){
        log.info("EmployeeService - finding all employees");
        return employeeRepository.findAll();
    }
}
