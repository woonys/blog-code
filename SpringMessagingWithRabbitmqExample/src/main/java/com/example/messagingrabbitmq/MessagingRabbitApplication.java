package com.example.messagingrabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MessagingRabbitApplication {
	static final String topicExchangeName = "spring-boot-exchange";
	static final String queueName = "spring-boot";

	@Bean
	Queue queue() { // 메시지 큐
		//  AMQP queue를 만든다.
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		/** topic exchange 를 생성
		 * Topic exchange: Simple container collecting information to describe a topic exchange. Used in conjunction with administrative operations.
		 */
		return new TopicExchange(topicExchangeName);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) { // queue와 exchange를 묶어주는(binding) 애
		/** RabbitTemplate이 exchange에 publish할 때 일어나는 행동을 정의.
		 *  foo.bar.#: 라우팅 키(Routing key). 큐는 라우팅 키에 묶이는데, 이는 즉 foo.bar.로 시작하는 라우팅 키를 가진 어떤 메시지든 받으면 큐로 라우팅한다는 뜻.
		 */
		return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
	}

	@Bean
	// Message listener container 구성
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	// receiver 메소드는 순수 자바코드이므로 이를 래핑해주는 게 MessageListenerAdapter. 얘가 spring-boot queue에 들어있는 메시지를 듣는다.
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) {
		SpringApplication.run(MessagingRabbitApplication.class, args).close();
	}

}
