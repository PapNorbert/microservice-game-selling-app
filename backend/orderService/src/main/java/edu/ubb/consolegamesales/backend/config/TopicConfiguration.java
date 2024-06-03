package edu.ubb.consolegamesales.backend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {
    private final String kafkaOrdersListAnnouncementsReqTopic;

    public TopicConfiguration(
            @Value("${kafkaOrdersListAnnouncementsReq}") String kafkaOrdersListAnnouncementsReqTopic
    ) {
        this.kafkaOrdersListAnnouncementsReqTopic = kafkaOrdersListAnnouncementsReqTopic;
    }

    @Bean
    public NewTopic produceTopickafkaOrdersListAnnouncementsReq() {
        return TopicBuilder.name(kafkaOrdersListAnnouncementsReqTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }


}
