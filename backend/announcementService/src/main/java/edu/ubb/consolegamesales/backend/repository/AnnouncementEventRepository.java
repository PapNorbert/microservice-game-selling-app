package edu.ubb.consolegamesales.backend.repository;


import edu.ubb.consolegamesales.backend.model.AnnouncementEvent;

public interface AnnouncementEventRepository {
    AnnouncementEvent saveAndFlush(AnnouncementEvent announcementEvent);
}
