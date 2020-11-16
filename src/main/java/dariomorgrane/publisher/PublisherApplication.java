package dariomorgrane.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import dariomorgrane.publisher.executors.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Scanner;

@SpringBootApplication
public class PublisherApplication implements CommandLineRunner {

    private static ConfigurableApplicationContext applicationContext;
    private Executor executor;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(PublisherApplication.class, args);
    }

    @Autowired
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void run(String... args) throws Exception {
        executor.keepOnSending();
        new Thread(() -> {
            new Scanner(System.in).nextLine();
            int exitCode = SpringApplication.exit(applicationContext, () -> 0);
            System.exit(exitCode);
        }).start();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpHeaders httpHeaders() {
        return new HttpHeaders();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Random random() {
        return new Random();
    }

}
