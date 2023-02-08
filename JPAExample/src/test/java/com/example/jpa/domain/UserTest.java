package com.example.jpa.domain;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void userTest() {
        User user = new User();
        user.setName("woony");

        userRepository.save(user);

        User thisUser = userRepository.findById(1l).orElse(null);
        thisUser.setName("woonys");

        List<User> userList = userRepository.findAll();
        System.out.println("userList = " + userList.get(0).getName());

    }
}
