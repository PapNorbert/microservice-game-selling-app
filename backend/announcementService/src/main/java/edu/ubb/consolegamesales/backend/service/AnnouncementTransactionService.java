package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.TransactionAnnouncementUpdateDto;
import edu.ubb.consolegamesales.backend.dto.kafka.TransactionAnnouncementRespDto;
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
    private final KafkaTemplate<String, TransactionAnnouncementRespDto> kafkaTemplateCreateResp;

    private final String kafkaOrderTransactionAnnouncDeleteProduceTopic;
    private final KafkaTemplate<String, TransactionAnnouncementRespDto> kafkaTemplateDeleteResp;

    public AnnouncementTransactionService(
            AnnouncementRepository announcementRepository,
            RedisService redisService,
            @Value("${kafkaOrderTransactionAnnouncCreateProduceTopic}")
            String kafkaOrderTransactionAnnouncCreateProduceTopic,
            KafkaTemplate<String, TransactionAnnouncementRespDto> kafkaTemplateCreateResp,
            @Value("${kafkaOrderTransactionAnnouncDeleteProduceTopic}")
            String kafkaOrderTransactionAnnouncDeleteProduceTopic,
            KafkaTemplate<String, TransactionAnnouncementRespDto> kafkaTemplateDeleteResp
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
        TransactionAnnouncementRespDto transactionAnnouncementRespDtoFailed = new TransactionAnnouncementRespDto(
                transactionAnnouncementUpdateDto.getOrder(),
                announcementId, false,
                transactionAnnouncementUpdateDto.getUserId());
        if (announcement == null) {
            // announcement not found
            kafkaTemplateCreateResp.send(
                    kafkaOrderTransactionAnnouncCreateProduceTopic,
                    announcementId.toString(),
                    transactionAnnouncementRespDtoFailed
            );
            return;
        }
        if (announcement.getSold()) {
            // announcement already sold
            kafkaTemplateCreateResp.send(
                    kafkaOrderTransactionAnnouncCreateProduceTopic,
                    announcementId.toString(),
                    transactionAnnouncementRespDtoFailed
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
                    new TransactionAnnouncementRespDto(
                            transactionAnnouncementUpdateDto.getOrder(),
                            announcementId,
                            true,
                            transactionAnnouncementUpdateDto.getUserId()
                    )
            );
        } catch (Exception e) {
            // any error during update, transaction fails
            kafkaTemplateCreateResp.send(
                    kafkaOrderTransactionAnnouncCreateProduceTopic,
                    announcementId.toString(),
                    transactionAnnouncementRespDtoFailed
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
        TransactionAnnouncementRespDto transactionAnnouncementRespDtoFailed = new TransactionAnnouncementRespDto(
                transactionAnnouncementUpdateDto.getOrder(),
                announcementId, false,
                transactionAnnouncementUpdateDto.getUserId());
        if (announcement == null) {
            // announcement not found
            kafkaTemplateDeleteResp.send(
                    kafkaOrderTransactionAnnouncDeleteProduceTopic,
                    announcementId.toString(),
                    transactionAnnouncementRespDtoFailed
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
                    new TransactionAnnouncementRespDto(
                            transactionAnnouncementUpdateDto.getOrder(),
                            announcementId,
                            true,
                            transactionAnnouncementUpdateDto.getUserId())
            );
        } catch (Exception e) {
            // any error during update, transaction fails
            kafkaTemplateDeleteResp.send(
                    kafkaOrderTransactionAnnouncDeleteProduceTopic,
                    announcementId.toString(),
                    transactionAnnouncementRespDtoFailed
            );
        }

    }
}
