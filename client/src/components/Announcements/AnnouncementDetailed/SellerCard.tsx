import { Button, Card, Col, Row, Stack } from 'react-bootstrap'
import { dateFormatShortOptions } from '../../../util/dateOptions';
import { ChevronRight } from 'react-bootstrap-icons';
import { useNavigate } from 'react-router-dom';
import useAuth from '../../../hooks/useAuth';
import { useState } from 'react';
import ChatBoxComponent from '../../ChatBoxComponent';

interface PropType {
  sellerId: number;
  sellerUsername: string;
  sellerRegistrationDate: string
}

export default function SellerCard({ sellerId, sellerUsername, sellerRegistrationDate }: PropType) {
  const navigate = useNavigate();
  const { auth } = useAuth();
  const [chatOpen, setChatOpen] = useState<boolean>(false);

  function handleSendMessageClicked() {
    setChatOpen(true);
  }

  return (
    <>
      <Card key={`container_order`} className='mt-4 mb-3'>
        <Row className='fw-semibold fs-5 ms-5'>
          Contact The Seller
        </Row>
        <Row className='mt-2'>
          <Col lg='3'>
            {sellerUsername}
          </Col>
          <Col lg={{ offset: 6 }}>
            <Button className='btn-orange-dark' disabled={!auth.logged_in} onClick={handleSendMessageClicked}>
              Send a message
            </Button>
          </Col>
        </Row>
        <Row className='fst-italic mt-1'>
          Registered since: {new Date(sellerRegistrationDate).toLocaleDateString('ro-RO', dateFormatShortOptions)}
        </Row>
        <Stack direction='horizontal' className='mt-3 clickable fw-semibold'
          onClick={() => navigate(`/reviews/${sellerId}`)}>
          User reviews
          <ChevronRight className='mt-1' />
        </Stack>
        <Stack direction='horizontal' className='mt-3 clickable fw-semibold'
          onClick={() => navigate(`/announcements?seller=${sellerId}`)}>
          Other sale announcements from this seller
          <ChevronRight className='mt-1' />
        </Stack>
      </Card >
      {chatOpen &&
        <ChatBoxComponent receiverId={sellerId} receiverUsername={sellerUsername} />
      }
    </>
  )
}
