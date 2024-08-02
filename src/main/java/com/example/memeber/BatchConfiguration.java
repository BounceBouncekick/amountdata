package com.example.memeber;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@EnableTransactionManagement
public class BatchConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final UserWriter userWriter;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    @Autowired
    public BatchConfiguration(EntityManagerFactory entityManagerFactory,
                              UserWriter userWriter,
                              PlatformTransactionManager transactionManager,
                              JobRepository jobRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.userWriter = userWriter;
        this.transactionManager = transactionManager;
        this.jobRepository = jobRepository;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16); // 최소 스레드 개수 설정
        executor.setMaxPoolSize(32); // 최대 스레드 개수 설정
        executor.setQueueCapacity(200); // 대기 큐 용량 설정
        executor.setThreadNamePrefix("step1-"); // 스레드 이름 접두사 설정
        executor.initialize();
        return executor;
    }

    @Bean
    public ItemReader<User> reader(long count) {
        return new UserReader(count);
    }

    @Bean
    public ItemProcessor<User, User> processor() {
        return new UserProcessor();
    }

    @Bean
    public ItemWriter<User> writer() {
        return userWriter;
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1(100_000_000)) // 기본값으로 500만 개
                .build();
    }

    @Bean
    public Step step1(long count) {
        return new StepBuilder("step1", jobRepository)
                .<User, User>chunk(1_000, transactionManager)
                .reader(reader(count))
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor()) // taskExecutor 추가
                .build();
    }

    private List<User> generateUserDtos(long count) {
        List<User> users = new ArrayList<>();
        Random random = new Random();
        for (long i = 0; i < count; i++) {
            User user = new User();
            user.setName(generateRandomName(random, 8));
            user.setEmail(generateRandomEmail(random, 10));
            user.setPassword("password" + random.nextInt(10_000));
            users.add(user);
        }
        return users;
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