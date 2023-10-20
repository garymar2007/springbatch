package com.gary.repository;

import com.gary.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Employee, Integer> {
}
