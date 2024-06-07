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
    private final String kafkaOrderTransactionOrderCreateProduceTopic;
    private final String kafkaOrderTransactionOrderDeleteProduceTopic;

    public TopicConfiguration(
            @Value("${kafkaOrdersListAnnouncementsReq}") String kafkaOrdersListAnnouncementsReqTopic,
            @Value("${kafkaOrderResponseProduceTopic}") String kafkaOrderResponseProduceTopic,
            @Value("${kafkaOrderTransactionOrderCreateProduceTopic}")
            String kafkaOrderTransactionOrderCreateProduceTopic,
            @Value("${kafkaOrderTransactionOrderDeleteProduceTopic}")
            String kafkaOrderTransactionOrderDeleteProduceTopic
    ) {
        this.kafkaOrdersListAnnouncementsReqTopic = kafkaOrdersListAnnouncementsReqTopic;
        this.kafkaOrderResponseProduceTopic = kafkaOrderResponseProduceTopic;
        this.kafkaOrderTransactionOrderCreateProduceTopic = kafkaOrderTransactionOrderCreateProduceTopic;
        this.kafkaOrderTransactionOrderDeleteProduceTopic = kafkaOrderTransactionOrderDeleteProduceTopic;
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

    @Bean
    public NewTopic produceTopicOrderTransactionOrderCreate() {
        return TopicBuilder.name(kafkaOrderTransactionOrderCreateProduceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic produceTopicOrderTransactionOrderDelete() {
        return TopicBuilder.name(kafkaOrderTransactionOrderDeleteProduceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
