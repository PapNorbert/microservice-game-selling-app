package edu.ubb.consolegamesales.backend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {
    private final String kafkaOrdersListAnnouncementsReqTopic;
    private final String kafkaOrderResponseProduceTopic;

    public TopicConfiguration(
            @Value("${kafkaOrdersListAnnouncementsReq}") String kafkaOrdersListAnnouncementsReqTopic,
            @Value("${kafkaOrderResponseProduceTopic}") String kafkaOrderResponseProduceTopic
    ) {
        this.kafkaOrdersListAnnouncementsReqTopic = kafkaOrdersListAnnouncementsReqTopic;
        this.kafkaOrderResponseProduceTopic = kafkaOrderResponseProduceTopic;
    }

    @Bean
    public NewTopic produceTopicOrdersListAnnouncementsReq() {
        return TopicBuilder.name(kafkaOrdersListAnnouncementsReqTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic produceTopicOrderResponse() {
        return TopicBuilder.name(kafkaOrderResponseProduceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
