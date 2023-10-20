package com.gary.writer;

import com.gary.model.Employee;
import com.gary.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<Employee> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void write(List<? extends Employee> users) throws Exception {
        userRepository.saveAll(users);
    }
}
