import { Pagination } from '../paginationInterface';

export interface AnnouncementsListShort {
    announcements: AnnouncementShort[];
    pagination: Pagination
}

export interface AnnouncementShort {
    announcementId: number;
    soldGameDiscName: string;
    title: string;
    price: number;
    transportPaidBySeller: boolean;
    newDisc: boolean;
    creationDate: string;
}
