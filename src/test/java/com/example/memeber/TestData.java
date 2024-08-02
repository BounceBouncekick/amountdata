package com.example.memeber;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    public static List<UserDto> generateTestUsers(int count) {
        List<UserDto> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UserDto user = new UserDto();
            user.setName("User" + i);
            user.setEmail("user" + i + "@example.com");
            user.setPassword("password" + i);
            users.add(user);
        }
        return users;
    }
}