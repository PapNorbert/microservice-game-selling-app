package edu.ubb.consolegamesales.backend.service;

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
    public void listenToMessageHistoryTopic(OrdersListAnnouncementsReqDto ordersListAnnouncementsReqDto) {
        LOGGER.info("Got request of announcements for orders of user {}",
                ordersListAnnouncementsReqDto.getUserId());
        announcementService.loadAnnouncementsOfOrders(ordersListAnnouncementsReqDto);
    }
}
