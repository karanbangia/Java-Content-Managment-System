package com.webapp.cmsshopping.repository;

import com.webapp.cmsshopping.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsername(String username);
}
