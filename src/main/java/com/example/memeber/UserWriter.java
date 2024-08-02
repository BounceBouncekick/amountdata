package com.example.memeber;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserWriter implements ItemWriter<User> {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void write(Chunk<? extends User> chunk) throws Exception {
        List<? extends User> users = chunk.getItems();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            for (User user : users) {
                if (!entityManager.contains(user)) {
                    entityManager.merge(user); // 또는 entityManager.persist(user);
                }
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }
}