package edu.ubb.consolegamesales.backend.repository;


import edu.ubb.consolegamesales.backend.model.AnnouncementsSaves;

public interface AnnouncementsSavesRepository extends Repository<AnnouncementsSaves, Long> {

    boolean existsByAnnouncementEntityIdAndUserId(Long announcementEntityId, Long userEntityId);

    AnnouncementsSaves saveAndFlush(AnnouncementsSaves entity);

    void delete(AnnouncementsSaves entity);

    void deleteById(Long id);

    void deleteByAnnouncementEntityIdAndUserId(Long announcementEntityId, Long userEntityId);

    void deleteByAnnouncementEntityId(Long announcementEntityId);

}
