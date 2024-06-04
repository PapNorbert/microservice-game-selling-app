package edu.ubb.consolegamesales.backend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {
    private final String kafkaOrdersListAnnouncementsResponseTopic;
    private final String kafkaOrderTransactionAnnouncCreateProduceTopic;
    private final String kafkaOrderTransactionAnnouncDeleteProduceTopic;

    public TopicConfiguration(
            @Value("${kafkaOrdersListAnnouncementsResponse}")
            String kafkaOrdersListAnnouncementsResponseTopic,
            @Value("${kafkaOrderTransactionAnnouncCreateProduceTopic}")
            String kafkaOrderTransactionAnnouncCreateProduceTopic,
            @Value("${kafkaOrderTransactionAnnouncDeleteProduceTopic}")
            String kafkaOrderTransactionAnnouncDeleteProduceTopic
    ) {
        this.kafkaOrdersListAnnouncementsResponseTopic = kafkaOrdersListAnnouncementsResponseTopic;
        this.kafkaOrderTransactionAnnouncCreateProduceTopic = kafkaOrderTransactionAnnouncCreateProduceTopic;
        this.kafkaOrderTransactionAnnouncDeleteProduceTopic = kafkaOrderTransactionAnnouncDeleteProduceTopic;
    }

    @Bean
    public NewTopic produceTopicOrdersListAnnouncementsResponse() {
        return TopicBuilder.name(kafkaOrdersListAnnouncementsResponseTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic produceTopicOrderTransactionAnnouncCreate() {
        return TopicBuilder.name(kafkaOrderTransactionAnnouncCreateProduceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic produceTopicOrderTransactionAnnouncDelete() {
        return TopicBuilder.name(kafkaOrderTransactionAnnouncDeleteProduceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
