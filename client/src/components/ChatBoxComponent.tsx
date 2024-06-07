import { useEffect, useState } from 'react'
import Chatbox from 'react-group-chatup'

import useAuth from '../hooks/useAuth';
import { useStompClient, useSubscription } from 'react-stomp-hooks';
import { dateFormatOptions } from '../util/dateOptions';
import { ReceivedMessage } from '../interface/receivedMessageInterface';

interface PropType {
  receiverId: number;
  receiverUsername: string
}

export default function ChatBoxComponent({ receiverId, receiverUsername }: PropType) {
  const [messageList, setMessageList] = useState<ReceivedMessage[]>([]);
  const { auth } = useAuth();
  const stompClient = useStompClient();

  useSubscription(`/queue/history/${auth.userId}-${receiverId}`, (message) => {
    // queue format:  /queue/history/{selfId}-{senderId}
    const messages: ReceivedMessage[] = JSON.parse(message.body);
    messages.map(message => {
      // setting for chatBox message format
      if (message.senderUsername === auth.username) {
        message.auther = 'me';
      } else {
        message.auther = 'other';
      }
      message.auther_name = message.senderUsername;
      message.created_at = new Date(message.sentTime).toLocaleDateString('ro-RO', dateFormatOptions);
      return message;
    });
    setMessageList(messages);
  });

  useSubscription(`/queue/messages/${auth.userId}-${receiverId}`, (message) => {
    // queue format:  /queue/messages/{selfId}-{senderId}
    const messageReceived: ReceivedMessage = JSON.parse(message.body);
    if (messageReceived.senderId === receiverId && messageReceived.receiverId === auth.userId) {
      // setting for chatBox message format
      messageReceived.auther = 'other';
      messageReceived.auther_name = messageReceived.senderUsername;
      messageReceived.created_at = new Date(messageReceived.sentTime).toLocaleDateString('ro-RO', dateFormatOptions);
      setMessageList((list) => [...list, messageReceived]);
    }
  });

  useEffect(() => {
    if (stompClient) {
      stompClient.publish({ destination: `/ws/history/${auth.userId}-${receiverId}` });
    }
  }, [receiverId]);

  function handleMessageSend(data: string) {
    if (stompClient && auth.logged_in && auth.username && auth.userId) {
      const sentData = {
        senderId: auth.userId,
        senderUsername: auth.username,
        receiverId: receiverId,
        data: data
      }
      stompClient.publish({ destination: '/ws/chat/sendMessage', body: JSON.stringify(sentData) });
      // format message for the chat box
      const messageData: ReceivedMessage = {
        sentTime: new Date(Date.now()).toDateString(),
        senderId: auth.userId,
        auther_name: auth.username,
        auther: 'me',
        data: data,
        created_at: new Date(Date.now()).toLocaleDateString('ro-RO', dateFormatOptions)
      };
      setMessageList((list) => [...list, messageData]);
    }
  }

  return (auth.logged_in &&
    < Chatbox _onSendMessage={handleMessageSend}
      messages={messageList} avatar={false} theme='#e99000fa' sound={false}
      noMessagesText='No messages'
      brandName={receiverUsername}
    />
  );

}
