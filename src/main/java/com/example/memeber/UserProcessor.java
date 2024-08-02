package com.example.memeber;


import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) {
        return user;
    }


}