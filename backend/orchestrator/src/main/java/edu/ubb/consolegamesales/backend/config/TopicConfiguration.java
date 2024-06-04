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
    private final String kafkaOrderTransactionOrderCreateProduceTopic;
    private final String kafkaOrderTransactionOrderDeleteProduceTopic;
    private final String kafkaOrderTransactionAnnouncCreateProduceTopic;
    private final String kafkaOrderTransactionAnnouncDeleteProduceTopic;
    private final String kafkaOrderTransactionRespProduceTopic;
    private final String kafkaOrderTransactionCompensationProduceTopic;

    public TopicConfiguration(
            @Value("${kafkaOrderListOfUserReqTopic}") String kafkaOrderListOfUserReqTopic,
            @Value("${kafkaOrderDataReqProduceTopic}") String kafkaOrderDataReqProduceTopic,
            @Value("${kafkaOrderAnnouncementReqProduceTopic}") String kafkaOrderAnnouncementReqProduceTopic,
            @Value("${kafkaOrderTransactionOrderCreateProduceTopic}")
            String kafkaOrderTransactionOrderCreateProduceTopic,
            @Value("${kafkaOrderTransactionOrderDeleteProduceTopic}")
            String kafkaOrderTransactionOrderDeleteProduceTopic,
            @Value("${kafkaOrderTransactionAnnouncCreateProduceTopic}")
            String kafkaOrderTransactionAnnouncCreateProduceTopic,
            @Value("${kafkaOrderTransactionAnnouncDeleteProduceTopic}")
            String kafkaOrderTransactionAnnouncDeleteProduceTopic,
            @Value("${kafkaOrderTransactionRespProduceTopic}")
            String kafkaOrderTransactionRespProduceTopic,
            @Value("${kafkaOrderTransactionCompensationProduceTopic}")
            String kafkaOrderTransactionCompensationProduceTopic
            ) {
        this.kafkaOrderListOfUserReqTopic = kafkaOrderListOfUserReqTopic;
        this.kafkaOrderDataReqProduceTopic = kafkaOrderDataReqProduceTopic;
        this.kafkaOrderAnnouncementReqProduceTopic = kafkaOrderAnnouncementReqProduceTopic;
        this.kafkaOrderTransactionOrderCreateProduceTopic = kafkaOrderTransactionOrderCreateProduceTopic;
        this.kafkaOrderTransactionOrderDeleteProduceTopic = kafkaOrderTransactionOrderDeleteProduceTopic;
        this.kafkaOrderTransactionAnnouncCreateProduceTopic = kafkaOrderTransactionAnnouncCreateProduceTopic;
        this.kafkaOrderTransactionAnnouncDeleteProduceTopic = kafkaOrderTransactionAnnouncDeleteProduceTopic;
        this.kafkaOrderTransactionRespProduceTopic = kafkaOrderTransactionRespProduceTopic;
        this.kafkaOrderTransactionCompensationProduceTopic = kafkaOrderTransactionCompensationProduceTopic;
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

    @Bean
    public NewTopic produceTopicOrderTransactionCompensation() {
        return TopicBuilder.name(kafkaOrderTransactionCompensationProduceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic produceTopicOrderTransactionResp() {
        return TopicBuilder.name(kafkaOrderTransactionRespProduceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
