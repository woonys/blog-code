package com.example.jpa.domain;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void userTest() {
        User users = new User();
        users.setUserName("woony");

        userRepository.save(users);

        User thisUser = userRepository.findById(1l).orElse(null);
        thisUser.setUserName("woonys");
        userRepository.save(thisUser);

        List<User> userList = userRepository.findAll();
        System.out.println("userList = " + userList.get(0).getUserName());

    }

}
