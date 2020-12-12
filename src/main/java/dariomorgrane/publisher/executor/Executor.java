package dariomorgrane.publisher.executor;

import dariomorgrane.publisher.core.MessageGenerator;
import dariomorgrane.publisher.core.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Executor {

    private final MessageGenerator messageGenerator;
    private final MessageSender messageSender;
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(25);

    @Autowired
    public Executor(MessageGenerator messageGenerator, MessageSender messageSender) {
        this.messageGenerator = messageGenerator;
        this.messageSender = messageSender;
    }

    public void keepOnSending() throws Exception {
        new Producer().start();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 5; i++) {
            executorService.submit(new Consumer());
            Thread.sleep(3000);
        }
        executorService.shutdown();
    }

    private class Producer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    queue.put(messageGenerator.generateMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class Consumer implements Runnable {
        String message;

        @Override
        public void run() {
            while (true) {
                try {
                    message = queue.take();
                    messageSender.send(message);
                    Thread.sleep(15000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
