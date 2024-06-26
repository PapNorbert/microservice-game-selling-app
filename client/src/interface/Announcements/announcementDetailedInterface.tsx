import { GameDiscWithId } from '../gameDiscInterface';

export interface AnnouncementDetailedResponse {
  announcementId: number;
  sellerId:  number;
  soldGameDisc: GameDiscWithId;
  description: string;
  title: string;
  price: number;
  transportPaidBySeller: boolean;
  sold: boolean;
  newDisc: boolean;
  creationDate: string;
  savedByUser: boolean;
}