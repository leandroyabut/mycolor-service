package com.arjay07.userservice.repository;

import com.arjay07.userservice.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
