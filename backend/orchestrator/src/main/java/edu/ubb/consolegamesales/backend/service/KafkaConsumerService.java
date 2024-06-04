package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {
    private final String kafkaOrderTransactionAnnouncCreateProduceTopic;
    private final KafkaTemplate<String, TransactionAnnouncementUpdateDto>
            kafkaTemplateOrderTransactionAnnouncCreate;

    private final String kafkaOrderTransactionAnnouncDeleteProduceTopic;
    private final KafkaTemplate<String, TransactionAnnouncementUpdateDto>
            kafkaTemplateOrderTransactionAnnouncDelete;

    private final String kafkaOrderTransactionRespProduceTopic;
    private final KafkaTemplate<String, TransactionRespDto>
            kafkaTemplateOrderTransactionResp;

    private final String kafkaOrderTransactionCompensationProduceTopic;
    private final KafkaTemplate<String, TransactionCompensationDto>
            kafkaTemplateOrderTransactionCompensation;


    public KafkaConsumerService(
            @Value("${kafkaOrderTransactionAnnouncCreateProduceTopic}")
            String kafkaOrderTransactionAnnouncCreateProduceTopic,
            KafkaTemplate<String, TransactionAnnouncementUpdateDto>
                    kafkaTemplateOrderTransactionAnnouncCreate,
            @Value("${kafkaOrderTransactionAnnouncDeleteProduceTopic}")
            String kafkaOrderTransactionAnnouncDeleteProduceTopic,
            KafkaTemplate<String, TransactionAnnouncementUpdateDto>
                    kafkaTemplateOrderTransactionAnnouncDelete,
            @Value("${kafkaOrderTransactionRespProduceTopic}")
            String kafkaOrderTransactionRespProduceTopic,
            KafkaTemplate<String, TransactionRespDto>
                    kafkaTemplateOrderTransactionResp,
            @Value("${kafkaOrderTransactionCompensationProduceTopic}")
            String kafkaOrderTransactionCompensationProduceTopic,
            KafkaTemplate<String, TransactionCompensationDto>
                    kafkaTemplateOrderTransactionCompensation
    ) {
        this.kafkaOrderTransactionAnnouncCreateProduceTopic = kafkaOrderTransactionAnnouncCreateProduceTopic;
        this.kafkaTemplateOrderTransactionAnnouncCreate = kafkaTemplateOrderTransactionAnnouncCreate;
        this.kafkaOrderTransactionAnnouncDeleteProduceTopic = kafkaOrderTransactionAnnouncDeleteProduceTopic;
        this.kafkaTemplateOrderTransactionAnnouncDelete = kafkaTemplateOrderTransactionAnnouncDelete;
        this.kafkaOrderTransactionRespProduceTopic = kafkaOrderTransactionRespProduceTopic;
        this.kafkaTemplateOrderTransactionResp = kafkaTemplateOrderTransactionResp;
        this.kafkaOrderTransactionCompensationProduceTopic = kafkaOrderTransactionCompensationProduceTopic;
        this.kafkaTemplateOrderTransactionCompensation = kafkaTemplateOrderTransactionCompensation;
    }


    @KafkaListener(topics = "${kafkaOrderTransactionOrderCreateConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.TransactionOrderCreateRespDto"})
    public void listenToOrderTransactionOrderCreateRespTopic(
            TransactionOrderCreateRespDto transactionOrderCreateRespDto) {
        LOGGER.info("Got response from order creation transaction, order part");
        if (transactionOrderCreateRespDto.isTransactionSuccess()) {
            kafkaTemplateOrderTransactionAnnouncCreate.send(
                    kafkaOrderTransactionAnnouncCreateProduceTopic,
                    transactionOrderCreateRespDto.getAnnouncementId().toString(),
                    new TransactionAnnouncementUpdateDto(
                            transactionOrderCreateRespDto.getOrder(),
                            transactionOrderCreateRespDto.getAnnouncementId()
                    )
            );
        } else {
            // transaction failed, can send response to client with failure
            kafkaTemplateOrderTransactionResp.send(
                    kafkaOrderTransactionRespProduceTopic,
                    new TransactionRespDto(
                            null,
                            null,
                            false,
                            true
                    )
            );
        }
    }


    @KafkaListener(topics = "${kafkaOrderTransactionOrderDeleteConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.TransactionOrderDeleteRespDto"})
    public void listenToOrderTransactionOrderDeleteRespTopic(
            TransactionOrderDeleteRespDto transactionOrderDeleteRespDto) {
        LOGGER.info("Got response from order deletion transaction, order part");
        if (transactionOrderDeleteRespDto.isTransactionSuccess()) {
            kafkaTemplateOrderTransactionAnnouncDelete.send(
                    kafkaOrderTransactionAnnouncDeleteProduceTopic,
                    transactionOrderDeleteRespDto.getAnnouncementId().toString(),
                    new TransactionAnnouncementUpdateDto(
                            transactionOrderDeleteRespDto.getOrder(),
                            transactionOrderDeleteRespDto.getAnnouncementId()
                    )
            );
        } else {
            // transaction failed, can send response to client with failure
            kafkaTemplateOrderTransactionResp.send(
                    kafkaOrderTransactionRespProduceTopic,
                    new TransactionRespDto(
                            transactionOrderDeleteRespDto.getOrder().getEntityId(),
                            null,
                            false,
                            false
                    )
            );
        }
    }

    @KafkaListener(topics = "${kafkaOrderTransactionAnnouncCreateConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.TransactionAnnouncementRespDto"})
    public void listenToOrderTransactionAnnouncCreate(
            TransactionAnnouncementRespDto transactionAnnouncementRespDto
    ) {
        LOGGER.info("Got response from order creation transaction, announcement part");
        Long announcementId = transactionAnnouncementRespDto.getAnnouncementId();
        if (transactionAnnouncementRespDto.isTransactionSuccess()) {
            // transaction successful, can send response to client
            kafkaTemplateOrderTransactionResp.send(
                    kafkaOrderTransactionRespProduceTopic,
                    announcementId.toString(),
                    new TransactionRespDto(
                            transactionAnnouncementRespDto.getOrder().getEntityId(),
                            announcementId,
                            true,
                            true
                    )
            );
            // TODO AnnouncementEvents create

        } else {
            // transaction failed, can send response to client with failure
            kafkaTemplateOrderTransactionResp.send(
                    kafkaOrderTransactionRespProduceTopic,
                    new TransactionRespDto(
                            null,
                            null,
                            false,
                            true
                    )
            );
            // TODO transaction compensation
        }
    }


    @KafkaListener(topics = "${kafkaOrderTransactionAnnouncDeleteConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.TransactionAnnouncementRespDto"})
    public void listenToOrderTransactionAnnouncDelete(
            TransactionAnnouncementRespDto transactionAnnouncementRespDto
    ) {
        LOGGER.info("Got response from order deletion transaction, announcement part");
        Long announcementId = transactionAnnouncementRespDto.getAnnouncementId();
        if (transactionAnnouncementRespDto.isTransactionSuccess()) {
            // transaction successful, can send response to client
            kafkaTemplateOrderTransactionResp.send(
                    kafkaOrderTransactionRespProduceTopic,
                    announcementId.toString(),
                    new TransactionRespDto(
                            transactionAnnouncementRespDto.getOrder().getEntityId(),
                            announcementId,
                            true,
                            false
                    )
            );
            // TODO AnnouncementEvents delete

        } else {
            // transaction failed, can send response to client with failure
            kafkaTemplateOrderTransactionResp.send(
                    kafkaOrderTransactionRespProduceTopic,
                    new TransactionRespDto(
                            transactionAnnouncementRespDto.getOrder().getEntityId(),
                            null,
                            false,
                            false
                    )
            );
            // TODO transaction compensation
        }
    }
}
