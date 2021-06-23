package com.arjay07.mycolorservice.repository;

import com.arjay07.mycolorservice.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
