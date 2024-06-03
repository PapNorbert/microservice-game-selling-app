package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.OrderAnnouncementReqDto;
import edu.ubb.consolegamesales.backend.dto.kafka.OrdersListAnnouncementsReqDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumerService {
    private final AnnouncementService announcementService;

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
}
