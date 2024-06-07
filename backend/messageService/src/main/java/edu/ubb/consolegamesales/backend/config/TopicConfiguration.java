package edu.ubb.consolegamesales.backend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {
    private final String kafkaProduceTopicUserChattedWith;
    private final String kafkaMessageHistoryProduceTopic;

    public TopicConfiguration(
            @Value("${kafkaUsersChatProduceTopic}") String kafkaProduceTopicUserChattedWith,
            @Value("${kafkaMessageHistoryProduceTopic}") String kafkaMessageHistoryProduceTopic
    ) {
        this.kafkaProduceTopicUserChattedWith = kafkaProduceTopicUserChattedWith;
        this.kafkaMessageHistoryProduceTopic = kafkaMessageHistoryProduceTopic;
    }

    @Bean
    public NewTopic produceTopicUserChattedWith() {
        return TopicBuilder.name(kafkaProduceTopicUserChattedWith)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic produceTopicMessageHistory() {
        return TopicBuilder.name(kafkaMessageHistoryProduceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

}
