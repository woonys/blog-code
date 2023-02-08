package com.example.messagingrabbitmq;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.receiver = receiver;
    }

    @Override
    public void run(String... args){
        try {
            System.out.println("Sending message...");
            rabbitTemplate.convertAndSend(MessagingRabbitApplication.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
            Thread.sleep(10000);
            receiver.getLatch().await(1, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
