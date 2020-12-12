package dariomorgrane.publisher;

import dariomorgrane.publisher.executor.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class PublisherApplication implements CommandLineRunner {

    private static ConfigurableApplicationContext applicationContext;
    private Executor executor;

    @Autowired
    public PublisherApplication(Executor executor) {
        this.executor = executor;
    }

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(PublisherApplication.class, args);
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

}
