package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.AnnouncementEventDto;
import edu.ubb.consolegamesales.backend.dto.kafka.OrderAnnouncementReqDto;
import edu.ubb.consolegamesales.backend.dto.kafka.OrdersListAnnouncementsReqDto;
import edu.ubb.consolegamesales.backend.dto.kafka.TransactionAnnouncementUpdateDto;
import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.model.AnnouncementEvent;
import edu.ubb.consolegamesales.backend.repository.AnnouncementEventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumerService {
    private final AnnouncementService announcementService;
    private final AnnouncementTransactionService announcementTransactionService;
    private final AnnouncementEventRepository announcementEventRepository;

    @KafkaListener(topics = "${kafkaOrdersListAnnouncementsReq}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.OrdersListAnnouncementsReqDto"})
    public void listenToOrdersRequest(OrdersListAnnouncementsReqDto ordersListAnnouncementsReqDto) {
        LOGGER.info("Got request of announcements for orders of user {}",
                ordersListAnnouncementsReqDto.getUserId());
        announcementService.loadAnnouncementsOfOrders(ordersListAnnouncementsReqDto);
    }

    @KafkaListener(topics = "${kafkaOrderAnnouncementReqConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.OrderAnnouncementReqDto"})
    public void listenToOrderAnnouncementRequest(
            OrderAnnouncementReqDto orderAnnouncementReqDto) {
        LOGGER.info("Got request of announcement for order {}",
                orderAnnouncementReqDto.getOrderId());
        announcementService.loadAnnouncementOfOrderById(orderAnnouncementReqDto);
    }

    @KafkaListener(topics = "${kafkaOrderTransactionAnnouncCreateConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.TransactionAnnouncementUpdateDto"})
    public void listenToOrderTransactionAnnouncCreate(
            TransactionAnnouncementUpdateDto transactionAnnouncementUpdateDto) {
        LOGGER.info("Updating status to sold for announcement {}",
                transactionAnnouncementUpdateDto.getAnnouncementId());
        announcementTransactionService.transactionAnnouncementSold(
                transactionAnnouncementUpdateDto);
    }

    @KafkaListener(topics = "${kafkaOrderTransactionAnnouncDeleteConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.TransactionAnnouncementUpdateDto"})
    public void listenToOrderTransactionAnnouncDelete(
            TransactionAnnouncementUpdateDto transactionAnnouncementUpdateDto) {
        LOGGER.info("Updating status to not sold for announcement {}",
                transactionAnnouncementUpdateDto.getAnnouncementId());
        announcementTransactionService.transactionAnnouncementNotSold(
                transactionAnnouncementUpdateDto);
    }

    @KafkaListener(topics = "${kafkaAnnouncementEventConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.AnnouncementEventDto"})
    public void listenToAnnouncementEvent(
            AnnouncementEventDto announcementEventDto) {
        LOGGER.info("Got Announcement event for announcement {}",
                announcementEventDto.getAnnouncementId());
        Announcement announcement = new Announcement();
        announcement.setEntityId(announcementEventDto.getAnnouncementId());
        announcementEventRepository.saveAndFlush(
                new AnnouncementEvent(
                        announcement, announcementEventDto.getEvent(),
                        new Date(), announcementEventDto.getChangedByUserId()
                )
        );

    }
}
