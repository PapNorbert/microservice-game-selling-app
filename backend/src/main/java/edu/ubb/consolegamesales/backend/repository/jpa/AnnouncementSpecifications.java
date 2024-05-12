package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.model.AnnouncementsSaves;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

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

    public static Specification<Announcement> isSavedByUserWithId(Long savedByUserWithId) {
        return (announcementRoot, criteriaQuery, criteriaBuilder) -> {
            Subquery<Announcement> subquery = criteriaQuery.subquery(Announcement.class);
            Root<AnnouncementsSaves> announcementsSavesRoot = subquery.from(AnnouncementsSaves.class);
            subquery.select(announcementsSavesRoot.get("announcement"));
            subquery.where(criteriaBuilder.equal(announcementsSavesRoot.get("user").get("id"), savedByUserWithId));
            return criteriaQuery.where(announcementRoot.in(subquery)).getRestriction();
        };
    }

    public static Specification<Announcement> createdAfter(LocalDateTime date) {
        return (announcementRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(announcementRoot.get("creationDate"), date);
    }

    public static Specification<Announcement> isCreatedByUserWithId(Long userId) {
        return (announcementRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(announcementRoot.get("seller").get("id"), userId);
    }
}
