package com.gary.processor;

import com.gary.model.Employee;
import org.springframework.stereotype.Component;

import org.springframework.batch.item.ItemProcessor;
import java.util.Date;

@Component
public class Processor implements ItemProcessor<Employee, Employee>{

    @Override
    public Employee process(Employee employee) throws Exception {
        employee.setTimestamp(new Date());
        return employee;
    }
}
