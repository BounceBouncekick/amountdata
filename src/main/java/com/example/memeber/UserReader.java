package com.example.memeber;


import org.springframework.batch.item.ItemReader;

import java.util.List;
import java.util.Random;

public class UserReader implements ItemReader<User> {

    private long count;
    private long nextUserIndex = 0;

    public UserReader(long count) {
        this.count = count;
    }

    @Override
    public User read() {
        if (nextUserIndex < count) {
            nextUserIndex++;
            return generateUser();
        }
        return null; // No more data
    }

    private User generateUser() {
        User user = new User();
        Random random = new Random();
        user.setName(generateRandomName(random, 8));
        user.setEmail(generateRandomEmail(random, 10));
        user.setPassword("password" + random.nextInt(10_000));
        return user;
    }

    private String generateRandomName(Random random, int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder name = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            name.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return name.toString();
    }

    private String generateRandomEmail(Random random, int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder email = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            email.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        email.append("@example.com");
        return email.toString();
    }
}