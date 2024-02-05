package com.testcontainers.controller;

import com.testcontainers.entity.Employee;
import com.testcontainers.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService service;

    @PostMapping
    public Employee addEmployee(@RequestBody Employee emp){
        log.info("Controller - adding new employee");
        return service.addEmp(emp);
    }

    @GetMapping
    public List<Employee> findAll(){
        log.info("Controller - finding all employees");
        return service.findAll();
    }
}
