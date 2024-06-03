package edu.ubb.consolegamesales.backend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {
    private final String kafkaOrderListOfUserReqTopic;
    private final String kafkaOrderDataReqProduceTopic;
    private final String kafkaOrderAnnouncementReqProduceTopic;

    public TopicConfiguration(
            @Value("${kafkaOrderListOfUserReqTopic}") String kafkaOrderListOfUserReqTopic,
            @Value("${kafkaOrderDataReqProduceTopic}") String kafkaOrderDataReqProduceTopic,
            @Value("${kafkaOrderAnnouncementReqProduceTopic}") String kafkaOrderAnnouncementReqProduceTopic
    ) {
        this.kafkaOrderListOfUserReqTopic = kafkaOrderListOfUserReqTopic;
        this.kafkaOrderDataReqProduceTopic = kafkaOrderDataReqProduceTopic;
        this.kafkaOrderAnnouncementReqProduceTopic = kafkaOrderAnnouncementReqProduceTopic;
    }

    @Bean
    public NewTopic produceTopicOrderListOfUserReq() {
        return TopicBuilder.name(kafkaOrderListOfUserReqTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic produceTopicOrderDataReq() {
        return TopicBuilder.name(kafkaOrderDataReqProduceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic produceTopicOrderAnnouncementReq() {
        return TopicBuilder.name(kafkaOrderAnnouncementReqProduceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
