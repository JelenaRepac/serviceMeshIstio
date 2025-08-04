package com.airline.notificationservice.kafka;

import com.airline.notificationservice.kafka.event.EmailEvent;
import com.airline.notificationservice.kafka.event.NewFlightScheduleEvent;
import com.airline.notificationservice.kafka.event.UpcomingFlightNotificationEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Bean
    public ConsumerFactory<String, EmailEvent> consumerFactory() {
        JsonDeserializer<EmailEvent> deserializer = new JsonDeserializer<>(EmailEvent.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,  // or your Kafka broker
//                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092",  // or your Kafka broker
                        ConsumerConfig.GROUP_ID_CONFIG, "email-group",
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer
                ),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EmailEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, UpcomingFlightNotificationEvent> upcomingFlightConsumerFactory() {
        JsonDeserializer<UpcomingFlightNotificationEvent> deserializer =
                new JsonDeserializer<>(UpcomingFlightNotificationEvent.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,  // or your Kafka broker
//                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092",
                        ConsumerConfig.GROUP_ID_CONFIG, "notification-group",
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer
                ),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean(name = "upcomingFlightKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, UpcomingFlightNotificationEvent> upcomingFlightKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UpcomingFlightNotificationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(upcomingFlightConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, NewFlightScheduleEvent> newFlightConsumerFactory() {
        JsonDeserializer<NewFlightScheduleEvent> deserializer =
                new JsonDeserializer<>(NewFlightScheduleEvent.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,  // or your Kafka broker
//                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092",
                        ConsumerConfig.GROUP_ID_CONFIG, "notification-group",
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer
                ),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean(name = "newFlightKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, NewFlightScheduleEvent> newFlightKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NewFlightScheduleEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(newFlightConsumerFactory());
        return factory;
    }
}

