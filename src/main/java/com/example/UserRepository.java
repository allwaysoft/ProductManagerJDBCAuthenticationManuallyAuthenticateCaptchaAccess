package com.example;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User getByUsername(String username);

    public User findByEmail(String email);

    public User findByUsername(String username);

}
