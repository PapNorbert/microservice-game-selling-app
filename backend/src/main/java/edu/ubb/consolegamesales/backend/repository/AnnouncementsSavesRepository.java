package edu.ubb.consolegamesales.backend.repository;


import edu.ubb.consolegamesales.backend.model.AnnouncementsSaves;

public interface AnnouncementsSavesRepository {
    boolean existsByAnnouncementEntityIdAndUserEntityId(Long announcementEntityId, Long userEntityId);

    AnnouncementsSaves saveAndFlush(AnnouncementsSaves entity);

    void delete(AnnouncementsSaves entity);

    void deleteById(Long id);

}
