import { Pagination } from '../paginationInterface';

export interface AnnouncementsListShort {
    announcements: AnnouncementsShort[];
    pagination: Pagination
}

interface AnnouncementsShort {
    announcementId: number;
    soldGameDiscName: string;
    title: string;
    price: number;
    transportPaidBySeller: boolean;
    newDisc: boolean;
    creationDate: string;
}
