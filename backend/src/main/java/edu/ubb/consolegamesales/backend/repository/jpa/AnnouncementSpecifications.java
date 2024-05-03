package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Announcement;
import org.springframework.data.jpa.domain.Specification;

public class AnnouncementSpecifications {

    public static Specification<Announcement> isSold(Boolean sold) {
        return (announcementRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(announcementRoot.get("sold"), sold);
    }

    public static Specification<Announcement> discNameContains(String discName) {
        return (announcementRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(announcementRoot.get("soldGameDisc").get("name"), "%" + discName + "%");
    }

    public static Specification<Announcement> hasConsoleType(String consoleType) {
        return (announcementRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(announcementRoot.get("soldGameDisc").get("type"), consoleType);
    }

    public static Specification<Announcement> isTransportPaidBySeller(Boolean paidBySeller) {
        return (announcementRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(announcementRoot.get("transportPaidBySeller"), paidBySeller);
    }

    public static Specification<Announcement> isNewDisc(Boolean newDisc) {
        return (announcementRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(announcementRoot.get("newDisc"), newDisc);
    }

    public static Specification<Announcement> priceGreaterOrEqualThen(double price) {
        return (announcementRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(announcementRoot.get("price"), price);
    }

    public static Specification<Announcement> priceLessOrEqualThen(double price) {
        return (announcementRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(announcementRoot.get("price"), price);
    }
}
