package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.TransactionAnnouncementUpdateDto;
import edu.ubb.consolegamesales.backend.dto.kafka.TransactionRespDto;
import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementTransactionService {
    private final AnnouncementRepository announcementRepository;
    private final RedisService redisService;

    private final String kafkaOrderTransactionAnnouncCreateProduceTopic;
    private final KafkaTemplate<String, TransactionRespDto> kafkaTemplateCreateResp;

    private final String kafkaOrderTransactionAnnouncDeleteProduceTopic;
    private final KafkaTemplate<String, TransactionRespDto> kafkaTemplateDeleteResp;

    public AnnouncementTransactionService(
            AnnouncementRepository announcementRepository,
            RedisService redisService,
            @Value("${kafkaOrderTransactionAnnouncCreateProduceTopic}")
            String kafkaOrderTransactionAnnouncCreateProduceTopic,
            KafkaTemplate<String, TransactionRespDto> kafkaTemplateCreateResp,
            @Value("${kafkaOrderTransactionAnnouncDeleteProduceTopic}")
            String kafkaOrderTransactionAnnouncDeleteProduceTopic,
            KafkaTemplate<String, TransactionRespDto> kafkaTemplateDeleteResp
    ) {
        this.announcementRepository = announcementRepository;
        this.redisService = redisService;
        this.kafkaOrderTransactionAnnouncCreateProduceTopic = kafkaOrderTransactionAnnouncCreateProduceTopic;
        this.kafkaTemplateCreateResp = kafkaTemplateCreateResp;
        this.kafkaOrderTransactionAnnouncDeleteProduceTopic = kafkaOrderTransactionAnnouncDeleteProduceTopic;
        this.kafkaTemplateDeleteResp = kafkaTemplateDeleteResp;
    }

    public void transactionAnnouncementSold(
            TransactionAnnouncementUpdateDto transactionAnnouncementUpdateDto) {

        Long announcementId = transactionAnnouncementUpdateDto.getAnnouncementId();
        Announcement announcement = redisService.getCachedAnnouncement(
                announcementId);
        if (announcement == null) {
            announcement = announcementRepository.findByEntityId(
                    announcementId).orElse(null);
        }
        TransactionRespDto transactionRespDtoFailed = new TransactionRespDto(
                transactionAnnouncementUpdateDto.getOrder(),
                announcementId,
                false);
        if (announcement == null) {
            // announcement not found
            kafkaTemplateCreateResp.send(
                    kafkaOrderTransactionAnnouncCreateProduceTopic,
                    announcementId.toString(),
                    transactionRespDtoFailed
            );
            return;
        }
        if (announcement.getSold()) {
            // announcement already sold
            kafkaTemplateCreateResp.send(
                    kafkaOrderTransactionAnnouncCreateProduceTopic,
                    announcementId.toString(),
                    transactionRespDtoFailed
            );
            return;
        }
        announcement.setSold(true);
        try {
            announcementRepository.update(announcement.getEntityId(), announcement);
            redisService.deleteCachedAnnouncement(announcement.getEntityId());
            // send response, announcement updated
            kafkaTemplateCreateResp.send(
                    kafkaOrderTransactionAnnouncCreateProduceTopic,
                    announcementId.toString(),
                    new TransactionRespDto(
                            transactionAnnouncementUpdateDto.getOrder(),
                            announcementId,
                            true
                    )
            );
        } catch (Exception e) {
            // any error during update, transaction fails
            kafkaTemplateCreateResp.send(
                    kafkaOrderTransactionAnnouncCreateProduceTopic,
                    announcementId.toString(),
                    transactionRespDtoFailed
            );
        }

    }

    public void transactionAnnouncementNotSold(
            TransactionAnnouncementUpdateDto transactionAnnouncementUpdateDto) {

        Long announcementId = transactionAnnouncementUpdateDto.getAnnouncementId();
        Announcement announcement = redisService.getCachedAnnouncement(
                announcementId);
        if (announcement == null) {
            announcement = announcementRepository.findByEntityId(
                    announcementId).orElse(null);
        }
        TransactionRespDto transactionRespDtoFailed = new TransactionRespDto(
                transactionAnnouncementUpdateDto.getOrder(),
                announcementId,
                false);
        if (announcement == null) {
            // announcement not found
            kafkaTemplateDeleteResp.send(
                    kafkaOrderTransactionAnnouncDeleteProduceTopic,
                    announcementId.toString(),
                    transactionRespDtoFailed
            );
            return;
        }
        announcement.setSold(false);
        try {
            announcementRepository.update(announcement.getEntityId(), announcement);
            redisService.deleteCachedAnnouncement(announcement.getEntityId());
            kafkaTemplateDeleteResp.send(
                    kafkaOrderTransactionAnnouncDeleteProduceTopic,
                    announcementId.toString(),
                    new TransactionRespDto(
                            transactionAnnouncementUpdateDto.getOrder(),
                            announcementId,
                            true)
            );
        } catch (Exception e) {
            // any error during update, transaction fails
            kafkaTemplateDeleteResp.send(
                    kafkaOrderTransactionAnnouncDeleteProduceTopic,
                    announcementId.toString(),
                    transactionRespDtoFailed
            );
        }


    }
}
