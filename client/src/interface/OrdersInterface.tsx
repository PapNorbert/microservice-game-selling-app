import { AnnouncementShort } from './Announcements/announcementsListShortInterface';
import { Pagination } from './paginationInterface';

export interface Orders {
  orders: Order[];
  pagination: Pagination
}

export interface Order {
  orderId: number;
  orderDate: string;
  price: number;
  orderAddress: string;
  announcement: AnnouncementShort;
}
