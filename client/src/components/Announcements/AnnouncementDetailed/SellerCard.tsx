import { useEffect, useState } from 'react';
import { Button, Card, Col, Row, Stack } from 'react-bootstrap'
import { ChevronRight } from 'react-bootstrap-icons';
import { useNavigate } from 'react-router-dom';

import useAuth from '../../../hooks/useAuth';
import ChatBoxComponent from '../../ChatBoxComponent';
import { User } from '../../../interface/UsersChattedWith';
import { dateFormatShortOptions } from '../../../util/dateOptions';
import configuredAxios from '../../../axios/configuredAxios';
import { apiPrefix } from '../../../config/application.json'

interface PropType {
  sellerId: number;
}

export default function SellerCard({ sellerId }: PropType) {
  const navigate = useNavigate();
  const { auth } = useAuth();
  const [chatOpen, setChatOpen] = useState<boolean>(false);
  const [seller, setSeller] = useState<User>();


  useEffect(() => {
    configuredAxios.get(`/${apiPrefix}/users/${sellerId}`)
      .then((response) => {
        setSeller(response.data);
      })
      .catch(() => {
      })
  }, []);
  

  function handleSendMessageClicked() {
    setChatOpen(true);
  }

  return ( seller &&
    <>
      <Card key={`container_order`} className='mt-4 mb-3'>
        <Row className='fw-semibold fs-5 ms-5'>
          Contact The Seller
        </Row>
        <Row className='mt-2'>
          <Col lg='3'>
            {seller.username}
          </Col>
          <Col lg={{ offset: 6 }}>
            <Button className='btn-orange-dark' disabled={!auth.logged_in} onClick={handleSendMessageClicked}>
              Send a message
            </Button>
          </Col>
        </Row>
        <Row className='fst-italic mt-1'>
          Registered since: {new Date(seller.registrationDate).toLocaleDateString('ro-RO', dateFormatShortOptions)}
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
        <ChatBoxComponent receiverId={sellerId} receiverUsername={seller.username} />
      }
    </>
  )
}
