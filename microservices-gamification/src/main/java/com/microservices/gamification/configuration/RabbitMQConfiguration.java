package com.microservices.gamification.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;


/**
 * Created by mihir.desai on 3/8/2018.
 */
@Configuration
public class RabbitMQConfiguration implements RabbitListenerConfigurer {
    private static final Logger logger = LogManager.getLogger(RabbitMQConfiguration.class);

    @Autowired
    private Environment env;

    @Bean
    public ConnectionFactory connectionFactory() {
        String host = env.getProperty("spring.rabbitmq.host");
        String username = env.getProperty("spring.rabbitmq.username");
        String password = env.getProperty("spring.rabbitmq.password");
        int port = Integer.valueOf(env.getProperty("spring.rabbitmq.port"));
        logger.debug("Connection factory for host {} and port {}",host,port);
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setVirtualHost("/test");
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        return connectionFactory;
    }

    @Bean
    public TopicExchange multiplicationExchange(@Value("${multiplication.exchange}") final String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue gamificationMultiplicationQueue(@Value("${multiplication.queue}") final String queueName) {
        return new Queue(queueName,true);
    }

    @Bean
    Binding binding(final Queue queue,final TopicExchange topicExchange,@Value("${multiplication.anything.routing-key}") final String routingKey) {
        return BindingBuilder.bind(queue).to(topicExchange).with(routingKey);
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    /*@Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        *//*factory.setMessageConverter((MessageConverter) consumerJackson2MessageConverter());*//*
        return factory;
    }*/

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
       /*rabbitListenerEndpointRegistrar.setContainerFactory(rabbitListenerContainerFactory());*/
        rabbitListenerEndpointRegistrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}
