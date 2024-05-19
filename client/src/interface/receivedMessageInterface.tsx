export interface ReceivedMessage {
  entityId?: number;
  senderId: number;
  senderUsername?: string;
  receiverId?: number;
  data: string;
  sentTime: string;
  auther?: string;
  auther_name?: string;
  created_at?: string;
}